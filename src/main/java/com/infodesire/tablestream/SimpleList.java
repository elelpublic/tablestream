// (C) 1998-2015 Information Desire Software GmbH
// www.infodesire.com

package com.infodesire.tablestream;

import com.thoughtworks.xstream.annotations.XStreamImplicit;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;


/**
 * An immutable list of strings.
 *
 */
public class SimpleList implements Comparable<SimpleList>, Iterable<String>, Serializable {
  
  
  private static final long serialVersionUID = 1L;
  
  
  @XStreamImplicit
  private ArrayList<String> list = new ArrayList<String>();
  
  
  public SimpleList( String... list ) {
    if( list != null ) {
      for( String entry : list ) {
        this.list.add( entry );
      }
    }
  }
  
  
  public SimpleList( Iterable<String> list ) {
    if( list != null ) {
      for( String entry : list ) {
        this.list.add( entry );
      }
    }
  }
  
  
  public int size() {
    return list.size();
  }
  
  
  /**
   * Getter
   * 
   * @return A list is null, when it is empty
   * 
   */
  public boolean isNull() {
    return list.isEmpty();
  }
  

  @Override
  public int compareTo( SimpleList o ) {
    if( o == null || o.isNull() ) {
      if( isNull() ) {
        return 0;
      }
      else {
        return 1;
      }
    }
    if( isNull() ) {
      return -1;
    }
    int result = size() - o.size();
    if( result == 0 ) {
      for( int i = 0; i < size(); i++ ) {
        result = stringCompare( get( i ), o.get( i ) );
      }
    }
    return result;
  }


  private int stringCompare( String string1, String string2 ) {
    if( string1 == null ) {
      if( string2 == null ) {
        return 0;
      }
      else {
        return -1;
      }
    }
    if( string2 == null ) {
      return 1;
    }
    return string1.compareTo( string2 );
  }


  public String get( int index ) {
    return list.get( index );
  }


  @Override
  public Iterator<String> iterator() {
    return list.iterator();
  }
  
  
  public int hashCode() {
    return list.hashCode();
  }
  
  
  public boolean equals( Object o ) {
    if( o == null ) {
      return isNull();
    }
    if( o instanceof SimpleList ) {
      return compareTo( (SimpleList) o ) == 0;
    }
    return false;
  }
  
  
  public String toString() {
    return list.toString();
  }
  
  
  /**
   * Get all entries of list
   * 
   * @param target Where to add the entries
   * 
   */
  public void getAll( Collection<String> target ) {
    for( String entry : list ) {
      target.add( entry );
    }
  }
  

  /**
   * Get all entries of list
   * 
   * @param target Where to add the entries
   * 
   */
  public ArrayList<String> getAll() {
    ArrayList<String> result = new ArrayList<String>();
    getAll( result );
    return result;
  }
  
  
}


