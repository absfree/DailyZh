package com.yxy.zlp.dailyzh.util.httpUtil;

import android.os.Handler;
import android.os.Looper;

import java.util.concurrent.Executor;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Created by Administrator on 2016/6/2.
 */
public class AsyncHttpUtils {
    public static final int CPU_COUNT = Runtime.getRuntime().availableProcessors();
    private static final int CORE_POOL_SIZE = CPU_COUNT + 1;
    private static final int MAX_POOL_SIZE = 2 * CPU_COUNT + 1;
    private static final long KEEP_ALIVE = 5L;

    public static final Executor threadPoolExecutor = new ThreadPoolExecutor(CORE_POOL_SIZE,
            MAX_POOL_SIZE, KEEP_ALIVE, TimeUnit.SECONDS, new LinkedBlockingQueue<Runnable>());

    public static void get(final String url, final ResponseHandler responseHandler) {
        final Handler mHandler = new Handler(Looper.getMainLooper());

        Runnable requestRunnable = new Runnable() {
            @Override
            public void run() {
                final byte[] result = HttpUtils.get(url);
                if (result != null) {
                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            responseHandler.onSuccess(result);
                        }
                    });
                } else {
                    responseHandler.onFailure();
                }
            }
        };
        threadPoolExecutor.execute(requestRunnable);
    }


}
