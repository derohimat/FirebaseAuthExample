package net.derohimat.firebasebasemvp.view.main;

import android.content.Context;

import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;

import net.derohimat.baseapp.presenter.BasePresenter;
import net.derohimat.firebasebasemvp.FireAuthApplication;
import net.derohimat.firebasebasemvp.data.local.PreferencesHelper;

import javax.inject.Inject;

/**
 * Created by deroh on 23/05/2016.
 */
public class MainPresenter implements BasePresenter<MainMvpView> {

    @Inject
    MainPresenter(Context context) {
        ((FireAuthApplication) context.getApplicationContext()).getApplicationComponent().inject(this);
    }

    @Inject
    Firebase mFirebase;
    @Inject
    PreferencesHelper mPreferencesHelper;

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

    void resetPassword(String email) {
        mFirebase.resetPassword(email, new Firebase.ResultHandler() {
            @Override
            public void onSuccess() {

            }

            @Override
            public void onError(FirebaseError firebaseError) {

            }
        });
    }

}
