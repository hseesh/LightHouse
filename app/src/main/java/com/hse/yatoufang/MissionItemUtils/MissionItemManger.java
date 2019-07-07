package com.hse.yatoufang.MissionItemUtils;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.hse.yatoufang.appUtils.FileInfor;
import com.hse.yatoufang.appUtils.SuperMap;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class MissionItemManger {
    Context context;
    SQLiteOpenHelper sqLiteOpenHelper;

    public MissionItemManger(Context context, int type) {
        this.context = context;
        sqLiteOpenHelper = new SQLiteHelper(context, 1, type);
    }

    public void addMission(MissionItem missionItem) {
        SQLiteDatabase sqLiteDatabase = sqLiteOpenHelper.getWritableDatabase();
        String sql = "insert into missionItem(missionName, missionLevel, missionCreattime) values(?,?,?)";
        sqLiteDatabase.execSQL(sql, new Object[]{missionItem.getMissionItemName(), missionItem.getMissionItemLevel(), missionItem.getMissionItemCreattime()});
        sqLiteDatabase.close();
    }

    public void deleteMission(MissionItem missionItem) {
        SQLiteDatabase sqLiteDatabase = sqLiteOpenHelper.getWritableDatabase();
        String sql = "delete from missionItem where missionName=?";
        sqLiteDatabase.execSQL(sql, new Object[]{missionItem.getMissionItemName()});
        sqLiteDatabase.close();
    }

    public void updateMission(MissionItem missionItem) {
        SQLiteDatabase sqLiteDatabase = sqLiteOpenHelper.getWritableDatabase();
        String sql = "update missionItem set missionName=?,missionLevel=? where missionCreattime=?";
        sqLiteDatabase.execSQL(sql, new Object[]{missionItem.getMissionItemName(), missionItem.getMissionItemLevel(), missionItem.getMissionItemCreattime()});
        sqLiteDatabase.close();
    }

    public void updateMissionState(MissionItem missionItem) {
        SQLiteDatabase sqLiteDatabase = sqLiteOpenHelper.getWritableDatabase();
        String sql = "update missionItemState set missionsSavetime=?,missionItems=?,missionLevels=?,missiona_state=? where diaryTime=?";
        sqLiteDatabase.execSQL(sql, new Object[]{missionItem.getMissionsSavetime(), missionItem.getMissionItems(), missionItem.getMissionLevels(),
                missionItem.getMissionState(), missionItem.getMissionCreattime()});
        sqLiteDatabase.close();
    }

    public void updateDiary(MissionItem missionItem) {
        SQLiteDatabase sqLiteDatabase = sqLiteOpenHelper.getWritableDatabase();
        String sql = "update diary set diaryTime=?,diary=? where diaryCreattime=?";
        sqLiteDatabase.execSQL(sql, new Object[]{missionItem.getDiaryCraettime(), missionItem.getDiary(), missionItem.getDiaryTime()});
        sqLiteDatabase.close();
    }

    public void updateMyPhoto(String date, byte[] bytes) {
        if (bytes.length != 0) {
            SQLiteDatabase sqLiteDatabase = sqLiteOpenHelper.getWritableDatabase();
            String sql = "update myphoto set photo=? where photoSavetime=?";
            sqLiteDatabase.execSQL(sql, new Object[]{bytes, date});
            sqLiteDatabase.close();
        }
    }

    public void storeMissionState(MissionItem missionItem) {
        SQLiteDatabase sqLiteDatabase = sqLiteOpenHelper.getWritableDatabase();
        String sql = "insert into missionItemState(diaryTime , missionsSavetime ,missionItems , missionLevels , missiona_state) values(?,?,?,?,?)";
        sqLiteDatabase.execSQL(sql, new Object[]{missionItem.getMissionCreattime(), missionItem.getMissionsSavetime(), missionItem.getMissionItems(),
                missionItem.getMissionLevels(), missionItem.getMissionState()});
        sqLiteDatabase.close();
    }

    public void storeDiary(MissionItem missionItem) {
        SQLiteDatabase sqLiteDatabase = sqLiteOpenHelper.getWritableDatabase();
        String sql = "insert into diary(diaryTime,diaryCreattime,diary) values(?,?,?)";
        sqLiteDatabase.execSQL(sql, new Object[]{missionItem.getDiaryTime(), missionItem.getDiaryCraettime(), missionItem.getDiary()});
        sqLiteDatabase.close();
    }

    public void storeMyPhoto(String date, byte[] bytes) {
        if (bytes.length != 0) {
            SQLiteDatabase sqLiteDatabase = sqLiteOpenHelper.getWritableDatabase();
            String sql = "insert into myphoto(photoSavetime,photo)values(?,?)";
            sqLiteDatabase.execSQL(sql, new Object[]{date, bytes});
            sqLiteDatabase.close();
        }
    }

    public void soreMissionData(String date, String content) {
        if (content != null) {
            SQLiteDatabase sqLiteDatabase = sqLiteOpenHelper.getWritableDatabase();
            String sql = "insert into missionState(month,data)values(?,?)";
            sqLiteDatabase.execSQL(sql, new Object[]{date, content});
            sqLiteDatabase.close();
        }
    }

    public List<MissionItem> getAllMission() {
        List<MissionItem> missionItems = new ArrayList<>();
        SQLiteDatabase sqLiteDatabase = sqLiteOpenHelper.getWritableDatabase();
        String sql = "select * from missionItem order by missionLevel desc";
        Cursor cursor = sqLiteDatabase.rawQuery(sql, null);
        while (cursor.moveToNext()) {
            String name = cursor.getString(cursor.getColumnIndex("missionName"));
            int level = cursor.getInt(cursor.getColumnIndex("missionLevel"));
            String time = cursor.getString(cursor.getColumnIndex("missionCreattime"));
            MissionItem missionItem = new MissionItem(level, time, name);
            missionItems.add(missionItem);
        }
        cursor.close();
        sqLiteDatabase.close();
        return missionItems;
    }

    /***
     *  get all mission states record from db
     * @return list<MissionItem>list</MissionItem>
     */
    public List<MissionItem> getAllMissionState() {
        List<MissionItem> missions = new ArrayList<>();
        SQLiteDatabase sqLiteDatabase = sqLiteOpenHelper.getWritableDatabase();
        String sql = "select * from missionItemState";
        Cursor cursor = sqLiteDatabase.rawQuery(sql, null);
        while (cursor.moveToNext()) {
            String diaryTime = cursor.getString(cursor.getColumnIndex("diaryTime"));
            String missionSavetime = cursor.getString(cursor.getColumnIndex("missionsSavetime"));
            String missionItems = cursor.getString((cursor.getColumnIndex("missionItems")));
            String missionLevels = cursor.getString(cursor.getColumnIndex("missionLevels"));
            String missions_state = cursor.getString(cursor.getColumnIndex("missiona_state"));
            missions.add(new MissionItem(diaryTime, missionSavetime, missionItems, missionLevels, missions_state));
        }
        cursor.close();
        sqLiteDatabase.close();
        return missions;
    }

    /**
     * get specific month's mission state
     *
     * @param month specific month
     * @return list<missionItem></missionItem>
     */
    public List<MissionItem> getMonthState(String month) {
        List<MissionItem> missions = new ArrayList<>();
        SQLiteDatabase sqLiteDatabase = sqLiteOpenHelper.getWritableDatabase();
        String sql = "select * from missionItemState where diaryTime like ? ";
        Cursor cursor = sqLiteDatabase.rawQuery(sql, new String[]{month + "%"});
        while (cursor.moveToNext()) {
            String diaryTime = cursor.getString(cursor.getColumnIndex("diaryTime"));
            String missionSavetime = cursor.getString(cursor.getColumnIndex("missionsSavetime"));
            String missionItems = cursor.getString((cursor.getColumnIndex("missionItems")));
            String missionLevels = cursor.getString(cursor.getColumnIndex("missionLevels"));
            String missions_state = cursor.getString(cursor.getColumnIndex("missiona_state"));
            missions.add(new MissionItem(diaryTime, missionSavetime, missionItems, missionLevels, missions_state));
        }
        cursor.close();
        sqLiteDatabase.close();
        return missions;
    }

    /**
     * get specific day's mission state
     *
     * @param month
     * @return
     */
    public MissionItem getMissionState(String month) {
        String str[] = {month};
        SQLiteDatabase sqLiteDatabase = sqLiteOpenHelper.getWritableDatabase();
        String sql = "select * from missionItemState where diaryTime =?";
        Cursor cursor = sqLiteDatabase.rawQuery(sql, str);
        if (cursor.moveToFirst()) {
            String diaryTime = cursor.getString(cursor.getColumnIndex("diaryTime"));
            String missionSavetime = cursor.getString(cursor.getColumnIndex("missionsSavetime"));
            String missionItems = cursor.getString((cursor.getColumnIndex("missionItems")));
            String missionLevels = cursor.getString(cursor.getColumnIndex("missionLevels"));
            String missions_state = cursor.getString(cursor.getColumnIndex("missiona_state"));

            cursor.close();
            sqLiteDatabase.close();
            return new MissionItem(diaryTime, missionSavetime, missionItems, missionLevels, missions_state);
        }
        cursor.close();
        sqLiteDatabase.close();
        return null;
    }

    /**
     * get specific month's diary
     *
     * @param month
     * @return
     */
    public List<MissionItem> getThisMonthDiary(String month) {
        List<MissionItem> diary = new ArrayList<>();
        SQLiteDatabase sqLiteDatabase = sqLiteOpenHelper.getWritableDatabase();
        String sql = "select * from diary where diaryTime =?";
        Cursor cursor = sqLiteDatabase.rawQuery(sql, new String[]{month});
        while (cursor.moveToNext()) {
            String content = cursor.getString(cursor.getColumnIndex("diary"));
            String date = cursor.getString(cursor.getColumnIndex("diaryTime"));
            String time = cursor.getString(cursor.getColumnIndex("diaryCreattime"));
            MissionItem missionItem = new MissionItem(date, time, content);
            diary.add(missionItem);
        }
        cursor.close();
        sqLiteDatabase.close();
        return diary;
    }

    public Object getThisDayDiary(String month) {
        SQLiteDatabase sqLiteDatabase = sqLiteOpenHelper.getWritableDatabase();
        String sql = "select * from diary where diaryCreattime =?";
        Cursor cursor = sqLiteDatabase.rawQuery(sql, new String[]{month});
        if (cursor.moveToFirst()) {
            String content = cursor.getString(cursor.getColumnIndex("diary"));
            String date = cursor.getString(cursor.getColumnIndex("diaryTime"));
            String time = cursor.getString(cursor.getColumnIndex("diaryCreattime"));
            cursor.close();
            sqLiteDatabase.close();
            return new MissionItem(date, time, content);
        }
        cursor.close();
        sqLiteDatabase.close();
        return null;
    }

    /**
     * 获取指定日期的分数
     *
     * @param date 获取指定日期
     * @return 分数
     */
    public double getGrade(String date) {
        SQLiteDatabase sqLiteDatabase = sqLiteOpenHelper.getWritableDatabase();
        String sql = "select * from missionItemState where diaryTime =?";
        Cursor cursor = sqLiteDatabase.rawQuery(sql, new String[]{date});
        DecimalFormat decimalFormat = new DecimalFormat("0.00");//格式化小数
        int rate = 0, max = 0;
        if (cursor.moveToFirst()) {
            String missions_state = cursor.getString(cursor.getColumnIndex("missiona_state"));
            String[] grade = missions_state.split("");
            max = grade.length;
            for (int i = 1; i < max; i++) {
                if (grade[i].equals("1")) {
                    ++rate;
                }
            }

            cursor.close();
            sqLiteDatabase.close();
            return Double.valueOf(decimalFormat.format((double) (rate) / (max - 1)));
        }

        cursor.close();
        sqLiteDatabase.close();
        return 0;
    }

    public byte[] getPhoto(String date) {
        SQLiteDatabase sqLiteDatabase = sqLiteOpenHelper.getWritableDatabase();
        String sql = "select * from myphoto where photoSavetime =?";
        Cursor cursor = sqLiteDatabase.rawQuery(sql, new String[]{date});
        if (cursor.moveToFirst()) {
            byte[] img = cursor.getBlob(cursor.getColumnIndex("photo"));
            cursor.close();
            sqLiteDatabase.close();
            return img;
        }
        cursor.close();
        sqLiteDatabase.close();
        return null;
    }

    /**
     * 获取指定年12个月的任务状况流
     *
     * @param date 指定年
     * @return 12个任务状况流
     */
    public Map<String, String> getMssionState(String date) {
        SQLiteDatabase sqLiteDatabase = sqLiteOpenHelper.getWritableDatabase();
        String sql = "select * from missionState where month like ?";
        Cursor cursor = sqLiteDatabase.rawQuery(sql, new String[]{date + "%"});
        Map<String, String> map = new HashMap<>(12);
        while (cursor.moveToNext()) {
            map.put(cursor.getString(cursor.getColumnIndex("month")), cursor.getString(cursor.getColumnIndex("data")));
        }
        cursor.close();
        sqLiteDatabase.close();
        return map;
    }


    public boolean photoIsSaved(String date) {
        SQLiteDatabase sqLiteDatabase = sqLiteOpenHelper.getWritableDatabase();
        String sql = "select * from myphoto where photoSavetime =?";
        Cursor cursor = sqLiteDatabase.rawQuery(sql, new String[]{date});
        if (cursor.moveToFirst()) {
            cursor.close();
            sqLiteDatabase.close();
            return true;
        }
        cursor.close();
        sqLiteDatabase.close();
        return false;
    }

    public SuperMap getSate(String str[]) {
        SQLiteDatabase sqLiteDatabase = sqLiteOpenHelper.getWritableDatabase();
        String sql = "select missionItems,missiona_state from missionItemState where diaryTime =? or diaryTime =? or diaryTime =? or diaryTime =? or diaryTime =? or diaryTime =?or diaryTime =?";
        Cursor cursor = sqLiteDatabase.rawQuery(sql, str);
        SuperMap map = new SuperMap(7);
        while (cursor.moveToNext()) {
            String missionItems = cursor.getString((cursor.getColumnIndex("missionItems")));
            String missions_state = cursor.getString(cursor.getColumnIndex("missiona_state"));
            map.add(missionItems, missions_state);
        }

        cursor.close();
        sqLiteDatabase.close();
        return map;
    }

    /**
     * judge wheather the Specified mission month is exist
     *
     * @param date
     * @return
     */
    public boolean isExistOfMIssionState(String date) {
        SQLiteDatabase sqLiteDatabase = sqLiteOpenHelper.getWritableDatabase();
        String sql = "select diaryTime from missionItemState where diaryTime like ?";
        Cursor cursor = sqLiteDatabase.rawQuery(sql, new String[]{date + "%"});
        if (cursor.moveToFirst()) {
            cursor.close();
            sqLiteDatabase.close();
            return true;
        }
        cursor.close();
        sqLiteDatabase.close();
        return false;

    }

    /**
     * judge wheather the Specified month data is exist
     *
     * @param date
     * @return
     */
    public boolean isExistOfMissionData(String date) {
        SQLiteDatabase sqLiteDatabase = sqLiteOpenHelper.getWritableDatabase();
        String sql = "select month from missionState where month = ?";
        Cursor cursor = sqLiteDatabase.rawQuery(sql, new String[]{date});
        if (cursor.moveToFirst()) {
            cursor.close();
            sqLiteDatabase.close();
            return true;
        }
        cursor.close();
        sqLiteDatabase.close();
        return false;
    }

    /**
     * judge wheather the Specified mission diary is exist
     *
     * @param date
     * @return
     */
    public boolean isExistOfDiary(String date) {
        SQLiteDatabase sqLiteDatabase = sqLiteOpenHelper.getWritableDatabase();
        String sql = "select diaryTime from diary where diaryTime like ?";
        Cursor cursor = sqLiteDatabase.rawQuery(sql, new String[]{date + "%"});
        if (cursor.moveToFirst()) {
            cursor.close();
            sqLiteDatabase.close();
            return true;
        }
        cursor.close();
        sqLiteDatabase.close();
        return false;

    }

    public FileInfor getItemsInfor() {
        String name = "任务", describe = "", count = "";
        SQLiteDatabase sqLiteDatabase = sqLiteOpenHelper.getWritableDatabase();
        String sql = "select missionCreattime from missionItem limit 1";
        String sql2 = "select missionCreattime from missionItem  order by missionCreattime desc limit 1";
        String sql3 = "select count(1) from missionItem";
        Cursor cursor = sqLiteDatabase.rawQuery(sql, null);
        if (cursor.moveToFirst()) {
            describe = cursor.getString(cursor.getColumnIndex("missionCreattime")).substring(0, 10);
        }
        cursor = sqLiteDatabase.rawQuery(sql2, null);
        if (cursor.moveToFirst()) {
            describe +=  "～" + cursor.getString(cursor.getColumnIndex("missionCreattime")).substring(0, 10);
        }
        cursor = sqLiteDatabase.rawQuery(sql3, null);
        if (cursor.moveToFirst()) {
            count = String.valueOf(cursor.getLong(0));
        }
        cursor.close();
        sqLiteDatabase.close();
        if (count.equals("0")) {
            return null;
        }
        return new FileInfor(name, describe, count);
    }

    public FileInfor getDiaryInfor() {
        String name = "日记", describe = "", count = "";
        SQLiteDatabase sqLiteDatabase = sqLiteOpenHelper.getWritableDatabase();
        String sql = "select diaryTime from diary limit 1";
        String sql2 = "select diaryTime from diary  order by diaryTime desc limit 1";
        String sql3 = "select count(1) from diary";
        Cursor cursor = sqLiteDatabase.rawQuery(sql, null);
        if (cursor.moveToFirst()) {
            describe = cursor.getString(cursor.getColumnIndex("diaryTime"));
        }
        cursor = sqLiteDatabase.rawQuery(sql2, null);
        if (cursor.moveToFirst()) {
            describe +=  "～" + cursor.getString(cursor.getColumnIndex("diaryTime"));
        }
        cursor = sqLiteDatabase.rawQuery(sql3, null);
        if (cursor.moveToFirst()) {
            count = String.valueOf(cursor.getLong(0));
        }
        cursor.close();
        sqLiteDatabase.close();
        if (count.equals("0")) {
            return null;
        }
        return new FileInfor(name, describe, count);
    }

    public FileInfor getMissionItemStateinfor() {
        String name = "统计表", describe = "", count = "";
        SQLiteDatabase sqLiteDatabase = sqLiteOpenHelper.getWritableDatabase();
        String sql = "select diaryTime from missionItemState limit 1";
        String sql2 = "select diaryTime from missionItemState  order by diaryTime desc limit 1";
        String sql3 = "select count(1) from missionItemState";
        Cursor cursor = sqLiteDatabase.rawQuery(sql, null);
        if (cursor.moveToFirst()) {
            describe = cursor.getString(cursor.getColumnIndex("diaryTime"));
        }
        cursor = sqLiteDatabase.rawQuery(sql2, null);
        if (cursor.moveToFirst()) {
            describe +=  "～" +  cursor.getString(cursor.getColumnIndex("diaryTime"));
        }
        cursor = sqLiteDatabase.rawQuery(sql3, null);
        if (cursor.moveToFirst()) {
            count = String.valueOf(cursor.getLong(0));
        }
        cursor.close();
        sqLiteDatabase.close();
        if (count.equals("0")) {
            return null;
        }
        return new FileInfor(name, describe, count);
    }


}
