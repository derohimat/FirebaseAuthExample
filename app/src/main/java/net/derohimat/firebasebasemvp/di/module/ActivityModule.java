package net.derohimat.firebasebasemvp.di.module;

import android.app.Activity;
import android.content.Context;

import dagger.Module;
import dagger.Provides;

@Module
class ActivityModule {
    final Activity mActivity;

    public ActivityModule(Activity activity) {
        mActivity = activity;
    }

    @Provides
    Context activityContext() {
        return mActivity;
    }
}