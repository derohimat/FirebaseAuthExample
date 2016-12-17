package net.derohimat.firebasebasemvp;

import android.app.Application;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.support.annotation.VisibleForTesting;

import com.facebook.FacebookSdk;
import com.twitter.sdk.android.Twitter;
import com.twitter.sdk.android.core.TwitterAuthConfig;

import net.derohimat.firebasebasemvp.di.component.ApplicationComponent;
import net.derohimat.firebasebasemvp.di.component.DaggerApplicationComponent;
import net.derohimat.firebasebasemvp.di.module.ApplicationModule;
import net.derohimat.firebasebasemvp.util.Constants;

import io.fabric.sdk.android.Fabric;
import rx.Scheduler;
import rx.schedulers.Schedulers;
import timber.log.Timber;

import static net.derohimat.firebasebasemvp.util.Constants.TWITTER_SECRET;

public class FireAuthApplication extends Application {

    private Scheduler mScheduler;
    private ApplicationComponent mApplicationComponent;

    @Override
    public void onCreate() {
        super.onCreate();
        TwitterAuthConfig authConfig = new TwitterAuthConfig(Constants.TWITTER_KEY, Constants.TWITTER_SECRET);
        Fabric.with(this, new Twitter(authConfig));

        FacebookSdk.sdkInitialize(getApplicationContext());

        boolean isDebuggable = (0 != (getApplicationInfo().flags & ApplicationInfo.FLAG_DEBUGGABLE));

        if (isDebuggable) {
            Timber.plant(new Timber.DebugTree());
        }

        mApplicationComponent = DaggerApplicationComponent.builder().applicationModule(new ApplicationModule(this)).build();

        mApplicationComponent.inject(this);
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

}
