package com.sz.library;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.animation.Animator;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.sz.library.pojo.Book;
import com.sz.library.pojo.Borrow;
import com.sz.library.utils.SystemUtils;
import com.sz.library.utils.UserUtils;

import org.litepal.crud.DataSupport;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class BookInfoActivity extends AppCompatActivity {
    @BindView(R.id.shared_book_name)
    Toolbar toolbar;
    @BindView(R.id.shared_book_background)
    ImageView background;
    @BindView(R.id.abi_author)
    TextView author;
    @BindView(R.id.abi_content)
    TextView content;
    @BindView(R.id.abi_floating_action_btn)
    FloatingActionButton floatingActionButton;

    private Intent intent;
    private Book book;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_info);
        SystemUtils.setStatusBarFullTransparent(this);
        ButterKnife.bind(this);
        intent = getIntent();
        book = (Book) intent.getSerializableExtra("book");
        init();
    }

    private void init() {
        initToolBar(book.getName());
        initComponent();
        initTransition();
    }

    private void initTransition() {
        View wrap = findViewById(R.id.abi_coordinator_layout);
        wrap.post(()->{
            Animator circularReveal = ViewAnimationUtils.createCircularReveal(wrap, wrap.getWidth()/2, wrap.getWidth()/2+100, 500, 7000);
            circularReveal.setDuration(2000);
            circularReveal.setInterpolator(new AccelerateDecelerateInterpolator());
            circularReveal.start();
        });
    }

    private void initToolBar(String bookName) {
        toolbar.setTitle(bookName);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(v-> finish());
        ActionBar supportActionBar = getSupportActionBar();
        if(supportActionBar != null){
            supportActionBar.setDisplayHomeAsUpEnabled(true);
        }
    }
    private void initComponent() {
        background.setImageResource(getResources().getIdentifier("book"+book.getId(),"drawable",getPackageName()));
        content.setText(book.getIntroduction());
        author.setText(book.getAuthor());
        floatingActionButton.setOnClickListener(borrowListener());
    }

    private View.OnClickListener borrowListener() {
        return v -> {
            int userId = UserUtils.getLoginId(this);
            if(userId != -1){
                List<Borrow> borrows = DataSupport.where("isReturnBack = ? and userId = ?", String.valueOf(0), String.valueOf(userId)).find(Borrow.class);
                if(borrows.size() == 0){
                    Intent intent = new Intent(BookInfoActivity.this, BorrowActivity.class);
                    intent.putExtra("bookId",book.getId());
                    intent.putExtra("userId",userId);
                    startActivity(intent);
                }else{
                    Toast.makeText(BookInfoActivity.this,"您还未归还此书",Toast.LENGTH_SHORT).show();
                }
            }
        };
    }

    /*@Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        return super.onOptionsItemSelected(item);
    }*/
}
