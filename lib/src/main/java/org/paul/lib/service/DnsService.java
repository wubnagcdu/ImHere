package org.paul.lib.service;

import android.os.Handler;
import android.os.HandlerThread;

public class DnsService {

    private static class Holder{
        private static DnsService INSTANCE=new DnsService();
    }

    private DnsService(){
        mDnsThread = new DnsThread(DNS_SERVICE);
        mDnsThread.start();
    }

    public enum DNS_MODE{
        MANUAL,//手动
        PASSIVE;//自动
    }

    static DnsService getInstance(){
        return Holder.INSTANCE;
    }

    private static final String DNS_SERVICE="dns_service";
    private DnsThread mDnsThread;
    private volatile Handler mHandler;

    private class DnsThread extends HandlerThread{

        public DnsThread(String name) {
            super(name);
        }

        @Override
        protected void onLooperPrepared() {
            super.onLooperPrepared();
            mHandler=new Handler(getLooper());
        }
    }

    void updateAll(){
        mHandler.post(new Runnable() {
            @Override
            public void run() {

            }
        });
    }
    void updateDns(String...args){

    }

    public interface UpdateIp{
        void update(String host);
    }

}
