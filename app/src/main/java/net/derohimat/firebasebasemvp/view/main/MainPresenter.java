package net.derohimat.firebasebasemvp.view.main;

import android.content.Context;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import net.derohimat.baseapp.presenter.BasePresenter;
import net.derohimat.firebasebasemvp.FireAuthApplication;
import net.derohimat.firebasebasemvp.data.local.PreferencesHelper;

import javax.inject.Inject;

import timber.log.Timber;

/**
 * Created by deroh on 23/05/2016.
 */
public class MainPresenter implements BasePresenter<MainMvpView> {

    @Inject
    MainPresenter(Context context) {
        ((FireAuthApplication) context.getApplicationContext()).getApplicationComponent().inject(this);
    }

    @Inject
    FirebaseAuth mAuth;
    @Inject
    PreferencesHelper mPreferencesHelper;

    private FirebaseAuth.AuthStateListener mAuthListener;
    private MainMvpView mView;
//    private Subscription mSubscription;

    @Override
    public void attachView(MainMvpView view) {
        mView = view;
    }

    @Override
    public void detachView() {
        mView = null;
//        if (mSubscription != null) mSubscription.unsubscribe();
    }

    void getUserData() {
        mView.showProgress();

        mAuthListener = firebaseAuth -> {
            mView.hideProgress();
            FirebaseUser user = firebaseAuth.getCurrentUser();
            if (user != null) {
                mView.getUserData(user);
                Timber.d("user signed in", user.getEmail());
            } else {
                Timber.d("user signed out");
            }
        };
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
