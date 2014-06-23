// (C) 1998-2014 Information Desire Software GmbH
// www.infodesire.com

package com.infodesire.tablestream;

import com.infodesire.commons.FILE;
import com.infodesire.commons.NUMBER;
import com.infodesire.commons.STRING;
import com.infodesire.commons.string.PowerString;
import com.infodesire.tablestream.sample.SampleData;
import com.infodesire.tablestream.tsfile.PeekReader;
import com.infodesire.tablestream.tsfile.TSReader;
import com.infodesire.tablestream.tsfile.TSWriter;
import com.infodesire.tablestream.util.Buffer;
import com.infodesire.tablestream.util.DistributePipe;
import com.infodesire.tablestream.util.SimplePipe;
import com.infodesire.tablestream.util.SplitWriter;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;


/**
 * Convenient access to all tablestream functionality
 *
 */
public class TS {

  
  private boolean quiet = true;
  
  
  /**
   * Number of concurrent threads
   * 
   */
  private int numThreads = 1;
  
  
  public TS() {
  }


  /**
   * Create a *.ts file with random data.
   * 
   * @param targetFile File to write to
   * @param rowCount Number of rows to write
   * @param columnCount Number of columns to write
   * @throws IOException if an error occurs while writing the data to file
   * 
   */
  public void random( File targetFile, int rowCount, int columnCount ) throws IOException {
    SampleData.random( targetFile, rowCount, columnCount );
  }


  /**
   * Create a *.ts file with generic data.
   * 
   * @param targetFile File to write to
   * @param rowCount Number of rows to write
   * @param columnCount Number of columns to write
   * @throws IOException if an error occurs while writing the data to file
   * 
   */
  public void generic( File targetFile, int rowCount, int columnCount ) throws IOException {
    SampleData.generic( targetFile, rowCount, columnCount );
  }


  /**
   * Split a *.ts file into smaller files. Store them in dir.
   * <p>
   * 
   * To create files which maintain the original order, you should
   * set the thread number to 1.
   * <p>
   * 
   * @param sourceFile Source file
   * @param rowCount Max number of rows in target files.
   * @param targetDir Directory for target files
   * @param sortIndex Optional: if specified the target files will be sorted in memory before writing to disk
   * @return Split files
   * @throws IOException If an error occurs reading the source file or writing the target files 
   * @throws FileNotFoundException The source file does not exists
   * @throws InterruptedException if a threading error occurred
   * 
   */
  public List<File> split( File sourceFile, int rowCount, File targetDir, SortIndex sortIndex ) throws FileNotFoundException, IOException, InterruptedException {
    
    TSReader reader = new TSReader( sourceFile );

    PowerString ps = new PowerString( sourceFile.getName() );
    String extension = ps.removeAfterLast( "." );
    String namePattern = ps.toString() + ".${thread}-${num}." + extension;
    
    List<SimplePipe> pipes = new ArrayList<SimplePipe>();
    List<TableWriter> writers = new ArrayList<TableWriter>();
    List<TableWriter> buffers = new ArrayList<TableWriter>();
    for( int i = 0; i < numThreads; i++ ) {
      String subPattern = STRING.replace( namePattern, "${thread}", NUMBER.digits( 2, i ) );
      SplitWriter splitWriter = new SplitWriter( rowCount, new TSWriter(), targetDir, subPattern, sortIndex );
      Buffer buffer = new Buffer( 100 );
      buffers.add( buffer );
      writers.add( splitWriter );
      SimplePipe pipe = new SimplePipe( splitWriter );
      pipe.setReader( buffer );
      pipes.add( pipe );
    }
    
    if( !targetDir.exists() ) {
      targetDir.mkdirs();
    }
    
    DistributePipe splitter = new DistributePipe( buffers );
    
    for( SimplePipe pipe : pipes ) {
      pipe.start();
    }
    
    splitter.start( reader );
    
    List<File> allFiles = new ArrayList<File>();
    
    for( SimplePipe pipe : pipes ) {
      pipe.join();
      pipe.getWriter().close();
      SplitWriter writer = (SplitWriter) pipe.getWriter();
      allFiles.addAll( writer.getFiles() );
    }
    
    splitter.join();
    
    return allFiles;
    
  }
  
  
//  private List<File> split( TableReader reader, int rowCount, File targetDir,
//    String namePrefix, String extension, SortIndex sortIndex )
//    throws FileNotFoundException, IOException, InterruptedException {
//    
//    if( !targetDir.exists() ) {
//      targetDir.mkdirs();
//    }
//    
////    List<SplitThread> threads = new ArrayList<SplitThread>();
////    for( int i = 0; i < numThreads; i++ ) {
////      threads.add( new SplitThread( i, numThreads, sourceFile, rowCount, targetDir, sortIndex ) );
////    }
////    
////    for( SplitThread thread : threads ) {
////      thread.start();
////    }
////    
////    TSReader reader = new TSReader( sourceFile );
////    int nextThreadNo = 0;
////    while( reader.hasNext() ) {
////      Row row = reader.next();
////      threads.get( nextThreadNo++ ).addRow( row );
////      if( nextThreadNo == numThreads ) {
////        nextThreadNo = 0;
////      }
////    }
////    reader.close();
////    
////    List<File> files = new ArrayList<File>();
////    for( SplitThread thread : threads ) {
////      thread.join();
////      files.addAll( thread.getFiles() );
////      Exception ex = thread.getEx();
////      if( ex != null ) {
////        if( ex instanceof IOException ) {
////          throw (IOException) ex;
////        }
////        else {
////          throw new RuntimeException( ex );
////        }
////      }
////    }
////    
////    return files;
//
//    int digits = 5;
//    int counter = 1;
//
//    File target = null;
//    TSWriter writer = null;
//    int targetSize = 0;
//    SortedSet<Row> buffer = null;
//    
//    List<File> splitFiles = new ArrayList<File>();
//    
//    for( Row row = reader.next(); row != null; row = reader.next() ) {
//      
//      if( targetSize == rowCount ) {
//        if( buffer != null ) {
//          for( Row bufferedRow : buffer ) {
//            writer.write( bufferedRow );
//          }
//        }
//        writer.close();
//        buffer = null;
//        writer = null;
//        target = null;
//      }
//      
//      if( target == null ) {
//        target = new File( targetDir, namePrefix + "."
//          + NUMBER.digits( digits, counter++ ) + "." + extension );
//        splitFiles.add( target );
//        writer = new TSWriter( target );
//        if( sortIndex != null ) {
//          buffer = new TreeSet<Row>( new RowSorter( sortIndex ) );
//        }
//        targetSize = 0;
//        info( "Writing " + target.getAbsolutePath() );
//      }
//      
//      if( buffer != null ) {
//        buffer.add( row );
//      }
//      else {
//        writer.write( row );
//      }
//
//      targetSize++;
//      
//    }
//    
//    if( buffer != null ) {
//      for( Row row : buffer ) {
//        writer.write( row );
//      }
//    }
//    
//    writer.close();
//    
//    return splitFiles;
//    
//  }
  
  
  /**
   * Join multiple *.ts files into one large file.
   * 
   * @param sourceDir Directory for source files
   * @param targetFile Target file
   * @param sortIndex Optional: if specified the source files are assumed sorted and will be joined in a sorted target file
   * @param maxOpen Max number of open source files at one time
   * @return All temporary files used for joining 
   * @throws IOException If an error occurs reading the source file or writing the target files 
   * @throws FileNotFoundException The source file does not exists
   * 
   */
  public List<File> join( File sourceDir, File targetFile, SortIndex sortIndex, int maxOpen ) throws FileNotFoundException, IOException {
    
    List<File> sourceFiles = new ArrayList<File>();
    for( File sourceFile : sourceDir.listFiles() ) {
      if( sourceFile.isFile() && sourceFile.getName().endsWith( ".ts" ) ) {
        sourceFiles.add( sourceFile );
      }
    }
    
    if( sourceFiles.isEmpty() ) {
      throw new FileNotFoundException( "No source files found" );
    }
    
    List<File> nextSourceFiles = new ArrayList<File>();
    
    List<File> tmpFiles = new ArrayList<File>();
    
    while( sourceFiles.size() > 1 ) {
    
      while( sourceFiles.size() > 1 ) {
        
        List<File> joinFiles = new ArrayList<File>();
        while( joinFiles.size() < maxOpen && !sourceFiles.isEmpty() ) {
          joinFiles.add( sourceFiles.remove( 0 ) );
        }
        
        if( joinFiles.size() == 1 ) {
          nextSourceFiles.add( joinFiles.get( 0 ) );
        }
        else if( joinFiles.size() > 1 ) {
          File joinFile = File.createTempFile( "join-", ".ts" );
          tmpFiles.add( joinFile );
          joinFile.deleteOnExit();
          join( joinFiles, joinFile, sortIndex );
          nextSourceFiles.add( joinFile );
        }
        
      }
      
      sourceFiles.addAll( nextSourceFiles );
      nextSourceFiles.clear();

    }
    
    FILE.move( sourceFiles.get( 0 ), targetFile );
    
    return tmpFiles;
    
  }


  /**
   * Join files into one larger file. If sort index is given, the source files are assumed
   * to be sorted by this index and the target file will be sorted too.
   * <p>
   * 
   * Attention: all source files will be oened at once. If your operating system
   * has limitations on open files, they should be applied here.
   * 
   * @param sourceFiles Source files
   * @param targetFile Target file
   * @param sortIndex Optional: if specified the source files are assumed sorted and will be joined in a sorted target file
   * @throws IOException if an exception occured while reading sources or writing target file
   * @throws FileNotFoundException Source file was not found 
   * 
   */
  public void join( List<File> sourceFiles, File targetFile, SortIndex sortIndex ) throws FileNotFoundException, IOException {
    
    info( "Join " + sourceFiles.size() + " files into " + targetFile );

    RowSorter rowSorter = sortIndex == null ? null : new RowSorter( sortIndex );
    
    List<PeekReader> readers = new ArrayList<PeekReader>();
    for( File sourceFile : sourceFiles ) {
      readers.add( new PeekReader( new TSReader( sourceFile ) ) );
    }
    
    TSWriter writer = new TSWriter( targetFile );

    boolean hasMore = true;
    while( hasMore ) {
      Row nextRow = null;
      int readerIndex = -1;
      for( int i = 0; i < readers.size(); i++ ) {
        PeekReader reader = readers.get( i );
        Row peekRow = reader.peek();
        if( peekRow != null ) {
          if( nextRow == null ) {
            nextRow = peekRow;
            readerIndex = i;
          }
          else {
            if( rowSorter == null || rowSorter.compare( peekRow, nextRow ) < 0 ) {
              nextRow = peekRow;
              readerIndex = i;
            }
          }
        }
      }
      if( readerIndex == -1 ) {
        hasMore = false;
      }
      else {
        writer.write( readers.get( readerIndex ).next() );
      }
    }
    
    writer.close();
    
  }


  private void info( String message ) {
    if( !quiet ) {
      System.out.println( message );
    }
  }


  /**
   * Sort a file in memory
   * 
   * @param file Input file
   * @param sortedFile Sorted output file
   * @param sortIndex Sort index specification
   * @throws FileNotFoundException If the input file does not exist 
   * @throws IOException If an error occurred writing or reading the file
   */
  public void sort( File file, File sortedFile, SortIndex sortIndex ) throws FileNotFoundException, IOException {
   
    SortedSet<Row> sorted = new TreeSet<Row>( new RowSorter( sortIndex ) );
    TSReader in = new TSReader( file );
    int counter = 1;
    for( Row row = in.next(); row != null; row = in.next() ) {
      sorted.add( row );
      if( counter % 1000 == 0 ) {
        info( "Sorted " + counter + " rows" );
      }
      counter++;
    }
    in.close();
    TSWriter out = new TSWriter( sortedFile );
    for( Row row : sorted ) {
      out.write( row );
    }
    out.close();
    
  }


  /**
   * Sort a file on disk by using sorted split and join
   * 
   * @param file Input file
   * @param sortedFile Sorted output file
   * @param sortIndex Sort index specification
   * @param rowCount Number of rows in split files
   * @param maxOpen Max number of files to read at one time while joining
   * @throws FileNotFoundException If the input file does not exist 
   * @throws IOException If an error occurred writing or reading the file
   * @throws InterruptedException if a threading error occurred
   */
  public void bigsort( File file, File sortedFile, SortIndex sortIndex,
    int rowCount, int maxOpen ) throws FileNotFoundException, IOException, InterruptedException {
    
    List<File> tmpFiles = new ArrayList<File>();
    File splitDir = null;
    try {
      splitDir = FILE.createTempDir( "bigsort-tmp-" );
      long t0 = System.currentTimeMillis();
      tmpFiles.addAll( split( file, rowCount, splitDir, sortIndex ) );
      long t1 = System.currentTimeMillis();
      info( "Finished splitting files in " + ( (double) ( t1 - t0 ) / 1000 ) + " s."  );
      tmpFiles.addAll( join( splitDir, sortedFile, sortIndex, maxOpen ) );
    }
    finally {
      for( File tmpFile : tmpFiles ) {
        try {
          if( tmpFile.exists() ) {
            tmpFile.delete();
          }
        }
        catch( Exception ex ) {}
      }
      if( splitDir != null ) {
        FILE.rmdir( splitDir );
      }
    }
    
  }
  
  
  /**
   * Count rows (and other stats in a *.ts file)
   * 
   * @param file File
   * @return Count result
   * @throws FileNotFoundException If the input file does not exist 
   * @throws IOException If an error occurred reading the file
   * 
   */
  public CountResult count( File file ) throws FileNotFoundException, IOException {
    
    CountResult result = new CountResult();
    
    int rows = 0;
    int minCols = Integer.MAX_VALUE;
    int maxCols = 0;
    
    TSReader in = new TSReader( file );
    for( Row row = in.next(); row != null; row = in.next() ) {
      rows++;
      int cols = row.size();
      minCols = Math.min( minCols, cols );
      maxCols = Math.max( maxCols, cols );
    }
    
    in.close();
    
    result.setRows( rows );
    result.setMinCols( minCols );
    result.setMaxCols( maxCols );
    
    return result;
    
  }


  /**
   * Test if file is sorted according to the given sort index
   * 
   * @param file File
   * @param sortIndex Sorting definition
   * @return -1: file ist sorted, otherwise number of first row, which is not sorted
   * @throws FileNotFoundException If the input file does not exist 
   * @throws IOException If an error occurred reading the file
   * 
   */
  public int sorted( File file, SortIndex sortIndex ) throws FileNotFoundException, IOException {
    
    RowSorter rowSorter = new RowSorter( sortIndex );
    
    int line = 1;
    TSReader in = new TSReader( file );
    Row lastRow = null;
    for( Row row = in.next(); row != null; row = in.next() ) {
      if( lastRow != null ) {
        if( rowSorter.compare( lastRow, row ) > 0 ) {
          return line;
        }
      }
      lastRow = row;
      line++;
    }
    
    return -1;
    
  }
  
  
  /**
   * @return the quiet
   */
  public boolean isQuiet() {
    return quiet;
  }

  
  /**
   * @param quiet the quiet to set
   */
  public void setQuiet( boolean quiet ) {
    this.quiet = quiet;
  }


  /**
   * Write part of larger file to new file
   * 
   * @param file Source file
   * @param outputFile Output file
   * @param first First row (1-based)
   * @param last Last row (1-base). 0 means last of file.
   * @return Number of rows written to output file
   * @throws IOException If an error occurs reading the source file or writing the target files 
   * @throws FileNotFoundException The source file does not exists
   * 
   */
  public int slice( File file, File outputFile, int first, int last ) throws FileNotFoundException, IOException {
    
    TSReader in = new TSReader( file );
    TSWriter out = new TSWriter( outputFile );
    int result = 0;
    try {
      int rowNum = 1;
      for( Row row = in.next(); row != null && ( last == 0 || rowNum <= last ); row = in
        .next() ) {
        if( rowNum >= first && ( last == 0 || rowNum <= last ) ) {
          out.write( row );
          result++;
        }
        rowNum++;
      }
    }
    finally {
      if( in != null ) {
        in.close();
      }
      if( out != null ) {
        out.close();
      }
    }
    
    return result;
    
  }
  
  
  /**
   * Getter
   * 
   * @return Number of concurrent threads
   * 
   */
  public int getNumThreads() {
    return numThreads;
  }

  
  /**
   * Setter
   * 
   * @param numThreads Number of concurrent threads
   * 
   */
  public void setNumThreads( int numThreads ) {
    this.numThreads = numThreads;
    if( this.numThreads < 1 ) {
      this.numThreads = 1;
    }
  }


  /**
   * Read a file into memory
   * 
   * @param tsFile File to read
   * @throws IOException if reading failed
   * @throws FileNotFoundException if file does not exist
   */
  public List<Row> read( File tsFile ) throws FileNotFoundException, IOException {
    TSReader reader = new TSReader( tsFile );
    List<Row> rows = new ArrayList<Row>();
    for( Row row = reader.next(); row != null; row = reader.next() ) {
      rows.add( row );
    }
    return rows;
  }
  
  
}


