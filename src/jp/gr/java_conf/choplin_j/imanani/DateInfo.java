package jp.gr.java_conf.choplin_j.imanani;

import java.util.Calendar;
import java.util.Date;

public class DateInfo {
    private static long MILLISEC_OF_DAY = 24 * 60 * 60 * 1000;
    private Calendar date;
    private int baseYear;
    private int baseMonth;

    public DateInfo(int year, int month, int day) {
        baseYear = year;
        baseMonth = month;
        this.date = Calendar.getInstance();
        date.clear();
        date.set(year, month, day);
    }
    public DateInfo(int year, int month) {
        this(year, month, 1);
    }
    public int getDate() {
        return date.get(Calendar.DATE);
    }
    public int getMonth() {
        return date.get(Calendar.MONTH);
    }
    public int getYear() {
        return date.get(Calendar.YEAR);
    }
    public Date getTime() {
        return date.getTime();
    }
    public boolean isSunday() {
        return date.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY;
    }
    public boolean isSaturday() {
        return date.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY;
    }
    public void moveTopCorner() {
        date.set(baseYear, baseMonth, 1);
        while (date.get(Calendar.DAY_OF_WEEK) != Calendar.SUNDAY) {
            date.add(Calendar.DATE, -1);
        }
    }
    public boolean isBottomCorner() {
        Calendar temp = Calendar.getInstance();
        temp.set(baseYear, baseMonth, 1);
        temp.add(Calendar.MONTH, 1);
        temp.add(Calendar.DATE, -1);
        while (temp.get(Calendar.DAY_OF_WEEK) != Calendar.SATURDAY) {
            temp.add(Calendar.DATE, 1);
        }
        return date.get(Calendar.YEAR)  == temp.get(Calendar.YEAR) &&
               date.get(Calendar.MONTH) == temp.get(Calendar.MONTH) &&
               date.get(Calendar.DATE)  == temp.get(Calendar.DATE);
    }
    public boolean moveToNext() {
        if ( isBottomCorner() ) return false;
        date.add(Calendar.DATE, 1);
        return true;
    }
    public int daysFrom(Calendar from) {
        long d = date.getTime().getTime() - from.getTime().getTime();
        return (int)(d / MILLISEC_OF_DAY);
    }
    public int getRow() {
        DateInfo topCorner = new DateInfo(baseYear, baseMonth);
        topCorner.moveTopCorner();
        return daysFrom(topCorner.date) / 7;
    }
    public int getColumn() {
        DateInfo topCorner = new DateInfo(baseYear, baseMonth);
        topCorner.moveTopCorner();
        return daysFrom(topCorner.date) % 7;
    }
}