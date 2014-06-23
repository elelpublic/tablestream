// (C) 1998-2015 Information Desire Software GmbH
// www.infodesire.com

package com.infodesire.tablestream;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import com.infodesire.commons.FILE;
import com.infodesire.tablestream.tsfile.TSReader;
import com.infodesire.tablestream.tsfile.TSWriter;

import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;



public class TSTest {


  private TS ts;
  private File testFile;
  private File outputFile;
  private File testDir;


  @Before
  public void setUp() throws Exception {
    createTestFileset();
  }


  private void createTestFileset() throws IOException {
    ts = new TS();
    ts.setQuiet( true );
    testFile = File.createTempFile( "TSTest-input-", ".ts" );
    outputFile = File.createTempFile( "TSTest-output-", ".ts" );
    testDir = FILE.createTempDir( "TSTest-" );
  }


  @After
  public void tearDown() throws Exception {
    deleteTestFileset();
  }


  private void deleteTestFileset() {
    if( testFile != null ) {
      testFile.delete();
    }
    if( testDir != null ) {
      FILE.rmdir( testDir );
    }
  }


  @Test
  public void testSplit() throws Exception {
    
    testSplit( false, 4 );
    testSplit( true, 4 );

    testSplit( false, 1 );
    testSplit( true, 1 );
    
    testSplit( false, 0 );
    testSplit( true, 0 );
    
  }


  private void testSplit( boolean sorted, int threads ) throws Exception {
    testSplit( 6, 2, sorted, threads );
    testSplit( 30, 10, sorted, threads );
  }


  private void testSplit( int rows, int splitRows, boolean sorted, int threads ) throws Exception {

    createTestFileset();
    
    ts.setNumThreads( threads );
    ts.random( testFile, rows, 1 );
    List<File> splitFiles = ts.split( testFile, splitRows, testDir, sorted ? new SortIndex( 1 ) : null );
    
    int minFiles = rows / splitRows;
    assertTrue( splitFiles.size() >= minFiles );
    
    Set<Row> actual = new HashSet<Row>();
    int count = 0;
    for( File splitFile : splitFiles ) {
      count += ts.count( splitFile ).getRows();
      actual.addAll( ts.read( splitFile ) );
      if( sorted ) {
        assertEquals( -1, ts.sorted( splitFile, new SortIndex( 1 ) ) );
      }
    }
    assertEquals( rows, count );
    
    Set<Row> expected = new HashSet<Row>();
    expected.addAll( ts.read( testFile ) );
    
    deleteTestFileset();
    
  }


  @Test
  public void testBigsort() throws Exception {
    
    testBigsort( 4 );
    testBigsort( 1 );
    testBigsort( 0 );
    testBigsort( 0 );
    
  }
  
  
  private void testBigsort( int threads ) throws Exception {
    testBigsort( 6, 2, threads );
    testBigsort( 30, 10, threads );
  }
  
  
  private void testBigsort( int rows, int splitRows, int threads ) throws Exception {
    
    createTestFileset();
    
    ts.setNumThreads( threads );
    ts.random( testFile, rows, 1 );
    
    SortIndex sortIndex = new SortIndex( 1 );
    ts.bigsort( testFile, outputFile, sortIndex, splitRows, 4 );
    assertEquals( -1, ts.sorted( outputFile, sortIndex ) );
    
    deleteTestFileset();
    
  }
  
  
  @Test
  public void testSlice() throws IOException {

    TSWriter out = new TSWriter( testFile );
    for( int i = 1; i <= 10; i++ ) {
      Row row = new Row();
      row.add( new Cell( i ) );
      out.write( row );
    }
    out.close();
    
    assertEquals( 5, ts.slice( testFile, outputFile, 3, 7 ) );
    assertEquals( 5, ts.count( outputFile ).getRows() );
    
    TSReader in = new TSReader( outputFile );
    for( int i = 3; i <=7; i++ ) {
      Row row = in.next();
      Cell cell = row.getCell( 0 );
      assertEquals( new Integer( i ), cell.getInt() );
    }
    assertNull( in.next() );
    in.close();
    
    assertEquals( 10, ts.slice( testFile, outputFile, 0, 0 ) );
    assertEquals( 10, ts.count( outputFile ).getRows() );
    
    in = new TSReader( outputFile );
    for( int i = 1; i <=10; i++ ) {
      Row row = in.next();
      Cell cell = row.getCell( 0 );
      assertEquals( new Integer( i ), cell.getInt() );
    }
    assertNull( in.next() );
    in.close();
    
  }


}
