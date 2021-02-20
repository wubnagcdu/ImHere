package org.paul.lib.manager;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import java.io.IOException;
import java.net.*;

public class IpTest {

    public static void main(String... args) {
        try {
            HttpsURLConnection urlConnection = null;
            URL url = new URL("https", "www.baidu.com", "");
            final String host = url.getHost();
            InetAddress inetAddress = InetAddress.getByName(host);
            String hostAddress = inetAddress.getHostAddress();
            System.out.println(hostAddress);
            String authority = url.getAuthority();
            url = new URL("https", hostAddress, "");
            urlConnection = (HttpsURLConnection) url.openConnection();
            urlConnection.setHostnameVerifier(new HostnameVerifier() {
                @Override
                public boolean verify(String hostname, SSLSession session) {
                    boolean verify = HttpsURLConnection.getDefaultHostnameVerifier().verify(host, session);
                    return verify;
                }
            });
            System.out.println(url.toString());
            int responseCode = urlConnection.getResponseCode();
            System.out.println(responseCode);

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    static class SSLFc extends SSLSocketFactory {

        @Override
        public String[] getDefaultCipherSuites() {
            return new String[0];
        }

        @Override
        public String[] getSupportedCipherSuites() {
            return new String[0];
        }

        @Override
        public Socket createSocket(Socket s, String host, int port, boolean autoClose) throws IOException {
            return null;
        }

        @Override
        public Socket createSocket(String host, int port) throws IOException, UnknownHostException {
            return null;
        }

        @Override
        public Socket createSocket(String host, int port, InetAddress localHost, int localPort) throws IOException, UnknownHostException {
            return null;
        }

        @Override
        public Socket createSocket(InetAddress host, int port) throws IOException {
            return null;
        }

        @Override
        public Socket createSocket(InetAddress address, int port, InetAddress localAddress, int localPort) throws IOException {
            return null;
        }
    }

}
