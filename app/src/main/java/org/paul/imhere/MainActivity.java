package org.paul.imhere;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class MainActivity extends AppCompatActivity {

    // Used to load the 'native-lib' library on application startup.
    static {
        System.loadLibrary("native-lib");
    }

    TextView tv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Example of a call to a native method
        tv = (TextView) findViewById(R.id.sample_text);
        tv.setText(stringFromJNI());
        IpAsyncTask ipAsyncTask = new IpAsyncTask();
        ipAsyncTask.execute();
    }

    /**
     * A native method that is implemented by the 'native-lib' native library,
     * which is packaged with this application.
     */
    public native String stringFromJNI();

    private class IpAsyncTask extends AsyncTask<Void,Void, InetAddress>{

        @Override
        protected InetAddress doInBackground(Void... voids) {
            InetAddress inetAddress = null;
            try {
                inetAddress = InetAddress.getLocalHost();
            } catch (UnknownHostException e) {
                e.printStackTrace();
            }
            return inetAddress;
        }

        @Override
        protected void onPostExecute(InetAddress inetAddress) {
            super.onPostExecute(inetAddress);
            if(null!=inetAddress)
            {
                byte[] address = inetAddress.getAddress();
//                String canonicalHostName = inetAddress.getCanonicalHostName();//远程主机名
                String hostAddress = inetAddress.getHostAddress();
                String hostName = inetAddress.getHostName();

                StringBuffer stringBuffer = new StringBuffer();
                stringBuffer.append("address:").append(address).append("\n")
//                        .append("canonicalHostName:").append(canonicalHostName).append("\n")
                        .append("hostAddress:").append(hostAddress).append("\n")
                        .append("hostName").append(hostName).append("\n");
                tv.setText(stringBuffer.toString());
            }
        }
    }
}
