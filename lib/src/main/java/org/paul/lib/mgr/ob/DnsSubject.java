package org.paul.lib.mgr.ob;

import org.paul.lib.mgr.DnsTaskManager;

import java.util.Vector;

public class DnsSubject {
    private boolean changed = false;
    private DnsObserver dnsObserver;
//    private Vector<DnsObserver> obs = new Vector<>();
//    public synchronized void addObserver(DnsObserver o) {
//        if (o == null)
//            throw new NullPointerException();
//        if (!obs.contains(o)) {
//            obs.addElement(o);
//        }
//    }
//    public synchronized void deleteObserver(DnsObserver o) {
//        obs.removeElement(o);
//    }
//    public void notifyObservers() {
//        notifyObservers(null);
//    }


    public void setDnsObserver(DnsObserver dnsObserver) {
        this.dnsObserver = dnsObserver;
    }

    public void notifyObservers(Object arg, DnsTaskManager.TaskCallback callback,Class clz) {
//        Object[] arrLocal;
//        synchronized (this) {
//            if (!hasChanged())
//                return;
//            arrLocal = obs.toArray();
//            clearChanged();
//        }
//
//        for (int i = arrLocal.length-1; i>=0; i--)
//            ((DnsObserver)arrLocal[i]).update(this, arg);
        dnsObserver.update(this,arg,callback,clz);
    }
    public String notifyObserversInstant(Object arg,Class clz){
//        Object[]arrLocal;
//        synchronized (this){
//            if(!hasChanged()){
//                return (String)arg;
//            }
//            arrLocal=obs.toArray();
//            clearChanged();
//        }
//        for(int i=arrLocal.length-1;i>=0;i--){
//            ((DnsObserver)arrLocal[i]).updateInstant(this,arg);
//        }
        return dnsObserver.updateInstant(this,arg,clz);
    }
//    public synchronized void deleteObservers() {
//        obs.removeAllElements();
//    }
    protected synchronized void setChanged() {
        changed = true;
    }
    protected synchronized void clearChanged() {
        changed = false;
    }
    public synchronized boolean hasChanged() {
        return changed;
    }
//    public synchronized int countObservers() {
//        return obs.size();
//    }
}
