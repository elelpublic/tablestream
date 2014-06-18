// (C) 1998-2015 Information Desire Software GmbH
// www.infodesire.com

package com.infodesire.commons;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;


/**
 * Ease your file handling tasks.
 *
 */
public class FILE {


  /**
   * Print the content of a file to stdout
   * 
   * @param file File to print
   * @throws IOException 
   * @throws FileNotFoundException 
   * 
   */
  public static void print( File file ) throws FileNotFoundException, IOException {
    print( file, System.out );
  }


  /**
   * Send the the content of a file to an output stream
   * 
   * @param file File to print
   * @param out Output stream target
   * @throws IOException 
   * @throws FileNotFoundException 
   * 
   */
  public static void print( File file, OutputStream out ) throws FileNotFoundException, IOException {
    pipe( new FileInputStream( file ), out );
  }


  /**
   * Pipe data from one data stream to another
   *
   * @param in Data source
   * @param out Data sink
   *
   */
  public static void pipe( InputStream in, OutputStream out )
    throws IOException {

    int bufferSize = 4096;

    byte[] buffer = new byte[ bufferSize ];
    int read = in.read( buffer );
    while( read != -1 ) {
      out.write( buffer, 0, read );
      read = in.read( buffer );
    }

  }


  /**
   * Copy file
   *
   * @param source Source file
   * @param target Target file or directory
   *
   */
  public static void copy( File source, File target )
    throws IOException {
    
    if( target.isDirectory() ) {
      target = new File( target, source.getName() );
    }

    FileInputStream in = new FileInputStream( source );
    FileOutputStream out = new FileOutputStream( target );
    pipe( in, out );
    in.close();
    out.close();
    
  }
  
  
  /**
   * Move a file from source to target
   * 
   * @param source File source file
   * @param target Target file or directory
   * 
   */
  public static void move( File source, File target ) {
    
    if( target.isDirectory() ) {
      target = new File( target, source.getName() );
    }
    
    if( target.exists() ) {
      target.delete();
    }
    
    source.renameTo( target );
    
  }


  /**
   * Create a temporary directory
   * 
   * @param prefix Prefix for directory name
   * @return A new, empty temporary directory
   * @throws IOException if creating a directory failed
   * 
   */
  public static File createTempDir( String prefix ) throws IOException {
    File tmpFile = File.createTempFile( prefix, "" );
    File tmpDir = new File( tmpFile.getParentFile(), tmpFile.getName() );
    tmpFile.delete();
    tmpDir.mkdirs();
    return tmpDir;
  }


  /**
   * Delete directory and all files and subdirectories
   * 
   * @param dir Directory to delete
   * 
   */
  public static void rmdir( File dir ) {
    for( File file : dir.listFiles() ) {
      if( file.isDirectory() ) {
        rmdir( file );
      }
      else {
        file.delete();
      }
    }
    dir.delete();
  }


}


