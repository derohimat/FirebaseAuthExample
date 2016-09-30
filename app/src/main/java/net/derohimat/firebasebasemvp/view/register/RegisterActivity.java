package net.derohimat.firebasebasemvp.view.register;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;

import net.derohimat.firebasebasemvp.R;
import net.derohimat.firebasebasemvp.events.RegisterEvent;
import net.derohimat.firebasebasemvp.util.DialogFactory;
import net.derohimat.firebasebasemvp.util.Utils;
import net.derohimat.firebasebasemvp.view.FireAuthBaseActivity;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by deroh on 23/05/2016.
 */
public class RegisterActivity extends FireAuthBaseActivity implements RegisterMvpView {

    @Bind(R.id.inpFullName)
    EditText mInpFullName;
    @Bind(R.id.inpEmail)
    EditText mInpEmail;
    @Bind(R.id.inpPhone)
    EditText mInpPhone;
    @Bind(R.id.inpPassword)
    EditText mInpPassword;
    private RegisterPresenter mPresenter;
    private static ProgressBar mProgressBar = null;

    @Inject
    EventBus eventBus;

    @Override
    protected int getResourceLayout() {
        return R.layout.register_activity;
    }

    @Override
    protected void onViewReady(Bundle savedInstanceState) {
        mPresenter = new RegisterPresenter(this);
        mPresenter.attachView(this);

        getBaseActionBar().setElevation(0);
        getBaseActionBar().hide();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getComponent().inject(this);
    }

    @OnClick(R.id.btnRegister)
    void onBtnLoginClick() {
        String fullName = mInpFullName.getText().toString();
        String email = mInpEmail.getText().toString();
        String phone = mInpPhone.getText().toString();
        String password = mInpPassword.getText().toString();
        if (TextUtils.isEmpty(fullName)) {
            mInpFullName.setError("Full Name masih kosong");
            mInpFullName.setFocusable(true);
            return;
        }
        if (TextUtils.isEmpty(email)) {
            mInpEmail.setError("Email masih kosong");
            mInpEmail.setFocusable(true);
            return;
        }
        if (!Utils.isEmailValid(email)) {
            mInpEmail.setError("Format Email salah");
            mInpEmail.setFocusable(true);
            return;
        }
        if (TextUtils.isEmpty(phone)) {
            mInpPhone.setError("Phone masih kosong");
            mInpPhone.setFocusable(true);
            return;
        }
        if (TextUtils.isEmpty(password)) {
            mInpPassword.setError("Password masih kosong");
            mInpPassword.setFocusable(true);
            return;
        }
        mPresenter.doRegister(email, password);
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
    public void onEvent(RegisterEvent event) {
        if (event.isSuccess()) {
            DialogFactory.createSimpleOkDialog(mContext, getString(R.string.app_name), event.getMessage()).show();
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

    public static void start(Context context) {
        Intent intent = new Intent(context, RegisterActivity.class);
        context.startActivity(intent);
    }

    @Override
    public Context getContext() {
        return this;
    }
}
