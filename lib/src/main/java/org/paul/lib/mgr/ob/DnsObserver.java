package org.paul.lib.mgr.ob;

public interface DnsObserver {

//    <T extends BaseBean>void update(DnsSubject subject, Object object, DnsTaskManager.TaskCallback callback, Class<T> clz);
//    <T extends BaseBean>String updateInstant(DnsSubject subject, Object object, Class<T> clz);
    void update(DnsSubject subject,Object object);
}
