// (C) 1998-2015 Information Desire Software GmbH
// www.infodesire.com

package com.infodesire.tablestream;

import static org.junit.Assert.*;

import com.infodesire.commons.datetime.SimpleDate;

import java.util.Iterator;
import java.util.SortedSet;
import java.util.TreeSet;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;



public class RowSorterTest {


  @Before
  public void setUp() throws Exception {
  }


  @After
  public void tearDown() throws Exception {
  }


  @Test
  public void testSorting() {
    
    Row r1 = create( "ABC", 16, new SimpleDate( 2014, 12, 12 ) );
    Row r2 = create( "XYZ", 11, new SimpleDate( 2014, 10, 1 ) );
    Row r3 = create( "DEF", 11, new SimpleDate( 2014, 10, 10 ) );
    Row r4 = create( "AAA", 10, new SimpleDate( 2014, 12, 12 ) );
    
    assertOrder( new RowSorter( new SortIndex( 1, 2, 3 ) ), r4, r1, r3, r2 );
    assertOrder( new RowSorter( new SortIndex( 2, 3, 1 ) ), r4, r2, r3, r1 );
    assertOrder( new RowSorter( new SortIndex( -2, 3, 1 ) ), r1, r2, r3, r4 );
    assertOrder( new RowSorter( new SortIndex( -2, -3, 1 ) ), r1, r3, r2, r4 );
    
    // two equal rows sorted by original index
    Row r5 = create( "AAA", 10, new SimpleDate( 2014, 12, 12 ) );
    r4.setOriginalIndex( 4 );
    r5.setOriginalIndex( 5 );
    assertOrder( new RowSorter( new SortIndex( 1 ) ), r4, r5 );
    r5.setOriginalIndex( 1 );
    assertOrder( new RowSorter( new SortIndex( 1 ) ), r5, r4 );
    
  }


  private void assertOrder( RowSorter rowSorter, Row... rows ) {
    SortedSet<Row> sorted = new TreeSet<Row>( rowSorter );
    for( Row row : rows ) {
      sorted.add( row );
    }
    Iterator<Row> i = sorted.iterator();
    for( Row row : rows ) {
      assertEquals( row, i.next() );
    }
  }


  private Row create( String string, int num, SimpleDate date ) {
    Row row = new Row();
    row.add( new Cell( string ) );
    row.add( new Cell( num ) );
    row.add( new Cell( date ) );
    return row;
  }
  

}
