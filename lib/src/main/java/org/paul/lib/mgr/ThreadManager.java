package org.paul.lib.mgr;

import org.paul.lib.bean.BaseBean;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;


final class ThreadManager {

    private static int THREAD_NUM;
    private ExecutorService mExecutorService;

    static {
        THREAD_NUM = Runtime.getRuntime().availableProcessors();
    }

    private static class Holder {
        private static ThreadManager INSTANCE = new ThreadManager();
    }

    private ThreadManager() {
        mExecutorService = Executors.newFixedThreadPool(THREAD_NUM);
    }

    static ThreadManager getInstance() {
        return Holder.INSTANCE;
    }

    <T extends BaseBean> Future<T> submitTask(Callable<T> callable) {
        return mExecutorService.submit(callable);
    }
}
