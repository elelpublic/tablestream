// (C) 1998-2015 Information Desire Software GmbH
// www.infodesire.com

package com.infodesire.tablestream;

import java.io.IOException;


/**
 * Writes table based data to file or other target
 *
 */
public interface TableWriter {


  /**
   * Write a row to table
   * 
   * @param row Row to write
   * @throws IOException Error in underlying storage system
   * 
   */
  void write( Row row ) throws IOException;
  
  
  /**
   * End writing
   * 
   * @throws IOException Error in underlying storage system
   * 
   */
  void close() throws IOException;


}


