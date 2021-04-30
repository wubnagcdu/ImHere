package org.paul.lib.mgr;

import android.content.Context;
import org.paul.lib.bean.BaseBean;
import org.paul.lib.bean.DomainBean;
import org.paul.lib.mgr.callback.Host2IpConverter;
import org.paul.lib.mgr.ob.DnsSubject;
import org.paul.lib.service.DbHelper;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * 数据操作类 观察者 当发现数据过期或不存在的数据时 驱动更新缓存
 */
public final class DataManager extends DnsSubject implements Host2IpConverter {

    private DbHelper dbHelper;
    private ConcurrentMap<String, DomainBean> mem = new ConcurrentHashMap<>();
    private boolean expired;


    @Override
    public String host2Ip(String host,boolean instant) {
        DomainBean domainBean = loadFromMem(host);
        if (null == domainBean) {
            domainBean = loadFromDb(host);
        }
        if (checkDomain(domainBean)) {
            return domainBean.getIp();
        } else {
            //TODO 启动更新ip-host任务 异步执行
            setChanged();
            if(instant) {
                notifyObservers(host, new DnsTaskManager.TaskCallback() {
                    @Override
                    public <T extends BaseBean> void onFinish(T t) {
                        if (t instanceof DomainBean) {
                            DomainBean bean = (DomainBean) t;
                            writeToMem(bean);
                            writeToDb(bean);
                        }
                    }
                }, DomainBean.class);
                return host;
            }else {
                return notifyObserversInstant(host, DomainBean.class);
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
        }
        return instance;
    }

    private DomainBean loadFromMem(String domain) {
        DomainBean domainBean = mem.get(domain);
        return domainBean;
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
        long queryTime = domainBean.getQueryTime();
        long ttl = domainBean.getTtl();
        boolean isOutTime = (queryTime + ttl) >= System.currentTimeMillis() / 1000;//未过期ip
        return isOutTime || expired;
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
