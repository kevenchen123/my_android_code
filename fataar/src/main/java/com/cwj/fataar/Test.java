package com.cwj.fataar;

import android.content.Context;
import android.support.v7.widget.AppCompatEditText;
import android.util.Log;

import com.cwj.fataarinner.Common;

import java.util.concurrent.TimeUnit;

import io.reactivex.Flowable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;

public class Test {
    public static AppCompatEditText textaa;

    public static void fun(Context context) {
        textaa = new AppCompatEditText(context);

        // 倒计时 10s
        Disposable mDisposable = Flowable.intervalRange(0, 11, 0, 1, TimeUnit.SECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .doOnNext(new Consumer<Long>() {
                    @Override
                    public void accept(Long aLong) throws Exception {
                        Log.d("TAG", "倒计时"+ Common.aa);
                    }
                })
                .doOnComplete(new Action() {
                    @Override
                    public void run() throws Exception {
                        Log.d("TAG", "倒计时完毕");
                    }
                })
                .subscribe();
    }
}