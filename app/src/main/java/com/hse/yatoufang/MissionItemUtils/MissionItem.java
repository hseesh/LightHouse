package com.hse.yatoufang.MissionItemUtils;

import java.io.Serializable;

public class MissionItem implements Serializable {

    private int missionItemLevel;
    private String missionItemCreattime;
    private String missionItemName;

    private String missionState;
    private String missionItems;
    private String missionLevels;
    private String missionCreattime;
    private String missionsSavetime;


    private String diary;
    private String diaryTime;
    private String diaryCraettime;

    public MissionItem(String date,String time,String diary){
        this.diary = diary;
        this.diaryTime = date;
        this.diaryCraettime = time;
    }

    public MissionItem(int missionLevel, String missionCreattime, String missionName) {
        this.missionItemLevel = missionLevel;
        this.missionItemCreattime = missionCreattime;
        this.missionItemName = missionName;
    }

    public MissionItem(String missionDtae,String missionsSavetime,String missionItems, String missionLevels,String missionStates){
        this.missionCreattime = missionDtae;
        this.missionsSavetime = missionsSavetime;
        this.missionState = missionStates;
        this.missionLevels = missionLevels;
        this.missionItems = missionItems;
    }


    public int getMissionItemLevel() {
        return missionItemLevel;
    }

    public void setMissionItemLevel(int missionItemLevel) {
        this.missionItemLevel = missionItemLevel;
    }

    public String getMissionItemCreattime() {
        return missionItemCreattime;
    }

    public String getMissionItemName() {
        return missionItemName;
    }

    public void setMissionItemName(String missionItemName) {
        this.missionItemName = missionItemName;
    }

    public String getMissionState() {
        return missionState;
    }

    public String getMissionItems() {
        return missionItems;
    }

    public String getMissionLevels() {
        return missionLevels;
    }

    public String getMissionCreattime() {
        return missionCreattime;
    }

    public String getMissionsSavetime() {
        return missionsSavetime;
    }

    public String getDiary() {
        return diary == null ? "nothing" : diary;
    }

    public void setDiary(String diary) {
        this.diary = diary;
    }

    public String getDiaryTime() {
        return diaryTime;
    }

    public String getDiaryCraettime() {
        return diaryCraettime;
    }

}
