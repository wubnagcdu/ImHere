package org.paul.lib.api;

import android.content.Context;
import org.paul.lib.mgr.DataManager;
import org.paul.lib.mgr.SharedManager;
import org.paul.lib.mgr.StreamManager;

import java.util.List;

public class HttpDnsService implements HttpDnsInterface{

    private Context context;
    private SharedManager sharedManager;
    private DataManager dataManager;

    /**
     * 设置是否允许日志打印
     * @param value
     */
    @Override
    public void setLogEnable(boolean value) {
        sharedManager.write(Config.LOG_ENABLE,value);
    }

    /**
     * 设置是否允许持久化缓存 （sqlite）
     * @param value
     */
    @Override
    public void setCacheEnable(boolean value) {
        sharedManager.write(Config.CACHE_ENABLE,value);
    }

    /**
     * 设置是否允许网络切换后更新ip
     * @param value
     */
    @Override
    public void setUpdateAfterNetChangedEnable(boolean value) {
        sharedManager.write(Config.UPDATE_ENABLE,value);
    }

    /**
     * 设置是否允许返回过期ip
     * @param value
     */
    @Override
    public void setExpireEnable(boolean value) {
        sharedManager.write(Config.EXPIRED_ENABLE,value);
        dataManager.setExpired(value);
    }

    /**
     * 设置降级逻辑
     * 由用户自己实现具体逻辑 自动在使用过程中使用
     * @param downGradingFilter
     */
    @Override
    public void setDonwGradingFilter(DownGradingFilter downGradingFilter) {

    }

    /**
     * 设置预解析域名集合
     * @param hosts
     */
    @Override
    public void setPreHosts(List<String> hosts) {
        if(null==hosts){
            throw new IllegalArgumentException("hosts should not be null");
        }
        sharedManager.write(Config.PRE_HOSTS, hosts.toArray(new String[0]));
    }

    /**
     * 根据参数domain返回缓存ip，
     * 如果同步获取 则判断是否有缓存，
     * 如果没有缓存则直接请求数据并返回，该方式会阻塞线程
     * 如果异步获取 则判断是否有缓存，
     * 如果没有缓存则返回原host并异步启动更新缓存任务，该方式不会阻塞线程
     * @param domain 请求域名
     * @param instant 是否异步
     * @return
     */
    public String getIp(String domain,boolean instant){
        return dataManager.host2Ip(domain,instant);
    }

    /**
     * 单例模式辅助类
     */
    private static class Holder{
        private static final HttpDnsService INSTANCE = new HttpDnsService();
    }

    private HttpDnsService() {
    }

    /**
     * 单例模式提供方法
     * @return HttpDnsService
     */
    public static HttpDnsService getInstance(Context context,String account, boolean auto){
        HttpDnsService httpDnsService = Holder.INSTANCE;
        if(null==httpDnsService.context){
            if(null==context){
                throw new IllegalArgumentException("context should not be null");
            }
            httpDnsService.context=context.getApplicationContext();
            httpDnsService.sharedManager=SharedManager.getInstance(httpDnsService.context);
            httpDnsService.dataManager=DataManager.getInstance(httpDnsService.context);
            httpDnsService.sharedManager.write(Config.ACCOUNT_ID,account);
            if(auto) {
                StreamManager.autoMode(httpDnsService.dataManager);
            }
            httpDnsService.sharedManager.write(Config.AUTO_MODE,auto);
        }
        return httpDnsService;
    }



}
