package com.hse.yatoufang.appUtils;

public class FileInfor {

    private String name;
    private String describe;
    private String count;

    public FileInfor(String name, String describe, String count) {
        this.name = name;
        this.describe = describe;
        this.count = count;
    }

    public String getName() {
        return name;
    }

    public String getDescribe() {
        return describe;
    }

    public String getCount() {
        return count;
    }
}
