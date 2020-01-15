package com.keven.utils;

import java.util.HashMap;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.subjects.PublishSubject;


/**
 * A simple event bus built with RxJava
 */
@Singleton
public class RxEventBus {

    private final PublishSubject<Object> mBusSubject;
    private final HashMap<MyConsumer, Disposable> mDisposables = new HashMap<>();

    @Inject
    public RxEventBus() {
        mBusSubject = PublishSubject.create();
    }

    /**
     * Observable that will emmit everything posted to the event bus.
     */
    public Observable<Object> observable() {
        return mBusSubject;
    }

    /**
     * Observable that only emits events of a specific class.
     * Use this if you only want to subscribe to one type of events.
     */
    public <T> Observable<T> filteredObservable(final Class<T> eventClass) {
        return mBusSubject.ofType(eventClass);
    }

    /**
     * Posts an object (usually an Event) to the bus
     */
    public void post(Object event) {
        mBusSubject.onNext(event);
    }

    public <T> Consumer subscribe(final PublishCallback<T> callback, final Class<T> type) {
        if (mBusSubject != null) {
            MyConsumer<Object> consumer = new MyConsumer<Object>() {
                @Override
                public void accept(Object o) {
                    if (o.getClass() == type) {
                        callback.accept((T)o);
                        unSubscribe(this);
                    }
                }
                @Override
                public Class getTypeClass() {
                    return type;
                }
            };
            mDisposables.put(consumer, mBusSubject.ofType(type).subscribe(consumer));
            return consumer;
        }
        return null;
    }

    public void unSubscribe(Consumer consumer) {
        if (mDisposables != null) {
            Disposable disposable = mDisposables.get(consumer);
            if (disposable != null && !disposable.isDisposed()) {
                disposable.dispose();
                mDisposables.remove(consumer);
            }
        }
    }


    public void unSubscribeAll() {
        if (mDisposables != null) {
            for (Consumer consumer : mDisposables.keySet()) {
                Disposable disposable = mDisposables.get(consumer);
                if (disposable != null && !disposable.isDisposed()) {
                    disposable.dispose();
                }
            }
            mDisposables.clear();
        }
    }

    public boolean isAnySubscribe() {
        if (mDisposables != null) {
            return !mDisposables.isEmpty();
        }
        return false;
    }

    public boolean isSubscribe(Class type) {
        if (mDisposables != null) {
            for (MyConsumer consumer : mDisposables.keySet()) {
                if (consumer.getTypeClass() == type) {
                    return true;
                }
            }
        }
        return false;
    }

    //--------------------------------------------------------------

    public interface PublishCallback<T> {
        void accept(T item);
    }

    private interface MyConsumer<T> extends Consumer<T> {
        Class getTypeClass();
    }
}