package net.derohimat.firebasebasemvp.di.module;

import com.firebase.client.Firebase;

import net.derohimat.firebasebasemvp.FireAuthApplication;
import net.derohimat.firebasebasemvp.data.local.PreferencesHelper;

import org.greenrobot.eventbus.EventBus;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class ApplicationModule {

    private final FireAuthApplication mBaseApplication;

    public ApplicationModule(FireAuthApplication baseApplication) {
        this.mBaseApplication = baseApplication;
    }

    @Provides
    @Singleton
    FireAuthApplication provideApplication() {
        return mBaseApplication;
    }

    @Provides
    @Singleton
    Firebase provideFireBase() {
        return new Firebase("https://radiant-fire-6599.firebaseio.com/");
    }

    @Provides
    @Singleton
    EventBus eventBus() {
        return new EventBus();
    }

    @Provides
    @Singleton
    PreferencesHelper prefsHelper() {
        return new PreferencesHelper(mBaseApplication);
    }

}