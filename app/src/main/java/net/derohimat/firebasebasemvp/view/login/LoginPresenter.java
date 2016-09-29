package net.derohimat.firebasebasemvp.view.login;

import android.content.Context;

import com.firebase.client.AuthData;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;

import net.derohimat.baseapp.presenter.BasePresenter;
import net.derohimat.firebasebasemvp.FireAuthApplication;
import net.derohimat.firebasebasemvp.data.local.PreferencesHelper;
import net.derohimat.firebasebasemvp.events.LoginEvent;

import org.greenrobot.eventbus.EventBus;

import javax.inject.Inject;

import timber.log.Timber;

/**
 * Created by deroh on 23/05/2016.
 */
public class LoginPresenter implements BasePresenter<LoginMvpView> {

    @Inject
    LoginPresenter(Context context) {
        ((FireAuthApplication) context.getApplicationContext()).getApplicationComponent().inject(this);
    }

    @Inject
    EventBus mEventBus;
    @Inject
    Firebase mFirebase;
    @Inject
    PreferencesHelper mPreferencesHelper;

    private LoginMvpView mView;
//    private Subscription mSubscription;

    @Override
    public void attachView(LoginMvpView view) {
        mView = view;
    }

    @Override
    public void detachView() {
        mView = null;
//        if (mSubscription != null) mSubscription.unsubscribe();
    }

    void doLogin(String username, String password) {
        mView.showProgress();
//        if (mSubscription != null) mSubscription.unsubscribe();

//        FireAuthApplication baseApplication = FireAuthApplication.get(mView.getContext());
        mFirebase.authWithPassword(username, password, new Firebase.AuthResultHandler() {
            @Override
            public void onAuthenticated(AuthData authData) {
                mView.hideProgress();
                mPreferencesHelper.setUserId(authData.getUid());
                mEventBus.post(new LoginEvent(true, "success"));
                Timber.e("Successfully logged in user account with uid: " + authData.getUid());
            }

            @Override
            public void onAuthenticationError(FirebaseError firebaseError) {
                mView.hideProgress();
                mEventBus.post(new LoginEvent(false, firebaseError.getMessage()));
                Timber.e("Unsuccessfully Login : " + firebaseError.getMessage());
            }
        });
    }

}
