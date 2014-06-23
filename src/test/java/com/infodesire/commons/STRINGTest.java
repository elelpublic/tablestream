// (C) 1998-2015 Information Desire Software GmbH
// www.infodesire.com

package com.infodesire.commons;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;



public class STRINGTest {


  @Before
  public void setUp() throws Exception {
  }


  @After
  public void tearDown() throws Exception {
  }


  @Test
  public void testReplace() {
    
    assertEquals( "ABCDEF", STRING.replace( "ABCDEF", "cde", "123" ) );
    assertEquals( "AB123F", STRING.replace( "ABCDEF", "CDE", "123" ) );
    
  }

}
