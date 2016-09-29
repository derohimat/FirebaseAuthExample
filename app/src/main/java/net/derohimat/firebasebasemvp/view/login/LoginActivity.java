package net.derohimat.firebasebasemvp.view.login;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;

import net.derohimat.firebasebasemvp.R;
import net.derohimat.firebasebasemvp.events.LoginEvent;
import net.derohimat.firebasebasemvp.util.DialogFactory;
import net.derohimat.firebasebasemvp.view.FireAuthBaseActivity;
import net.derohimat.firebasebasemvp.view.register.RegisterActivity;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by deroh on 23/05/2016.
 */
public class LoginActivity extends FireAuthBaseActivity implements LoginMvpView {

    @Bind(R.id.inpEmail)
    EditText mInpEmail;
    @Bind(R.id.inpPassword)
    EditText mInpPassword;
    private LoginPresenter mPresenter;
    private static ProgressBar mProgressBar = null;

    @Inject
    EventBus eventBus;

    @Override
    protected int getResourceLayout() {
        return R.layout.login_activity;
    }

    @Override
    protected void onViewReady(Bundle savedInstanceState) {
        mPresenter = new LoginPresenter(this);
        mPresenter.attachView(this);

        getBaseActionBar().setElevation(0);
        getBaseActionBar().hide();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getComponent().inject(this);
    }

    @OnClick(R.id.btnLogin)
    void onBtnLoginClick() {
        String email = mInpEmail.getText().toString();
        String password = mInpPassword.getText().toString();
        if (TextUtils.isEmpty(email)) {
            mInpEmail.setError("Email masih kosong");
            mInpEmail.setFocusable(true);
            return;
        }
        if (TextUtils.isEmpty(password)) {
            mInpPassword.setError("Password masih kosong");
            mInpPassword.setFocusable(true);
            return;
        }
        mPresenter.doLogin(email, password);
    }

    @Override
    protected void onDestroy() {
        mPresenter.detachView();
        super.onDestroy();
    }

    @Override
    public void onStart() {
        super.onStart();
        eventBus.register(this);
    }

    @Override
    public void onStop() {
        eventBus.unregister(this);
        super.onStop();
    }

    @Subscribe
    public void onEvent(LoginEvent event) {
        if (event.isSuccess()) {
//            DialogFactory.createSimpleOkDialog(mContext, getString(R.string.app_name), event.getMessage()).show();
            Intent intent = new Intent(mContext, RegisterActivity.class);
            startActivity(intent);
            finish();
        } else {
            DialogFactory.showErrorSnackBar(mContext, findViewById(android.R.id.content), new Throwable(event.getMessage())).show();
        }
    }

    @Override
    public void showProgress() {
        if (mProgressBar == null) {
            mProgressBar = DialogFactory.DProgressBar(mContext);
        } else {
            mProgressBar.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void hideProgress() {
        mProgressBar.setVisibility(View.GONE);
    }

    @Override
    public Context getContext() {
        return this;
    }
}
