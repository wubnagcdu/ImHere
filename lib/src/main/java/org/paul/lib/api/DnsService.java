package org.paul.lib.api;

import android.content.Context;
import org.paul.lib.mgr.DataManager;
import org.paul.lib.mgr.DnsTaskManager;
import org.paul.lib.mgr.SharedManager;
import org.paul.lib.mgr.StreamManager;

import java.util.List;

/**
 * sdk 控制入口
 */
public final class DnsService {

    private static volatile boolean INITED;
    private static Context CONTEXT;
    private static SharedManager sharedManager;
    private static DataManager dataManager;

    public static void setPreHosts(List<String> hosts) {
        sharedManager.write(Config.PRE_HOSTS, hosts.toArray(new String[hosts.size()]));
    }

    public static void setResourceIp(String ip) {
        sharedManager.write(Config.RESCOURCE_IP, ip);
    }

    public static void setRequestTimeout(int time) {
        sharedManager.write(Config.TIME_OUT, time);
    }

    public static void setAccountId(String account) {
        sharedManager.write(Config.ACCOUNT_ID, account);
    }

    public static void setCachedEnable(boolean enable) {
        sharedManager.write(Config.CACHE_ENABLE, enable);
    }

    public static void setUpdateAfterNetChangedEnable(boolean enable) {
        sharedManager.write(Config.UPDATE_ENABLE, enable);
    }

    public static void setExpiredEnable(boolean enable) {
        sharedManager.write(Config.EXPIRED_ENABLE, enable);
    }

    public static void setLogEnable(boolean enable) {
        sharedManager.write(Config.LOG_ENABLE, enable);
    }

    public static void setDownGradingFilter(DownGradingFilter filter) {
    }

    public static void init(Context context, boolean autoMode) {
        if (null == DnsService.CONTEXT) {
            CONTEXT = context.getApplicationContext();
            initMgr(autoMode);
        }
    }

    private static void initMgr(boolean auto) {
        sharedManager = SharedManager.getInstance(CONTEXT);
        dataManager = DataManager.getInstance(CONTEXT);
        sharedManager.write(Config.AUTO_MODE,auto);
        if (auto) {
//            dataManager.setDnsObserver(DnsTaskManager.getInstance(sharedManager));
            StreamManager.autoMode(dataManager);
        }
    }

    /**
     * 根据参数domain返回缓存ip，
     * 如果同步获取 则判断是否有缓存，
     * 如果没有缓存则直接请求数据并返回，该方式会阻塞线程
     * 如果异步获取 则判断是否有缓存，
     * 如果没有缓存则返回原host并异步启动更新缓存任务，该方式不会阻塞线程
     * @param domain
     * @param instant 是否异步
     * @return
     */
    public String getIp(String domain,boolean instant){
        return dataManager.host2Ip(domain,instant);
    }

}
