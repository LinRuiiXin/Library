package com.sz.library.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.makeramen.roundedimageview.RoundedImageView;
import com.sz.library.BookInfoActivity;
import com.sz.library.R;
import com.sz.library.pojo.Book;

import java.util.List;

public class SearchAdapter extends RecyclerView.Adapter {
    private Context context;
    private List<Book> books;
    private final LayoutInflater inflater;

    public SearchAdapter(Context context, List<Book> books){
        this.context = context;
        this.books = books;
        inflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(inflater.inflate(R.layout.search_result_item,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if(holder instanceof ViewHolder){
            ViewHolder viewHolder = (ViewHolder) holder;
            Book book = books.get(position);
            viewHolder.preview.setImageResource(context.getResources().getIdentifier("book"+book.getId(),"drawable",context.getPackageName()));
            viewHolder.bookName.setText(book.getName());
            viewHolder.author.setText(book.getAuthor());
            viewHolder.clickAble.setOnClickListener(v->{
                Intent intent = new Intent(context, BookInfoActivity.class);
                intent.putExtra("book",book);
                context.startActivity(intent);
            });
        }
    }

    @Override
    public int getItemCount() {
        return books == null ? 0 : books.size();
    }
    public void setData(List<Book> books){
        this.books = books;
    }
    class ViewHolder extends RecyclerView.ViewHolder {

        private final RoundedImageView preview;
        private final TextView bookName;
        private final TextView author;
        private final RelativeLayout clickAble;

        public ViewHolder(@NonNull View view) {
            super(view);
            preview = view.findViewById(R.id.sri_book_preview);
            bookName = view.findViewById(R.id.sri_book_name);
            author = view.findViewById(R.id.sri_author);
            clickAble = view.findViewById(R.id.sri_click_able);
        }
    }
}
