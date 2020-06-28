package com.sz.library.data;

import android.content.Context;
import android.os.Build;

import androidx.annotation.RequiresApi;

import com.google.gson.Gson;
import com.sz.library.pojo.Book;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

public class Books {
    private static List<Book> data;

    @RequiresApi(api = Build.VERSION_CODES.N)
    public static List<Book> getData(Context context) {
        if (data == null) {
            String json = getJsonContent("books.json", context);
            if(json != null){
                Gson gson = new Gson();
                List list = gson.fromJson(json, List.class);
                data = new ArrayList<>(list.size());
                list.forEach(o->{
                    data.add(gson.fromJson(gson.toJson(o),Book.class));
                });
            }
        }
        return data;
    }

    private static String getJsonContent(String fileName, Context context) {
        InputStream inputStream = null;
        try {
            inputStream = context.getAssets().open(fileName);
            ByteBuffer byteBuffer = ByteBuffer.allocate(inputStream.available());
            byte[] buffer = new byte[1024];
            int len;
            while ((len = inputStream.read(buffer)) != -1) {
                byteBuffer.put(buffer,0,len);
            }
            return new String(byteBuffer.array(),"utf-8");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }
}
