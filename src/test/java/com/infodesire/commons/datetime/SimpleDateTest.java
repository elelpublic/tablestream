// (C) 1998-2015 Information Desire Software GmbH
// www.infodesire.com

package com.infodesire.commons.datetime;

import static org.junit.Assert.assertEquals;

import java.text.ParseException;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;



public class SimpleDateTest {


  @Before
  public void setUp() throws Exception {
  }


  @After
  public void tearDown() throws Exception {
  }


  @Test
  public void testFormatParse() throws ParseException {
    
    assertEquals( new SimpleDate( 2014, 6, 16 ), SimpleDate.fromString( "2014-06-16" ) );
    assertEquals( "2014-06-16", new SimpleDate( 2014, 6, 16 ).toString() );
    
    testWrongFormat( null );
    testWrongFormat( "2014-06" );
    testWrongFormat( "2014-06-" );
    
  }


  private void testWrongFormat( String value ) {
    try {
      SimpleDate.fromString( value );
      Assert.fail( "Missing parse exception when parsing: " + value );
    }
    catch( ParseException ex ) {}
  }

}
