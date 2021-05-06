package org.paul.lib.api;

import org.paul.lib.bean.DomainBean;

import java.util.HashMap;
import java.util.Map;

/**
 * 配置文件
 */
public final class Config {

    public static final String LOG_ENABLE = "log_enable";
    public static final String UPDATE_ENABLE = "update_enable";
    public static final String EXPIRED_ENABLE = "expired_enable";
    public static final String CACHE_ENABLE = "cache_enable";
    public static final String RESCOURCE_IP = "resource_ip";
    public static final String TIME_OUT = "time_out";
    public static final String ACCOUNT_ID = "account_id";
    public static final String AUTO_MODE = "auto_mode";

    public static final String PRE_HOSTS = "pre_host";

    public static final Map<String, DomainBean> ALL_HOSTS = new HashMap<>();

    /**
     * @param args
     */
    public void addHost(String... args) {

    }

}
