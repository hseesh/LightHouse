package com.hse.yatoufang.MissionItemUtils;

public class HistoryMissionItem {
    private String missionName;
    private String missionDate;
    private int missionLevel;
    private int missionCount;

    public HistoryMissionItem(String missionName, String missionDate, int missionLevel, int missionCount) {
        this.missionName = missionName;
        this.missionDate = missionDate;
        this.missionLevel = missionLevel;
        this.missionCount = missionCount;
    }

    public String getMissionName() {
        return missionName;
    }

    public String getMissionDate() {
        return "创建于" + missionDate;
    }

    public int getMissionLevel() {
        return missionLevel;
    }

    public String  getMissionCount() {
        return "完成了" + missionCount +"次";
    }

    public void setMissionName(String missionName) {
        this.missionName = missionName;
    }

    public void setMissionDate(String missionDate) {
        this.missionDate = missionDate;
    }

    public void setMissionLevel(int missionLevel) {
        this.missionLevel = missionLevel;
    }

    public void setMissionCount(int missionCount) {
        this.missionCount = missionCount;
    }
}
