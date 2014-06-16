// (C) 1998-2015 Information Desire Software GmbH
// www.infodesire.com

package com.infodesire.commons.string;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;



public class PowerStringTest {


  @Before
  public void setUp() throws Exception {
  }


  @After
  public void tearDown() throws Exception {
  }


  @Test
  public void testRemoveBeforeFirst() {
    
    PowerString ps;
    
    ps = new PowerString( null );
    
    assertNull( ps.removeBeforeFirst( "-" ) );
    assertNull( ps.removeBeforeFirst( null ) );
    
    ps = new PowerString( "abc-def-ghi" );
    assertNull( ps.removeBeforeFirst( null ) );
    assertEquals( "abc", ps.removeBeforeFirst( "-" ) );
    assertEquals( "def-ghi", ps.toString() );
    assertEquals( "def", ps.removeBeforeFirst( "-" ) );
    assertEquals( "ghi", ps.toString() );
    assertEquals( "ghi", ps.removeBeforeFirst( "-" ) );
    assertTrue( ps.isEmpty() );
    assertNull( ps.toString() );
    
  }

}
