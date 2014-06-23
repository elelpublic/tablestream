// (C) 1998-2015 Information Desire Software GmbH
// www.infodesire.com

package com.infodesire.tablestream.tsfile;

import com.infodesire.tablestream.Row;


/**
 * Wrapper around TSReader which allows for a peek at the next row.
 *
 */
public class PeekReader {
  
  
  private TSReader reader;
  private Row peekRow;

  public PeekReader( TSReader reader ) {
    this.reader = reader;
    peekRow = reader.next();
  }
  
  
//  /**
//   * Test
//   * 
//   * @return Another row is available
//   * 
//   */
//  public boolean hasNext() {
//    return peekRow != null;
//  }
  
  
  /**
   * Peek at next row, but do not yet read.
   * 
   * @return Next row
   * 
   */
  public Row peek() {
    return peekRow;
  }
  
  
  /**
   * Return the next row and remove it from stream
   *  
   * @return Next row
   * 
   */
  public Row next() {
    Row next = peekRow;
    peekRow = reader.next();
    return next;
  }
  
  
  /**
   * Close the reader
   * 
   */
  public void close() {
    reader.close();
  }
  
  
  public String toString() {
    return "PeekReader " + reader;
  }


}


