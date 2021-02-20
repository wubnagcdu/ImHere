package org.paul.lib.manager;

import org.apache.commons.jnet.Installer;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLStreamHandler;
import java.net.URLStreamHandlerFactory;

public class UrlMgr {

    private static class Holder {
        private static UrlMgr INSTANCE = new UrlMgr();
    }

    private UrlMgr() {
    }

    public static UrlMgr getInstance() {
        return Holder.INSTANCE;
    }

    public void enableAuto(boolean enabled) {
        try {
            Installer.setURLStreamHandlerFactory(new URLStreamHandlerFactory() {
                @Override
                public URLStreamHandler createURLStreamHandler(String protocol) {
                    return null;
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    static class UrlHandler extends URLStreamHandler {

        private URLStreamHandler urlStreamHandler;

        UrlHandler(URLStreamHandler urlStreamHandler) {
            this.urlStreamHandler = urlStreamHandler;
        }

        @Override
        protected URLConnection openConnection(URL url) throws IOException {
            Method method = null;
            try {
                method = URLStreamHandler.class.getDeclaredMethod("openConnection",
                        new Class[]{URL.class});
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            }
            method.setAccessible(true);
            URLConnection urlConnection = null;
            try {
                urlConnection = (URLConnection) method.invoke(this.urlStreamHandler,
                        new Object[]{url});
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
            return urlConnection;
        }
    }

}
