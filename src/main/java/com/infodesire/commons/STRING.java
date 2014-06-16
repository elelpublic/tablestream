// (C) 1998-2015 Information Desire Software GmbH
// www.infodesire.com

package com.infodesire.commons;


/**
 * Some functions which makes handling strings easier. 
 *
 */
public class STRING {

  
  /**
   * Test method
   * 
   * @param value String value
   * @return A string object is empty when it is null or contains only whitespaces
   *  
   */
  public static boolean isEmpty( String value ) {
    return value == null || value.trim().length() == 0;
  }
  

  /**
   * Generate some random text
   * 
   * @param length Lenght of text to be generated
   * @return Random text
   * 
   */
  public static String random( int length ) {
    
    StringBuffer result = new StringBuffer();
    for( int i = 0; i < length; i++ ) {
      char c = ' ';
      if( Math.random() * 10 > 1 ) {
        c = (char) ( 'A' + Math.round( Math.random() * 26 ) );
      }
      if( Math.random() > 0.5 ) {
        c = Character.toLowerCase( c );
      }
      result.append( c );
    }
    return result.toString();
    
  }


  /**
   * Create a mumber with leading zeros
   * 
   * @param digits Total number of digits
   * @param number Number
   * @return Formatted number
   * 
   */
  public static String digits( int digits, int number ) {
    String result = "" + number;
    while( result.length() < digits ) {
      result = "0" + result;
    }
    return result;
  }


}


