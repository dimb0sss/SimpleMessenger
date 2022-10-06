package com.lvovds.simplemessenger;

import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Objects;
import java.util.TimeZone;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Time {
    private String lastDay;
    private String lastMonth;
    private String lastTime;

    public Time(String lastDay, String lastMonth, String lastTime) {
        this.lastDay = lastDay;
        this.lastMonth = lastMonth;
        this.lastTime = lastTime;
    }

    public Time() {
    }

    public static String currentDate() {
        SimpleDateFormat formater = new SimpleDateFormat("dd-MM HH:mm");
        Date currentTime = Calendar.getInstance(TimeZone.getDefault()).getTime();
        Log.d("UserAdapter", String.valueOf(currentTime));
        return formater.format(currentTime);
    }

    public static String timeOfSending(String dateOfSending) {
        Pattern patternDate = Pattern.compile("(\\d{2})\\-(\\d{2})\\s(\\d{2})\\:(\\d{2})");
        Matcher matcher = patternDate.matcher(dateOfSending);

        String currentHours = "";
        String currentMinutes= "";
        while (matcher.find()) {
            currentHours = matcher.group(3);
            currentMinutes = matcher.group(4);
        }
//        int hour = Integer.parseInt(currentHours)+3;
        return currentHours+":"+currentMinutes;
    }

    public static Time lastOnlineTime(String lastOnlineInfo) {
        if (lastOnlineInfo == null) {
            return new Time();
        }
        Pattern patternLastDate = Pattern.compile("(\\d{2})\\-(\\d{2})\\s(\\d{2})\\:(\\d{2})");
        Matcher matcherLast = patternLastDate.matcher(lastOnlineInfo);
        Time lastDate = new Time();
        while (matcherLast.find()) {
            lastDate.lastDay = matcherLast.group(1);
            lastDate.lastMonth = matcherLast.group(2);
            lastDate.lastTime = matcherLast.group(3)+":"+matcherLast.group(4);
        }
        return lastDate;
    }

    public static String lastOnlineStatus(Time lastDate) {
        String currentDate = currentDate();
        Pattern patternDate = Pattern.compile("(\\d{2})\\-(\\d{2})\\s(\\d{2})\\:(\\d{2})");
        Matcher matcher = patternDate.matcher(currentDate);

        String currentDay = "";
        String currentMonth = "";
        while (matcher.find()) {
            currentDay = matcher.group(1);
            currentMonth = matcher.group(2);
        }

        if (Objects.equals(currentDay, lastDate.lastDay) && Objects.equals(currentMonth, lastDate.lastMonth)) {
            return String.format("был(а) сегодня в %s", lastDate.lastTime);
        } else {
            return String.format("был(а) %s/%s в %s", lastDate.lastDay, lastDate.lastMonth, lastDate.lastTime);
        }
    }

    public String getLastDay() {
        return lastDay;
    }

    public String getLastMonth() {
        return lastMonth;
    }

    public String getLastTime() {
        return lastTime;
    }

    @Override
    public String toString() {
        return "Time{" +
                "lastDay='" + lastDay + '\'' +
                ", lastMonth='" + lastMonth + '\'' +
                ", lastTime='" + lastTime + '\'' +
                '}';
    }
}
