package org.paul.lib.mgr;

import android.content.Context;
import org.paul.lib.bean.DomainBean;
import org.paul.lib.mgr.callback.Host2IpConverter;
import org.paul.lib.mgr.ob.DnsObserver;
import org.paul.lib.mgr.ob.DnsSubject;
import org.paul.lib.service.DbHelper;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * 数据操作类
 */
public final class DataManager /*extends DnsSubject*/ implements Host2IpConverter, DnsObserver {

    private DbHelper dbHelper;
    private ConcurrentMap<String, DomainBean> mem = new ConcurrentHashMap<>();
    private boolean expired;
    private DnsTaskManager dnsTaskManager;
    private SharedManager sharedManager;


    @Override
    public String host2Ip(String host, boolean instant) {
        DomainBean domainBean = loadFromMem(host);
        if (null == domainBean) {
            domainBean = loadFromDb(host);
        }
        if (checkDomain(domainBean)) {
            if (!isInTime(domainBean)) {
                dnsTaskManager.excuteTaskAsync(host, DomainBean.class);
            }
            return domainBean.getIp();
        } else {
            if (instant) {
                return dnsTaskManager.excuteTaskSync(host, DomainBean.class).getIp();
            } else {
                dnsTaskManager.excuteTaskAsync(host, DomainBean.class);
                return host;
            }
        }
    }

    @Override
    public void update(DnsSubject subject, Object object) {
        if (null != object) {
            if (object instanceof DomainBean) {
                DomainBean domainBean = (DomainBean) object;
                domainBean.setQueryTime(System.currentTimeMillis() / 1000);
                writeToMem(domainBean);
                writeToDb(domainBean);
            }
        }
    }

    private static class Holder {
        private static DataManager INSTANCE = new DataManager();
    }

    public static DataManager getInstance(Context context) {
        DataManager instance = Holder.INSTANCE;
        if (null == instance.dbHelper) {
            instance.dbHelper = new DbHelper(context);
            instance.sharedManager = SharedManager.getInstance(context);
            instance.dnsTaskManager = DnsTaskManager.getInstance(instance.sharedManager);
            instance.dnsTaskManager.setDnsObserver(instance);
        }
        return instance;
    }

    private DomainBean loadFromMem(String domain) {
        return mem.get(domain);
    }

    private DomainBean loadFromDb(String domain) {
        return dbHelper.query(domain);
    }

    private void writeToMem(DomainBean domainBean) {
        if (null != domainBean) {
            mem.put(domainBean.getDomainName(), domainBean);
        }
    }

    private void writeToDb(DomainBean domainBean) {
        if (null != domainBean) {
            dbHelper.update(domainBean);
        }
    }

    /**
     * 验证数据是否可用（是否为空，是否允许返回过期ip）
     *
     * @param domainBean
     * @return
     */
    private boolean checkDomain(DomainBean domainBean) {
        if (null == domainBean) {
            return false;
        }
        boolean isInTime = isInTime(domainBean);
        return isInTime || expired;
    }

    /**
     * 是否过期
     *
     * @param domainBean
     * @return
     */
    private boolean isInTime(DomainBean domainBean) {
        long queryTime = domainBean.getQueryTime();
        long ttl = domainBean.getTtl();
        return (queryTime + ttl) >= System.currentTimeMillis() / 1000;
    }

    /**
     * 设置是否允许过期
     *
     * @param expired
     */
    public void setExpired(boolean expired) {
        this.expired = expired;
    }
}
