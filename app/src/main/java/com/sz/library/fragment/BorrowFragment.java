package com.sz.library.fragment;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.getbase.floatingactionbutton.FloatingActionButton;
import com.getbase.floatingactionbutton.FloatingActionsMenu;
import com.sz.library.R;

public class BorrowFragment extends Fragment {
    private Activity activity;
    private View view;

    FloatingActionButton buttonA;
    FloatingActionButton buttonB;
    private FloatingActionsMenu menu;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if(context instanceof Activity){
            this.activity = (Activity) context;
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if(view == null){
            view = inflater.inflate(R.layout.borrow_fragment, container, false);
            menu = view.findViewById(R.id.menu);
            buttonA = view.findViewById(R.id.action_a);
            buttonB = view.findViewById(R.id.action_b);
            buttonA.setOnClickListener(v->{
                System.out.println("11111");
            });
            buttonB.setOnClickListener(v->{
                System.out.println("22222");
            });

        }
        return view;
    }
}