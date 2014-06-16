// (C) 1998-2014 Information Desire Software GmbH
// www.infodesire.com

package com.infodesire.tablestream.tsfile;

import com.thoughtworks.xstream.annotations.XStreamAlias;


/**
 * TS file format version number
 *
 */
@XStreamAlias( "Header" )
public class TSHeader {
  

  @XStreamAlias( "Version" )
  public int version = 1;
  
  
  @XStreamAlias( "RowCount" )
  public int rowCount = 0;
  
  
}

