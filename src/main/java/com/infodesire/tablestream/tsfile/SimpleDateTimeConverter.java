// (C) 1998-2014 Information Desire Software GmbH
// www.infodesire.com

package com.infodesire.tablestream.tsfile;

import com.infodesire.commons.datetime.SimpleDateTime;
import com.thoughtworks.xstream.converters.ConversionException;
import com.thoughtworks.xstream.converters.Converter;
import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.converters.UnmarshallingContext;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;

import java.text.ParseException;


/**
 * Converter to and from string for class: SimpleDate
 * 
 *
 */
public class SimpleDateTimeConverter implements Converter {


  @SuppressWarnings("rawtypes")
  @Override
  public boolean canConvert( Class type ) {
    return type.equals( SimpleDateTime.class );
  }


  @Override
  public void marshal( Object source, HierarchicalStreamWriter writer,
    MarshallingContext context ) {
    
    SimpleDateTime date = (SimpleDateTime) source;
    writer.setValue( date.toString() );

  }


  @Override
  public Object unmarshal( HierarchicalStreamReader reader,
    UnmarshallingContext context ) {
    
    try {
      SimpleDateTime date;
      date = SimpleDateTime.fromString( reader.getValue() );
      return date;
    }
    catch( ParseException ex ) {
      throw new ConversionException( ex );
    }
    
  }


}

