// (C) 1998-2014 Information Desire Software GmbH
// www.infodesire.com

package com.infodesire.commons.datetime;

import com.infodesire.commons.JAVA;
import com.infodesire.commons.STRING;
import com.infodesire.commons.string.PowerString;

import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;


/**
 * An immutable date value. The only operations supported are formatting, parsing, comparing.
 * <p>
 * 
 * No validity checking, calendar operations supported here. Please be reminded,
 * that we use the human readable month numbers 1-12.
 * <p>
 * 
 * There are however convenience conversion oparations to the java type Date.
 * 
 */
public class SimpleDate implements Comparable<SimpleDate> {
  
  
  private int year;
  
  
  private byte month;
  
  
  private byte day;
  
  
  /**
   * Create date
   * 
   * @param year Year
   * @param month Month (1-12)
   * @param day (1-31)
   * 
   */
  public SimpleDate( int year, int month, int day ) {
    this.year = year;
    this.month = (byte) month;
    this.day = (byte) day;
    JAVA.assertThat( month, 1, 12 );
    JAVA.assertThat( day, 1, 31 );
  }
  

  /**
   * Create date
   *
   * @param data Java date
   * 
   */
  public SimpleDate( Date date ) {
    this( DateUtils.getCalendar( date ) );
  }
  

  /**
   * Create date
   *
   * @param data Java date
   * 
   */
  public SimpleDate( Calendar calendar ) {
    this( calendar.get( Calendar.YEAR ), calendar.get( Calendar.MONTH ) + 1,
      calendar.get( Calendar.DAY_OF_MONTH ) );
  }
  
  
  public String toString() {
    return year + "-" + twoDigit( month ) + "-" + twoDigit( day );
  }
  
  
  /**
   * Create date value by parsing string
   * 
   * @param value String value
   * @return Date value
   */
  public static SimpleDate fromString( String value ) throws ParseException {
    
    PowerString s = new PowerString( value );
    String yearString = s.removeBeforeFirst( "-" );
    missing( yearString, "Year" );
    String monthString = s.removeBeforeFirst( "-" );
    missing( monthString, "Month" );
    String dayString = s.toString();
    missing( dayString, "Day" );
    
    try {
      int year = Integer.parseInt( yearString );
      int month = Integer.parseInt( monthString );
      int day = Integer.parseInt( dayString );
      return new SimpleDate( year, month, day );
    }
    catch( NumberFormatException ex ) {
      throw new ParseException( ex.toString(), 0 );
    }
    
  }


  private static void missing( String value, String message ) throws ParseException {
    if( STRING.isEmpty( value ) ) {
      throw new ParseException( "Missing: " + message, 0 );
    }
  }


  private String twoDigit( byte num ) {
    return num < 10 ? "0" + num : "" + num;
  }
  
  public int getYear() {
    return year;
  }
  
  public int getMonth() {
    return month;
  }
  
  public int getDay() {
    return day;
  }

  @Override
  public int compareTo( SimpleDate o ) {
    int result = year - o.year;
    if( result == 0 ) {
      result = month - o.month;
      if( result == 0 ) {
        result = day - o.day;
      }
    }
    return result;
  }
  
  
  public int hashCode() {
    return toString().hashCode();
  }
  
  
  public boolean equals( Object o ) {
    if( o instanceof SimpleDate ) {
      SimpleDate d = (SimpleDate) o;
      return year == d.year && month == d.month && day == d.day;
    }
    return false;
  }
  
  
  /**
   * Create java date from this date information, using default calendar and timezone.
   * 
   * @return Java date
   * 
   */
  public Date toDate() {
    return toDate( Calendar.getInstance() );
  }
  

  /**
   * Create java date from this date information.
   * 
   * @param calendar Calendar to use for transformation
   * @return Java date
   * 
   */
  public Date toDate( Calendar calendar ) {
    calendar.set( Calendar.YEAR, year );
    calendar.set( Calendar.MONTH, month - 1 );
    calendar.set( Calendar.DAY_OF_MONTH, day );
    return calendar.getTime();
  }


}


