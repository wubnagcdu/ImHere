package org.paul.lib.bean;

import android.content.ContentValues;
import org.json.JSONException;
import org.json.JSONObject;

public final class DomainBean extends BaseBean {

    private String domainName;
    private String[] ips;
    private long ttl;
    private long queryTime;

    public DomainBean(String domainName, String[] ips, long ttl, long queryTime) {
        this.domainName = domainName;
        this.ips = ips;
        this.ttl = ttl;
        this.queryTime = queryTime;
    }

//    public DomainBean newInstance(String response) throws JSONException {
//        JSONObject jsonObject = new JSONObject(response);
//        String domainName = jsonObject.getString("domainName");
//        String[] ips = jsonObject.getString("ips").split(",");
//        long ttl = jsonObject.getLong("ttl");
//        long queryTime = System.currentTimeMillis() / 1000;
//        DomainBean domainBean = new DomainBean(domainName, ips, ttl, queryTime);
//        return domainBean;
//    }

    public String getDomainName() {
        return domainName;
    }

    public String[] getIps() {
        return ips;
    }

    int idx;

    public String getIp() {

        int length = ips.length;
        if (idx > length - 1) {
            idx = 0;
        }
        String ip = ips[idx];
        idx++;
        return ip;
    }

    public long getTtl() {
        return ttl;
    }

    public long getQueryTime() {
        return queryTime;
    }

    @Override
    public ContentValues toContentValues() {
        ContentValues contentValues = new ContentValues();
        contentValues.put("domainName", domainName);
        contentValues.put("ips", ips.toString());
        contentValues.put("ttl", ttl);
        contentValues.put("queryTime", queryTime);
        return contentValues;
    }

}
