// (C) 1998-2015 Information Desire Software GmbH
// www.infodesire.com

package com.infodesire.commons;

import java.util.Arrays;
import java.util.Collections;


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

  
  /**
   * Null safe compare operation
   * 
   * @param string1 String or null
   * @param string2 String or null
   * @return Null safe string comparison, with null < Object, Object > null, null == null. 
   * 
   */
  public static int compareTo( String string1, String string2 ) {
    
    if( string1 == null ) {
      if( string2 == null ) {
        return 0;
      }
      else {
        return -1;
      }
    }
    else {
      if( string2 == null ) {
        return 1;
      }
      return string1.compareTo( string2 );
    }
    
  }


  /**
   * Null safe iterable
   * 
   * @param iterable Iterable or null
   * @return Iterable object
   */
  public static <T> Iterable<T> iterable( Iterable<T> iterable ) {
    if( iterable != null ) {
      return iterable;
    }
    else {
      return Collections.emptyList();
    }
  }

  
  /**
   * Null safe iterable
   * 
   * @param iterable Iterable or null
   * @return Iterable object
   */
  public static <T> Iterable<T> iterable( T[] iterable ) {
    if( iterable != null ) {
      return Arrays.asList( iterable );
    }
    else {
      return Collections.emptyList();
    }
  }


}


