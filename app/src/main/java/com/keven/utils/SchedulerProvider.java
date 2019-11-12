package com.keven.utils;

import com.keven.utils.executor.JobExecutor;

import java.util.concurrent.Executor;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public interface SchedulerProvider {
  <T> Observable.Transformer<T, T> applySchedulers();

  SchedulerProvider DEFAULT = new SchedulerProvider() {
    @Override public <T> Observable.Transformer<T, T> applySchedulers() {
      return new Observable.Transformer<T, T>() {
        @Override
        public Observable<T> call(Observable<T> tObservable) {
          return tObservable.subscribeOn(Schedulers.io())
                  .observeOn(AndroidSchedulers.mainThread());
        }};
    }};

  Executor threadPoolExecutor = new JobExecutor();
  SchedulerProvider DEFAULTTREADPOOL = new SchedulerProvider() {
    @Override public <T> Observable.Transformer<T, T> applySchedulers() {
      return new Observable.Transformer<T, T>() {
        @Override
        public Observable<T> call(Observable<T> tObservable) {
          return tObservable.subscribeOn(Schedulers.from(threadPoolExecutor))
                  .observeOn(AndroidSchedulers.mainThread());
        }};
    }};
}
