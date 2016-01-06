package com.go3.cpy.go.ui;

import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
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

public class FoodActivity extends AppCompatActivity {

    private ListView lv_food_list;

    private TextView tv_food_name;
    private TextView tv_food_price;

    ArrayList<HashMap<String, Object>> list = new ArrayList<HashMap<String, Object>>();
    HashMap<String, Object> map = null;

    //handler
    private Handler go3_handler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    Toast.makeText(FoodActivity.this, "网络错误", LENGTH_SHORT).show();
                    break;

                case 1:
                    try {
                        JSONObject shop = new JSONObject(msg.obj.toString());
                        JSONArray array = shop.getJSONArray("foodlist");
                        for (int i = 0; i < array.length(); i++) {
                            JSONObject lan = new JSONObject((String) array.get(i));

                            map = new HashMap<String, Object>();
                            map.put("food_id",lan.getString("food_id"));
                            map.put("food_name", lan.getString("food_name"));
                            map.put("food_price",lan.getString("food_price"));
                            list.add(map);
                        }

                        food_Adapter sa = new food_Adapter();
                        lv_food_list.setAdapter(sa);

                        Toast.makeText(FoodActivity.this, "接收成功", LENGTH_SHORT).show();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    break;

                case 2:
                    Toast.makeText(FoodActivity.this, "Json Exception", LENGTH_SHORT).show();
                    break;
                default:
                    break;
            }
        }
    };

    //listview的适配器
    class food_Adapter extends BaseAdapter {

        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            map = list.get(position);
            Set keySet = map.keySet(); // key的set集合
            Iterator it = keySet.iterator();
            final String str[] = new String[3];
            int i = 0;
            //解析数据
            while (it.hasNext()) {
                Object k = it.next(); // key
                Object v = map.get(k); //value
                str[i] = (String) v;
                i++;
            }

            View view = null;
            if (convertView == null) {
                LayoutInflater inflater = LayoutInflater.from(FoodActivity.this);
                view = inflater.inflate(R.layout.foodlayout, null);
            } else {
                view = convertView;
            }

            tv_food_name = (TextView) view.findViewById(R.id.food_name);
            tv_food_price = (TextView) view.findViewById(R.id.food_price);

            tv_food_name.setText(str[1]);
            tv_food_name.setTextSize(20);
            tv_food_name.setTextColor(Color.BLACK);
            tv_food_price.setText(str[2] + "/每份");
            tv_food_price.setTextSize(15);
            tv_food_price.setTextColor(Color.BLACK);

            return view;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public Object getItem(int position) {
            return null;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.food);

        Intent i = getIntent();
        final Bundle b = i.getExtras();

        setTitle(b.getString("shop_name"));

        lv_food_list = (ListView) findViewById(R.id.food_list);
        lv_food_list.setOnItemClickListener(new AdapterView.OnItemClickListener(){

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent s = new Intent(FoodActivity.this, OrderActivity.class);
                startActivity(s);
            }
        });

        //用来网络连接的线程
        new Thread() {
            public void run() {
                Message msg = Message.obtain(go3_handler);
                try {
                    JSONObject param = new JSONObject();
                    param.put("shop_id",b.getInt("shop_id"));//给服务器传手机当前的位置，备用
                    msg.obj = HttpURLConnection.go3_connect(param, "FoodActivity");//获得服务器返回的json
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

