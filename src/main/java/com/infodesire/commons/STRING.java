// (C) 1998-2015 Information Desire Software GmbH
// www.infodesire.com

package com.infodesire.commons;

import com.infodesire.commons.string.PowerString;


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
   * Replace once a part in a string with a new part
   * 
   * @param string The string containing the old part
   * @param oldPart The old part to be removed
   * @param newPart The new part to be inserted
   * @return The string with replacement
   * 
   */
  public static String replace( String string, String oldPart,
    String newPart ) {
    
    if( string.indexOf( oldPart ) == -1 ) {
      return string;
    }
    else {
      PowerString s = new PowerString( string );
      String before = s.removeBeforeFirst( oldPart );
      return before + newPart + s.toString();
    }
    
  }


}


