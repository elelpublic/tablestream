// (C) 1998-2015 Information Desire Software GmbH
// www.infodesire.com

package com.infodesire.tablestream.util;

import com.infodesire.commons.NUMBER;
import com.infodesire.commons.STRING;
import com.infodesire.tablestream.FileWriter;
import com.infodesire.tablestream.Row;
import com.infodesire.tablestream.SortIndex;
import com.infodesire.tablestream.TableWriter;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


/**
 * A writer which splits the data into multiple files
 *
 */
public class SplitWriter implements TableWriter {
  
  
  private int rowsPerFile = 0;
  private FileWriter fileWriter;
  private File dir;
  private String namePattern;
  private int rowCounter;
  private File currentFile;
  private int fileIndex = 0;
  private List<File> files = new ArrayList<File>();
  private SortIndex sortIndex;
  private BufferedWriter bufferedWriter;
  

  /**
   * Create writer
   * 
   * @param rowsPerFile After how many rows to create a new file. 0 means no splitting.
   * @param fileWriter Underlying file writer
   * @param dir Target dir for creating output files
   * @param namePattern Pattern for names of the created output files. Use ${num} for the file number.
   * @param sortIndex Optional sort index or null. If not null: files will be sorted before written.
   * 
   */
  public SplitWriter( int rowsPerFile, FileWriter fileWriter, File dir, String namePattern, SortIndex sortIndex ) {
    this.rowsPerFile = rowsPerFile;
    this.fileWriter = fileWriter;
    this.dir = dir;
    this.namePattern = namePattern;
    this.sortIndex = sortIndex;
  }
  

  @Override
  public void write( Row row ) throws IOException {
    checkOpen();
    getWriter().write( row );
    rowCounter++;
    if( rowsPerFile > 0 && rowCounter >= rowsPerFile ) {
      getWriter().close();
      currentFile = null;
      rowCounter = 0;
    }
  }
  

  private void checkOpen() throws IOException {
    if( currentFile == null ) {
      String fileIndexFormatted = NUMBER.digits( 5, fileIndex++ );
      String name = STRING.replace( namePattern, "${num}", fileIndexFormatted );
      currentFile = new File( dir, name );
      files.add( currentFile );
      fileWriter.open( currentFile );
      if( sortIndex != null ) {
        bufferedWriter = new BufferedWriter( fileWriter, sortIndex );
      }
    }
  }


  @Override
  public void close() throws IOException {
    if( currentFile != null ) {
      getWriter().close();
    }
  }


  private TableWriter getWriter() {
    if( bufferedWriter != null ) {
     return bufferedWriter;
    }
    else {
      return fileWriter;
    }
  }


  /**
   * Getter
   * 
   * @return List of created files
   * 
   */
  public List<File> getFiles() {
    return files;
  }
  
  
  public String toString() {
    return "SplitWriter current file: " + currentFile;
  }
  

}


