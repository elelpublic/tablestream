// (C) 1998-2015 Information Desire Software GmbH
// www.infodesire.com

package com.infodesire.commons;

import com.infodesire.commons.datetime.SimpleDate;

import java.util.Calendar;


/**
 * Makes your date manipulation life easier
 *
 */
public class DATE {

  
  /**
   * Add (or subtract) number of days to date. Returns result.
   * 
   * @param date Orginal date
   * @param days Number of days to add (use negative to subtract)
   * @return New date
   * 
   */
  public static SimpleDate add( SimpleDate date, int days ) {
    Calendar calendar = date.toCalendar();
    calendar.add( Calendar.DATE, days );
    return new SimpleDate( calendar );
  }
  

}
