// (C) 1998-2014 Information Desire Software GmbH
// www.infodesire.com

package com.infodesire.tablestream;

import com.infodesire.commons.FILE;
import com.infodesire.commons.STRING;
import com.infodesire.commons.string.PowerString;
import com.infodesire.tablestream.sample.SampleData;
import com.infodesire.tablestream.tsfile.PeekReader;
import com.infodesire.tablestream.tsfile.TSReader;
import com.infodesire.tablestream.tsfile.TSWriter;

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

  
  public static boolean quiet = false;


  /**
   * Create a *.ts file with random data.
   * 
   * @param targetFile File to write to
   * @param rowCount Number of rows to write
   * @param columnCount Number of columns to write
   * @throws IOException if an error occurs while writing the data to file
   * 
   */
  public static void random( File targetFile, int rowCount, int columnCount ) throws IOException {
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
  public static void generic( File targetFile, int rowCount, int columnCount ) throws IOException {
    SampleData.generic( targetFile, rowCount, columnCount );
  }


  /**
   * Split a *.ts file into smaller files. Store them in dir.
   * 
   * @param sourceFile Source file
   * @param rowCount Max number of rows in target files.
   * @param targetDir Directory for target files
   * @param sortIndex Optiona: if specified the target files will be sorted in memory before writing to disk
   * @return Split files
   * @throws IOException If an error occurs reading the source file or writing the target files 
   * @throws FileNotFoundException The source file does not exists
   * 
   */
  public static List<File> split( File sourceFile, int rowCount, File targetDir, SortIndex sortIndex ) throws FileNotFoundException, IOException {
    
    PowerString s = new PowerString( sourceFile.getName() );
    String name = s.removeBeforeFirst( "." );
    String extenstion = s.toString();
    
    int digits = 5;
    int counter = 1;

    TSReader reader = new TSReader( sourceFile );
    
    int remaining = reader.getRowCount();
    
    if( remaining > 0 ) {
      digits = (int) Math.log10( remaining ) + 1;
    }
    
    if( !targetDir.exists() ) {
      targetDir.mkdirs();
    }
    
    File target = null;
    TSWriter writer = null;
    int targetSize = 0;
    SortedSet<Row> buffer = null;
    
    List<File> splitFiles = new ArrayList<File>();
    
    while( reader.hasNext() ) {
      
      if( targetSize == rowCount ) {
        if( buffer != null ) {
          for( Row row : buffer ) {
            writer.write( row );
          }
        }
        writer.close();
        writer = null;
        target = null;
      }
      
      if( target == null ) {
        target = new File( targetDir, name + "." + STRING.digits( digits, counter++ ) + "." + extenstion );
        splitFiles.add( target );
        writer = new TSWriter( target, Math.min( rowCount, remaining ) );
        if( sortIndex != null ) {
          buffer = new TreeSet<Row>( new RowSorter( sortIndex ) );
        }
        targetSize = 0;
        info( "Writing " + target.getAbsolutePath() );
      }
      
      Row row = reader.next();

      if( buffer != null ) {
        buffer.add( row );
      }
      else {
        writer.write( row );
      }

      targetSize++;
      remaining--;
      
    }
    
    writer.close();
    
    return splitFiles;
    
  }
  
  
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
  public static List<File> join( File sourceDir, File targetFile, SortIndex sortIndex, int maxOpen ) throws FileNotFoundException, IOException {
    
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
  public static void join( List<File> sourceFiles, File targetFile, SortIndex sortIndex ) throws FileNotFoundException, IOException {
    
    info( "Join " + sourceFiles.size() + " files into " + targetFile );

    RowSorter rowSorter = sortIndex == null ? null : new RowSorter( sortIndex );
    
    List<PeekReader> readers = new ArrayList<PeekReader>();
    for( File sourceFile : sourceFiles ) {
      readers.add( new PeekReader( new TSReader( sourceFile ) ) );
    }
    
    TSWriter writer = new TSWriter( targetFile, 0 );

    boolean hasMore = true;
    while( hasMore ) {
      Row nextRow = null;
      int readerIndex = -1;
      for( int i = 0; i < readers.size(); i++ ) {
        PeekReader reader = readers.get( i );
        if( reader.hasNext() ) {
          Row peekRow = reader.peek();
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


  private static void info( String message ) {
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
  public static void sort( File file, File sortedFile, SortIndex sortIndex ) throws FileNotFoundException, IOException {
   
    SortedSet<Row> sorted = new TreeSet<Row>( new RowSorter( sortIndex ) );
    TSReader in = new TSReader( file );
    while( in.hasNext() ) {
      Row row = in.next();
      sorted.add( row );
    }
    in.close();
    TSWriter out = new TSWriter( sortedFile, sorted.size() );
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
   */
  public static void bigsort( File file, File sortedFile, SortIndex sortIndex,
    int rowCount, int maxOpen ) throws FileNotFoundException, IOException {
    
    List<File> tmpFiles = new ArrayList<File>();
    File splitDir = null;
    try {
      splitDir = FILE.createTempDir( "bigsort-tmp-" );
      tmpFiles.addAll( split( file, rowCount, splitDir, sortIndex ) );
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
  public static CountResult count( File file ) throws FileNotFoundException, IOException {
    
    CountResult result = new CountResult();
    
    int rows = 0;
    int minCols = Integer.MAX_VALUE;
    int maxCols = 0;
    
    TSReader in = new TSReader( file );
    while( in.hasNext() ) {
      Row row = in.next();
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
  public static int sorted( File file, SortIndex sortIndex ) throws FileNotFoundException, IOException {
    
    RowSorter rowSorter = new RowSorter( sortIndex );
    
    int line = 1;
    TSReader in = new TSReader( file );
    Row lastRow = null;
    while( in.hasNext() ) {
      Row row = in.next();
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
  
  
}


