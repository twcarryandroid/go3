package com.go3.cpy.go.ui;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.go3.cpy.go.R;

public class MainActivity extends AppCompatActivity {

    // 測試測試元兒～的進度
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        if(true){
            Intent s = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(s);
        }else if(false){
            Intent s = new Intent(MainActivity.this, ShopActivity.class);
            startActivity(s);
        }
    }
}
