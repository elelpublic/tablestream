// (C) 1998-2015 Information Desire Software GmbH
// www.infodesire.com

package com.infodesire.tablestream;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

import com.infodesire.commons.datetime.SimpleDate;
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
  

}


