package com.sz.library.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.ContactsContract;
import android.transition.ChangeBounds;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityOptionsCompat;
import androidx.core.util.Pair;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.getbase.floatingactionbutton.FloatingActionButton;
import com.getbase.floatingactionbutton.FloatingActionsMenu;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.sz.library.R;
import com.sz.library.SearchActivity;
import com.sz.library.adapter.BorrowListAdapter;
import com.sz.library.pojo.Borrow;
import com.sz.library.pojo.User;
import com.sz.library.utils.UserUtils;

import org.litepal.crud.DataSupport;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

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
    private View dialogView;
    private BottomSheetDialog dialog;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof Activity) {
            this.activity = (Activity) context;
        }
        int loginId = UserUtils.getLoginId(context, false);
        borrows = DataSupport.where("userId=?", String.valueOf(loginId)).order("isReturnBack asc").find(Borrow.class);
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
        });
        buttonB.setOnClickListener(v -> {
            String userId = String.valueOf(UserUtils.getUser(getContext()).getId());
            List<Borrow> borrows = DataSupport.where("userId = ? and isReturnBack = ?",userId,"0").find(Borrow.class);
            for (Borrow borrow:borrows){
                borrow.setReturnDay(dateFormatter.format(new Date()));
                borrow.setReturnBack(true);
                borrow.save();
            }
        });
        recyclerView.setAdapter(new BorrowListAdapter(activity, borrows,(view,borrow) -> {
            if(!borrow.isReturnBack()){
                this.choiceBorrow = borrow;
                this.choiceView = view;
                dialog.show();
            }
        }));
        recyclerView.setLayoutManager(new LinearLayoutManager(activity));

    }

    private void initDialog() {
        dialogView = loadDialogView();
        dialog = new BottomSheetDialog(activity);
        dialog.setContentView(dialogView);
        dialog.getWindow().findViewById(R.id.design_bottom_sheet).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        BottomSheetBehavior<View> behavior = BottomSheetBehavior.from((View) dialogView.getParent());
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
    }

    private View loadDialogView() {
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


}