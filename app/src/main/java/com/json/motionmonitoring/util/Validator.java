package com.json.motionmonitoring.util;

import java.util.regex.Pattern;

public class Validator {
    /**
     * 邮箱格式：只允许出现字母、数字、下划线，且以字母开头
     */
    private static final String REGEX_EMAIL = "^([0-9A-Za-z\\-_\\.]+)@([0-9a-z]+\\.[a-z]{2,3}(\\.[a-z]{2})?)$";

    /**
     * 密码格式：长度为6-20个字符，且不含特殊字符
     */
    private static final String REGEX_PASSWORD = "^[a-zA-Z0-9]{6,20}$";

    /**
     * 手机号码格式：长度为11，且只包含数字，以1开头
     */
    private static final String REGEX_PHONE = "/^[1][3,4,5,7,8][0-9]{9}$/";

    /**
     * 验证邮箱
     * @param email
     * @return
     */
    public static boolean verifyEmail(String email){
        return Pattern.matches(REGEX_EMAIL, email);
    }

    /**
     * 验证密码
     * @param password
     * @return
     */
    public static boolean verifyPassword(String password){
        return Pattern.matches(REGEX_PASSWORD, password);
    }

    /**
     * 验证手机号码
     * @param phone
     * @return
     */
    public static boolean verifyPhone(String phone){
        return Pattern.matches(REGEX_PHONE, phone);
    }


}
