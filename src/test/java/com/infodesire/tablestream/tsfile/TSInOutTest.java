// (C) 1998-2015 Information Desire Software GmbH
// www.infodesire.com

package com.infodesire.tablestream.tsfile;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import com.infodesire.commons.DATE;
import com.infodesire.commons.FILE;
import com.infodesire.commons.datetime.SimpleDate;
import com.infodesire.commons.datetime.SimpleDateTime;
import com.infodesire.commons.datetime.SimpleTime;
import com.infodesire.tablestream.Cell;
import com.infodesire.tablestream.Row;
import com.infodesire.tablestream.ValueType;

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
    
    SimpleDate date = new SimpleDate( 2014, 10, 10 );
    
    OutputStream out = getOut();
    TSWriter tsout = new TSWriter( out );
    List<Row> rows = new ArrayList<Row>();
    for( int i = 0; i < ROWS; i++ ) {
      
      Row row = new Row();
      
      for( int c = 0; c < COLUMNS; c++ ) {
        
        Cell cell = null;
        ValueType type = ValueType.values()[ c % ValueType.values().length ];
        
        if( type == ValueType.STRING ) {
          cell = new Cell( "CELL " + i + ":" + c );
        }
        else if( type == ValueType.INTEGER ) {
          cell = new Cell( i * c );
        }
        else if( type == ValueType.DOUBLE ) {
          cell = new Cell( (double) i / (double) c );
        }
        else if( type == ValueType.BOOLEAN ) {
          cell = new Cell( i % 2 == 0 );
        }
        else if( type == ValueType.LIST ) {
          List<String> list = new ArrayList<String>();
          for( int e = 0; e < c * 2; e++ ) {
            list.add( "Entry_" + e );
          }
          cell = new Cell( list );
        }
        else if( type == ValueType.DATE ) {
          cell = new Cell( date );
        }
        else if( type == ValueType.TIME ) {
          SimpleTime time = new SimpleTime( i % 23, i % 59 );
          cell = new Cell( time );
        }
        else if( type == ValueType.DATETIME ) {
          SimpleTime time = new SimpleTime( i % 23, i % 59 );
          cell = new Cell( new SimpleDateTime( date, time ) );
        }
        
        row.add( cell );
        
        date = DATE.add( date, 1 );
        
      }
      
      rows.add( row );
      tsout.write( row );
    }
    tsout.close();
    
    FILE.print( tmpFile );
    
    InputStream in = getIn();
    TSReader tsin = new TSReader( in );
    for( int i = 0; i < ROWS; i++ ) {
      Row row = tsin.next();
      assertTrue( row != null );
      assertEquals( row, rows.get( i ) );
    }
    assertNull( tsin.next() );
    tsin.close();
     
  }


  private OutputStream getOut() throws IOException {
    if( useFile ) {
      tmpFile = File.createTempFile( getClass().getSimpleName() + "-", ".ts" );
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
