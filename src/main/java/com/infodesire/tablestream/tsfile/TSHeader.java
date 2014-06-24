// (C) 1998-2014 Information Desire Software GmbH
// www.infodesire.com

package com.infodesire.tablestream.tsfile;

import com.thoughtworks.xstream.annotations.XStreamAlias;

import java.io.Serializable;


/**
 * TS file format version number
 *
 */
@XStreamAlias( "Header" )
public class TSHeader implements Serializable {
  

  private static final long serialVersionUID = 1L;
  
  
  @XStreamAlias( "Version" )
  public int version = 1;
  
  
}

