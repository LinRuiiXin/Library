package com.sz.library.fragment;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.getbase.floatingactionbutton.FloatingActionButton;
import com.getbase.floatingactionbutton.FloatingActionsMenu;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.sz.library.R;
import com.sz.library.adapter.BorrowListAdapter;
import com.sz.library.adapter.SearchAdapter;
import com.sz.library.data.Books;
import com.sz.library.pojo.Book;
import com.sz.library.pojo.Borrow;
import com.sz.library.utils.UserUtils;

import org.litepal.crud.DataSupport;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

public class BorrowFragment extends Fragment {
    private static SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
    private Activity activity;
    private View view;
    private Borrow choiceBorrow;
    private View choiceView;
    FloatingActionButton buttonA;
    FloatingActionButton buttonB;
    private FloatingActionsMenu menu;
    private List<Borrow> borrows;
    private RecyclerView recyclerView;
    private BottomSheetDialog dialog;
    private int loginId;
    private BottomSheetDialog searchDialog;
    private List<Book> books;
    private SearchAdapter searchAdapter;
    private BorrowListAdapter borrowListAdapter;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof Activity) {
            this.activity = (Activity) context;
        }
        loginId = UserUtils.getLoginId(context, false);
        borrows = DataSupport.where("userId=?", String.valueOf(loginId)).order("isReturnBack asc").find(Borrow.class);
        books = Books.getData(context);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (view == null) {
            view = inflater.inflate(R.layout.borrow_fragment, container, false);
            menu = view.findViewById(R.id.menu);
            buttonA = view.findViewById(R.id.action_search);
            buttonB = view.findViewById(R.id.action_return_back);
            recyclerView = view.findViewById(R.id.bf_borrow_list);
            initComponent();
        }
        return view;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void initComponent() {
        initDialog();
        buttonA.setOnClickListener(v -> {
            searchDialog.show();
        });
        buttonB.setOnClickListener(v -> {
            returnAllBooks();
        });
        borrowListAdapter = new BorrowListAdapter(activity, borrows, (view, borrow) -> {
            if (!borrow.isReturnBack()) {
                this.choiceBorrow = borrow;
                this.choiceView = view;
                dialog.show();
            }
        });
        recyclerView.setAdapter(borrowListAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(activity));

    }

    private void returnAllBooks() {
//        String userId = String.valueOf(UserUtils.getUser(getContext()).getId());
        List<Borrow> borrows = DataSupport.where("userId = ? and isReturnBack = ?",String.valueOf(loginId),"0").find(Borrow.class);
        for (Borrow borrow:borrows){
            borrow.setReturnDay(dateFormatter.format(new Date()));
            borrow.setReturnBack(true);
            borrow.save();
        }
        borrowListAdapter.serData(DataSupport.where("userId = ?",String.valueOf(loginId)).order("id desc").find(Borrow.class));
        borrowListAdapter.notifyDataSetChanged();
    }

    private void initDialog(){
        dialog = newDialog(activity, returnBackDialogView());
        searchDialog = newDialog(activity, searchDialogView());
    }

    private View searchDialogView() {
        View view = View.inflate(activity, R.layout.search_dialog, null);
        EditText editText = view.findViewById(R.id.sd_search);
        RecyclerView result = view.findViewById(R.id.sd_recycler_view);
        searchAdapter = new SearchAdapter(activity, books);
        result.setAdapter(searchAdapter);
        result.setLayoutManager(new GridLayoutManager(activity,2));
        editText.addTextChangedListener(new TextWatcher() {
            private boolean hasInput = false;
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String edit = s.toString().trim();
                if(edit.length() == 0 || edit.equals("") || edit == null){
                    if(hasInput){
                        books = Books.getData(activity);
                        searchAdapter.setData(books);
                        searchAdapter.notifyDataSetChanged();
                        hasInput = false;
                    }
                }else{
                    hasInput = true;
                    books = books.stream().filter(book -> {
                        String bookName = book.getName();
                        return bookName.indexOf(edit) != -1;
                    }).collect(Collectors.toList());
                    searchAdapter.setData(books);
                    searchAdapter.notifyDataSetChanged();
                }
            }
            @Override
            public void afterTextChanged(Editable s) { }
        });
        return view;
    }

    private View returnBackDialogView() {
        View view = View.inflate(activity, R.layout.borrow_option_dialog, null);
        Button button = view.findViewById(R.id.bod_return_back);
        button.setOnClickListener(v->{
            if(choiceBorrow != null){
                choiceBorrow.setReturnBack(true);
                choiceBorrow.setReturnDay(dateFormatter.format(new Date()));
                choiceBorrow.save();
                upDateUI();
            }
        });
        return view;
    }
    private void upDateUI() {//点击归还后的样式更改
        TextView status = choiceView.findViewById(R.id.bi_status);
        status.setTextColor(Color.parseColor("#000000"));
        status.setText("已归还");
        dialog.dismiss();
    }

    private BottomSheetDialog newDialog(Context context,View view){

        BottomSheetDialog dialog = new BottomSheetDialog(context);
        dialog.setContentView(view);
        dialog.getWindow().findViewById(R.id.design_bottom_sheet).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        BottomSheetBehavior<View> behavior = BottomSheetBehavior.from((View) view.getParent());
//        behavior.setPeekHeight(getPeekHeight());
        behavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View view, int i) {
                if (i == BottomSheetBehavior.STATE_HIDDEN) {
                    dialog.dismiss();
                    behavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                }
            }
            @Override
            public void onSlide(@NonNull View view, float v) {

            }
        });
        return dialog;
    }
}