// (C) 1998-2014 Information Desire Software GmbH
// www.infodesire.com

package com.infodesire.tablestream;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamImplicit;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


/**
 * Row of data
 *
 */
@XStreamAlias( "r" )
public class Row implements Serializable {

  
  private static final long serialVersionUID = 1L;
  
  
  @XStreamImplicit
  private List<Cell> cells;
  
  
  public Row() {}
  
  
  public Row( Cell... cells ) {
    for( Cell cell: cells ) {
      add( cell );
    }
  }
  
  
  private List<Cell> getCells() {
    if( cells == null ) {
      cells = new ArrayList<Cell>();
    }
    return cells;
  }
  
  
  public void add( Cell cell ) {
    getCells().add( cell );
  }
  

  public Cell getCell( int index ) {
    return getCells().get( index );
  }


  public boolean hasCell( int index ) {
    return index < getCells().size() && index >= 0;
  }


  public int size() {
    return getCells().size();
  }
  
  
  public int hashCode() {
    return getCells().hashCode();
  }
  
  /**
   * @param row Other row
   * @return Two rows are equal if all their cells are equal and the number of their cells is equal. 
   * 
   */
  public boolean equals( Object o ) {
    if( o instanceof Row ) {
      Row row = (Row) o;
      if( size() == row.size() ) {
        for( int i = 0; i < size(); i++ ) {
          if( !getCell( i ).equals( row.getCell( i ) ) ) {
            return false;
          }
        }
        return true;
      }
    }
    return false;
  }
  
  
  public String toString() {
    return "" + getCells();
  }
  

}


