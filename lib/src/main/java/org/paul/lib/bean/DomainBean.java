package org.paul.lib.bean;

import android.content.ContentValues;
import org.json.JSONException;
import org.json.JSONObject;

public final class DomainBean extends BaseBean {

    private String domain;
    private String[] ips;
    private long ttl;
    private long queryTime;

    public DomainBean(String domain, String[] ips, long ttl, long queryTime) {
        this.domain = domain;
        this.ips = ips;
        this.ttl = ttl;
        this.queryTime = queryTime;
    }

    public void setQueryTime(long queryTime) {
        this.queryTime = queryTime;
    }

    public String getDomainName() {
        return domain;
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
        contentValues.put("domainName", domain);
        contentValues.put("ips", ips.toString());
        contentValues.put("ttl", ttl);
        contentValues.put("queryTime", queryTime);
        return contentValues;
    }

}
