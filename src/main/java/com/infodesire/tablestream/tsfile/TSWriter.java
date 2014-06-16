// (C) 1998-2014 Information Desire Software GmbH
// www.infodesire.com

package com.infodesire.tablestream.tsfile;

import com.infodesire.tablestream.Row;
import com.thoughtworks.xstream.XStream;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;


/**
 * Write large brd files as a stream of data
 *
 */
public class TSWriter extends TSInOut {

  
  private Writer writer;


  private ObjectOutputStream oout;


  /**
   * Create writer
   * 
   * @param out Underlying file
   * @param rowCount Optional number of rows to be written
   * @throws IOException if underlying file has a problem
   * 
   */
  public TSWriter( File file, int rowCount ) throws IOException {
    this( new FileOutputStream( file ), rowCount );
  }
  
  
  /**
   * Create writer
   * 
   * @param out Underlying data stream
   * @param rowCount Optional number of rows to be written
   * @throws IOException if underlying stream has a problem
   * 
   */
  public TSWriter( OutputStream out, int rowCount ) throws IOException {
    
    writer = new OutputStreamWriter( out, charset );
    writer.write( "<?xml version=\"1.0\" encoding=\"" + charset.displayName()
      + "\"?>" );
    writer.write( "\n\n" );
    writer.flush();
    
    XStream xStream = createXStream();
    oout = xStream.createObjectOutputStream( writer, "tablestream" );
  
    TSHeader brdHeader = new TSHeader();
    brdHeader.rowCount = rowCount;
    oout.writeObject( brdHeader ); // write file format version number
    
  }


  public void write( Row row ) throws IOException {
    oout.writeObject( row );
  }
  
  
  public void close() throws IOException {
    oout.close();
  }


}


