// (C) 1998-2015 Information Desire Software GmbH
// www.infodesire.com

package com.infodesire.tablestream.tsfile;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import com.infodesire.tablestream.Cell;
import com.infodesire.tablestream.Row;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;



public class TSInOutTest {
  
  boolean useFile = true;
  private File tmpFile;
  private ByteArrayOutputStream byteStream;
  

  @Before
  public void setUp() throws Exception {
  }


  @After
  public void tearDown() throws Exception {
  }


  @Test
  public void testReadWrite() throws IOException {
    
    int ROWS = 10;
    int COLUMNS = 10;
    
    OutputStream out = getOut();
    TSWriter tsout = new TSWriter( out, ROWS );
    List<Row> rows = new ArrayList<Row>();
    for( int i = 0; i < ROWS; i++ ) {
      Row row = new Row();
      for( int c = 0; c < COLUMNS; c++ ) {
        Cell cell;
        if( c % 4 == 0 ) { // LIST
          List<String> list = new ArrayList<String>();
          for( int e = 0; e < c * 2; e++ ) {
            list.add( "Entry_" + e );
          }
          cell = new Cell( list );
        }
        else if( c % 3 == 0 ) { // STRING
          cell = new Cell( "CELL " + i + ":" + c );
        }
        else if( c % 2 == 0 ) { // DOUBLE
          cell = new Cell( (double) i / (double) c );
        }
        else { // INT
          cell = new Cell( i * c );
        }
        row.add( cell );
      }
      rows.add( row );
      tsout.write( row );
    }
    tsout.close();
    
    InputStream in = getIn();
    TSReader tsin = new TSReader( in );
    assertEquals( ROWS, tsin.getRowCount() );
    for( int i = 0; i < ROWS; i++ ) {
      assertTrue( tsin.hasNext() );
      Row row = tsin.next();
      assertEquals( row, rows.get( i ) );
    }
    assertFalse( tsin.hasNext() );
    tsin.close();
     
  }


  private OutputStream getOut() throws IOException {
    if( useFile ) {
      tmpFile = File.createTempFile( getClass().getSimpleName() + "-", ".ts" );
      System.out.println( tmpFile.getAbsolutePath() );
      return new FileOutputStream( tmpFile );
    }
    else {
      byteStream = new ByteArrayOutputStream();
      return byteStream;
    }
  }
  
  
  private InputStream getIn() throws IOException {
    if( useFile ) {
      return new FileInputStream( tmpFile );
    }
    else {
      return new ByteArrayInputStream( byteStream.toByteArray() );
    }
  }


}
