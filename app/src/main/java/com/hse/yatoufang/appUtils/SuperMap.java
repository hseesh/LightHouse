package com.hse.yatoufang.appUtils;

public class  SuperMap{

    private Object[] key;
    private Object[] value;
    private int cursor = 0;

    public SuperMap(int size){
        key = new Object[size];
        value = new Object[size];
    }

    public int size(){
        return cursor;
    }

    public void add(Object x , Object y){
        if(x == null || y == null){
            return;
        }else{
            key[cursor] = x;
            value[cursor] = y;
            cursor++;
        }
    }

    public Object getKey(int index) {
        return key[index];
    }

    public Object getValue(int index) {
        return value[index];
    }
}
