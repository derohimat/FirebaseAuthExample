package net.derohimat.firebasebasemvp.di.component;

import net.derohimat.firebasebasemvp.di.ActivityScope;
import net.derohimat.firebasebasemvp.view.login.LoginActivity;
import net.derohimat.firebasebasemvp.view.register.RegisterActivity;

import dagger.Component;

@ActivityScope
@Component(dependencies = ApplicationComponent.class)
public interface ActivityComponent extends ApplicationComponent {

    void inject(LoginActivity loginActivity);

    void inject(RegisterActivity registerActivity);

}