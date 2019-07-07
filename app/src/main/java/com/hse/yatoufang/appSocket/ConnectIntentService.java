package com.hse.yatoufang.appSocket;

import android.app.IntentService;
import android.content.Intent;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.widget.Toast;


import com.hse.yatoufang.appUtils.ActionCallback;
import com.hse.yatoufang.appUtils.MessageCallback;
import com.hse.yatoufang.appUtils.MyApplication;
import com.hse.yatoufang.appUtils.SocketServiceUtil;

import java.io.IOException;


public class ConnectIntentService extends IntentService {

    private static final String ACTION_UPLOAD_DATA = "com.hse.yatoufang.appSocket.action.UPLOAD_DATA";
    private static final String ACTION_UPLOAD_MSG = "com.hse.yatoufang.appSocket.action.UPLOAD_MSG";
    private static final String ACTION_UPLOAD_ADVICE = "com.hse.yatoufang.appSocket.action.UPLOAD_ADV";
    private static MessageCallback messageCallback;
    private static ActionCallback actionCallback;


    public ConnectIntentService() {
        super("ConnectIntentService");
    }


    public static void uploadData(Context context, String path, int requestCode, String content, String addtionalData, MessageCallback callback) {
        Intent intent = new Intent(context, ConnectIntentService.class);
        messageCallback = callback;
        intent.setAction(ACTION_UPLOAD_DATA);
        intent.putExtra("path", path);
        intent.putExtra("content", content);
        intent.putExtra("requestCode", requestCode);
        intent.putExtra("addtionalData", addtionalData);
        context.startService(intent);
    }

    public static void upLoadMessage(Context context, int requestCode, String content, String addtionalData) {
        Intent intent = new Intent(context, ConnectIntentService.class);
        intent.setAction(ACTION_UPLOAD_ADVICE);
        intent.putExtra("content", content);
        intent.putExtra("requestCode", requestCode);
        intent.putExtra("addtionalData", addtionalData);
        context.startService(intent);
    }

    public static void upoLoadMessage(Context context, String content) {
        Intent intent = new Intent(context, ConnectIntentService.class);
        intent.setAction(ACTION_UPLOAD_MSG);
        intent.putExtra("content", content);
        context.startService(intent);
    }


    public static void upoLoadMessage(Context context, String content, MessageCallback callback) {
        Intent intent = new Intent(context, ConnectIntentService.class);
        intent.setAction(ACTION_UPLOAD_MSG);
        intent.putExtra("content", content);
        messageCallback = callback;
        context.startService(intent);
    }

    public static void upoLoadMessage(Context context, String content, ActionCallback callback) {
        Intent intent = new Intent(context, ConnectIntentService.class);
        intent.setAction(ACTION_UPLOAD_MSG);
        intent.putExtra("content", content);
        actionCallback = callback;
        context.startService(intent);
    }

    public static void upLoadMessage(Context context, String content, MessageCallback callback) {
        Intent intent = new Intent(context, ConnectIntentService.class);
        intent.setAction(ACTION_UPLOAD_MSG);
        intent.putExtra("content", content);
        messageCallback = callback;
        context.startService(intent);
    }


    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null && MyApplication.isNetworkAvailable()) {
            String action = intent.getAction();
            String content = intent.getStringExtra("content");
            int requestCode = intent.getIntExtra("requestCode", 1000);
            SocketClient socket = new SocketClient("192.168.43.121", 2018);

            try {
               if(socket.getServiceState()){
                   if (action.equals(ACTION_UPLOAD_MSG)) {
                       SocketServiceUtil util = new SocketServiceUtil(content);
                       socket.sendBuffer(util.getStreambBytes(), "");
                       callbackAction(socket.getFeedbackCode(), socket.getContent());
                   } else if (action.equals(ACTION_UPLOAD_DATA)) {
                       String path = intent.getStringExtra("path");
                       String addtionalData = intent.getStringExtra("addtionalData");
                       SocketServiceUtil util = new SocketServiceUtil(requestCode, content, addtionalData);
                       socket.sendBuffer(util.getStreambBytes(), path);
                       callbackAction(socket.getFeedbackCode(), socket.getContent());
                   } else if (action.equals(ACTION_UPLOAD_ADVICE)) {
                       String addtionalData = intent.getStringExtra("addtionalData");
                       SocketServiceUtil util = new SocketServiceUtil(requestCode, content, addtionalData);
                       socket.sendBuffer(util.getStreambBytes(), "");
                   }
               }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    private void callbackAction(int requestCode, String data) {

        switch (requestCode) {
            case 1001:
                actionCallback.action(getData(1001));
                break;
            case 1002:
                actionCallback.action(getData(1002));
                break;
            case 1022:

                messageCallback.getData(data);
                break;
            case 1029:
                messageCallback.getData(getData(1029));
                break;
            case 1030:
                messageCallback.getData(getData(1030));
                break;
            case 1035:
                messageCallback.getData(data);
                break;
            case 1129:
                messageCallback.getData(getData(1129));
                break;
            case 1135:
                messageCallback.getData(getData(1135));
                break;
            default:
                showTips(requestCode);
                break;
        }

    }

    private void showTips(int requestCode) {

        final String str = getData(requestCode);
        if (!str.equals("")) {
            Handler handler = new Handler(Looper.getMainLooper());
            handler.post(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(getApplicationContext(), str, Toast.LENGTH_LONG).show();

                }
            });
        }
    }


    public String getData(int code) {
        String str = "";
        switch (code) {
            case 1001:
                str = "注册成功,同步数据中...";
                break;
            case 1101:
                str = "注册失败，邮箱已被使用";
                break;
            case 1002:
                str = "登陆成功";
                break;
//            case 1003:
//                str = "信息修改成功";
//                break;
            case 1102:
                str = "登录失败，密码或账号不正确";
                break;
            case 1103:
                str = "信息修改失败";
                break;
            case 1029:
                str = "文件上传成功";
                break;
            case 1129:
                str = "文件上传失败,执行码--10102097";
                break;
            case 1030:
                str = "文件下载成功";
                break;
            case 1130:
                str = "文件下载失败,执行码--10103014";
                break;
            case 1135:
                str = "获取远程信息失败";
                break;

        }
        return str;
    }
}


//  case 1006:
//                str = "头像上传成功";
//                break;
//            case 1106:
//                str = "头像上传失败";
//                break;
//            case 1007:
//                str = "头像下载成功";
//                break;
//            case 1107:
//                str = "头像下载失败";
//                break;
//            case 1023:
//                str = "任务清单上传成功";
//                break;
//            case 1123:
//                str = "任务清单上传失败";
//                break;
//            case 1024:
//                str = "任务清单下载成功";
//                break;
//            case 1124:
//                str = "任务清单下载失败";
//                break;
//            case 1125:
//                str = "获取任务清单失败";
//                break;
//            case 1026:
//                str = "日记文件上传成功";
//                break;
//            case 1126:
//                str = "日记文件上传失败";
//                break;
//            case 1027:
//                str = "日记文件下载成功";
//                break;
//            case 1127:
//                str = "日记文件下载失败";
//                break;
//            case 1128:
//                str = "获取日记文件信息失败";
//                break;