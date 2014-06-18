// (C) 1998-2015 Information Desire Software GmbH
// www.infodesire.com

package com.infodesire.tablestream;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


/**
 * Defines which columns are sorted. Each entry is a 1-based column number. To sort
 * descending, simply specify -column number.
 *
 */
public class SortIndex implements Iterable<Integer> {
  
  
  private List<Integer> indexes = new ArrayList<Integer>();

  
  /**
   * Create sort index for one column
   *  
   * @param columnSort Sort by column at index. Sort descending if columnSort < 0.
   * 
   */
  public SortIndex( int... columnSort ) {
    for( int index : columnSort ) {
      add( index );
    }
  }


  /**
   * Create sort index for one column
   *  
   * @param columnSort Sort by column at index. Sort descending if columnSort < 0.
   * 
   */
  public SortIndex( int columnSort ) {
    indexes.add( columnSort );
  }
  
  
  /**
   * Parse index from a comma separated list of index numbers. <p>
   * 
   * Remember: -num means sort column num descending. <p>
   * 
   * Example: 3,-1,2
   * 
   * @param value
   * @return
   */
  public static SortIndex fromString( String value ) {
    SortIndex sortIndex = new SortIndex();
    for( String entry : value.split( "," ) ) {
      int index = Integer.parseInt( entry );
      sortIndex.add( index );
    }
    return sortIndex;
  }


  public void add( int index ) {
    int neg = -index;
    if( indexes.contains( index ) || indexes.contains( neg ) ) {
      throw new RuntimeException( "Duplicate index: " + index + " in sort index " + this );
    }
    indexes.add( index );
  }
  
  
  public String toString() {
    String result = "";
    String sep = "";
    for( Integer index : indexes ) {
      result += sep + index;
    }
    return result;
  }


  @Override
  public Iterator<Integer> iterator() {
    return indexes.iterator();
  }
  

}


