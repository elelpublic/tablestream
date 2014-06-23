// (C) 1998-2015 Information Desire Software GmbH
// www.infodesire.com

package com.infodesire.tablestream.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;

import com.infodesire.tablestream.Cell;
import com.infodesire.tablestream.Row;
import com.infodesire.tablestream.TableReader;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;



public class BufferTest {


  @Before
  public void setUp() throws Exception {
  }


  @After
  public void tearDown() throws Exception {
  }
  
  
  class DummyReader implements TableReader {
    private int i = 0;
    private int rows;
    public DummyReader( int rows ) {
      this.rows = rows;
    }
    @Override
    public Row next() {
      if( i < rows ) {
        Row row = new Row();
        row.add( new Cell( i++ ) );
        return row; 
      }
      else {
        return null;
      }
    }
    @Override
    public void close() {
    }
  }
  
 
  @SuppressWarnings("deprecation")
  @Test
  public void test() throws InterruptedException, IOException {
    
    class BufferThread extends Thread {
      private boolean stop = false;
      private Buffer buffer;
      private Row current;
      private boolean eof = false;
      private boolean sleepMode = false;
      public BufferThread( Buffer buffer ) {
        this.buffer = buffer;
      }
      public void run() {
        while( !stop ) {
          if( sleepMode ) {
            try {
              Thread.sleep( 50 );
            }
            catch( InterruptedException ex ) {
            }
          }
          else {
            if( !sleepMode ) {
              current = buffer.next();
            }
          }
        }
        eof = true;
      }
      public void setStop() {
        stop = true;
      }
      public Row getCurrent() {
        return current;
      }
      public boolean getEof() {
        return eof;
      }
      public void setSleepMode( boolean sleepMode ) {
        this.sleepMode = sleepMode;
      }
    }
    
    final Buffer buffer = new Buffer( 3 );
    BufferThread t = new BufferThread( buffer );
    t.start();
    
    try {
      
      assertFalse( t.getEof() );
      assertNull( t.getCurrent() );
      
      Row row = new Row( new Cell( "ABC" ) );
      buffer.write( row );
      Thread.sleep( 2 );
      assertEquals( new Row( new Cell( "ABC" ) ), t.getCurrent() );
      
      t.setSleepMode( true );
      Thread.sleep( 2 );

      // fill until blocked
      final Set<String> finished = new HashSet<String>();
      Thread t2 = new Thread() {
        public void run() {
          try {
            for( int i = 0; i < 10; i++ ) {
              buffer.write( new Row( new Cell( i++ ) ) );
            }
            finished.add( "true" );
          }
          catch( Exception ex ) {}
        }
      };
      t2.start();
      Thread.sleep( 2 );
      
      assertEquals( 0, finished.size() );
      
      // restart the reader
      t.setSleepMode( false );
      Thread.sleep( 50 );
      assertEquals( 1, finished.size() );

      t2.stop();
      
    }
    finally {
      t.setStop();
    }
    
  }

}
