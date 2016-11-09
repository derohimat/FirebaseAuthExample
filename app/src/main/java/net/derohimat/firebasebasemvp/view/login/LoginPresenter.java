package net.derohimat.firebasebasemvp.view.login;

import android.app.Activity;
import android.content.Context;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

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
        ((FireAuthApplication) context.getApplicationContext()).getApplicationComponent().inject(this);
    }

    @Inject
    EventBus mEventBus;
    @Inject
    FirebaseAuth mAuth;
    @Inject
    PreferencesHelper mPreferencesHelper;

    private FirebaseAuth.AuthStateListener mAuthListener;
    private LoginMvpView mView;
//    private Subscription mSubscription;

    @Override
    public void attachView(LoginMvpView view) {
        mView = view;
        mAuthListener = firebaseAuth -> {
            FirebaseUser user = firebaseAuth.getCurrentUser();
            if (user != null) {
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
