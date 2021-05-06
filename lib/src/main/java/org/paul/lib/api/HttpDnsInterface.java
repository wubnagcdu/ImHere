package org.paul.lib.api;

import java.util.List;

interface HttpDnsInterface {

    void setLogEnable(boolean value);
    void setCacheEnable(boolean value);
    void setUpdateAfterNetChangedEnable(boolean value);
    void setExpireEnable(boolean value);

    void setDonwGradingFilter(DownGradingFilter downGradingFilter);
    void setPreHosts(List<String> hosts);
    String getIp(String domain,boolean instant);

}
