// (C) 1998-2015 Information Desire Software GmbH
// www.infodesire.com

package com.infodesire.commons;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;



public class NUMBERTest {


  @Before
  public void setUp() throws Exception {
  }


  @After
  public void tearDown() throws Exception {
  }


  @Test
  public void testDigits() {
    
    assertEquals( "001", NUMBER.digits( 3, 1 ) );
    assertEquals( "999", NUMBER.digits( 3, 999 ) );
    assertEquals( "1999", NUMBER.digits( 3, 1999 ) );
    
  }
  
}
