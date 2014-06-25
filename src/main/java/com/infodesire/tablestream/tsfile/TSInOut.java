// (C) 1998-2014 Information Desire Software GmbH
// www.infodesire.com

package com.infodesire.tablestream.tsfile;

import com.infodesire.tablestream.Cell;
import com.infodesire.tablestream.Property;
import com.infodesire.tablestream.Row;
import com.infodesire.tablestream.SimpleList;
import com.thoughtworks.xstream.XStream;

import java.nio.charset.Charset;


/**
 * Base class for TS file readers, writers.
 *
 */
public class TSInOut {


  protected static Charset charset = Charset.forName( "UTF-8" );
  
  
  protected XStream createXStream() {
    XStream xStream = new XStream();
    xStream.processAnnotations( TSHeader.class);
    xStream.processAnnotations( Row.class);
    xStream.processAnnotations( Cell.class);
    xStream.processAnnotations( Property.class);
    xStream.processAnnotations( SimpleList.class);
    xStream.registerConverter( new SimpleDateConverter() );
    xStream.registerConverter( new SimpleTimeConverter() );
    xStream.registerConverter( new SimpleDateTimeConverter() );
    xStream.alias( "s", String.class );
    return xStream;
  }


}


