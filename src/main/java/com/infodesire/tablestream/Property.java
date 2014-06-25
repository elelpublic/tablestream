// (C) 1998-2015 Information Desire Software GmbH
// www.infodesire.com

package com.infodesire.tablestream;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;


/**
 * A user defined property of a cell
 *
 */
@XStreamAlias( "prop" )
public class Property {
  
  @XStreamAlias( "k" )
  @XStreamAsAttribute
  private String key;
  
  @XStreamAlias( "v" )
  @XStreamAsAttribute
  private String value;
  
  public Property( String key, String value ) {
    this.key = key;
    this.value = value;
  }
  
  public int hashCode() {
    return key.hashCode();
  }
  
  public boolean equals( Object o ) {
    if( o instanceof Property ) {
      return ( (Property) o ).key.equals( key );
    }
    return false;
  }

  public String getKey() {
    return key;
  }

  public String getValue() {
    return value;
  }

}
