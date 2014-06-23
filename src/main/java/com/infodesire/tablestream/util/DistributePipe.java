// (C) 1998-2015 Information Desire Software GmbH
// www.infodesire.com

package com.infodesire.tablestream.util;

import com.infodesire.tablestream.Row;
import com.infodesire.tablestream.TableReader;
import com.infodesire.tablestream.TableWriter;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;


/**
 * A pipe distributing input to multiple targets using simple round robin strategy.
 *
 */
public class DistributePipe extends Thread {


  private List<TableWriter> writers;
  private int index = 0;
  private TableReader reader;


  public DistributePipe( TableWriter... writers ) {
    this( Arrays.asList( writers ) );
  }


  public DistributePipe( List<TableWriter> writers ) {
    this.writers = writers;
  }
  
  /**
   * Start thread reading from reader. Will end thread when no more data is available.
   * 
   * @param reader Data source
   * 
   */
  public void start( TableReader reader ) {
    this.reader = reader;
    start();
  }
  
  public void run() {
    for( Row row = reader.next(); row != null; row = reader.next() ) {
      try {
        writers.get( index++ ).write( row );
        if( index == writers.size() ) {
          index = 0;
        }
      }
      catch( IOException ex ) {
        throw new RuntimeException( ex );
      }
    }
    for( TableWriter writer : writers ) {
      try {
        writer.close();
      }
      catch( IOException ex ) {
        throw new RuntimeException( ex );
      }
    }
  }


}


