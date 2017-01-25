package net.derohimat.firebasebasemvp.view.main;

import com.google.firebase.auth.FirebaseUser;

import net.derohimat.baseapp.view.BaseView;

/**
 * Created by deroh on 23/05/2016.
 */
interface MainView extends BaseView {

    void showProgress();

    void hideProgress();

    void getUserData(FirebaseUser firebaseUser);
}
