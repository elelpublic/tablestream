// (C) 1998-2015 Information Desire Software GmbH
// www.infodesire.com

package com.infodesire.commons;


/**
 * Utilities for numbers
 *
 */
public class NUMBER {
  
  
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
