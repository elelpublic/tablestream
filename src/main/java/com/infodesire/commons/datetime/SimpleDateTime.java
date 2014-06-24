// (C) 1998-2014 Information Desire Software GmbH
// www.infodesire.com

package com.infodesire.commons.datetime;

import com.infodesire.commons.string.PowerString;

import java.io.Serializable;
import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;


/**
 * An immutable date and time value. The only operations supported are formatting, parsing, comparing.
 * <p>
 * 
 * No validity checking, calendar operations supported here. Please be reminded,
 * that we use the human readable month numbers 1-12.
 * <p>
 * 
 * There are however convenience conversion oparations to the java type Date.
 * 
 */
public class SimpleDateTime implements Comparable<SimpleDateTime>, Serializable {
  
  
  private static final long serialVersionUID = 1L;
  
  
  private SimpleDate date;
  private SimpleTime time;
  
  
  /**
   * Create date
   * 
   * @param year Year
   * @param month Month (1-12)
   * @param day Day (1-31)
   * @param hour Hour (0-23)
   * @param minute Minute (0-59)
   * @param second Second (0-59)
   * @param millisecond Millisecond (0-999)
   * 
   */
  public SimpleDateTime( int year, int month, int day, int hour, int minute,
    int second, int millisecond ) {
    
    this( new SimpleDate( year, month, day ), // 
      new SimpleTime( hour, minute, second, millisecond ) );
    
  }
  

  /**
   * Create date
   * 
   * @param date Year
   * @param month Month (1-12)
   * @param day (1-31)
   * 
   */
  public SimpleDateTime( SimpleDate date, SimpleTime time ) {
    this.date = date;
    this.time = time;
  }
  
  
  /**
   * Create date
   *
   * @param data Java date
   * 
   */
  public SimpleDateTime( Date date ) {
    this( DateUtils.getCalendar( date ) );
  }
  

  /**
   * Create date
   *
   * @param data Java date
   * 
   */
  public SimpleDateTime( Calendar calendar ) {
    this( calendar.get( Calendar.YEAR ) //
      , calendar.get( Calendar.MONTH ) + 1 //
      , calendar.get( Calendar.DAY_OF_MONTH ) //
      , calendar.get( Calendar.HOUR_OF_DAY ) //
      , calendar.get( Calendar.MINUTE ) //
      , calendar.get( Calendar.SECOND ) //
      , calendar.get( Calendar.MILLISECOND ) //
      );
  }
  
  
  public String toString() {
    return date.toString() + "T" + time.toString();
  }
  
  public static SimpleDateTime fromString( String value ) throws ParseException {
    
    PowerString s = new PowerString( value );
    String dateValue = s.removeBeforeFirst( "T" );
    SimpleDate date = SimpleDate.fromString( dateValue );
    SimpleTime time = new SimpleTime( 0, 0 );
    if( !s.isEmpty() ) {
      time = SimpleTime.fromString( s.toString() );
    }
    return new SimpleDateTime( date, time );
    
  }

  public SimpleDate getDate() {
    return date;
  }
  
  public SimpleTime getTime() {
    return time;
  }
  

  @Override
  public int compareTo( SimpleDateTime o ) {
    int result = date.compareTo( o.date );
    if( result == 0 ) {
      result = time.compareTo( o.time );
    }
    return result;
  }
  
  
  public int hashCode() {
    return toString().hashCode();
  }
  
  
  public boolean equals( Object o ) {
    if( o instanceof SimpleDateTime ) {
      SimpleDateTime d = (SimpleDateTime) o;
      return date.equals( d.date ) && time.equals( d.time );
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
    calendar.set( Calendar.YEAR, date.getYear() );
    calendar.set( Calendar.MONTH, date.getMonth() - 1 );
    calendar.set( Calendar.DAY_OF_MONTH, date.getDay() );
    calendar.set( Calendar.HOUR_OF_DAY, time.getHour() );
    calendar.set( Calendar.MINUTE, time.getMinute() );
    calendar.set( Calendar.SECOND, time.getSecond() );
    calendar.set( Calendar.MILLISECOND, time.getMillisecond() );
    return calendar.getTime();
  }
  
  
}


