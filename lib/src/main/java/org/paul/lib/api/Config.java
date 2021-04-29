package org.paul.lib.api;

import org.paul.lib.bean.DomainBean;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 配置文件
 */
class Config {

    static final String LOG_ENABLE="log_enable";
    static final String UPDATE_ENABLE="update_enable";
    static final String EXPIRED_ENABLE="expired_enable";
    static final String CACHE_ENABLE="cache_enable";
    static final String RESCOURCE_IP="resource_ip";
    static final String TIME_OUT="time_out";
    static final String ACCOUNT_ID="account_id";
    static final String AUTO_MODE="auto_mode";

    static final String PRE_HOSTS="pre_host";

    static final Map<String, DomainBean> ALL_HOSTS = new HashMap<>();

    /**
     *
     * @param args
     */
    void addHost(String...args){

    }

}
