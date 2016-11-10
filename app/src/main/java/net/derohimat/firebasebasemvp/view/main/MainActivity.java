package net.derohimat.firebasebasemvp.view.main;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.google.firebase.auth.FirebaseUser;

import net.derohimat.firebasebasemvp.R;
import net.derohimat.firebasebasemvp.util.DialogFactory;
import net.derohimat.firebasebasemvp.view.FireAuthBaseActivity;
import net.derohimat.firebasebasemvp.view.login.LoginActivity;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by deroh on 23/05/2016.
 */
public class MainActivity extends FireAuthBaseActivity implements MainMvpView {

    @Bind(R.id.inpEmail)
    EditText mInpEmail;
    private MainPresenter mPresenter;
    ProgressBar mProgressBar = null;


    @Override
    protected int getResourceLayout() {
        return R.layout.main_activity;
    }

    @Override
    protected void onViewReady(Bundle savedInstanceState) {
        mPresenter = new MainPresenter(this);
        mPresenter.attachView(this);

        getBaseActionBar().setElevation(0);
        getBaseActionBar().hide();

        mPresenter.getUserData();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getComponent().inject(this);
    }

    @Override
    protected void onDestroy() {
        mPresenter.detachView();
        super.onDestroy();
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
    public void getUserData(FirebaseUser firebaseUser) {
        mInpEmail.setText(firebaseUser.getEmail());
    }

    @Override
    public void onStart() {
        super.onStart();
        mPresenter.addAuthListener();
    }

    @Override
    public void onStop() {
        super.onStop();
        mPresenter.removeAuthListener();
    }

    @OnClick(R.id.btnLogout)
    void logoutCLick() {
        mPresenter.doLogout();
        LoginActivity.start(mContext);
    }

    public static void start(Context context) {
        Intent intent = new Intent(context, MainActivity.class);
        context.startActivity(intent);
    }

    @Override
    public Context getContext() {
        return this;
    }
}
