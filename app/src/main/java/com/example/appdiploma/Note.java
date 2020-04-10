package com.example.appdiploma;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

@Entity
public class Note implements Serializable {

    @PrimaryKey
    private long id;
    @ColumnInfo(name = "title")
    private String title;
    @ColumnInfo(name = "text")
    private String text;
    @Ignore
    private GregorianCalendar date;
    @ColumnInfo(name = "year")
    private int year;
    @ColumnInfo(name = "month")
    private int month;
    @ColumnInfo(name = "day")
    private int day;
    @ColumnInfo(name = "milis")
    private long milis;
    @ColumnInfo(name = "state")
    private int state;

    public Note(String title, String text, int year, int month, int day) {
        this.id = new Date().getTime();
        this.title = title;
        this.text = text;
        this.year = year;
        this.month = month;
        this.day = day;
        this.date = new GregorianCalendar(year, month, day, 0, 0, 0);
        this.milis = this.date.getTimeInMillis();
        this.state = compareToToday(this.date);
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public GregorianCalendar getDate() {
        return date;
    }

    public void setDate(GregorianCalendar date) {
        this.date = date;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
        this.date = new GregorianCalendar(year, this.month, this.day, 0, 0, 0);
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
        this.date = new GregorianCalendar(this.year, month, this.day, 0, 0, 0);
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
        this.date = new GregorianCalendar(this.year, this.month, day, 0, 0, 0);
    }

    public long getMilis() {
        return this.milis;
    }

    public void setMilis(long milis) {
        this.date.setTimeInMillis(milis);
    }

    public int getState() {
        return this.state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public int compareToToday(GregorianCalendar date) {
        GregorianCalendar today = new GregorianCalendar();
        today.set(Calendar.HOUR_OF_DAY, 0);
        today.set(Calendar.MINUTE, 0);
        today.set(Calendar.SECOND, 0);
        today.set(Calendar.MILLISECOND, 0);

       return date.compareTo(today);
    }
}
