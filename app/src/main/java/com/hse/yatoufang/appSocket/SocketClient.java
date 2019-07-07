package com.hse.yatoufang.appSocket;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.SharedPreferences;

import android.support.v4.app.NotificationCompat;

import com.hse.yatoufang.appUtils.MyApplication;
import com.hse.yatoufang.appUtils.StreamData;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Calendar;

import static android.content.Context.NOTIFICATION_SERVICE;


public class SocketClient {

    private static Socket client;
    private int feedbackCode;
    private String content;

    SocketClient(String site, int port) {
        try {
            feedbackCode = 0;
            client = new Socket(site, port);
            client.setSoTimeout(3000);
            client.setSendBufferSize(8 * 1024);
            client.setReceiveBufferSize(8 * 1024);
            client.setSoLinger(true,100);
          //  System.out.println("Client is created! site:" + site + " port:" + port);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    // useless method ， do not use it
    public boolean getServiceState() {
        return !(client == null);
    }


    public void sendBuffer(byte[] buffer, String path) {
        if (buffer != null) {
            OutputStream outputStream = null;
            try {
                outputStream = client.getOutputStream();
                if (outputStream != null) {
                    outputStream.write(buffer);
                    outputStream.flush();
                    if (path.equals("")) {
                        getBuffer(outputStream);
                    } else {
                        sendFile(outputStream, path);
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

    private void sendFile(OutputStream outputStream, String path) {
        try {
            File file = new File(path);
            if (file.exists()) {
                FileInputStream fileInput = new FileInputStream(file);

                int size = 0;
                byte[] buffer = new byte[1024];
                while ((size = fileInput.read(buffer, 0, 1024)) != -1) {
                    outputStream.write(buffer, 0, size);
                }
                outputStream.flush();
                client.shutdownOutput();
                getBuffer(outputStream);
                fileInput.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private void getBuffer(OutputStream outputStream) {
        byte[] buffer = new byte[4];
        InputStream inputStream = null;
        try {
            inputStream = client.getInputStream();
            inputStream.read(buffer);
            int packageSize = bytesToInt(buffer);
            byte[] bytes = new byte[packageSize];
            inputStream.read(bytes, 0, packageSize);

            ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bytes);
            ObjectInputStream objectInputStream = new ObjectInputStream(byteArrayInputStream);
            StreamData data = (StreamData) objectInputStream.readObject();
            feedbackCode = data.getRequestCode();
            System.out.println("feedbackCode = " + feedbackCode);
            switch (feedbackCode) {
                case 1002:
                    storeUserInfor(data.getContent());
                    break;
                case 1005:
                    break;
                case 1006:
                    storeUserPhoto(data.getContent());
                    break;
                case 1009:
                    break;
                case 1021:
                    break;
                case 1022:
                    content = data.getContent();
                    break;
                case 1023:
                    showNotification(0);
                    break;
                case 1024:
                    storeStream(inputStream, feedbackCode);
                    break;
                case 1026:
                    showNotification(1);
                    break;
                case 1027:
                    storeStream(inputStream, feedbackCode);
                    break;
                case 1029:
                    showNotification(2);
                    break;
                case 1030:
                    storeStream(inputStream, feedbackCode);
                    break;
                case 1035:
                    content = data.getContent();
                    break;

            }
            objectInputStream.close();
            byteArrayInputStream.close();
            closeStream(outputStream, inputStream);
            closeSocket();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void storeStream(InputStream inputStream, int feedbackCode) throws Exception {
        String str = "/data/data/com.hse.yatoufang/databases/";
        switch (feedbackCode) {
            case 1024:
                str += "missionItem";
                break;
            case 1027:
                str += "diary";
                break;
            case 1030:
                str += "missionItemState";
                break;
        }
        File file = new File(str);
        if (file.exists()) {
            file.delete();
        }
        byte[] bytes = new byte[4];
        inputStream.read(bytes);
        int length = bytesToInt(bytes);
        System.out.println("length = " + length);
        FileOutputStream fileOutputStream = new FileOutputStream(file);
        int point = 0;
        bytes = new byte[2048];
        while ((point = inputStream.read(bytes, 0, bytes.length)) != -1) {
            fileOutputStream.write(bytes, 0, point);
        }
        fileOutputStream.flush();
    }

    private void closeStream(OutputStream outputStream, InputStream inputStream) {
        try {
            if (outputStream != null) {
                outputStream.close();
            }
            if (inputStream != null) {
                inputStream.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void storeUserInfor(String content) {
        if (!content.equals("")) {
            SharedPreferences sharedPreferences = MyApplication.getContext().getSharedPreferences("user", 0);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            String[] str = content.split("%");
            editor.putBoolean("is_tourist", false);
            editor.putString("account", str[0]);
            editor.putString("password", str[1]);
            editor.putString("user_nickname", str[2]);
            editor.putString("user_level", str[3]);
            editor.putInt("grade", Integer.valueOf(str[4]));
            editor.putInt("user_days", Integer.valueOf(str[5]));
            editor.apply();
        }

    }

    private void storeUserPhoto(String Img){
        SharedPreferences sharedPreferences = MyApplication.getContext().getSharedPreferences("user", 0);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        if (Img != null && !Img.equals("")) {
            editor.putString("user_img", Img);
        }
        editor.apply();
    }


    public int getFeedbackCode() {
        int code = feedbackCode;
        feedbackCode = 0;
        return code;
    }

    public String getContent() {
        String data = content;
        content = null;
        return data == null ? "" : data;
    }

    public void closeSocket() {
        try {
            client.close();
            System.out.println("socket closed");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void showNotification(int type) {
        String message = "";
        switch (type) {
            case 0:
                message = "任务表文件上传完成\n";
                break;
            case 1:
                message = "任务表文件上传完成\n" + "日记表文件传完成\n";
                break;
            case 2:
                message = "任务表文件上传完成\n" + "日记表文件传完成\n" + "统计表上传完成\n";
                break;
            case 3:
                message = "下载成功,同步完成";
                break;
        }
        NotificationManager manager = (NotificationManager) MyApplication.getContext().getSystemService(NOTIFICATION_SERVICE);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(MyApplication.getContext());
        builder.setSmallIcon(android.R.drawable.sym_def_app_icon);
        NotificationCompat.BigTextStyle bigTextStyle = new NotificationCompat.BigTextStyle();
        bigTextStyle.setBigContentTitle("数据同步   ").bigText(message);
        builder.setStyle(bigTextStyle); //设置大文本样式
        builder.setDefaults(Notification.DEFAULT_SOUND);
        builder.setAutoCancel(true);
        builder.setWhen(System.currentTimeMillis());
        Notification notification = builder.build();
        manager.notify(9980, notification);
    }

    private int bytesToInt(byte[] src) {
        return ((src[0] & 0xFF) | ((src[1] & 0xFF) << 8) | ((src[2] & 0xFF) << 16) | ((src[3] & 0xFF) << 24));
    }


}


