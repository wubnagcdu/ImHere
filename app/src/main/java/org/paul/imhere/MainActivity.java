package org.paul.imhere;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import okhttp3.*;
import org.paul.lib.api.DnsService;
import org.paul.lib.api.HttpDnsService;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

public class MainActivity extends AppCompatActivity {

    // Used to load the 'native-lib' library on application startup.
    static {
        System.loadLibrary("native-lib");
    }

    TextView tv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        DnsService.init(this,true);
        HttpDnsService.getInstance(this,"1000013",true);

        setContentView(R.layout.activity_main);

        // Example of a call to a native method
        TextView original = (TextView) findViewById(R.id.original);
        TextView okhttp = findViewById(R.id.okhttp);
//        tv.setText(stringFromJNI());

        original.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TaskS taskS=new TaskS();
                taskS.execute();
            }
        });
        okhttp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDataAsync();
            }
        });
    }
    private void getDataAsync() {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url("http://www.baidu.com")
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if(response.isSuccessful()){//回调的方法执行在子线程。
//                    Log.d("paul","获取数据成功了");
                    Log.d("paul","response.code()=="+response.code());
//                    Log.d("paul","response.body().string()=="+response.body().string());
                }
            }
        });
    }

    private void getDatasync(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    OkHttpClient client = new OkHttpClient();//创建OkHttpClient对象
                    Request request = new Request.Builder()
                            .url("http://www.baidu.com")//请求接口。如果需要传参拼接到接口后面。
                            .build();//创建Request 对象
                    Response response = null;
                    response = client.newCall(request).execute();//得到Response 对象
                    if (response.isSuccessful()) {
                        Log.d("paul","response.code()=="+response.code());
                        Log.d("paul","response.message()=="+response.message());
                        Log.d("paul","res=="+response.body().string());
                        //此时的代码执行在子线程，修改UI的操作请使用handler跳转到UI线程。
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
    /**
     * A native method that is implemented by the 'native-lib' native library,
     * which is packaged with this application.
     */
    public native String stringFromJNI();


    class TaskS extends AsyncTask<Void,Void,Void>{

        @Override
        protected Void doInBackground(Void... voids) {
            HttpURLConnection urlConnection =null;
            try {
//                URL url = new URL("http://www.androidchina.net");
                URL url = new URL("http://www.baidu.com");
                urlConnection = (HttpURLConnection) url.openConnection();
                int responseCode = urlConnection.getResponseCode();
                Log.e("paul","==="+responseCode+"===");

            } catch (IOException e) {
                e.printStackTrace();
            }finally {
                if(null!=urlConnection)
                {
                    urlConnection.disconnect();
                }
            }
            return null;
        }
    }

}
