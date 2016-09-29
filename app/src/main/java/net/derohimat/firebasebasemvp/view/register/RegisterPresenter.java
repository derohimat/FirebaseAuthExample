package net.derohimat.firebasebasemvp.view.register;

import android.content.Context;

import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;

import net.derohimat.baseapp.presenter.BasePresenter;
import net.derohimat.firebasebasemvp.FireAuthApplication;
import net.derohimat.firebasebasemvp.events.RegisterEvent;

import org.greenrobot.eventbus.EventBus;

import java.util.Map;

import javax.inject.Inject;

import timber.log.Timber;

/**
 * Created by deroh on 23/05/2016.
 */
public class RegisterPresenter implements BasePresenter<RegisterMvpView> {

    @Inject
    RegisterPresenter(Context context) {
        ((FireAuthApplication) context.getApplicationContext()).getApplicationComponent().inject(this);
    }

    @Inject
    Firebase mFirebase;
    @Inject
    EventBus mEventBus;

    private RegisterMvpView mView;
//    private Subscription mSubscription;

    @Override
    public void attachView(RegisterMvpView view) {
        mView = view;
    }

    @Override
    public void detachView() {
        mView = null;
//        if (mSubscription != null) mSubscription.unsubscribe();
    }

    void doRegister(String email, String password) {
        mView.showProgress();
//        if (mSubscription != null) mSubscription.unsubscribe();

//        FireAuthApplication baseApplication = FireAuthApplication.get(mView.getContext());
        mFirebase.createUser(email, password, new Firebase.ValueResultHandler<Map<String, Object>>() {
            @Override
            public void onSuccess(Map<String, Object> stringObjectMap) {
                mView.hideProgress();
                mEventBus.post(new RegisterEvent(true, "success"));
                Timber.e("Successfully created user account with uid: " + stringObjectMap.get("uid"));
            }

            @Override
            public void onError(FirebaseError firebaseError) {
                mView.hideProgress();
                mEventBus.post(new RegisterEvent(false, firebaseError.getMessage()));
                Timber.e("Unsuccessfully Register : " + firebaseError.getMessage());
            }
        });
    }

}
