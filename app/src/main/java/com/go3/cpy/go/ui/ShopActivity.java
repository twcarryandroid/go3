package com.go3.cpy.go.ui;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.go3.cpy.go.R;
import com.go3.cpy.go.connect.HttpURLConnection;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

import static android.widget.Toast.LENGTH_SHORT;

public class ShopActivity extends AppCompatActivity {

    private ListView lv_shop_list;

    private TextView tv_shop_name;
    private TextView tv_shop_msg;
    private TextView tv_shop_time;


    //用于存储收到的json数据
    ArrayList<HashMap<String, Object>> list = new ArrayList<HashMap<String, Object>>();
    HashMap<String, Object> map = null;

    //handler
    private Handler go3_handler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    Toast.makeText(ShopActivity.this, "网络错误", LENGTH_SHORT).show();
                    break;

                case 1:
                    try {
                        JSONObject shop = new JSONObject(msg.obj.toString());
                        JSONArray array = shop.getJSONArray("shoplist");
                        for (int i = 0; i < array.length(); i++) {
                            JSONObject lan = new JSONObject((String) array.get(i));

                            map = new HashMap<String, Object>();
                            map.put("shop_id",lan.getString("id"));
                            map.put("shop_name", lan.getString("name"));
                            map.put("shop_msg",lan.getString("state"));
                            list.add(map);
                        }

                        shop_Adapter sa = new shop_Adapter();
                        lv_shop_list.setAdapter(sa);

                        Toast.makeText(ShopActivity.this, "接收成功", LENGTH_SHORT).show();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    break;

                case 2:
                    Toast.makeText(ShopActivity.this, "Json Exception", LENGTH_SHORT).show();
                    break;
                default:
                    break;
            }
        }
    };

    //listview的适配器
    class shop_Adapter extends BaseAdapter{

        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            map = list.get(position);
            Set keySet = map.keySet(); // key的set集合
            Iterator it = keySet.iterator();
            String str [] = new String[3];
            int i = 0;
            //解析数据
            while(it.hasNext()){
                Object k = it.next(); // key
                Object v = map.get(k); //value
                str[i] = (String) v;
                i++;
            }

            View view = null;
            if (convertView == null){
                LayoutInflater inflater = LayoutInflater.from(ShopActivity.this);
                view = inflater.inflate(R.layout.shoplayout,null);
            }else{
                view= convertView;
            }

            tv_shop_name = (TextView) view.findViewById(R.id.shop_name);
            tv_shop_msg = (TextView) view.findViewById(R.id.shop_msg);
            tv_shop_time = (TextView) view.findViewById(R.id.shop_time);

            if (str[1].equals("open")) {
                tv_shop_name.setText(str[2]);
                tv_shop_name.setTextSize(30);
                tv_shop_name.setTextColor(Color.BLACK);
                tv_shop_msg.setText(str[1]);
                tv_shop_msg.setTextSize(15);
                tv_shop_msg.setTextColor(Color.BLACK);
                tv_shop_time.setText(str[0]);
                tv_shop_time.setTextSize(10);
                tv_shop_time.setTextColor(Color.BLACK);
            }else {
                tv_shop_name.setText(str[2]);
                tv_shop_name.setTextSize(30);
                tv_shop_name.setTextColor(Color.GRAY);
                tv_shop_msg.setText(str[1]);
                tv_shop_msg.setTextSize(15);
                tv_shop_msg.setTextColor(Color.GRAY);
                tv_shop_time.setText(str[0]);
                tv_shop_time.setTextSize(10);
                tv_shop_time.setTextColor(Color.GRAY);
            }

            return view;
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.shop);

        setTitle("中華大學");
        lv_shop_list = (ListView) findViewById(R.id.shop_list);
        lv_shop_list.setOnItemClickListener(new AdapterView.OnItemClickListener(){

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent s = new Intent(ShopActivity.this, FoodActivity.class);

                map = list.get(position);
                Set keySet = map.keySet(); // key的set集合
                Iterator it = keySet.iterator();
                String str [] = new String[3];
                int i = 0;
                //解析数据
                while(it.hasNext()){
                    Object k = it.next(); // key
                    Object v = map.get(k); //value
                    str[i] = (String) v;
                    i++;
                }

                Bundle b = new Bundle();
                b.putString("shop_name",str[2]);
                b.putInt("shop_id",  Integer.parseInt(str[0]));
                s.putExtras(b);

                startActivity(s);
            }
        });

        //用来网络连接的线程
        new Thread() {
            public void run() {
                Message msg = Message.obtain(go3_handler);
                try {
                    JSONObject param = new JSONObject();
                    param.put("locate","tw");//给服务器传手机当前的位置，备用
                    msg.obj = HttpURLConnection.go3_connect(param, "ShopActivity");//获得服务器返回的json
                    msg.what = 1;
                    go3_handler.sendMessage(msg);
                } catch (IOException e) {
                    e.printStackTrace();
                    msg.what = 0;
                    go3_handler.sendMessage(msg);
                } catch (JSONException e){
                    e.printStackTrace();
                    msg.what = 2;
                    go3_handler.sendMessage(msg);
                }
            }
        }.start();
    }
}


//添加长按点击
//list.setOnCreateContextMenuListener(new OnCreateContextMenuListener() {});