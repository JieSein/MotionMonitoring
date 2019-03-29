package com.json.motionmonitoring.util;

import android.widget.EditText;

public class EdittextContent {

    public static String getEditString(EditText editText){
        return editText.getText().toString().trim();
    }
}
