package com.go3.cpy.go.ui;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.go3.cpy.go.R;
import com.go3.cpy.go.connect.HttpURLConnection;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import static android.widget.Toast.LENGTH_SHORT;

public class LoginActivity extends AppCompatActivity {

    private EditText et_account;
    private EditText et_password;

    private Handler go3_handler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    Toast.makeText(LoginActivity.this, getString(R.string.msg_account_or_password_err), LENGTH_SHORT).show();
                    break;
                case 1:
                    Toast.makeText(LoginActivity.this, "登录成功", LENGTH_SHORT).show();
                    Intent s = new Intent(LoginActivity.this, ShopActivity.class);
                    startActivity(s);
                    break;
                case 2:
                    Toast.makeText(LoginActivity.this, "网络错误", LENGTH_SHORT).show();
                    break;
                case 3:
                    Toast.makeText(LoginActivity.this, "发送内容错误", LENGTH_SHORT).show();
                    break;
                case 4:
                    Toast.makeText(LoginActivity.this, "Json Exception", LENGTH_SHORT).show();
                    break;

                default:
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        et_account = (EditText) findViewById(R.id.et_account);
        et_password = (EditText) findViewById(R.id.et_password);
        Button btn_go = (Button) findViewById(R.id.btn_go);

        btn_go.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String account = et_account.getText().toString().trim();
                final String password = et_password.getText().toString().trim();
                if (TextUtils.isEmpty(account) || TextUtils.isEmpty(password)) {
                    Toast.makeText(LoginActivity.this, "用户名或密码为空", LENGTH_SHORT).show();
                } else new Thread() {
                    public void run() {
                        Message msg = Message.obtain(go3_handler);
                        try {
                            JSONObject param = new JSONObject();
                            param.put("account", account);
                            param.put("password", password);
                            String json = HttpURLConnection.go3_connect(param, "LoginActivity");
                            JSONObject log = new JSONObject(json);
                            if (log.getString("res").equals("success")) {
                                msg.what = 1;
                                go3_handler.sendMessage(msg);
                            } else if (log.getString("res").equals("error")) {
                                msg.what = 3;
                                go3_handler.sendMessage(msg);
                            } else if (log.getString("res").equals("fail")) {
                                msg.what = 0;
                                go3_handler.sendMessage(msg);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            msg.what = 4;
                            go3_handler.sendMessage(msg);
                        } catch (IOException e){
                            e.printStackTrace();
                            msg.what = 2;
                            go3_handler.sendMessage(msg);
                        }
                    }
                }.start();
            }
        });
    }
}
