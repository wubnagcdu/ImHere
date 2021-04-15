package org.paul.lib.mgr;

import org.json.JSONException;
import org.paul.lib.bean.DomainBean;
import org.paul.lib.utils.IoStreamUtil;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.atomic.AtomicInteger;

public class NetManager {
    private static class Holder {
        private static NetManager INSTANCE = new NetManager();
    }

    private NetManager() {
    }

    static NetManager getInstance() {
        return Holder.INSTANCE;
    }

    private static volatile String[] SERVER_IPS = new String[]{"", ""};
    private static AtomicInteger idx;

    private HttpURLConnection initConnection() throws IOException {
        HttpURLConnection httpURLConnection = null;
        URL url = new URL(SERVER_IPS[idx.get()]);
        HttpURLConnection httpUrlConnection = (HttpURLConnection) url.openConnection();
        return httpURLConnection;
    }

    DomainBean post() {
        HttpURLConnection httpURLConnection = null;
        try {
            httpURLConnection = initConnection();
            int responseCode = httpURLConnection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                String response = IoStreamUtil.getString(httpURLConnection.getInputStream());
                return DomainBean.newInstance(response);
            } else if (responseCode == HttpURLConnection.HTTP_UNAUTHORIZED) {
                //未授权帐户
                return null;
            } else {
                //retry
//                if (idx.get() == SERVER_IPS.length - 1) {
//                    idx.set(0);
//                } else {
//                    idx.incrementAndGet();
//                }
                return post();
            }
        } catch (IOException e) {
//            e.printStackTrace();
            return post();
        } catch (JSONException e) {
//            e.printStackTrace();
            return post();
        } finally {
            httpURLConnection.disconnect();
        }
    }

    void get() {

    }

    private static final int PROTECTED_LENGTH = 51200;// 输入流保护 50KB

    private String strFromStream(InputStream inputStream) throws IOException {

        return null;
    }
}
