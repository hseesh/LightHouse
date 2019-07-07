package com.hse.yatoufang.MissionItemUtils;

import android.content.Context;
import android.content.Intent;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.hse.yatoufang.appUtils.SuperMap;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class MissionItemStatistics {


    private Map<String, Integer> missions = new HashMap<>();
    private Map<String, Object[]> histories;
    private Context context;

    public MissionItemStatistics(Context context) {
        this.context = context;
    }

    public MissionItemStatistics(Context context, int type) {

        MissionItemManger missionItemManger = new MissionItemManger(context, 2);
        SuperMap map = missionItemManger.getSate(getDateOfWeek());
        for (int i = 0; i < map.size(); i++) {
            String content = (String) map.getKey(i);
            String value = (String) map.getValue(i);
//            System.out.println("value = " + value);
//            System.out.println("content = " + content);
            getCount(content, value);
        }

    }

    /**
     * get all histories mission statistics
     *
     * @return List<HistoryMissionItem>
     */
    @SuppressWarnings("all")
    public List<HistoryMissionItem> getAllCount() {
        MissionItemManger missionItemManger = new MissionItemManger(context, 2);
        List<MissionItem> missionItemList = missionItemManger.getAllMissionState();
        List<HistoryMissionItem> historyMissionItemList = new ArrayList<>(12);
        if (missionItemList.size() > -1) {
            histories = new HashMap<>();
            for (int i = 0; i < missionItemList.size(); i++) {//统计所有项任务完成情况
                MissionItem item = missionItemList.get(i);
                getCount(item.getMissionItems(), item.getMissionState(), item.getMissionsSavetime(), item.getMissionLevels());
            }

            for (Map.Entry<String, Object[]> entry : histories.entrySet()) {
                Object[] objects = entry.getValue();
                String missionName = entry.getKey();
                String missionDate = (String) objects[1];
                int missionLevel = Integer.valueOf(objects[2].toString());//java.lang.String cannot be cast to java.lang.Integer
                int missionCount = (int) objects[0];
                historyMissionItemList.add(new HistoryMissionItem(missionName, missionDate, missionLevel, missionCount));
            }

        } else {
            return null;
        }

        return historyMissionItemList;
    }


    public Map<String, Integer> getState() {
        return this.missions;
    }

    public void clearData() {
        this.missions.clear();
    }

    /**
     * @param date 指定月份
     * @return 获取指定月份任务状况
     */
    public List<MissionItem> getMissionCount(String date) {
        MissionItemManger missionItemManger = new MissionItemManger(context, 2);
        if (missionItemManger.isExistOfMIssionState(date)) {
            List<MissionItem> missionItems = missionItemManger.getMonthState(date);
            for (int i = 0; i < missionItems.size(); i++) {
                MissionItem item = missionItems.get(i);
                getCount(item.getMissionItems(), item.getMissionState());
            }
            return missionItems;
        }
        return null;
    }

    /**
     * get specific year's mission statistics
     *
     * @param date specific year
     * @return one year's grade
     */
    public double[] getMisssionCountOfYear(String date) {
        String[] dates = getDate(date);
        MissionItemManger missionItemManger = new MissionItemManger(context, 2);
        LinkedList<List<MissionItem>> linkedList = new LinkedList<>();
        double[] grade = new double[12];
        for (int i = 0; i < 12; i++) {
            linkedList.add(missionItemManger.getMonthState(dates[i]));
            grade[i] = 0d;
        }
        if ((linkedList.size() > 0)) {
            int year = Integer.valueOf(dates[0].split("-")[0]);
            DecimalFormat decimalFormat = new DecimalFormat("0.00");

            for (int i = 0; i < linkedList.size(); i++) {
                List<MissionItem> list = linkedList.get(i);

                if (list != null && list.size() > 0) {
                    int month = Integer.valueOf(dates[i].split("-")[1]) - 1;

                    double data = list.size() / (double) getMaxDay(year, month);
                    grade[i] = Double.valueOf(decimalFormat.format(data));

                    for (int j = 0; j < list.size(); j++) {
                        MissionItem item = list.get(j);
                        getCount(item.getMissionItems(), item.getMissionState());
                    }
                }
            }

        } else {
            return null;
        }

        return grade;
    }

    private int getMaxDay(int year, int month) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, 1);
        calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
        return calendar.get(Calendar.DATE);
    }


    private String[] getDateOfWeek() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Calendar calendar = Calendar.getInstance();
        int counts = calendar.get(Calendar.DAY_OF_WEEK) - 1;
        counts = counts == 0 ? 7 : counts;
        String[] data = {"1", "2", "3", "4", "5", "6", "7"};
        data[counts - 1] = simpleDateFormat.format(calendar.getTime());
        calendar.add(Calendar.DAY_OF_MONTH, 1);
        while (counts > 0) {
            counts--;
            calendar.add(Calendar.DAY_OF_MONTH, -1);
            data[counts] = simpleDateFormat.format(calendar.getTime());
        }
        return data;
    }

    /**
     * get one week states of statistic
     *
     * @param string mission
     * @param grade  name
     */
    private void getCount(String string, String grade) {
        // System.out.println("grade" + grade.length());
        String mission[] = string.split(",");
        for (int i = 0; i < grade.length(); i++) {
            if (missions.containsKey(mission[i])) {
                for (Map.Entry<String, Integer> entry : missions.entrySet()) {
                    if (entry.getKey().equals(mission[i])) {
                        if (grade.charAt(i) == 49) {
                            entry.setValue(entry.getValue() + 1);
                            break;
                        }
                    }
                }
            } else {
                if (grade.charAt(i) == 49) {//--err
                    missions.put(mission[i], 1);
                } else {
                    missions.put(mission[i], 0);
                }
            }
        }
    }

    private void getCount(String string, String grade, String savetime, String level) {
        String mission[] = string.split(",");
        String[] levels = level.split(",");
        for (int i = 0; i < grade.length(); i++) {
            if (histories.containsKey(mission[i])) {
                for (Map.Entry<String, Object[]> entry : histories.entrySet()) {
                    if (entry.getKey().equals(mission[i])) {
                        if (grade.charAt(i) == 49) {
                            Object[] object = entry.getValue();
                            object[0] = (int) entry.getValue()[0] + 1;
                            entry.setValue(object);
                            break;
                        }
                    }
                }
            } else {
                if (grade.charAt(i) == 49) {
                    histories.put(mission[i], new Object[]{1, savetime, levels[i]});
                } else {
                    histories.put(mission[i], new Object[]{0, savetime, levels[i]});
                }
            }
        }
    }

    private String[] getDate(String year) {
        String[] dates = {year + "-01", year + "-02", year + "-03", year + "-04", year + "-05", year + "-06", year + "-07", year + "-08", year + "-09", year + "-10", year + "-11", year + "-12"};
        return dates;
    }


}
