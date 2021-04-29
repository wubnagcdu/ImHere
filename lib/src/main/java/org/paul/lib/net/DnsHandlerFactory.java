package org.paul.lib.net;

import java.net.URLStreamHandler;
import java.net.URLStreamHandlerFactory;

public class DnsHandlerFactory implements URLStreamHandlerFactory {
    @Override
    public URLStreamHandler createURLStreamHandler(String protocol) {
        if(protocol.equalsIgnoreCase("http")){

        }
        return null;//仅支持http请求
    }
}
