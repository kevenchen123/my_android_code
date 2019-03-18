package com.keven.utils;

import android.support.annotation.CallSuper;
import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import io.reactivex.observers.DisposableMaybeObserver;

import static com.keven.utils.NetworkViewModel.Constants.*;


public abstract class NetworkViewModel {

    public static class Constants {
        @IntDef({
                REQUEST_NONE,
                REQUEST_RUNNING,
                REQUEST_SUCCEEDED,
                REQUEST_FAILED
        })
        @Retention(RetentionPolicy.SOURCE)
        public @interface RequestState{}

        public static final int REQUEST_NONE = 0;
        public static final int REQUEST_RUNNING = 1;
        public static final int REQUEST_SUCCEEDED = 2;
        public static final int REQUEST_FAILED = 3;
    }


    protected @Constants.RequestState int requestState;
    protected Throwable lastError;

    public abstract boolean isRequestingInformation();

    public NetworkViewModel() {
        this.requestState = REQUEST_NONE;
    }

    public @RequestState int getRequestState() {
        if (isRequestingInformation()) {
            return REQUEST_RUNNING;
        }
        return requestState;
    }

    public Throwable getLastError() {
        return lastError;
    }

    protected class MaybeNetworkObserver<T> extends DisposableMaybeObserver<T> {
        @Override
        @CallSuper
        public void onSuccess(T value) {
            requestState = REQUEST_SUCCEEDED;
        }

        @Override
        @CallSuper
        public void onError(Throwable e) {
            lastError = e;
            requestState = REQUEST_FAILED;
        }

        @Override
        public void onComplete() {
        }
    }
}