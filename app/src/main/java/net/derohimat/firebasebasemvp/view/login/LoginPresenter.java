package net.derohimat.firebasebasemvp.view.login;

import android.app.Activity;
import android.content.Context;

import com.facebook.AccessToken;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.TwitterAuthProvider;
import com.twitter.sdk.android.core.TwitterSession;

import net.derohimat.baseapp.presenter.BasePresenter;
import net.derohimat.firebasebasemvp.FireAuthApplication;
import net.derohimat.firebasebasemvp.data.local.PreferencesHelper;
import net.derohimat.firebasebasemvp.events.LoginEvent;

import org.greenrobot.eventbus.EventBus;

import javax.inject.Inject;

import timber.log.Timber;

/**
 * Created by derohimat on 23/05/2016.
 */
public class LoginPresenter implements BasePresenter<LoginMvpView> {

    @Inject
    LoginPresenter(Context context) {
        mContext = context;
        ((FireAuthApplication) context.getApplicationContext()).getApplicationComponent().inject(this);
    }

    @Inject
    EventBus mEventBus;
    @Inject
    FirebaseAuth mAuth;
    @Inject
    PreferencesHelper mPreferencesHelper;

    private Context mContext;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private LoginMvpView mView;
//    private Subscription mSubscription;

    @Override
    public void attachView(LoginMvpView view) {
        mView = view;
        mAuthListener = firebaseAuth -> {
            FirebaseUser user = firebaseAuth.getCurrentUser();
            if (user != null) {
                mEventBus.post(new LoginEvent(true, "User already signed in"));
                Timber.d("user signed in", user.getDisplayName());
            } else {
                Timber.d("user signed out");
            }
        };
    }

    @Override
    public void detachView() {
        mView = null;
//        if (mSubscription != null) mSubscription.unsubscribe();
    }

    void doLogin(Context context, String username, String password) {
        mView.showProgress();
//        if (mSubscription != null) mSubscription.unsubscribe();

        mAuth.signInWithEmailAndPassword(username, password)
                .addOnCompleteListener((Activity) context, task -> {
                    if (!task.isSuccessful()) {
                        mEventBus.post(new LoginEvent(false, task.getException().getMessage()));
                    } else {
                        mPreferencesHelper.setUserId(task.getResult().getUser().getProviderId());
                        mEventBus.post(new LoginEvent(true, "Success"));
                    }

                });
    }

    void handleFacebookAccessToken(AccessToken token) {
        mView.showProgress();
        Timber.d("handleFacebookAccessToken:" + token);

        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener((Activity) mContext, task -> {
                    mView.hideProgress();

                    Timber.d("signInWithCredential:onComplete:" + task.isSuccessful());

                    if (!task.isSuccessful()) {
                        Timber.w("signInWithFbCredential", task.getException());
                        mEventBus.post(new LoginEvent(false, "Fb Auth Failed : " + task.getException().getMessage()));
                    } else {
                        Timber.w("signInWithFbCredential", "success with email " + task.getResult().getUser().getEmail());
                        mEventBus.post(new LoginEvent(true, "Fb Auth Success"));
                    }
                });
    }

    void handleTwitterSession(TwitterSession session) {
        Timber.d("handleTwitterSession:" + session);

        AuthCredential credential = TwitterAuthProvider.getCredential(
                session.getAuthToken().token,
                session.getAuthToken().secret);

        mAuth.signInWithCredential(credential)
                .addOnCompleteListener((Activity) mContext, task -> {
                    Timber.d("signInWithCredential:onComplete:" + task.isSuccessful());

                    // If sign in fails, display a message to the user. If sign in succeeds
                    // the auth state listener will be notified and logic to handle the
                    // signed in user can be handled in the listener.
                    if (!task.isSuccessful()) {
                        Timber.w("signInWithTwitterCredential", task.getException());
                        mEventBus.post(new LoginEvent(false, "Twitter Auth Failed : " + task.getException().getMessage()));
                    } else {
                        Timber.w("signInWithTwitterCredential", "success with email " + task.getResult().getUser().getEmail());
                        mEventBus.post(new LoginEvent(true, "Twitter Auth Success"));
                    }
                });
    }


    void removeAuthListener() {
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }

    void addAuthListener() {
        if (mAuthListener != null) {
            mAuth.addAuthStateListener(mAuthListener);
        }
    }
}
