package org.paul.lib.mgr;

import com.google.gson.Gson;
import org.paul.lib.bean.BaseBean;
import org.paul.lib.err.AuthorizeErr;
import org.paul.lib.err.RetryErr;
import org.paul.lib.utils.IoStreamUtil;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

final class NetManager {
    private static class Holder {
        private static NetManager INSTANCE = new NetManager();
    }

    private NetManager() {
    }

    static NetManager getInstance() {
        return Holder.INSTANCE;
    }

    private HttpURLConnection initConnection(String spec) throws IOException {
        URL url = new URL(spec);
        return (HttpURLConnection) url.openConnection();
    }

//    DomainBean post() throws IOException, JSONException, RetryErr {
//        HttpURLConnection httpURLConnection = null;
//        try {
//            httpURLConnection = initConnection();
//            httpURLConnection.setRequestMethod("post");
//            int responseCode = httpURLConnection.getResponseCode();
//            if (responseCode == HttpURLConnection.HTTP_OK) {
//                String response = IoStreamUtil.getString(httpURLConnection.getInputStream());
//                return DomainBean.newInstance(response);
//            } else if (responseCode == HttpURLConnection.HTTP_UNAUTHORIZED) {
//                //未授权帐户
//                return null;
//            }else{
//                throw new RetryErr();
//            }
//        } finally {
//            httpURLConnection.disconnect();
//        }
//    }

    <T extends BaseBean> T getRequest(String spec, Class<T> clz) throws IOException, RetryErr, AuthorizeErr {
        HttpURLConnection httpURLConnection = null;
        try {
            httpURLConnection = initConnection(spec);
            httpURLConnection.setRequestMethod("GET");
            int responseCode = httpURLConnection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                String response = IoStreamUtil.getString(httpURLConnection.getInputStream());
                return new Gson().fromJson(response, clz);
            } else if (responseCode == HttpURLConnection.HTTP_UNAUTHORIZED) {
                //未授权帐户
                throw new AuthorizeErr();
            } else {
                //需要重试
                throw new RetryErr();
            }
        } finally {
            if (null != httpURLConnection) {
                httpURLConnection.disconnect();
            }
        }
    }
}
