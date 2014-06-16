// (C) 1998-2015 Information Desire Software GmbH
// www.infodesire.com

package com.infodesire.commons.datetime;

import static org.junit.Assert.assertEquals;

import java.text.ParseException;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;



public class SimpleTimeTest {


  @Before
  public void setUp() throws Exception {
  }


  @After
  public void tearDown() throws Exception {
  }


  @Test
  public void testFormatParse() throws ParseException {
    
    assertEquals( new SimpleTime( 10, 22 ), SimpleTime.fromString( "10:22:00.000Z" ) );
    assertEquals( new SimpleTime( 10, 22, 33 ), SimpleTime.fromString( "10:22:33.000Z" ) );
    assertEquals( new SimpleTime( 10, 22, 33, 444 ), SimpleTime.fromString( "10:22:33.444Z" ) );
    assertEquals( new SimpleTime( 10, 22, 33, 444 ), SimpleTime.fromString( "10:22:33.444" ) );
    assertEquals( "10:22:33.444Z", new SimpleTime( 10, 22, 33, 444 ).toString() );
    
    testWrongFormat( null );
    testWrongFormat( "10" );
    
  }


  private void testWrongFormat( String value ) {
    try {
      SimpleTime.fromString( value );
      Assert.fail( "Missing parse exception when parsing: " + value );
    }
    catch( ParseException ex ) {}
  }

}
