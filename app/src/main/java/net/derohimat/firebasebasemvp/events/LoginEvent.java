package net.derohimat.firebasebasemvp.events;

import android.support.annotation.Nullable;

public class LoginEvent {
    @Nullable
    private final boolean mSuccess;
    @Nullable
    private final String message;

    public LoginEvent(boolean mSuccess, @Nullable String message) {
        this.mSuccess = mSuccess;
        this.message = message;
    }

    @Nullable
    public boolean isSuccess() {
        return mSuccess;
    }

    @Nullable
    public String getMessage() {
        return message;
    }
}
