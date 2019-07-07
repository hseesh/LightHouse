package com.hse.yatoufang.appUtils;


import android.content.SharedPreferences;

public class UserInforUtil {


    public static String getUserInformation(){
        SharedPreferences sharedPreferences = MyApplication.getContext().getSharedPreferences("user",0);
        boolean is_tourist = sharedPreferences.getBoolean("is_tourist",true);
        if(is_tourist){
            return " ";
        }else{
            return getUserInfor();
        }
    }

    public static boolean isTourist(){
        SharedPreferences sharedPreferences = MyApplication.getContext().getSharedPreferences("user",0);
        return sharedPreferences.getBoolean("is_tourist",true);
    }


    public static String getAccount(){
        SharedPreferences sharedPreferences = MyApplication.getContext().getSharedPreferences("user",0);
        return sharedPreferences.getString("account","游客");
    }

    private static String getUserInfor(){
        SharedPreferences sharedPreferences = MyApplication.getContext().getSharedPreferences("user",0);
        String account = sharedPreferences.getString("account","游客");
        String password = sharedPreferences.getString("password","admin");
        String userLevel = sharedPreferences.getString("user_level","LV1");
        String nickname = sharedPreferences.getString("user_nickname","游客");
        int userGrade = sharedPreferences.getInt("user_grade",0);
        int useDays = sharedPreferences.getInt("user_days",0);
        return account + "%" + password + "%" + nickname + "%" + userLevel  + "%" + userGrade + "%" + useDays;
    }


}
