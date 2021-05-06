package org.paul.lib.mgr;

import android.text.TextUtils;
import org.paul.lib.api.Config;
import org.paul.lib.bean.BaseBean;
import org.paul.lib.err.AuthorizeErr;
import org.paul.lib.err.RetryErr;
import org.paul.lib.mgr.ob.DnsSubject;
import org.paul.lib.utils.LogUtil;

import java.io.IOException;
import java.util.Locale;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;

/**
 * 任务启动管理
 */
public final class DnsTaskManager /*implements DnsObserver */extends DnsSubject{

    private static String[] ips = new String[]{"49.4.57.102", "49.4.121.11"};
    private static int idx;
    private ThreadManager threadManager;
    private static NetManager netManager;
    private static SharedManager sharedManager;

    public static class Holder {
        private static DnsTaskManager INSTANCE = new DnsTaskManager();
    }

    private DnsTaskManager() {
        threadManager = ThreadManager.getInstance();
        netManager = NetManager.getInstance();
    }

    public static DnsTaskManager getInstance(SharedManager sharedManager) {
        DnsTaskManager instance = Holder.INSTANCE;
        if(null==instance.sharedManager){
            if(null!=sharedManager){
                instance.sharedManager=sharedManager;
            }
        }
        return instance;
    }

    class Task<T extends BaseBean> implements Callable<T> ,Runnable{
        private String domain;
        private Class<T> clz;
        private int retryTime;

        Task(String domain, Class<T> clz) {
            this.domain = domain;
            this.clz = clz;
        }

        @Override
        public T call(){
            try {
                T response = netManager.getRequest(buildSpec(domain), clz);
                notifyObservers(response);
                return response;
            } catch (IOException e) {
                return retry();
            } catch (RetryErr retryErr) {
                return retry();
            } catch (AuthorizeErr authorizeErr) {
                return null;
            }
        }

        private T retry() {
            synchronized (Task.this) {
                if (retryTime >= ips.length) {
                    return null;
                }
                if (++idx == ips.length) {
                    idx = 0;
                }
                retryTime++;
                return call();
            }
        }

        @Override
        public void run() {
            call();
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
        try {
            return threadManager.submitTask(new Task<T>(domain, clz)).get();
        } catch (ExecutionException e) {
            LogUtil.logE(e);
        } catch (InterruptedException e) {
            LogUtil.logE(e);
        }
        return null;
    }

    /**
     * 异步任务
     */
    <T extends BaseBean> void excuteTaskAsync(String domain, Class<T> clz) {
        threadManager.submitTask(new Task<T>(domain,clz));
    }

    /**
     * 根据domain 以及用户id创建请求url
     *
     * @param domain
     * @return
     */
    private static String buildSpec(String domain) {
        StringBuilder stringBuilder = new StringBuilder();
        String account = sharedManager.read(Config.ACCOUNT_ID, "1000013");
        String sourceIp = sharedManager.read(Config.RESCOURCE_IP, "");
        String apiPath = String.format(Locale.getDefault(), "/%s/resolve?domain=%s", account, domain);
        stringBuilder.append("http://").append(ips[idx]).append(apiPath).append(
                TextUtils.isEmpty(sourceIp) ? "" :
                        String.format(Locale.getDefault(), "&ip=%s", sourceIp)
        );
        return stringBuilder.toString();
    }

}
