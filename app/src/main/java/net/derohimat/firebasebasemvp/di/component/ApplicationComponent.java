package net.derohimat.firebasebasemvp.di.component;

import com.firebase.client.Firebase;

import net.derohimat.firebasebasemvp.FireAuthApplication;
import net.derohimat.firebasebasemvp.data.local.PreferencesHelper;
import net.derohimat.firebasebasemvp.di.module.ApplicationModule;
import net.derohimat.firebasebasemvp.view.login.LoginPresenter;
import net.derohimat.firebasebasemvp.view.register.RegisterPresenter;

import org.greenrobot.eventbus.EventBus;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = {ApplicationModule.class})
public interface ApplicationComponent {

    void inject(LoginPresenter loginPresenter);

    void inject(RegisterPresenter registerPresenter);

    void inject(FireAuthApplication baseApplication);

    Firebase fireBase();

    EventBus eventBus();

    PreferencesHelper prefsHelper();

}