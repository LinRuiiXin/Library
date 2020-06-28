package com.sz.library.service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;

import com.sz.library.R;
import com.sz.library.data.Books;
import com.sz.library.pojo.Book;
import com.sz.library.pojo.Borrow;
import com.sz.library.utils.SystemUtils;
import com.sz.library.utils.UserUtils;

import org.litepal.crud.DataSupport;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class BookOverTimeService extends Service {
    private Timer timer;
    private List<Book> data;
    private Context context;
    private final SimpleDateFormat dateFormatter;

    public BookOverTimeService() {
        dateFormatter = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.i("Service","the service has create");
        timer = new Timer();
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        context = getApplicationContext();
        data = Books.getData(context);
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                int loginId = UserUtils.getLoginId(context, false);
                List<Book> userOverTimeBooks = getUserOverTimeBooks(loginId);
                if(userOverTimeBooks != null){
                    NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
                    Notification notification = new NotificationCompat.Builder(context)
                            .setContentTitle("Library")
                            .setContentText("您有" + userOverTimeBooks.size() + "本书未归还，请尽快归还")
                            .setSmallIcon(R.mipmap.ic_launcher)
                            .setWhen(System.currentTimeMillis())
                            .setPriority(NotificationCompat.PRIORITY_MAX)
                            .setDefaults(Notification.DEFAULT_ALL)
                            .build();
                    notificationManager.notify(1,notification);
                }
            }
        },0,18000000l);
        return super.onStartCommand(intent, flags, startId);
    }

    private List<Book> getUserOverTimeBooks(int loginId) {
        List<Book> books = null;
        try {
            List<Borrow> borrows = DataSupport.where("userId=? and isReturnBack=?", String.valueOf(loginId), String.valueOf(0)).find(Borrow.class);
            long now = System.currentTimeMillis();
            for(Borrow borrow:borrows){
                String promiseDay = borrow.getPromiseDay();
                Date parse = dateFormatter.parse(promiseDay);
                long promiseDate = parse.getTime();
                if(now-promiseDate > 0){
                    if(books == null){
                        books = new ArrayList<>();
                    }
                    for(Book book : data){
                        if(book.getId() == borrow.getBookId()){
                            books.add(book);
                            break;
                        }
                    }
                }
            }
        }catch (Exception e) {
            Log.i("Service","the borrow time format wrong!!!");
            e.printStackTrace();
        }
        return books;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i("Service","the service has stop");
        timer.cancel();
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
