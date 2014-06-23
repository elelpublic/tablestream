// (C) 1998-2015 Information Desire Software GmbH
// www.infodesire.com

package com.infodesire.tablestream.sample;

import com.infodesire.commons.DATE;
import com.infodesire.commons.STRING;
import com.infodesire.commons.datetime.SimpleDate;
import com.infodesire.commons.datetime.SimpleDateTime;
import com.infodesire.commons.datetime.SimpleTime;
import com.infodesire.tablestream.Cell;
import com.infodesire.tablestream.Row;
import com.infodesire.tablestream.SimpleList;
import com.infodesire.tablestream.ValueType;
import com.infodesire.tablestream.tsfile.TSWriter;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;


/**
 * Sample data for tests.
 *
 */
public class SampleData {


  private static SimpleDate REFERENCE_DATE = new SimpleDate( 2000, 1, 1 );


  /**
   * Create a *.ts file with random data.
   * 
   * @param file File to write to
   * @param rowCount Number of rows to write
   * @param columnCount Number of columns to write
   * @param random True: random, false: generic. Unlike random data, generic data is the same for every run.
   * @throws IOException if an error occurs while writing the data to file
   * 
   */
  public static void random( File file, int rowCount, int columnCount )
    throws IOException {
    sample( file, rowCount, columnCount, true );
  }
  
  
  /**
   * Create a *.ts file with generic data. Unlike random data, generic data is the same for every run.
   * 
   * @param file File to write to
   * @param rowCount Number of rows to write
   * @param columnCount Number of columns to write
   * @throws IOException if an error occurs while writing the data to file
   * 
   */
  public static void generic( File file, int rowCount, int columnCount ) throws IOException {
    sample( file, rowCount, columnCount, false );
  }


  /**
   * Create a *.ts file with sample data.
   * 
   * @param file File to write to
   * @param rowCount Number of rows to write
   * @param columnCount Number of columns to write
   * @param random True: random, false: generic. Unlike random data, generic data is the same for every run.
   * @throws IOException
   * 
   */
  public static void sample( File file, int rowCount, int columnCount,
    boolean random ) throws IOException {

    TSWriter writer = new TSWriter( file );
    for( int i = 0; i < rowCount; i++ ) {
      writer.write( createSampleRow( i, columnCount, random ) );
    }
    writer.close();

  }


  /**
   * Creates a sample row of data. Columns will have same type across all rows.
   * 
   * @param row Index in the file (has no special meaning, but can be used for data generation)
   * @param columnCount Number of columns to generate
   * @param random True: random, false: generic. Unlike random data, generic data is the same for every run.
   * @return Random row of data.
   * 
   */
  public static Row createSampleRow( int row, int columnCount, boolean random ) {

    Row rowObject = new Row();
    int numTypes = ValueType.values().length;
    for( int i = 0; i < columnCount; i++ ) {
      ValueType type = ValueType.values()[ i % numTypes ];
      rowObject.add( createSampleCell( row, i, type, random ) );
    }
    return rowObject;

  }


  /**
   * Creates a sample data cell.
   * 
   * @param row Row index in the file (has no special meaning, but can be used for data generation)
   * @param col Column index (has no special meaning, but can be used for data generation)
   * @param type Type of data value of cell
   * @param random True: random, false: generic. Unlike random data, generic data is the same for every run.
   * @return A sample data cell
   * 
   */
  public static Cell createSampleCell( int row, int col, ValueType type,
    boolean random ) {
    
    if( type == ValueType.STRING ) {
      if( random ) {
        return new Cell( STRING.random( 40 ) );
      }
      else {
        return new Cell( "Sample Text in row " + row + " col " + col );
      }
    }
    else if( type == ValueType.INTEGER ) {
      if( random ) {
        return new Cell( (int) ( Math.random() * 10000 ) );
      }
      else {
        return new Cell( row * col );
      }
    }
    else if( type == ValueType.DOUBLE ) {
      if( random ) {
        return new Cell( Math.random() * 10000 );
      }
      else {
        return new Cell( ( row * col ) / 3.141526 );
      }
    }
    else if( type == ValueType.BOOLEAN ) {
      if( random ) {
        return new Cell( Math.random() > 0.5 );
      }
      else {
        return new Cell( ( row * col ) % 2 == 0 );
      }
    }
    else if( type == ValueType.DATE ) {
      if( random ) {
        return new Cell( date( (int) ( Math.random() * 10000 ) ) );
      }
      else {
        return new Cell( date( row * col ) );
      }
    }
    else if( type == ValueType.TIME ) {
      if( random ) {
        return new Cell( new SimpleTime( (int) ( Math.random() * 23 ),
          (int) ( Math.random() * 59 ), (int) ( Math.random() * 59 ), (int) ( Math.random() * 999 ) ) );
      }
      else {
        int num = row * col;
        return new Cell( new SimpleTime( num % 24, num % 60, num % 60, num % 1000 ) );
      }
    }
    else if( type == ValueType.DATETIME ) {
      Cell c1 = createSampleCell( row, col, ValueType.DATE, random );
      Cell c2 = createSampleCell( row, col, ValueType.TIME, random );
      return new Cell( new SimpleDateTime( c1.getDate(), c2.getTime() ) );
    }
    else { // LIST
      int num = ( row * col ) % 20;
      ArrayList<String> list = new ArrayList<String>();
      for( int i = 0; i < num; i++ ) {
        if( random ) {
          list.add( STRING.random( 20 ) );
        }
        else {
          list.add( "List entry " + i );
        }
      }
      return new Cell( new SimpleList( list ) );
    }
    
  }


  /**
   * Create date
   * 
   * @param days Number of days between reference date and result
   * @return New date
   * 
   */
  private static SimpleDate date( int days ) {
    return DATE.add( REFERENCE_DATE, days );
  }


}
