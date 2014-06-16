// (C) 1998-2014 Information Desire Software GmbH
// www.infodesire.com
// 

package com.infodesire.tablestream.tsfile;

import com.infodesire.tablestream.Row;
import com.thoughtworks.xstream.XStream;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.Reader;
import java.util.Iterator;


/**
 * Reader for table stream files *.ts
 *
 */
public class TSReader extends TSInOut implements Iterator<Row> {
  
  
  private ObjectInputStream oin;
  
  
  private TSHeader header;


  private boolean eof = false;


  private Row next;


  public TSReader( InputStream inputStream ) throws IOException {
    
    XStream xstream = createXStream();
    
    Reader in = new InputStreamReader( inputStream, charset );
    oin = xstream.createObjectInputStream( in );
    
    try {
      header = (TSHeader) oin.readObject();
    }
    catch( ClassNotFoundException ex ) {
      throw new RuntimeException( ex );
    }
    
  }
  
  
  public TSReader( File file ) throws FileNotFoundException, IOException {
    this( new FileInputStream( file ) );
  }


  public int getVersion() {
    return header.version;
  }

  
  @Override
  public boolean hasNext() {
    if( next == null ) {
      try {
        next = (Row) oin.readObject();
      }
      catch( Exception ex ) {
        eof = true;
      }
    }
    return !eof;
  }
  

  @Override
  public Row next() {
    Row nextRow = next;
    next = null;
    return nextRow;
  }
  

  @Override
  public void remove() {
    throw new UnsupportedOperationException();
  }


  public int getRowCount() {
    return header.rowCount;
  }


  public void close() {
    try {
      oin.close();
    }
    catch( IOException ex ) {
    }
  }


  public TSHeader getHeader() {
    return header;
  }
  
  
  /**
   * Read the header only of a brd file
   * 
   * @throws IOException 
   * @throws FileNotFoundException 
   * 
   */
  public static TSHeader readHeader( File brdFile ) throws FileNotFoundException, IOException {
    TSReader tsReader = new TSReader( brdFile );
    TSHeader header = tsReader.getHeader();
    tsReader.close();
    return header;
  }

}
