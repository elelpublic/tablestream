// (C) 1998-2015 Information Desire Software GmbH
// www.infodesire.com

package com.infodesire.tablestream;


/**
 * Reads table data from file or other source
 *
 */
public interface TableReader {
  
  
//  /**
//   * Test
//   * 
//   * @return Another row is available
//   * 
//   */
//  boolean hasNext();
  
  
  /**
   * Fetch next row
   * 
   * @return Next row or null if no more data is available
   * 
   */
  Row next();
  
  
  /**
   * Finish reading
   * 
   */
  void close();
  

}

