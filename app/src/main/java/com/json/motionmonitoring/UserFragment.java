package com.json.motionmonitoring;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class UserFragment extends Fragment {

    private TextView userInformation;
    private TextView deviceBinding;
    private TextView alterPassword;

    private TextView user_information_icon;
    private TextView device_binding_icon;
    private TextView alter_password_icon;

    private Bundle bundle;
    private String data;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
       View view = inflater.inflate(R.layout.page_user, container, false);

       initView(view);

       bundle = getArguments();
       data = bundle.getString("usernames");
       Log.d("UserFragment", "Fragment接收的登录名"+data);

       return view;
    }

    private void initView(View view){
        Typeface iconfont = Typeface.createFromAsset(getActivity().getAssets(), "icons/iconfont.ttf");

        user_information_icon = (TextView) view.findViewById(R.id.user_information_text_view);
        user_information_icon.setTypeface(iconfont);

        device_binding_icon = (TextView) view.findViewById(R.id.device_binding_text_view);
        device_binding_icon.setTypeface(iconfont);

        alter_password_icon = (TextView) view.findViewById(R.id.alter_password_text_view);
        alter_password_icon.setTypeface(iconfont);

        userInformation = (TextView) view.findViewById(R.id.user_information);
        deviceBinding = (TextView) view.findViewById(R.id.device_binding);
        alterPassword = (TextView)view.findViewById(R.id.alter_password);

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        userInformation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), UserInformation.class);
                bundle.putString("fragment_username", data);
                intent.putExtras(bundle);
                Log.d("Fragment", "Fragment想UserActivity传输的登录名"+data);
                startActivity(intent);
            }
        });

        deviceBinding.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), DeviceInformation.class);
                bundle.putString("fragment_username", data);
                intent.putExtras(bundle);
                Log.d("Fragment", "Fragment向DeviceActivity传输的登录名"+data);
                startActivity(intent);
            }
        });

        alterPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), AlterPasswordActivity.class);
                bundle.putString("fragment_username", data);
                intent.putExtras(bundle);
                Log.d("Fragment", "Fragment向AlterPasswordActivity传输的登录名"+data);
                startActivity(intent);
            }
        });
    }
}
