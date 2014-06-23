// (C) 1998-2015 Information Desire Software GmbH
// www.infodesire.com

package com.infodesire.tablestream.util;

import com.infodesire.tablestream.Row;
import com.infodesire.tablestream.RowSorter;
import com.infodesire.tablestream.SortIndex;
import com.infodesire.tablestream.TableWriter;

import java.io.IOException;
import java.util.TreeSet;


/**
 * Buffer rows in memory and sort them before writing on close.
 *
 */
public class BufferedWriter implements TableWriter {
  
  
  private TableWriter target;
  private TreeSet<Row> buffer;

  public BufferedWriter( TableWriter target, SortIndex sortIndex ) {
    this.target = target;
    buffer = new TreeSet<Row>( new RowSorter( sortIndex ) );
  }

  @Override
  public void write( Row row ) throws IOException {
    buffer.add( row );
  }

  @Override
  public void close() throws IOException {
    for( Row row : buffer ) {
      target.write( row );
    }
    target.close();
  }
  

}


