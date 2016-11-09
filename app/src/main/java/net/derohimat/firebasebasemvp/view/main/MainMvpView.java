package net.derohimat.firebasebasemvp.view.main;

import com.google.firebase.auth.FirebaseUser;

import net.derohimat.firebasebasemvp.view.MvpView;

/**
 * Created by deroh on 23/05/2016.
 */
interface MainMvpView extends MvpView {

    void showProgress();

    void hideProgress();

    void getUserData(FirebaseUser firebaseUser);
}
