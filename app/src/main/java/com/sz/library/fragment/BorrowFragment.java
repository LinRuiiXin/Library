package com.sz.library.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.transition.ChangeBounds;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityOptionsCompat;
import androidx.core.util.Pair;
import androidx.fragment.app.Fragment;

import com.getbase.floatingactionbutton.FloatingActionButton;
import com.getbase.floatingactionbutton.FloatingActionsMenu;
import com.sz.library.R;
import com.sz.library.SearchActivity;

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
            buttonA = view.findViewById(R.id.action_search);
            buttonB = view.findViewById(R.id.action_return_back);
            buttonA.setOnClickListener(v->{

            });
            buttonB.setOnClickListener(v->{
            });

        }
        return view;
    }
}