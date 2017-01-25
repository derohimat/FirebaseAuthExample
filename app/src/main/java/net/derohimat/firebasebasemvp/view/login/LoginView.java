package net.derohimat.firebasebasemvp.view.login;

import net.derohimat.baseapp.view.BaseView;

/**
 * Created by derohimat on 23/05/2016.
 */
interface LoginView extends BaseView {

    void showProgress();

    void hideProgress();

    void initFbLoginButton();

    void initTwitterLoginButton();
}
