// (C) 1998-2014 Information Desire Software GmbH
// www.infodesire.com

package com.infodesire.tablestream;

import com.infodesire.commons.JAVA;
import com.infodesire.commons.STRING;
import com.infodesire.commons.datetime.SimpleDate;
import com.infodesire.commons.datetime.SimpleDateTime;
import com.infodesire.commons.datetime.SimpleTime;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import com.thoughtworks.xstream.annotations.XStreamOmitField;

import java.awt.Color;


/**
 * Cell in a row. Cells have value and format properties. 
 * <p>
 * 
 * A cell has a value of one of the defined values, see {@link ValueType}.
 * <p>
 * 
 * A cell can be empty if its isNull() method returns true. 
 * <p>
 * 
 * Cells may optionally have a display caption. This caption is
 * used when the cell value is printed out for the user to see, while
 * the internal value is used for processing operations, like sum.
 * <p>
 * 
 * Captions will also be used for sorting.
 * <p>
 * 
 * Some format properties, like bold, color etc. will be used when
 * the output format supports these formattings.
 * <p>
 * 
 * Cells are comparable.
 *
 */
@XStreamAlias( "c" )
public class Cell implements Comparable<Cell> {


  @XStreamOmitField
  private ValueType type;


  @XStreamAlias( "s" )
  private String stringValue;


  @XStreamAlias( "i" )
  private Integer intValue;
  
  
  @XStreamAlias( "d" )
  private Double doubleValue;
  
  
  @XStreamAlias( "b" )
  private Boolean booleanValue;
  
  
  @XStreamAlias( "l" )
  private SimpleList listValue;
  

  @XStreamAlias( "da" )
  private SimpleDate dateValue;
  
  
  @XStreamAlias( "ti" )
  private SimpleTime timeValue;
  
  
  @XStreamAlias( "dt" )
  private SimpleDateTime datetimeValue;
  
  
  @XStreamAlias( "ca" )
  @XStreamAsAttribute
  private String caption;
  
  
  @XStreamAlias( "bg" )
  @XStreamAsAttribute
  private Color bg;


  @XStreamAlias( "fg" )
  @XStreamAsAttribute
  private Color fg;


  /**
   * Create a string typed cell
   * 
   * @param value Value of the cell.
   * 
   */
  public Cell( String value ) {
    this.stringValue = value;
    type = ValueType.STRING;
  }


  /**
   * Create an int typed cell.
   * 
   * @param value Value of the cell.
   */
  public Cell( Integer value ) {
    this.intValue = value;
    type = ValueType.INTEGER;
  }


  /**
   * Create a double typed cell.
   * 
   * @param value Value of the cell.
   */
  public Cell( Double value ) {
    this.doubleValue = value;
    type = ValueType.DOUBLE;
  }
  
  
  /**
   * Create a boolean typed cell.
   * 
   * @param value Value of the cell.
   */
  public Cell( Boolean value ) {
    this.booleanValue = value;
    type = ValueType.BOOLEAN;
  }
  
  
  /**
   * Create a list typed cell.
   * 
   * @param value Value of the cell.
   */
  public Cell( Iterable<String> value ) {
    this.listValue = new SimpleList( value );
    type = ValueType.LIST;
  }
  
  
  /**
   * Create a date typed cell.
   * 
   * @param value Value of the cell.
   */
  public Cell( SimpleDate value ) {
    this.dateValue = value;
    type = ValueType.DATE;
  }
  
  
  /**
   * Create a date typed cell.
   * 
   * @param value Value of the cell.
   */
  public Cell( SimpleTime value ) {
    this.timeValue = value;
    type = ValueType.TIME;
  }
  
  
  /**
   * Create a date typed cell.
   * 
   * @param value Value of the cell.
   */
  public Cell( SimpleDateTime value ) {
    this.datetimeValue = value;
    type = ValueType.DATETIME;
  }
  
  
  /**
   * Getter
   * 
   * @return Value of the cell if this is a string typed cell. Null otherwise.
   */
  public String getString() {
    return stringValue;
  }


  /**
   * Getter
   * 
   * @return Value of the cell if this is an int typed cell. Null otherwise.
   */
  public Integer getInt() {
    return intValue;
  }


  /**
   * Getter
   * 
   * @return Value of the cell if this is an double typed cell. Null otherwise.
   */
  public Double getDouble() {
    return doubleValue;
  }
  
  
  /**
   * Getter
   * 
   * @return Value of the cell if this is an date typed cell. Null otherwise.
   */
  public SimpleDate getDate() {
    return dateValue;
  }
  
  
  /**
   * Getter
   * 
   * @return Value of the cell if this is an time typed cell. Null otherwise.
   */
  public SimpleTime getTime() {
    return timeValue;
  }
  
  
  /**
   * Getter
   * 
   * @return Value of the cell if this is an datetime typed cell. Null otherwise.
   */
  public SimpleDateTime getDatetime() {
    return datetimeValue;
  }
  
  
  /**
   * Getter
   * 
   * @return Background color (optional)
   */
  public Color getBg() {
    return bg;
  }


  /**
   * Setter
   * 
   * @param bg Background color (optional)
   */
  public void setBg( Color bg ) {
    this.bg = bg;
  }


  /**
   * Getter
   * 
   * @return Foreground color (optional)
   */
  public Color getFg() {
    return fg;
  }


  /**
   * Setter
   * 
   * @param fg Foreground color (optional)
   */
  public void setFg( Color fg ) {
    this.fg = fg;
  }


  /**
   * Test for null.
   * 
   * @return Value contains null
   * 
   */
  public boolean isNull() {
    return intValue == null //
      && stringValue == null //
      && doubleValue == null //
      && booleanValue == null //
      && ( listValue == null || listValue.isNull() )//
      && dateValue == null //
      && timeValue == null //
      && datetimeValue == null //
      ;
  }
  
  
  /**
   * Getter
   * 
   * @return Optional caption used to display the intern value
   */
  public String getCaption() {
    return caption;
  }
  
  
  /**
   * Setter
   * 
   * @param caption Optional caption used to display the intern value
   */
  public void setCaption( String caption ) {
    this.caption = caption;
  }
  
  
  /**
   * Create a debug info.
   * 
   * @return Debug info.
   * 
   */
  public String toString() {
    String value = null;
    ValueType type = getType();
    if( isNull() ) {
      value = "null";
    }
    else {
      if( type == ValueType.STRING ) {
        value = stringValue;
      }
      else if( type == ValueType.INTEGER ) {
        value = "" + intValue;
      }
      else if( type == ValueType.DOUBLE ) {
        value = "" + doubleValue;
      }
      else if( type == ValueType.BOOLEAN ) {
        value = "" + booleanValue;
      }
      else if( type == ValueType.LIST ) {
        value = "" + listValue;
      }
      else if( type == ValueType.DATE ) {
        value = "" + dateValue;
      }
      else if( type == ValueType.TIME ) {
        value = "" + timeValue;
      }
      else if( type == ValueType.DATETIME ) {
        value = "" + datetimeValue;
      }
    }
    return type.shortName() + ":" + value // 
      + ( caption == null ? "" : " (cap=" + caption + ")" ) //
      + ( bg == null ? "" : " (bg=" + bg + ")" ) //
      + ( fg == null ? "" : " (fg=" + fg + ")" ) //
      ;
  }


  /**
   * Getter
   * 
   * @return Type of cell value. Empty cells are always considered of type String.
   * 
   */
  public ValueType getType() {
    if( type == null ) {
      if( intValue != null ) {
        type = ValueType.INTEGER;
      }
      else if( doubleValue != null ) {
        type = ValueType.DOUBLE;
      }
      else if( booleanValue != null ) {
        type = ValueType.BOOLEAN;
      }
      else if( listValue != null ) {
        type = ValueType.LIST;
      }
      else if( dateValue != null ) {
        type = ValueType.DATE;
      }
      else if( timeValue != null ) {
        type = ValueType.TIME;
      }
      else if( datetimeValue != null ) {
        type = ValueType.DATETIME;
      }
      else {
        type = ValueType.STRING;
      }
    }
    return type;
  }


  public int hashCode() {
    return toString().hashCode();
  }
  
  
  /**
   * Two cells are equal if they are of the same type, contain the same value
   * or are both null, and if they have the same formatting properties.
   * Also the captions must be equal or both null.
   * 
   */
  public boolean equals( Object o ) {
    if( o instanceof Cell ) {
      Cell cell = (Cell) o;
      if( getType() == cell.getType() ) {
        if( isNull() != cell.isNull() ) {
          return false;
        }
        if( type == ValueType.STRING ) {
          if( !stringValue.equals( cell.stringValue ) ) {
            return false;
          }
        }
        else if( type == ValueType.INTEGER ) {
          if( !intValue.equals( cell.intValue ) ) {
            return false;
          }
        }
        else if( type == ValueType.DOUBLE ) {
          if( !doubleValue.equals( cell.doubleValue ) ) {
            return false;
          }
        }
        else if( type == ValueType.BOOLEAN ) {
          if( !booleanValue.equals( cell.booleanValue ) ) {
            return false;
          }
        }
        else if( type == ValueType.LIST ) {
          if( !listValue.equals( cell.listValue ) ) {
            return false;
          }
        }
        else if( type == ValueType.DATE ) {
          if( !dateValue.equals( cell.dateValue ) ) {
            return false;
          }
        }
        else if( type == ValueType.TIME ) {
          if( !timeValue.equals( cell.timeValue ) ) {
            return false;
          }
        }
        else if( type == ValueType.DATETIME ) {
          if( !datetimeValue.equals( cell.datetimeValue ) ) {
            return false;
          }
        }
        if( !nullSafeEquals( bg, cell.bg ) ) {
          return false;
        }
        if( !nullSafeEquals( fg, cell.fg ) ) {
          return false;
        }
        if( !nullSafeEquals( caption, cell.caption ) ) {
          return false;
        }
        return true;
      }
    }
    return false;
  }


  /**
   * Equals method for two objects with the extra functionality, that two nulls are considered equal.
   * 
   * @param o1 First object
   * @param o2 Second object
   * @return Two objects are equal or both null
   * 
   */
  private boolean nullSafeEquals( Object o1, Object o2 ) {
    if( o1 == null || o2 == null ) {
      return o1 == o2;
    }
    return o1.equals( o2 );
  }


  @Override
  public int compareTo( Cell o ) {
    
    if( isNull() ) {
      if( o.isNull() ) {
        return 0;
      }
      else {
        return -1;
      }
    }
    
    if( o.isNull() ) {
      return 1;
    }
    
    // if at least one of the 2 cells have caption, we compare by caption
    if( !STRING.isEmpty( caption ) || !STRING.isEmpty( o.caption ) ) {
      return JAVA.compareTo( caption, o.caption );
    }
    
    ValueType type = getType();
    ValueType oType = o.getType();
    if( type != oType ) {
      return type.compareTo( oType );
    }
    
    if( type == ValueType.INTEGER ) {
      return intValue.compareTo( o.intValue );
    }
    else if( type == ValueType.DOUBLE ) {
      return doubleValue.compareTo( o.doubleValue );
    }
    else if( type == ValueType.BOOLEAN ) {
      return booleanValue.compareTo( o.booleanValue );
    }
    else if( type == ValueType.DATE ) {
      return dateValue.compareTo( o.dateValue );
    }
    else if( type == ValueType.TIME ) {
      return timeValue.compareTo( o.timeValue );
    }
    else if( type == ValueType.DATETIME ) {
      return datetimeValue.compareTo( o.datetimeValue );
    }
    else if( type == ValueType.LIST ) {
      return listValue.compareTo( o.listValue );
    }
    else { // STRING
      return stringValue.compareTo( o.stringValue );
    }
    
  }
  

}
 