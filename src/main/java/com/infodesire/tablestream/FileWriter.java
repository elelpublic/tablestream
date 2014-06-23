// (C) 1998-2015 Information Desire Software GmbH
// www.infodesire.com

package com.infodesire.tablestream;

import java.io.File;
import java.io.IOException;


/**
 * File based writer
 * 
 *
 */
public interface FileWriter extends TableWriter {
  
  
  /**
   * Open file for writing
   * 
   * @param file File to write to
   * @throws IOException if opening or writing to the file failed
   * 
   */
  void open( File file ) throws IOException;
  

}


