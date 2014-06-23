// (C) 1998-2015 Information Desire Software GmbH
// www.infodesire.com

package com.infodesire.tablestream.util;

import com.infodesire.tablestream.Cell;
import com.infodesire.tablestream.Row;
import com.infodesire.tablestream.TableReader;
import com.infodesire.tablestream.TableWriter;

import java.io.IOException;
import java.util.concurrent.LinkedBlockingQueue;


/**
 * A buffer acts as a writer to a producer of rows and as a reader to a consumer of
 * rows. It decouples different speeds of producers and consumers.
 * <p>
 * 
 * To limit the size of the buffer, specify a max buffer size.
 * <p>
 * 
 * A buffer will block on write(...) if the buffer is full.
 * <p>
 * 
 * A buffer will block on next() if the buffer is empty and no end of data was signalled.
 * <p>
 * 
 * Internally the Buffer is just a thin wrapper around a BlockingQueue with added
 * EOF handling.
 * 
 *
 */
public class Buffer implements TableReader, TableWriter {
  
  
  private static Row EOF = new Row();
  static {
    EOF.add( new Cell( "EOF" ) );
  }
  
  
  private LinkedBlockingQueue<Row> queue;
  private Row next;


  /**
   * Create a buffered reader which will wait for data to come in via
   * the TableWriter interface.
   * <p>
   * 
   * To use the BufferedReader this way, you have to call start()
   * in a separate thread which will cause the data to be read.
   * 
   * @param maxSize Max number of rows in buffer before blocking and waiting for space to become available.
   * 
   */
  public Buffer( int maxSize ) {
    queue = new LinkedBlockingQueue<Row>( maxSize );
  }
  
  
  @Override
  public Row next() {
    fetch();
    Row nextRow = next;
    if( nextRow == EOF ) {
      return null;
    }
    else {
      next = null;
      return nextRow;
    }
  }


  private void fetch() {
    if( next == null ) {
      try {
        next = queue.take();
        if( next == EOF ) {
          queue.put( EOF ); // reinsert for other threads to get stopped
        }
      }
      catch( InterruptedException ex ) {
        throw new RuntimeException( ex );
      }
    }
  }


  @Override
  public void close() {
    try {
      queue.put( EOF );
    }
    catch( InterruptedException ex ) {
      throw new RuntimeException( ex );
    }
  }


  @Override
  public void write( Row row ) throws IOException {
    try {
      queue.put( row );
    }
    catch( InterruptedException ex ) {
      throw new RuntimeException( ex );
    }
  }
  
  
}


