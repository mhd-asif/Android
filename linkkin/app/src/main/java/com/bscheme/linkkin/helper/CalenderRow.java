package com.bscheme.linkkin.helper;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;

public class CalenderRow
{
  private static final int DAY_OFFSET = 1;
  private int currentDayOfMonth;
  private int daysInMonth;
  private final int[] daysOfMonth = { 31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31 };
  private ArrayList<String> list;
  
  private int getNumberOfDaysOfMonth(int paramInt)
  {
    return this.daysOfMonth[paramInt];
  }
  
  private void setCurrentDayOfMonth(int paramInt)
  {
    this.currentDayOfMonth = paramInt;
  }
  
  public int getCurrentDayOfMonth()
  {
    return this.currentDayOfMonth;
  }
  
  public int getTotalRow(int month, int year)
  {
    this.list = new ArrayList();


    Calendar calendar = Calendar.getInstance();
    if(month==calendar.get(Calendar.MONTH)&&year==calendar.get(Calendar.YEAR))
    {
      setCurrentDayOfMonth(calendar.get(Calendar.DAY_OF_MONTH));
    }
    else
    {
      setCurrentDayOfMonth(0);
    }


    daysInMonth = getNumberOfDaysOfMonth(month);

    // Gregorian Calendar : MINUS 1, set to FIRST OF MONTH
    GregorianCalendar cal = new GregorianCalendar(year, month, 1);


    //setCurrentWeekDay(calendar.get(Calendar.DAY_OF_WEEK));

    int currentWeekDay = cal.get(Calendar.DAY_OF_WEEK) - 1;
    int trailingSpaces = currentWeekDay;

    if (cal.isLeapYear(cal.get(Calendar.YEAR)) && month == 1)
    {
      ++daysInMonth;
    }

    // Trailing Month days
    for (int i = 0; i < trailingSpaces; i++)
    {
      list.add(String.valueOf(0));
    }

    int startPos=trailingSpaces;
    int endPos=startPos+daysInMonth-1;

    // Current Month Days
    for (int i = 1; i <= daysInMonth; i++)
    {
      list.add(String.valueOf(i));
    }

    // Leading Month days
    for (int i = 0; i < list.size()%7; i++)
    {
      list.add(String.valueOf(i+1));
    }

    if (list.size() <= 35) {
      return 5;
    }
    else return 6;
  }
}

