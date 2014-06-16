// (C) 1998-2015 Information Desire Software GmbH
// www.infodesire.com

package com.infodesire.commons.string;


/**
 * String class wrapper with focus on manipulation (not performance!).
 *
 */
public class PowerString {

  private String string;

  public PowerString( String string ) {
    this.string = string;
  }

  
  /**
   * Remove and return the part of the string before the first occurrence of 'sep'.
   * <p>
   * 
   * This method is handy for instance to parse a date format "YYYY-MM-DD". Each call with "-"
   * as separator will remove and return the next component. If no separator is found anymore,
   * the whole remaining string (DD) will be returned and the powerstring set to null.  
   * 
   * @param sep The separator string
   * @return The string removed or the whole string if 'sep' was not found. This object will contain only the rest of the original text after 'sep'.
   * 
   */
  public String removeBeforeFirst( String sep ) {
    
    if( string == null || sep == null ) {
      return null;
    }
    
    int at = string.indexOf( sep );
    
    if( at == -1 ) {
      String result = string;
      string = null;
      return result;
    }
    
    String result = string.substring( 0, at );
    string = string.substring( at + sep.length() );
    return result;
    
  }
  
  public String toString() {
    return string;
  }
  
  public boolean isEmpty() {
    return string == null || string.length() == 0;
  }

}


