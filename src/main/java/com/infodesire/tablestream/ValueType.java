// (C) 1998-2015 Information Desire Software GmbH
// www.infodesire.com

package com.infodesire.tablestream;


/**
 * Supported value types
 *
 */
public enum ValueType {
  
  
  STRING( "s" ),
  
  
  /**
   * Integer
   * 
   */
  INTEGER( "i" ),
  
  
  /**
   * Double
   * 
   */
  DOUBLE( "d" ),
  
  
  /**
   * Boolean
   * 
   */
  BOOLEAN( "b" ), 
  
  
  /**
   * List of strings
   * 
   */
  LIST( "l" ),
  
  
  /**
   * Date
   * 
   */
  DATE( "da" ), 
  
  
  /**
   * Time
   * 
   */
  TIME( "ti"),
  
  
  /**
   * Date and time (in the sense of a timestamp)
   * 
   */
  DATETIME( "dt")
  
  
  ;
  
  private String shortName;

  private ValueType( String shortName ) {
    this.shortName = shortName;
  }

  public String shortName() {
    return shortName;
  }
  
}


