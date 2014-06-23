// (C) 1998-2015 Information Desire Software GmbH
// www.infodesire.com

package com.infodesire.tablestream.util;

import com.infodesire.tablestream.Row;
import com.infodesire.tablestream.TableReader;
import com.infodesire.tablestream.TableWriter;

import java.io.IOException;


/**
 * Thread which reads from a reader and writes to a writer.
 * <p>
 * 
 * Usage: either call start( reader ) or call setReader( reader ) first and call start() later.
 * 
 *
 */
public class SimplePipe extends Thread {

  private TableWriter writer;
  private TableReader reader;
  private int count;

  public SimplePipe( TableWriter writer ) {
    this.writer = writer;
  }
  
  
  public TableWriter getWriter() {
    return writer;
  }
  
  
  /**
   * Getter
   * 
   * @return Numbers of rows processed sofar
   * 
   */
  public int getCount() {
    return count;
  }
  
  
  /**
   * Start thread reading from reader. Will end thread when no more data is available.
   * 
   * @param reader Data source
   * 
   */
  public void start( TableReader reader ) {
    setReader( reader );
    start();
  }
  
  public void setReader( TableReader reader ) {
    this.reader = reader;
  }
  
  public void run() {
    for( Row row = reader.next(); row != null; row = reader.next() ) {
      try {
        writer.write( row );
        count++;
      }
      catch( IOException ex ) {
        throw new RuntimeException( ex );
      }
    }
  }


}


