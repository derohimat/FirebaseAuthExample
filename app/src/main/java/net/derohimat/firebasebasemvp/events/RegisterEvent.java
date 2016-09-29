package net.derohimat.firebasebasemvp.events;

import android.support.annotation.Nullable;

public class RegisterEvent {
    @Nullable
    private final boolean success;
    @Nullable
    private final String message;

    public RegisterEvent(boolean mSuccess, @Nullable String message) {
        this.success = mSuccess;
        this.message = message;
    }

    @Nullable
    public boolean isSuccess() {
        return success;
    }

    @Nullable
    public String getMessage() {
        return message;
    }
}
