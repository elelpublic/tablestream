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

}


