package net.derohimat.firebasebasemvp.view.login;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.TwitterSession;
import com.twitter.sdk.android.core.identity.TwitterLoginButton;

import net.derohimat.firebasebasemvp.R;
import net.derohimat.firebasebasemvp.events.LoginEvent;
import net.derohimat.firebasebasemvp.util.DialogFactory;
import net.derohimat.firebasebasemvp.util.Utils;
import net.derohimat.firebasebasemvp.view.FireAuthBaseActivity;
import net.derohimat.firebasebasemvp.view.forgot.ForgotActivity;
import net.derohimat.firebasebasemvp.view.main.MainActivity;
import net.derohimat.firebasebasemvp.view.register.RegisterActivity;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.OnClick;
import timber.log.Timber;

/**
 * Created by derohimat on 23/05/2016.
 */
public class LoginActivity extends FireAuthBaseActivity implements LoginView {

    @Bind(R.id.inpEmail)
    EditText mInpEmail;
    @Bind(R.id.inpPassword)
    EditText mInpPassword;
    @Bind(R.id.login_button)
    LoginButton mLoginFbButton;
    @Bind(R.id.twitter_login)
    TwitterLoginButton mTwitterLoginButton;
    private LoginPresenter mPresenter;
    ProgressBar mProgressBar = null;

    @Inject
    EventBus eventBus;
    private CallbackManager mCallbackManager;

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

        initFbLoginButton();
        initTwitterLoginButton();
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
            mInpEmail.setError("Email still empty");
            mInpEmail.setFocusable(true);
            return;
        }
        if (!Utils.isEmailValid(email)) {
            mInpEmail.setError("Wrong email format");
            mInpEmail.setFocusable(true);
            return;
        }
        if (TextUtils.isEmpty(password)) {
            mInpPassword.setError("Password still empty");
            mInpPassword.setFocusable(true);
            return;
        }
        mPresenter.doLogin(mContext, email, password);
    }

    @OnClick(R.id.txtForgot)
    void onBtnForgotClick() {
        ForgotActivity.start(mContext);
    }

    @OnClick(R.id.txtRegister)
    void onBtnRegisterClick() {
        RegisterActivity.start(mContext);
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
        mPresenter.addAuthListener();
    }

    @Override
    public void onStop() {
        super.onStop();
        eventBus.unregister(this);
        mPresenter.removeAuthListener();
    }

    @Subscribe
    public void onEvent(LoginEvent event) {
        if (event.isSuccess()) {
//            DialogFactory.createSimpleOkDialog(mContext, getString(R.string.app_name), event.getMessage()).show();
            MainActivity.start(mContext);
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
    public void initFbLoginButton() {
        mCallbackManager = CallbackManager.Factory.create();
        mLoginFbButton.setReadPermissions("email", "public_profile");
        mLoginFbButton.registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Timber.d("facebook:onSuccess:" + loginResult);
                mPresenter.handleFacebookAccessToken(loginResult.getAccessToken());
            }

            @Override
            public void onCancel() {
                Timber.d("facebook:onCancel");
            }

            @Override
            public void onError(FacebookException error) {
                Timber.d("facebook:onError", error);
            }
        });
    }

    @Override
    public void initTwitterLoginButton() {
        mTwitterLoginButton.setCallback(new Callback<TwitterSession>() {
            @Override
            public void success(Result<TwitterSession> result) {
                // Do something with result, which provides a TwitterSession for making API calls
                mPresenter.handleTwitterSession(result.data);
            }

            @Override
            public void failure(TwitterException exception) {
                // Do something on failure
                DialogFactory.showErrorSnackBar(mContext, findViewById(android.R.id.content), new Throwable(exception.getMessage())).show();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mCallbackManager.onActivityResult(requestCode, resultCode, data);
        mTwitterLoginButton.onActivityResult(requestCode, resultCode, data);
    }

    public static void start(Context context) {
        Intent intent = new Intent(context, LoginActivity.class);
        context.startActivity(intent);
    }

    @Override
    public Context getContext() {
        return this;
    }
}
