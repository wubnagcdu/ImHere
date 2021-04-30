package org.paul.lib.mgr;

import org.paul.lib.bean.BaseBean;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

final class ThreadManager {

    private static int THREAD_NUM;
    private ExecutorService mExecutorService;
    private NetManager netManager;

    static {
        THREAD_NUM = Runtime.getRuntime().availableProcessors() + 1;
    }

    private static class Holder {
        private static ThreadManager INSTANCE = new ThreadManager();
    }

    private ThreadManager() {
        mExecutorService = Executors.newFixedThreadPool(THREAD_NUM);
        netManager = NetManager.getInstance();
    }

    static ThreadManager getInstance() {
        return Holder.INSTANCE;
    }

    <T extends BaseBean> T submitTask(Callable<T>task) throws ExecutionException, InterruptedException {
        return mExecutorService.submit(task).get();
    }

}
