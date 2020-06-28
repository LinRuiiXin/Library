package com.sz.library;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.sz.library.pojo.Borrow;
import com.sz.library.pojo.User;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.litepal.crud.DataSupport;

import java.text.SimpleDateFormat;
import java.util.Date;

import static org.junit.Assert.*;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class ExampleInstrumentedTest {
    @Test
    public void useAppContext() {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
        DataSupport.deleteAll(Borrow.class);

    }
    @Test
    public void clearAllData() {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
        DataSupport.deleteAll(User.class);
        String spName = appContext.getString(R.string.sharedPreferenceName);
        SharedPreferences sharedPreferences = appContext.getSharedPreferences(spName, Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = sharedPreferences.edit();
        edit.clear();
        edit.commit();
    }
    @Test
    public void m3() {
        Borrow borrow = new Borrow();
        borrow.setUserId(3);
        borrow.setBookId(3);
        borrow.setReturnBack(false);
        borrow.setPromiseDay(new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(new Date()));
        borrow.setBorrowDay(new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(new Date()));
        borrow.save();
    }
    @Test
    public void m4() {
        DataSupport.deleteAll(Borrow.class);
    }
    @Test
    public void m5() {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
        String spName = appContext.getString(R.string.sharedPreferenceName);
        SharedPreferences sharedPreferences = appContext.getSharedPreferences(spName, Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = sharedPreferences.edit();
        edit.clear();
        edit.commit();
    }
}
