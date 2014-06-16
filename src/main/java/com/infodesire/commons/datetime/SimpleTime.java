// (C) 1998-2014 Information Desire Software GmbH
// www.infodesire.com

package com.infodesire.commons.datetime;

import com.infodesire.commons.JAVA;
import com.infodesire.commons.STRING;
import com.infodesire.commons.datetime.DateUtils;
import com.infodesire.commons.string.PowerString;

import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;


/**
 * An immutable time value. The only operations supported are formatting, parsing, comparing.
 * <p>
 * 
 * No validity checking operations are supported here.
 * <p>
 * 
 * There are however convenience conversion oparations to the java type Date.
 * 
 */
public class SimpleTime implements Comparable<SimpleTime> {
  
  
  private byte hour;
  
  
  private byte minute;
  
  
  private byte second;
  
  
  private short millisecond;
  
  
  public SimpleTime( int hour, int minute ) {
    this( hour, minute, 0, 0 );
  }
  
  public SimpleTime( int hour, int minute, int second ) {
    this( hour, minute, second, 0 );
  }
  
  
  /**
   * Create time
   * 
   * @param hour Hour (0-23)
   * @param minute Minute (0-59)
   * @param second Second (0-59)
   * @param millisecond Millisecond (0-999)
   * 
   */
  public SimpleTime( int hour, int minute, int second, int millisecond ) {
    this.hour = (byte) hour;
    this.minute = (byte) minute;
    this.second = (byte) second;
    this.millisecond = (short) millisecond;
    JAVA.assertThat( hour, 0, 23 );
    JAVA.assertThat( minute, 0, 59 );
    JAVA.assertThat( second, 0, 59 );
    JAVA.assertThat( millisecond, 0, 999 );
  }
  

  /**
   * Create date
   *
   * @param data Java date
   * 
   */
  public SimpleTime( Date date ) {
    this( DateUtils.getCalendar( date ) );
  }
  

  /**
   * Create date
   *
   * @param data Java date
   * 
   */
  public SimpleTime( Calendar calendar ) {
    this( calendar.get( Calendar.HOUR_OF_DAY ) //
      , calendar.get( Calendar.MINUTE ) //
      , calendar.get( Calendar.SECOND ) //
      , calendar.get( Calendar.MILLISECOND ) // 
      );
  }
  
  
  public String toString() {
    return twoDigit( hour ) + ":" + twoDigit( minute ) + ":"
      + twoDigit( second ) + "." + threeDigit( millisecond ) + "Z";
   }
  
  
  /**
   * Create date value by parsing string
   * 
   * @param value String value
   * @return Date value
   */
  public static SimpleTime fromString( String value ) throws ParseException {
    
    PowerString s = new PowerString( value );
    String hourString = s.removeBeforeFirst( ":" );
    missing( hourString, "Hour" );
    String minuteString = s.removeBeforeFirst( ":" );
    missing( minuteString, "Minute" );
    
    String secondString = null;
    String millisecondString = null;
    
    if( !s.isEmpty() ) {
      secondString = s.removeBeforeFirst( "." );

      if( !s.isEmpty() ) {
        millisecondString = s.removeBeforeFirst( "Z" );
      }

    }
    
    try {
      int hour = Integer.parseInt( hourString );
      int minute = Integer.parseInt( minuteString );
      int second = secondString == null ? 0 : Integer.parseInt( secondString );
      int milliseconds = millisecondString == null ? 0 : Integer.parseInt( millisecondString );
      return new SimpleTime( hour, minute, second, milliseconds );
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
  
  private String threeDigit( short num ) {
    return num < 10 ? "00" + num : num < 100 ? "0" + num : "" + num;
  }
  
  public int getHour() {
    return hour;
  }
  
  public int getMinute() {
    return minute;
  }
  
  public int getSecond() {
    return second;
  }
  
  public int getMillisecond() {
    return millisecond;
  }
  

  @Override
  public int compareTo( SimpleTime o ) {
    int result = hour - o.hour;
    if( result == 0 ) {
      result = minute - o.minute;
      if( result == 0 ) {
        result = second - o.second;
        if( result == 0 ) {
          result = millisecond - o.millisecond;
        }
      }
    }
    return result;
  }
  
  
  public int hashCode() {
    return toString().hashCode();
  }
  
  
  public boolean equals( Object o ) {
    if( o instanceof SimpleTime ) {
      SimpleTime d = (SimpleTime) o;
      return compareTo( d ) == 0;
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
    calendar.set( Calendar.HOUR_OF_DAY, hour );
    calendar.set( Calendar.MINUTE, minute );
    calendar.set( Calendar.SECOND, second );
    calendar.set( Calendar.MILLISECOND, millisecond );
    return calendar.getTime();
  }
  
  
}


