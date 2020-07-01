package com.sz.library.adapter;

import android.app.Activity;
import android.graphics.Color;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.makeramen.roundedimageview.RoundedImageView;
import com.sz.library.R;
import com.sz.library.data.Books;
import com.sz.library.pojo.Book;
import com.sz.library.pojo.Borrow;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.function.BiConsumer;

public class BorrowListAdapter extends RecyclerView.Adapter {

    private Activity activity;
    private List<Borrow> borrows;
    private final LayoutInflater inflater;
    private final SimpleDateFormat dateFormatter;
    private BiConsumer<View,Borrow> consumer;
    private final List<Book> books;

    @RequiresApi(api = Build.VERSION_CODES.N)
    public BorrowListAdapter(Activity activity, List<Borrow> borrows, BiConsumer<View,Borrow> consumer){
        dateFormatter = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        books = Books.getData(activity);
        this.activity = activity;
        this.borrows = borrows;
        this.consumer = consumer;
        inflater = LayoutInflater.from(activity);
    }
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(inflater.inflate(R.layout.borrow_item,parent,false));
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if(holder instanceof ViewHolder){
            ViewHolder viewHolder = (ViewHolder) holder;
            Borrow borrow = borrows.get(position);
            Book book = books.get(borrow.getBookId()-1);
            viewHolder.bookPreview.setImageResource(activity.getResources().getIdentifier("book"+borrow.getBookId(),"drawable",activity.getPackageName()));
            viewHolder.bookName.setText(book.getName());
            viewHolder.borrowDate.setText(borrow.getBorrowDay());
            viewHolder.returnBackDate.setText(borrow.getPromiseDay());
            if(borrow.isReturnBack()){
                viewHolder.status.setText("已归还");
            }else{
                if(conditionStatus(borrow.getPromiseDay())){
                    viewHolder.status.setText("未归还");
                }else{
                    viewHolder.status.setText("已超时");
                    viewHolder.status.setTextColor(Color.RED);
                }
            }
            viewHolder.itemView.setOnClickListener(v->consumer.accept(v,borrows.get(position)));
        }
    }


    // 如果未超时，返回true，否则返回false
    private boolean conditionStatus(String promiseDay) {
        try {
            Date parse = dateFormatter.parse(promiseDay);
            long now = System.currentTimeMillis();
            long promiseDayMillis = parse.getTime();
            return now-promiseDayMillis < 0;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public int getItemCount() {
        return borrows.size();
    }


    class ViewHolder extends RecyclerView.ViewHolder{

        private final RoundedImageView bookPreview;
        private final TextView bookName;
        private final TextView borrowDate;
        private final TextView returnBackDate;
        private final TextView status;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            bookPreview = itemView.findViewById(R.id.bi_book_preview);
            bookName = itemView.findViewById(R.id.bi_book_name);
            borrowDate = itemView.findViewById(R.id.bi_borrow_date);
            returnBackDate = itemView.findViewById(R.id.bi_return_back_date);
            status = itemView.findViewById(R.id.bi_status);
        }
    }
}
