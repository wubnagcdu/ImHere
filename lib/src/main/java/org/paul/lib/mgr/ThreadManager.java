package org.paul.lib.mgr;

import org.paul.lib.bean.DomainBean;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ThreadManager {

    private static int THREAD_NUM;
    private ExecutorService mExecutorService;

    static {
        THREAD_NUM = Runtime.getRuntime().availableProcessors() + 1;
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

    void submitSync() {

    }

    void submitAsync() {

        mExecutorService.submit(new Callable<DomainBean>() {
            @Override
            public DomainBean call() throws Exception {
                return NetManager.getInstance().post();
            }
        });

    }

}
