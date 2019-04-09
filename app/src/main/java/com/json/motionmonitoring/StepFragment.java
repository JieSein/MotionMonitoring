package com.json.motionmonitoring;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class StepFragment extends Fragment {
    private static final String TAG = "StepFragment";
    private TextView step_data;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.page_step, container, false);

        init_view(view);
        return view;
    }

    private void init_view(View view){
        step_data = (TextView)view.findViewById(R.id.step_data);
    }


}
