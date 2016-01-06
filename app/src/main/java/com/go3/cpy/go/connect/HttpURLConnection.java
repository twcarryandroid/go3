package com.go3.cpy.go.connect;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.net.URL;

/**
 * Created by cpy on 2015/12/22.
 */
public class HttpURLConnection {
    public static String go3_connect(final JSONObject json_str, final String url_str) throws IOException, JSONException {
        String json_string = null;

        final String connectURL = URLName.go3_URL_chose(url_str);//选择要用的URL
        java.net.HttpURLConnection conn;
        InputStream is = null;
        URL url = new URL(connectURL);//建立一个URL对象
        conn = (java.net.HttpURLConnection) url.openConnection();//得到打开的链接对象
        conn.setDoOutput(true);
        conn.setDoInput(true);
        conn.setUseCaches(false);
        conn.setInstanceFollowRedirects(true);
        conn.setRequestMethod("POST");//设置请求方式
        conn.setRequestProperty("Content-Type", "application/json"); // 设置发送数据的格式
        conn.setRequestProperty("Accept", "application/json"); // 设置接收数据的格式
        conn.setConnectTimeout(5000);//设置请求允许等待时间
        conn.setReadTimeout(5000);
        conn.connect();
        OutputStreamWriter out = new OutputStreamWriter(conn.getOutputStream(), "utf-8");
        out.write(json_str.toString());
        out.flush();
        out.close();
        is = conn.getInputStream();//从链接中获取一个输入流对象,发送请求的代码段

        int login_code = conn.getResponseCode();
        if (login_code == 200) {
            json_string = StreamTools.readStream(is);//调用数据流处理方法

        } else if (login_code == 404 || login_code == 503) {
            json_string = "net_error";
        }
        assert is != null;
        is.close();
        conn.disconnect();

        return json_string;
    }
}