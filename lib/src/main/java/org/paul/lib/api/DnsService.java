package org.paul.lib.api;

import android.content.Context;
import org.paul.lib.mgr.DataManager;
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

    public static void init(Context context) {
        if (null == DnsService.CONTEXT) {
            CONTEXT = context.getApplicationContext();
            initMgr();
        }
    }

    private static void initMgr() {
        sharedManager = SharedManager.getInstance(CONTEXT);
        dataManager = DataManager.getInstance(CONTEXT);
        boolean autoMode = sharedManager.read(Config.AUTO_MODE, false);

        if (autoMode) {
            StreamManager.autoMode(dataManager);
        }
    }

}
