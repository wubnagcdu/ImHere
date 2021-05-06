package org.paul.lib.mgr;

import org.paul.lib.api.DownGradingFilter;
import org.paul.lib.mgr.callback.Host2IpConverter;
import org.paul.lib.utils.IpUtil;
import org.paul.lib.utils.LogUtil;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.net.*;
import java.util.*;

/**
 * 修改jvm网络协议，将用户的http／https类型请求监听并修改请求地址中的host为缓存的ip
 */
public final class StreamManager {

    private static final Map<String, URLStreamHandler> STREAM_HANDLER_MAP = new HashMap();
    private static final List<String> PROTOCOLS = new ArrayList();
    private volatile static Boolean ENABLE_CONVERT = null;
    private static Host2IpConverter HOST2IPCONVERTER;
    private static DownGradingFilter defaultFilter = new DownGradingFilter() {
        @Override
        public boolean shouldGrade(String host) {
            //默认不降级
            return false;
        }
    };
    private static DownGradingFilter userFilter;

    public static void setUserFilter(DownGradingFilter userFilter) {
        StreamManager.userFilter = userFilter;
    }

    /**
     * 加载固定类型协议
     */
    static {
        PROTOCOLS.add("http");
        PROTOCOLS.add("https");
    }

    /**
     * 启动自动模式
     *
     * @return
     */
    public static boolean autoMode(Host2IpConverter host2IpConverter) {
        if (ENABLE_CONVERT != null) {
            return ENABLE_CONVERT.booleanValue();
        }
        setHost2IpConverter(host2IpConverter);
        try {
            Iterator iterator = PROTOCOLS.iterator();
            while (iterator.hasNext()) {
                String next = (String) iterator.next();
                new URL(next, "feona.net", "");
            }
            Field field = URL.class.getDeclaredField("handlers");
            if(null==field){
                field=URL.class.getDeclaredField("streamHandlers");
            }
            field.setAccessible(true);

            Hashtable hashtable = (Hashtable) field.get(null);
            Iterator iterator1 = PROTOCOLS.iterator();
            while (iterator1.hasNext()) {
                String next = (String) iterator1.next();
                URLStreamHandler urlStreamHandler = (URLStreamHandler) hashtable.get(next);
                STREAM_HANDLER_MAP.put(next, urlStreamHandler);
            }
            URL.setURLStreamHandlerFactory(new URLStreamHandlerFactory() {
                public URLStreamHandler createURLStreamHandler(String s) {
                    return STREAM_HANDLER_MAP.containsKey(s) ?
                            new Handler((URLStreamHandler) STREAM_HANDLER_MAP.get(s)) : null;
                }
            });
            ENABLE_CONVERT = Boolean.TRUE;
        } catch (MalformedURLException e) {
            ENABLE_CONVERT = Boolean.FALSE;
            LogUtil.logE(e);
        } catch (NoSuchFieldException e) {
            ENABLE_CONVERT = Boolean.FALSE;
            LogUtil.logE(e);
        } catch (IllegalAccessException e) {
            ENABLE_CONVERT = Boolean.FALSE;
            LogUtil.logE(e);
        }
        return ENABLE_CONVERT.booleanValue();
    }

    /**
     * 自定义协议生产类
     */
    static class Handler extends URLStreamHandler {
        private URLStreamHandler handler;

        public Handler(URLStreamHandler urlStreamHandler) {
            this.handler = urlStreamHandler;
        }

        /********
         *
         * @param url
         * @return
         * @throws IOException
         */
        protected URLConnection openConnection(URL url)
                throws IOException {
            try {
                Method method = URLStreamHandler.class.getDeclaredMethod("openConnection",
                        new Class[]{URL.class});
                method.setAccessible(true);
                URLConnection urlConnection = (URLConnection) method.invoke(this.handler,
                        new Object[]{buildUrl(url)});
                LogUtil.logD(urlConnection.getURL().toString());
                return urlConnection;
            } catch (Exception e) {
                throw new IOException();
            }
        }

        /*********
         *
         * @param url
         * @param proxy
         * @return
         * @throws IOException
         */
        protected URLConnection openConnection(URL url, Proxy proxy)
                throws IOException {
            try {
                Method method = URLStreamHandler.class.getDeclaredMethod("openConnection",
                        new Class[]{URL.class, Proxy.class});
                method.setAccessible(true);
                URLConnection urlConnection = (URLConnection) method.invoke(this.handler,
                        new Object[]{buildUrl(url), proxy});
                return urlConnection;
            } catch (Exception e) {
                throw new IOException();
            }
        }
    }

    /**
     * 重新制定url
     * 将用户的host更改为本地缓存的ip
     * 如果ip不可用（ip为空 或已过期且用户不允许返回过期ip）则返回host并启动更新本地缓存任务
     *
     * @param url
     * @return
     * @throws MalformedURLException
     */
    private static URL buildUrl(URL url) throws MalformedURLException {
        String spec = url.toString();
        String host = url.getHost();
        if (checkHost(host)) {
            return url;
        }
        String ip = HOST2IPCONVERTER.host2Ip(host, false);
        String s = spec.replaceFirst(host, ip);
        return new URL(s);
    }

    /**
     *  是否需要降级 除去已经替换的 以及服务器地址 以及用户配置降级地址
     * @param host
     * @return
     */
    private static boolean checkHost(String host) {
        return IpUtil.isIpv4(host) || IpUtil.isIpv6(host) || defaultFilter.shouldGrade(host)
                || (null != userFilter && userFilter.shouldGrade(host));
    }

    private static void setHost2IpConverter(Host2IpConverter host2IPCONVERTER) {
        StreamManager.HOST2IPCONVERTER = host2IPCONVERTER;
    }
}
