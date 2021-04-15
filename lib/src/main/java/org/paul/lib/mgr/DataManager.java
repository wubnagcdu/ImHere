package org.paul.lib.mgr;

import android.content.Context;
import org.paul.lib.bean.DomainBean;
import org.paul.lib.service.DataHelper;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class DataManager {

    private DataHelper dataHelper;
    private ConcurrentMap<String, DomainBean> mem = new ConcurrentHashMap<>();

    private static class Holder {
        private static DataManager INSTANCE = new DataManager();
    }

    private DataManager() {
    }

    static DataManager getInstance(Context context) {
        DataManager instance = Holder.INSTANCE;
        if (null == instance.dataHelper) {
            instance.dataHelper = new DataHelper(context, 1);
        }
        return instance;
    }

    private DomainBean loadFromMem(String domain) {
        DomainBean domainBean = mem.get(domain);
        return domainBean;
    }

    private DomainBean loadFromDb(String domain) {
        DomainBean domainBean = dataHelper.query(domain);
        return domainBean;
    }

    private void writeToMem(DomainBean domainBean) {
        if (null != domainBean) {
            mem.put(domainBean.getDomainName(), domainBean);
        }
    }

    private void writeToDb(DomainBean domainBean) {
        if (null != domainBean) {
            dataHelper.update(domainBean);
        }
    }

    void loadIpByDomain(String domain) {
        DomainBean result = null;
        //优先从缓存读取
        result = loadFromMem(domain);
        if (null == result) {
            //从数据库读取
            result = loadFromDb(domain);
            mem.put(domain, result);
        }
        if (!checkDomain(result)) {
            //缓存不可用(包括过期、为空)
            ThreadManager.getInstance().submitSync();
        }
        //数据库不可用(包括过期、为空)
        //更新本地
    }

    private boolean checkDomain(DomainBean domainBean) {
        if (null == domainBean) {
            return false;
        }
        long queryTime = domainBean.getQueryTime();
        long ttl = domainBean.getTtl();
        boolean b = (queryTime + ttl) >= System.currentTimeMillis() / 1000;
        return b;//TODO 允许返回过期
    }
}
