package com.go3.cpy.go.connect;

import com.go3.cpy.go.config.Config;

/**
 * Created by cpy on 2015/12/22.
 */
public class URLName {
    public static String go3_URL_chose(String url_str){

        switch (url_str){
            case "LoginActivity":
                url_str= Config.login_URL;
                break;
            case "RegistActivity":
                url_str=Config.regist_URL;
                break;
            case "ShopActivity":
                url_str=Config.shop_list_URL;
                break;
            case "FoodActivity":
                url_str=Config.food_list_URL;
                break;
            default:
                url_str=null;
                break;
        }
        return url_str;
    }
}
