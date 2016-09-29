package net.derohimat.firebasebasemvp;

import android.app.Application;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.support.annotation.VisibleForTesting;

import com.firebase.client.Firebase;

import net.derohimat.firebasebasemvp.di.component.ApplicationComponent;
import net.derohimat.firebasebasemvp.di.component.DaggerApplicationComponent;
import net.derohimat.firebasebasemvp.di.module.ApplicationModule;

import org.greenrobot.eventbus.EventBus;

import javax.inject.Inject;

import rx.Scheduler;
import rx.schedulers.Schedulers;
import timber.log.Timber;

public class FireAuthApplication extends Application {

    private Scheduler mScheduler;
    private ApplicationComponent mApplicationComponent;
    @Inject
    EventBus mEventBus;

    @Override
    public void onCreate() {
        super.onCreate();

        boolean isDebuggable = (0 != (getApplicationInfo().flags & ApplicationInfo.FLAG_DEBUGGABLE));

        if (isDebuggable) {
            Timber.plant(new Timber.DebugTree());
        }
        Firebase.setAndroidContext(this);

        mApplicationComponent = DaggerApplicationComponent.builder().applicationModule(new ApplicationModule(this)).build();

        mApplicationComponent.inject(this);
        mEventBus.register(this);
    }

    public static FireAuthApplication get(Context context) {
        return (FireAuthApplication) context.getApplicationContext();
    }

    public ApplicationComponent getApplicationComponent() {
        return mApplicationComponent;
    }

    @VisibleForTesting
    public void setApplicationComponent(ApplicationComponent applicationComponent) {
        this.mApplicationComponent = applicationComponent;
    }

    public Scheduler getSubscribeScheduler() {
        if (mScheduler == null) {
            mScheduler = Schedulers.io();
        }
        return mScheduler;
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        Timber.e("########## onLowMemory ##########");
    }

    @Override
    public void onTerminate() {
        mEventBus.unregister(this);
        super.onTerminate();
    }

}
