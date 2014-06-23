// (C) 1998-2015 Information Desire Software GmbH
// www.infodesire.com

package com.infodesire.tablestream;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

import com.infodesire.commons.datetime.SimpleDate;
import com.infodesire.commons.datetime.SimpleDateTime;
import com.infodesire.commons.datetime.SimpleTime;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;



public class CellTest {


  @Before
  public void setUp() throws Exception {
  }


  @After
  public void tearDown() throws Exception {
  }


  @Test
  public void testEquality() throws InterruptedException {
    
    Cell c1, c2;
    
    c1 = new Cell( "ABC" );
    c2 = new Cell( "ABC" );
    
    assertEquals( c1, c1 );
    assertEquals( c1, c2 );
    
    c2.setBg( Color.WHITE );
    assertNotEquals( c1, c2 );
    
    c1.setBg( Color.BLACK );
    assertNotEquals( c1, c2 );
    
    c1.setBg( Color.WHITE );
    assertEquals( c1, c2 );
    
    c2.setFg( Color.WHITE );
    assertNotEquals( c1, c2 );
    
    c1.setFg( Color.BLACK );
    assertNotEquals( c1, c2 );
    
    c1.setFg( Color.WHITE );
    assertEquals( c1, c2 );
    
    c1.setCaption( "XXX" );
    assertNotEquals( c1, c2 );
    
    c2.setCaption( "XXX" );
    assertEquals( c1, c2 );
    
    c1 = new Cell( 123 );
    assertNotEquals( c1, c2 );
    
    c2 = new Cell( "123" );
    assertNotEquals( c1, c2 );
    
    c2 = new Cell( 124 );
    assertNotEquals( c1, c2 );
    
    c2 = new Cell( 123 );
    assertEquals( c1, c2 );
    
    c1 = new Cell( 123.2 );
    assertNotEquals( c1, c2 );

    c2 = new Cell( 123.3 );
    assertNotEquals( c1, c2 );
    
    c2 = new Cell( 123.2 );
    assertEquals( c1, c2 );
    
    c1 = new Cell( true );
    assertNotEquals( c1, c2 );
    
    c2 = new Cell( false );
    assertNotEquals( c1, c2 );
    
    c2 = new Cell( true );
    assertEquals( c1, c2 );
    
    List<String> l1 = null, l2 = null;
    
    c2 = new Cell( l2 );
    assertNotEquals( c1, c2 );
    
    c1 = new Cell( (String) null );
    assertNotEquals( c1, c2 );
    
    c1 = new Cell( l1 );
    assertEquals( c1, c2 );
    
    l1 = new ArrayList<String>();
    c1 = new Cell( l1 );
    assertEquals( c1, c2 ); // empty collection equals null collection
    
    l2 = new ArrayList<String>();
    c2 = new Cell( l2 );
    assertEquals( c1, c2 );

    l1.add( "A" );
    c1 = new Cell( l1 );
    assertNotEquals( c1, c2 );
    
    l2.add( "A" );
    c2 = new Cell( l2 );
    assertEquals( c1, c2 );
    
    l1.add( "B" );
    l1.add( "C" );
    l2.add( "C" );
    l2.add( "B" );
    c1 = new Cell( l1 );
    c2 = new Cell( l2 );
    assertNotEquals( c1, c2 );
    
    SimpleDate d1 = new SimpleDate( 2014, 6, 16 );
    SimpleDate d2 = new SimpleDate( 2014, 6, 17 );
    
    c1 = new Cell( d1 );
    assertNotEquals( c1, c2 );
    
    c2 = new Cell( d1 );
    assertEquals( c1, c2 );
    
    c2 = new Cell( d2 );
    assertNotEquals( c1, c2 );
    
    c2 = new Cell( new SimpleDate( 2014, 6, 16 ) );
    assertEquals( c1, c2 );
    
    SimpleTime t1 = new SimpleTime( 10, 20 );
    SimpleTime t2 = new SimpleTime( 10, 21 );
    
    c1 = new Cell( t1 );
    assertNotEquals( c1, c2 );
    
    c2 = new Cell( t1 );
    assertEquals( c1, c2 );
    
    c2 = new Cell( t2 );
    assertNotEquals( c1, c2 );
    
    t2 = new SimpleTime( 10, 20 );
    c2 = new Cell( t2 );
    assertEquals( c1, c2 );
    
  }
  

  @Test
  public void testCompare() throws InterruptedException {
    
    // nulls
    assertTrue( new Cell( (String) null ).compareTo( new Cell( "ABC" ) ) < 0 );
    assertTrue( new Cell( "ABC" ).compareTo( new Cell( (String) null ) ) > 0 );
    assertTrue( new Cell( (String) null ).compareTo( new Cell( (String) null ) ) == 0 );

    // different types
    int a = new Cell( "ABC" ).compareTo( new Cell( 12 ) );
    int b = new Cell( 12 ).compareTo( new Cell( "ABC" ) );
    assertTrue( a != 0 );
    assertTrue( a == -b );
    
    // string
    assertTrue( new Cell( "ABC" ).compareTo( new Cell( "ABC" ) ) == 0 );
    assertTrue( new Cell( "ABC" ).compareTo( new Cell( "DEF" ) ) < 0 );
    assertTrue( new Cell( "ABCD" ).compareTo( new Cell( "ABC" ) ) > 0 );
    
    // integer
    assertTrue( new Cell( 12 ).compareTo( new Cell( 12 ) ) == 0 );
    assertTrue( new Cell( 12 ).compareTo( new Cell( 13 ) ) < 0 );
    assertTrue( new Cell( 13 ).compareTo( new Cell( 12 ) ) > 0 );
    
    // double
    assertTrue( new Cell( 12.3 ).compareTo( new Cell( 12.3 ) ) == 0 );
    assertTrue( new Cell( 12.3 ).compareTo( new Cell( 13.2 ) ) < 0 );
    assertTrue( new Cell( 13.2 ).compareTo( new Cell( 12.3 ) ) > 0 );
    
    // boolean
    assertTrue( new Cell( true ).compareTo( new Cell( true ) ) == 0 );
    assertTrue( new Cell( false ).compareTo( new Cell( false ) ) == 0 );
    assertTrue( new Cell( false ).compareTo( new Cell( true ) ) < 0 );
    assertTrue( new Cell( true ).compareTo( new Cell( false ) ) > 0 );
    
    // date
    assertTrue( new Cell( new SimpleDate( 2014, 10, 3 ) ).compareTo( new Cell( new SimpleDate( 2014, 10, 3 ) ) ) == 0 );
    assertTrue( new Cell( new SimpleDate( 2014, 10, 3 ) ).compareTo( new Cell( new SimpleDate( 2014, 10, 4 ) ) ) < 0 );
    assertTrue( new Cell( new SimpleDate( 2014, 10, 4 ) ).compareTo( new Cell( new SimpleDate( 2014, 10, 3 ) ) ) > 0 );
    
    // time
    assertTrue( new Cell( new SimpleTime( 10, 11, 12, 200 ) ).compareTo( new Cell( new SimpleTime( 10, 11, 12, 200 ) ) ) == 0 );
    assertTrue( new Cell( new SimpleTime( 10, 11, 12, 200 ) ).compareTo( new Cell( new SimpleTime( 10, 11, 12, 201 ) ) ) < 0 );
    assertTrue( new Cell( new SimpleTime( 10, 11, 12, 201 ) ).compareTo( new Cell( new SimpleTime( 10, 11, 12, 200 ) ) ) > 0 );
    
    // datetime
    assertTrue( new Cell( new SimpleDateTime( 2014, 10, 3, 10, 11, 12, 200 ) ).compareTo( new Cell( new SimpleDateTime( 2014, 10, 3, 10, 11, 12, 200 ) ) ) == 0 );
    assertTrue( new Cell( new SimpleDateTime( 2014, 10, 3, 10, 11, 12, 200 ) ).compareTo( new Cell( new SimpleDateTime( 2014, 10, 3, 10, 11, 12, 201 ) ) ) < 0 );
    assertTrue( new Cell( new SimpleDateTime( 2014, 10, 3, 10, 11, 12, 201 ) ).compareTo( new Cell( new SimpleDateTime( 2014, 10, 3, 10, 11, 12, 200 ) ) ) > 0 );

    // list
    assertTrue( new Cell( new SimpleList( "A", "B", "C" ) ).compareTo( new Cell( new SimpleList( "A", "B", "C" ) ) ) == 0 );
    assertTrue( new Cell( new SimpleList( "A", "B", "A" ) ).compareTo( new Cell( new SimpleList( "A", "B", "C" ) ) ) < 0 );
    assertTrue( new Cell( new SimpleList( "A", "B", "D" ) ).compareTo( new Cell( new SimpleList( "A", "B", "C" ) ) ) > 0 );
    
  }
  
  
  @Test
  public void testCaptions() {
    
    Cell c1, c2;
    
    c1 = new Cell( 10 );
    c2 = new Cell( 20 );

    assertTrue( c1.compareTo( c2 ) < 0 );
    
    c1.setCaption( "Zulauf" );
    assertTrue( c1.compareTo( c2 ) > 0 );

    c2.setCaption( "Abfluss" );
    assertTrue( c1.compareTo( c2 ) > 0 );
    
    c1.setCaption( null );
    assertTrue( c1.compareTo( c2 ) < 0 );
    
  }
  
  
}


