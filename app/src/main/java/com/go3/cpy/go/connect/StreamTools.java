package com.go3.cpy.go.connect;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by cpy on 2015/12/20.
 * 将输入流is转换为字符串
 * 转换失败返回null
 */
public class StreamTools {
    public static String readStream(InputStream is) throws IOException {
        try{
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];
            int length = -1;

            while(-1 != (length = is.read(buffer))){
                byteArrayOutputStream.write(buffer,0,length);
            }
            is.close();
            return new String(byteArrayOutputStream.toByteArray());
        }catch(Exception e){
            return null;
        }
    }
}
