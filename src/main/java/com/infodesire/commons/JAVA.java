// (C) 1998-2015 Information Desire Software GmbH
// www.infodesire.com

package com.infodesire.commons;


/**
 * Collection of handy tools which we think, improve the daily java programming life.
 *
 */
public class JAVA {

  
  /**
   * Assert that an integer is in a valid range, and throw a RuntimeException if not.
   * 
   * @param num The integer
   * @param from The lowest allowed value
   * @param to The highest allowed value
   * 
   */
  public static void assertThat( int num, int from, int to ) {
    if( num < from || num > to ) {
      throw new RuntimeException( "Value " + num + " out of valid range: " + from + "-" + to );
    }
  }
  

}
