// (C) 1998-2015 Information Desire Software GmbH
// www.infodesire.com

package com.infodesire.tablestream.util;

import static org.junit.Assert.*;

import com.infodesire.commons.FILE;
import com.infodesire.commons.NUMBER;
import com.infodesire.tablestream.Cell;
import com.infodesire.tablestream.FileWriter;
import com.infodesire.tablestream.Row;
import com.infodesire.tablestream.TS;
import com.infodesire.tablestream.tsfile.TSReader;
import com.infodesire.tablestream.tsfile.TSWriter;

import java.io.File;
import java.io.IOException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;



public class SplitWriterTest {


  @Before
  public void setUp() throws Exception {
  }


  @After
  public void tearDown() throws Exception {
  }


  @Test
  public void test() throws IOException {
    
    File dir = FILE.createTempDir( "SplitWriterTest-" );
    try {
      FileWriter fileWriter = new TSWriter();
      SplitWriter sw = new SplitWriter( 5, fileWriter, dir, "split.${num}.ts", null );
      for( int i = 0; i < 43; i++ ) {
        Row row = new Row();
        row.add( new Cell( i ) );
        sw.write( row );
      }
      sw.close();
      int fileCounter = 0;
      int rowIndex = 0;
      TS ts = new TS();
      for( File file : dir.listFiles() ) {
        if( file.isFile() ) {
          assertEquals( fileCounter < 8 ? 5 : 3, ts.count( file ).getRows() );
          File expected = new File( dir, "split." + NUMBER.digits( 5, fileCounter ) + ".ts" );
          assertEquals( "Expected: " + expected + " was " + file, expected, file );
          fileCounter++;
          TSReader reader = new TSReader( file );
          for( Row row = reader.next(); row != null; row = reader.next() ) {
            assertEquals( new Integer( rowIndex++ ), row.getCell( 0 ).getInt() );
          }
        }
      }
      assertEquals( 9, fileCounter );
    }
    finally {
      FILE.rmdir( dir );
    }
    
  }

}
