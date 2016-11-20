package com.bscheme.linkkin.utils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

/**
 * Created by root on 10/27/14.
 */
public class CustomFormateDate {

    static String[] banglaNumbers={"০","১","২","৩","৪","৫","৬","৭","৮","৯"};

    public static String getFormatedDate(String paramDate){
        /*SimpleDateFormat dateFormat = new SimpleDateFormat("ccc, dd MMM yyyy");
        try {
            Date date = dateFormat.parse(paramDate);
            paramDate = dateFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }*/

        DateFormat readFormat = new SimpleDateFormat( "EEE, dd MMM yyyy HH:mm:ss");

        DateFormat writeFormat = new SimpleDateFormat( "yyyy-MM-dd HH:mm:ss");

        Date d = null;
        try {
            d = readFormat.parse(paramDate);//catch exception
            Calendar newsDay = Calendar.getInstance();
            newsDay.setTime(d);

            Calendar today = Calendar.getInstance();

            TimeZone gmt = newsDay.getTimeZone();
            long diff = today.getTimeInMillis() - newsDay.getTimeInMillis(); //result in millis

            long seconds = (diff - gmt.getRawOffset()) / 1000;
            long minutes = seconds / 60;
            long hours = minutes / 60;
            long days = hours / 24;
            long months = days/30;

            if (seconds < 60){
                return String.valueOf(seconds)+" seconds";
            }else if(minutes < 60){
                return String.valueOf(minutes)+" minutes";
            }else if(hours < 24){
                return String.valueOf(hours)+" hours";
            }else if (days < 30){
                if (hours > 24 && hours <48){
                    return "Yesterday";
                }else {
                    return String.valueOf(days) + " days";
                }
            }else if (months < 12){
                return String.valueOf(months)+" months";
            }else {
                return customFormatedDate(paramDate);
            }

        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return null;

//        return paramDate;
    }
    public static String getFormatedDate_Bangla(String paramDate){
        /*SimpleDateFormat dateFormat = new SimpleDateFormat("ccc, dd MMM yyyy");
        try {
            Date date = dateFormat.parse(paramDate);
            paramDate = dateFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }*/

        DateFormat readFormat = new SimpleDateFormat( "EEE, dd MMM yyyy HH:mm:ss");

        DateFormat writeFormat = new SimpleDateFormat( "yyyy-MM-dd HH:mm:ss");

        Date d = null;
        try {
            d = readFormat.parse(paramDate);//catch exception
            Calendar newsDay = Calendar.getInstance();
            newsDay.setTime(d);

            Calendar today = Calendar.getInstance();

            TimeZone gmt = newsDay.getTimeZone();
            long diff = today.getTimeInMillis() - newsDay.getTimeInMillis(); //result in millis

            long seconds = (diff - gmt.getRawOffset()) / 1000;
            long minutes = seconds / 60;
            long hours = minutes / 60;
            long days = hours / 24;
            long months = days/30;

            if (seconds < 60){
                return getBengaliText(String.valueOf(seconds))+" সেকেন্ড";
            }else if(minutes < 60){
                return getBengaliText(String.valueOf(minutes))+" মিনিট";
            }else if(hours < 24){
                return getBengaliText(String.valueOf(hours))+" ঘন্টা";
            }else if (days < 30){
                if (hours > 24 && hours <48){
                    return "গতকাল";
                }else {
                    return getBengaliText(String.valueOf(days)) + " দিন";
                }
            }else if (months < 12){
                return getBengaliText(String.valueOf(months))+" মাস";
            }else {
                return customFormatedDate(paramDate);
            }

        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return null;

//        return paramDate;
    }
    public static String customFormatedDateForFullFeed_Bangla(String paramDate) {

        DateFormat readFormat = new SimpleDateFormat( "EEE, dd MMM yyyy HH:mm:ss");

        DateFormat writeFormat = new SimpleDateFormat( "yyyy-MM-dd HH:mm:ss");

        Date d = null;
        try {
            d = readFormat.parse(paramDate);//catch exception
            Calendar newsDay = Calendar.getInstance();
            newsDay.setTime(d);

            Calendar today = Calendar.getInstance();

            TimeZone gmt = newsDay.getTimeZone();
            long diff = (today.getTimeInMillis() - newsDay.getTimeInMillis()); //result in millis

            long seconds = (diff-(gmt.getRawOffset())) / 1000;
            long minutes = seconds / 60;
            long hours = minutes / 60;
            long days = hours / 24;
            long months = days/30;

            if (seconds < 60){
                return getBengaliText(String.valueOf(seconds))+" সেকেন্ড পূর্বে";
            }else if(minutes < 60){
                return getBengaliText(String.valueOf(minutes))+" মিনিট পূর্বে";
            }else if(hours < 24){
                return getBengaliText(String.valueOf(hours))+" ঘন্টা পূর্বে";
            }else if (days < 30){
                if (hours > 24 && hours <48){
                    return "গতকাল";
                }else {
                    return getBengaliText(String.valueOf(days)) + " দিন পূর্বে";
                }
            }else if (months < 12){
                return getBengaliText(String.valueOf(months))+" মাস পূর্বে";
            }else {
                return customFormatedDate(paramDate);
            }

        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return null;
    }

    public static String customFormatedDateForFullFeed(String paramDate) {

        //DateFormat readFormat = new SimpleDateFormat( "EEE, dd MMM yyyy HH:mm:ss");
        DateFormat readFormat = new SimpleDateFormat( "yyyy-MM-dd HH:mm:ss");

        DateFormat writeFormat = new SimpleDateFormat( "yyyy-MM-dd HH:mm:ss");

        Date d = null;
        try {
            d = readFormat.parse(paramDate);//catch exception
            Calendar newsDay = Calendar.getInstance();
            newsDay.setTime(d);

            Calendar today = Calendar.getInstance();

            TimeZone gmt = newsDay.getTimeZone();
            long diff = (today.getTimeInMillis() - newsDay.getTimeInMillis()); //result in millis

            long seconds = (diff-(gmt.getRawOffset())) / 1000;
            long minutes = seconds / 60;
            long hours = minutes / 60;
            long days = hours / 24;
            long months = days/30;

            if (seconds < 60){
                return String.valueOf(seconds)+" seconds ago";
            }else if(minutes < 60){
                return String.valueOf(minutes)+" minutes ago";
            }else if(hours < 24){
                return String.valueOf(hours)+" hours ago";
            }else if (days < 30){
                if (hours > 24 && hours <48){
                    return "Yesterday";
                }else {
                    return String.valueOf(days) + " days ago";
                }
            }else if (months < 12){
                return String.valueOf(months)+" months ago";
            }else {
                return customFormatedDate(paramDate);
            }

        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return null;
    }
    public static String customFormatedDate(String paramDate) {

       // DateFormat readFormat = new SimpleDateFormat( "EEE, dd MMM yyyy HH:mm:ss");
        DateFormat readFormat = new SimpleDateFormat( "yyyy-MM-dd HH:mm:ss");

        DateFormat writeFormat = new SimpleDateFormat("ccc, dd MMM yyyy");

        Date d = null;
        try {
            d = readFormat.parse(paramDate);//catch exception
            Calendar newsDay = Calendar.getInstance();
            newsDay.setTime(d);

            Calendar today = Calendar.getInstance();

            long diff = today.getTimeInMillis() - newsDay.getTimeInMillis(); //result in millis


                return writeFormat.format(d);


        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return null;
    }



    private static String getBengaliText(String text)
    {
        String banglaText="";
        for(int i=0;i<text.length();i++)
        {
            String temp=String.valueOf(text.charAt(i));
            int number=Integer.valueOf(temp);
            banglaText+=banglaNumbers[number];
        }
        return banglaText;
    }



}
