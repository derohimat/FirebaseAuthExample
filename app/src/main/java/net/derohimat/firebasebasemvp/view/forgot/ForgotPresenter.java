package net.derohimat.firebasebasemvp.view.forgot;

import android.content.Context;

import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;

import net.derohimat.baseapp.presenter.BasePresenter;
import net.derohimat.firebasebasemvp.FireAuthApplication;
import net.derohimat.firebasebasemvp.events.ForgotEvent;
import net.derohimat.firebasebasemvp.events.RegisterEvent;

import org.greenrobot.eventbus.EventBus;

import javax.inject.Inject;

import timber.log.Timber;

/**
 * Created by deroh on 23/05/2016.
 */
public class ForgotPresenter implements BasePresenter<ForgotMvpView> {

    @Inject
    ForgotPresenter(Context context) {
        ((FireAuthApplication) context.getApplicationContext()).getApplicationComponent().inject(this);
    }

    @Inject
    EventBus mEventBus;
    @Inject
    Firebase mFirebase;

    private ForgotMvpView mView;
//    private Subscription mSubscription;

    @Override
    public void attachView(ForgotMvpView view) {
        mView = view;
    }

    @Override
    public void detachView() {
        mView = null;
//        if (mSubscription != null) mSubscription.unsubscribe();
    }

    void resetPassword(String email) {
        mView.showProgress();
        mFirebase.resetPassword(email, new Firebase.ResultHandler() {
            @Override
            public void onSuccess() {
                mView.hideProgress();
                mEventBus.post(new ForgotEvent(true, "Reset password successfully, Please check your email"));
                Timber.e("Reset password successfully, Please check your email");
            }

            @Override
            public void onError(FirebaseError firebaseError) {
                mView.hideProgress();
                mEventBus.post(new RegisterEvent(false, firebaseError.getMessage()));
                Timber.e("Unsuccessfully Reset Password : " + firebaseError.getMessage());
            }
        });
    }

}
