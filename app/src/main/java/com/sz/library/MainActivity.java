package com.sz.library;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.sz.library.adapter.MainViewPagerAdapter;
import com.sz.library.fragment.BookCaseFragment;
import com.sz.library.fragment.BorrowFragment;
import com.sz.library.fragment.MineFragment;
import com.sz.library.scroller.ViewPagerScroller;
import com.sz.library.utils.SystemUtils;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {
    private static List<Fragment> fragments = new ArrayList<>();
    static {
        fragments.add(new BookCaseFragment());
        fragments.add(new BorrowFragment());
        fragments.add(new MineFragment());
    }
    private ViewPagerScroller viewPagerScroller;
    @BindView(R.id.top_bar_item_wrap)
    LinearLayout wrap;
    @BindView(R.id.top_bar_block)
    TextView block;
    @BindView(R.id.top_bar_book_case)
    TextView bookCase;
    @BindView(R.id.top_bar_borrowed)
    TextView borrowed;
    @BindView(R.id.top_bar_mine)
    TextView mine;
    @BindView(R.id.main_view_pager)
    ViewPager viewPager;
    private MainViewPagerAdapter fragmentAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        SystemUtils.setStatusBarFullTransparent(this);
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {
        fragmentAdapter = new MainViewPagerAdapter(getSupportFragmentManager(),fragments);
        viewPager.setAdapter(fragmentAdapter);
        try {
            // 通过class文件获取mScroller属性
            Field mField = ViewPager.class.getDeclaredField("mScroller");
            mField.setAccessible(true);
            viewPagerScroller = new ViewPagerScroller(viewPager.getContext(),new AccelerateInterpolator());
            mField.set(viewPager, viewPagerScroller);
        } catch (Exception e) {
            e.printStackTrace();
        }
        setListeners();
    }

    private void setListeners() {
        bookCase.setOnClickListener(v -> {
            //设置动画
            ObjectAnimator.ofFloat(block,"translationX",0).setDuration(500).start();
            viewPager.setCurrentItem(0);
        });
        borrowed.setOnClickListener(v -> {
            //设置动画
            int offset = block.getWidth()-14;
            ObjectAnimator.ofFloat(block,"translationX",offset).setDuration(500).start();
            viewPager.setCurrentItem(1);
        });
        mine.setOnClickListener(v -> {
            //设置动画
            int offset = (block.getWidth()*2)-18;
            ObjectAnimator.ofFloat(block,"translationX",offset).setDuration(500).start();
            viewPager.setCurrentItem(2);
        });
    }
}
