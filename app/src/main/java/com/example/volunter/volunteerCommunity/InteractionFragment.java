package com.example.volunter.volunteerCommunity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.volunter.R;

import java.util.List;

/**
 * Created by dell on 2017/5/30.
 */

public class InteractionFragment extends Fragment {

    private View parentView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        parentView = inflater.inflate(R.layout.interaction_fragment, container, false);
        return parentView;
    }
}
