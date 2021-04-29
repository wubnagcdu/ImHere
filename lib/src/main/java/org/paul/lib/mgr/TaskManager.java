package org.paul.lib.mgr;

import org.paul.lib.bean.BaseBean;
import org.paul.lib.bean.DomainBean;
import org.paul.lib.err.AuthorizeErr;
import org.paul.lib.err.RetryErr;
import org.paul.lib.utils.LogUtil;

import java.io.IOException;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;

/**
 * 任务启动管理
 */
public final class TaskManager {

    private static String[] ips = new String[]{"49.4.57.102", "49.4.121.11"};
    private static int idx;
    private ThreadManager threadManager;
    private NetManager netManager;

    private static class Holder {
        private static TaskManager INSTANCE = new TaskManager();
    }

    private TaskManager() {
        threadManager = ThreadManager.getInstance();
        netManager = NetManager.getInstance();
    }

    public static TaskManager getInstance() {
        return Holder.INSTANCE;
    }

    class Task<T extends BaseBean> implements Callable<T> {

        private String domain;
        private Class<T> clz;

        Task(String domain, Class<T> clz) {
            this.domain = domain;
            this.clz = clz;
        }

        @Override
        public T call() {
            try {
                return netManager.getRequest(buildSpec(domain), clz);
            } catch (IOException e) {
                return retry();
            } catch (RetryErr retryErr) {
                return retry();
            } catch (AuthorizeErr authorizeErr) {
                return null;
            }
        }

        private T retry() {
            fixIp();
            return call();
        }
    }

    private synchronized void fixIp() {
        if (++idx == ips.length) {
            idx = 0;
        }
    }

    public interface TaskCallback {
        <T extends BaseBean> void onFinish(T t);
    }

    /**
     * 同步任务
     *
     * @return DomainBean
     */
    DomainBean excuteTaskSync(String domain) {
        DomainBean result = null;
        try {
            result = threadManager.submitSync(new Task<>(domain, DomainBean.class));
        } catch (ExecutionException e) {
            LogUtil.logE(e);
        } catch (InterruptedException e) {
            LogUtil.logE(e);
        }
        return result;
    }

    /**
     * 异步任务
     */
    void excuteTaskAsync(String domain,TaskCallback taskCallback) {
        try {
            ThreadManager.getInstance().submitAsync(domain,taskCallback,DomainBean.class);
        } catch (InterruptedException e) {
            LogUtil.logE(e);
        } catch (ExecutionException e) {
            LogUtil.logE(e);
        }
    }

    private String buildSpec(String domain) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("http://").append(ips[idx]).append(100).append(domain);
        //TODO ip 相关构造 url 地址
        return stringBuilder.toString();
    }

}
