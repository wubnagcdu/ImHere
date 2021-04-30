package org.paul.lib.mgr;

import org.paul.lib.bean.BaseBean;
import org.paul.lib.bean.DomainBean;
import org.paul.lib.err.AuthorizeErr;
import org.paul.lib.err.RetryErr;
import org.paul.lib.mgr.ob.DnsObserver;
import org.paul.lib.mgr.ob.DnsSubject;
import org.paul.lib.utils.LogUtil;

import java.io.IOException;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;

/**
 * 任务启动管理
 */
public final class DnsTaskManager implements DnsObserver {

    private static String[] ips = new String[]{"49.4.57.102", "49.4.121.11"};
    private static int idx;
    private ThreadManager threadManager;
    private NetManager netManager;

    @Override
    public void update(DnsSubject subject, Object object,TaskCallback callback,Class clz) {
        //TODO 启动任务
        excuteTaskAsync((String) object, callback,clz);
    }

    @Override
    public <T extends BaseBean> String updateInstant(DnsSubject subject, Object object,Class<T> clz) {
        T t = excuteTaskSync((String) object, clz);
        if(t instanceof DomainBean){
            return ((DomainBean) t).getIp();
        }
        return (String)object;
    }

    private static class Holder {
        private static DnsTaskManager INSTANCE = new DnsTaskManager();
    }

    private DnsTaskManager() {
        threadManager = ThreadManager.getInstance();
        netManager = NetManager.getInstance();
    }

    public static DnsTaskManager getInstance() {
        return Holder.INSTANCE;
    }

    class Task<T extends BaseBean> implements Callable<T> {

        private String domain;
        private Class<T> clz;
        private int retryTime;

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
            synchronized (DnsTaskManager.this) {
//                fixIp();
                if(retryTime>=ips.length){
                    return null;
                }
                if(++idx==ips.length){
                    idx=0;
                }
                retryTime++;
                return call();
            }
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
     * @return T
     */
    <T extends BaseBean> T excuteTaskSync(String domain, Class<T> clz) {
        T result = null;
        try {
            result = threadManager.submitTask(new Task<>(domain, clz));
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
    <T extends BaseBean> void excuteTaskAsync(String domain, TaskCallback taskCallback, Class<T> clz) {
        T result =null;
        try {
            result=threadManager.submitTask(new Task<>(domain,clz));
            taskCallback.onFinish(result);
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
