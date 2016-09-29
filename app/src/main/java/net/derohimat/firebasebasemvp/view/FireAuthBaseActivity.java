package net.derohimat.firebasebasemvp.view;

import android.os.Bundle;
import android.view.LayoutInflater;

import net.derohimat.firebasebasemvp.FireAuthApplication;
import net.derohimat.firebasebasemvp.di.component.ActivityComponent;
import net.derohimat.firebasebasemvp.di.component.DaggerActivityComponent;

import net.derohimat.baseapp.ui.BaseActivity;

import butterknife.ButterKnife;
import timber.log.Timber;

public abstract class FireAuthBaseActivity extends BaseActivity {

    private ActivityComponent mComponent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getResourceLayout());
        ButterKnife.bind(this);
        Timber.tag(getClass().getSimpleName());
        mInflater = LayoutInflater.from(mContext);
        mComponent = DaggerActivityComponent.builder().applicationComponent(getApp().getApplicationComponent()).build();
        onViewReady(savedInstanceState);
    }

    protected ActivityComponent getComponent() {
        return mComponent;
    }

    protected FireAuthApplication getApp() {
        return (FireAuthApplication) getApplicationContext();
    }

}
