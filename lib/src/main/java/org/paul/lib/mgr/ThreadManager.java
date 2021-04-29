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

//    Future<DomainBean> submitSync() throws ExecutionException, InterruptedException {
//        return mExecutorService.submit(new Callable<DomainBean>() {
//            @Override
//            public DomainBean call() throws Exception {
//                return NetManager.getInstance().getRequest(spec,DomainBean.class);
//            }
//        });
//    }

    /**
     * 异步带回调
     *
     * @param spec
     * @param taskCallback
     * @param clz
     * @param <T>
     * @throws ExecutionException
     * @throws InterruptedException
     */
    <T extends BaseBean> void submitAsync(final String spec, final TaskManager.TaskCallback taskCallback, final Class<T> clz) throws ExecutionException, InterruptedException {
        T t = mExecutorService.submit(new Callable<T>() {
            @Override
            public T call() throws Exception {
                return netManager.getRequest(spec, clz);
            }
        }).get();
        taskCallback.onFinish(t);
    }

    /**
     * 同步返回
     *
     * @param task
     * @param <T>
     * @return
     * @throws ExecutionException
     * @throws InterruptedException
     */
    <T extends BaseBean> T submitSync(Callable<T>task) throws ExecutionException, InterruptedException {
        return mExecutorService.submit(task).get();
    }

}
