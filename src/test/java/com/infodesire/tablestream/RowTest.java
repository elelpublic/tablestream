// (C) 1998-2015 Information Desire Software GmbH
// www.infodesire.com

package com.infodesire.tablestream;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;



public class RowTest {


  @Before
  public void setUp() throws Exception {
  }


  @After
  public void tearDown() throws Exception {
  }


  @Test
  public void testEquality() {
    
    Row r1 = new Row( new Cell( 1 ) );
    Row r2 = new Row( new Cell( 1 ) );
    assertEquals( r1, r2 );
    
    r1.setOriginalIndex( 1 );
    r2.setOriginalIndex( 1 );
    assertEquals( r1, r2 );
    
    r2.setOriginalIndex( 2 );
    assertNotEquals( r1, r2 );
    
    r1.setOriginalIndex( 2 );
    assertEquals( r1, r2 );
    
    r1 = new Row( new Cell( 1 ) );
    r2 = new Row( new Cell( 2 ) );
    assertNotEquals( r1, r2 );
    
  }

}
