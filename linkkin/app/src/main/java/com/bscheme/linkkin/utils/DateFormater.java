package com.bscheme.linkkin.utils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by newton-pc on 6/29/2015.
 */
public class DateFormater {
    public static String formatDate(String paramDate){

        DateFormat formater = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        SimpleDateFormat dateFormat = new SimpleDateFormat("MMMM d, yyyy");
        formater.setLenient(true);

        try{
            Date d = formater.parse(paramDate);
            paramDate = dateFormat.format(d);

        }catch (ParseException e) {
            e.printStackTrace();
        }
        return paramDate;
    }
    public static String getFormatedDate(String paramDate) {

        DateFormat readFormat = new SimpleDateFormat( "yyyy-MM-dd");
        SimpleDateFormat dateFormat = new SimpleDateFormat("MMMM d, yyyy");
        try {
            Date date = readFormat.parse(paramDate);
            paramDate = dateFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return paramDate;
    }
    public static Date getDate(String paramDate) {

        Date date = null;
        DateFormat readFormat = new SimpleDateFormat( "dd-MM-yyyy");
        try {
            date = readFormat.parse(paramDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return date;
    }
    public static Date getDate_Range(String paramDate) {

        Date date = null;
        SimpleDateFormat dateFormat = new SimpleDateFormat("MMMM d, yyyy", Locale.ENGLISH);
        DateFormat readFormat = new SimpleDateFormat( "dd-MM-yyyy");
        try {
            date = dateFormat.parse(paramDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return date;
    }

    public static String getFormatServerDate(String paramDate) {

        Date date = null;
        SimpleDateFormat readFormat = new SimpleDateFormat("MMMM d, yyyy", Locale.ENGLISH);
        DateFormat dateFormat = new SimpleDateFormat( "dd-MM-yyyy");
        try {
            date = readFormat.parse(paramDate);
            paramDate = dateFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return paramDate;
    }
}
