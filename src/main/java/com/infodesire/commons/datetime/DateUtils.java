// (C) 1998-2015 Information Desire Software GmbH
// www.infodesire.com

package com.infodesire.commons.datetime;

import java.util.Calendar;
import java.util.Date;


/**
 * Utilities for working with the java date/time classes
 *
 */
public class DateUtils {

  
  /**
   * Get a calendar from a date. Calendars are useful for date manipulation
   * 
   * @param date Date
   * @return Calendar for date.
   * 
   */
  public static Calendar getCalendar( Date date ) {
    Calendar calendar = Calendar.getInstance();
    calendar.setTime( date );
    return calendar;
  }
  

}


