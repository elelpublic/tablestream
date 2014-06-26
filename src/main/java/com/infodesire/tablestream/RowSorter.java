// (C) 1998-2015 Information Desire Software GmbH
// www.infodesire.com

package com.infodesire.tablestream;

import java.util.Comparator;


/**
 * Sorts rows using a sort index
 *
 */
public class RowSorter implements Comparator<Row> {

  private SortIndex sortIndex;

  public RowSorter( SortIndex sortIndex ) {
    this.sortIndex = sortIndex;
  }

  @Override
  public int compare( Row row1, Row row2 ) {
    for( int column : sortIndex ) {
      boolean desc = column < 0;
      column = Math.abs( column );
      Cell cell1 = row1.getCell( column - 1 );
      Cell cell2 = row2.getCell( column - 1 );
      int result = cell1.compareTo( cell2 );
      if( result != 0 ) {
        return desc ? -result : result;
      }
    }
    // as a fallback: rows might provide for an original index
    return row1.getOriginalIndex() - row2.getOriginalIndex();
  }

}
