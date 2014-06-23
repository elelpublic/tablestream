// (C) 1998-2014 Information Desire Software GmbH
// www.infodesire.com

package com.infodesire.tablestream;

import com.infodesire.commons.string.PowerString;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.OptionBuilder;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.PosixParser;

@SuppressWarnings("static-access")
public class Main {
  
  private static Options options = new Options();
  private static TS ts = new TS();
  
  static {
    options.addOption( OptionBuilder //
      .withDescription( "Wait for user to press RETURN before executing command." ) //
      .create( "d" ) // 
      );
    options.addOption( OptionBuilder //
      .hasArg() //
      .withDescription( "Number of rows" ) //
      .create( "rows" ) // 
      );
    options.addOption( OptionBuilder //
      .hasArg() //
      .withDescription( "Number of columns" ) //
      .create( "cols" ) // 
      );
    options.addOption( OptionBuilder //
      .hasArg() //
      .withDescription( "Name of directory" ) //
      .create( "dir" ) // 
      );
    options.addOption( OptionBuilder //
      .hasArg() //
      .withDescription( "Sort index. A comma separated list (no spaces please) of column numbers. Use -num for descending." ) //
      .create( "sort" ) // 
      );
    options.addOption( OptionBuilder //
      .hasArg() //
      .withDescription( "Max number of open source files at one time." ) //
      .create( "maxOpen" ) // 
      );
    options.addOption( OptionBuilder //
      .hasArg() //
      .withDescription( "Index (1-based) of first row to use." ) //
      .create( "first" ) // 
      );
    options.addOption( OptionBuilder //
      .hasArg() //
      .withDescription( "Index (1-based) of last first row to use. Use 0 for last row of file." ) //
      .create( "last" ) // 
      );
    options.addOption( OptionBuilder //
      .withDescription( "Operate quietly." ) //
      .create( "q" ) // 
      );
    options.addOption( OptionBuilder //
      .hasArg() //
      .withDescription( "Output file." ) //
      .create( "out" ) // 
      );
    options.addOption( OptionBuilder //
      .hasArg() //
      .withDescription( "Number of concurrent threads." ) //
      .create( "threads" ) // 
      );
  }
  

  public static void main( String[] args ) {
    
    CommandLineParser parser = new PosixParser();
    try {
      
      CommandLine cli = parser.parse( options, args );
      
      ts.setQuiet( cli.hasOption( "q" ) );
      
      if( cli.hasOption( "d" ) ) {
        System.out.println( "Press RETURN to continue." );
        System.in.read();
      }
      
      @SuppressWarnings("unchecked")
      List<String> commands = cli.getArgList();
      if( commands.size() == 0 ) {
        usage( "Command missing" );
        return;
      }

      if( commands.size() == 1 ) {
        usage( "File name missing" );
        return;
      }
      
      String value = cli.getOptionValue( "threads" );
      if( value != null ) {
        ts.setNumThreads( Integer.parseInt( value ) );
      }
      
      String command = commands.get( 0 );
      File file = new File( commands.get( 1 ) );
      commands.remove( 0 );
      commands.remove( 0 );
      
      long t0 = System.currentTimeMillis();
      
      if( command.equals( "random" ) ) {
        randomOrGeneric( cli, file, commands,  true );
      }
      else if( command.equals( "generic" ) ) {
        randomOrGeneric( cli, file, commands, false );
      }
      else if( command.equals( "split" ) ) {
        split( cli, file, commands );
      }
      else if( command.equals( "slice" ) ) {
        slice( cli, file, commands );
      }
      else if( command.equals( "join" ) ) {
        join( cli, file, commands );
      }
      else if( command.equals( "sort" ) ) {
        sort( cli, file, commands );
      }
      else if( command.equals( "bigsort" ) ) {
        bigsort( cli, file, commands );
      }
      else if( command.equals( "count" ) ) {
        count( cli, file, commands );
      }
      else if( command.equals( "sorted" ) ) {
        sorted( cli, file, commands );
      }
      else {
        usage( "Unknwon command '" + command + "'" );
        return;
      }
      
      long t1 = System.currentTimeMillis();
      info( "Finished command '" + command + "' in " + ( (double) ( t1 - t0 ) / 1000 ) + " s."  );
      
    }
    catch( ParseException ex ) {
      usage( "" + ex );
    }
    catch( IOException ex ) {
      usage( "" + ex );
    }
    
  }


  private static void sort( CommandLine cli, File file, List<String> commands ) {
    
    PowerString s = new PowerString( file.getName() );
    String extension = s.removeAfterLast( "." );
    String name = s.toString();
    
    File sortedFile = new File( name + ".sorted." + extension );

    SortIndex sortIndex = new SortIndex( 1 );
    String value = cli.getOptionValue( "sort" );
    if( value == null ) {
      info( "No sort index specified. Using default: -sort 1" );
    }
    else {
      sortIndex.fromString( value );
    }
    
    try {
      ts.sort( file, sortedFile, sortIndex );
    }
    catch( FileNotFoundException ex ) {
      usage( "" + ex );
    }
    catch( IOException ex ) {
      usage( "" + ex );
    }
    
  }


  private static void bigsort( CommandLine cli, File file, List<String> commands ) {
    
    PowerString s = new PowerString( file.getName() );
    String extension = s.removeAfterLast( "." );
    String name = s.toString();
    
    File sortedFile = new File( name + ".sorted." + extension );
    
    SortIndex sortIndex = new SortIndex( 1 );
    String value = cli.getOptionValue( "sort" );
    if( value == null ) {
      info( "No sort index specified. Using default: -sort 1" );
    }
    else {
      sortIndex.fromString( value );
    }
    
    int rowCount;

    value = cli.getOptionValue( "rows" );
    
    if( value == null ) {
      rowCount = 1000;
      info( "No row count was specified. Using default: -rows 1000" );
    } 
    else {
      rowCount = Integer.parseInt( value );
    }

    int maxOpen = 4;
    value = cli.getOptionValue( "maxOpen" );
    if( value != null ) {
      maxOpen = Integer.parseInt( value );
    }
    else {
      info( "No max open files was specified. Using default: -maxOpen 4" );
    }
    
    try {
      ts.bigsort( file, sortedFile, sortIndex, rowCount, maxOpen );
    }
    catch( FileNotFoundException ex ) {
      usage( "" + ex );
    }
    catch( IOException ex ) {
      usage( "" + ex );
    }
    catch( InterruptedException ex ) {
      usage( "" + ex );
    }
    
  }
  
  
  private static void count( CommandLine cli, File file, List<String> commands ) {
    
    try {
      CountResult result = ts.count( file );
      error( result.toString() );
    }
    catch( FileNotFoundException ex ) {
      usage( "" + ex );
    }
    catch( IOException ex ) {
      usage( "" + ex );
    }
    
  }
  
  
  private static void sorted( CommandLine cli, File file, List<String> commands ) {
    
    SortIndex sortIndex = new SortIndex( 1 );
    String value = cli.getOptionValue( "sort" );
    if( value == null ) {
      info( "No sort index specified. Using default: -sort 1" );
    }
    else {
      sortIndex.fromString( value );
    }
    
    try {
      int line = ts.sorted( file, sortIndex );
      error( line == -1 ? "sorted" : "not sorted at row " + line );
    }
    catch( FileNotFoundException ex ) {
      usage( "" + ex );
    }
    catch( IOException ex ) {
      usage( "" + ex );
    }
    
  }
  
  private static void split( CommandLine cli, File file, List<String> commands ) {

    String value = cli.getOptionValue( "dir" );
    if( value == null ) {
      usage( "No dir specified (-dir)" );
      return;
    }
    
    File dir = new File( value );

    int rowCount;

    value = cli.getOptionValue( "rows" );
    
    if( value == null ) {
      rowCount = 1000;
      info( "No row count was specified. Using default: -rows 1000" );
    } 
    else {
      rowCount = Integer.parseInt( value );
    }
    
    SortIndex sortIndex = null;
    value = cli.getOptionValue( "sort" );
    if( value != null ) {
      sortIndex = SortIndex.fromString( value );
    }

    try {
      ts.split( file, rowCount, dir, sortIndex );
    }
    catch( FileNotFoundException ex ) {
      usage( "" + ex );
    }
    catch( IOException ex ) {
      usage( "" + ex );
    }
    catch( InterruptedException ex ) {
      usage( "" + ex );
    }

  }


  private static void slice( CommandLine cli, File file, List<String> commands ) {
    
    String value = cli.getOptionValue( "first" );
    
    int first = 1;
    if( value == null ) {
      info( "No first row was specified. Using default: -first 1" );
    } 
    else {
      first = Integer.parseInt( value );
    }
    
    int last = 0;
    if( value == null ) {
      info( "No last row was specified. Using default: -last 0 (0 means end of file)" );
    } 
    else {
      last = Integer.parseInt( value );
    }
    
    value = cli.getOptionValue( "out" );
    if( value == null ) {
      PowerString s = new PowerString( file.getName() );
      String extension = s.removeAfterLast( "." );
      String name = s.toString();
      value = name + ".slice." + extension;
      info( "No output file was specified. Using default: -out " + value );
    } 
    File outputFile = new File( value );
    
    try {
      int rows = ts.slice( file, outputFile, first, last );
      info( rows + " rows written." );
    }
    catch( FileNotFoundException ex ) {
      usage( "" + ex );
    }
    catch( IOException ex ) {
      usage( "" + ex );
    }
    
  }
  
  
  private static void join( CommandLine cli, File file, List<String> commands ) {
    
    String value = cli.getOptionValue( "dir" );
    if( value == null ) {
      usage( "No dir specified (-dir)" );
      return;
    }
    
    File dir = new File( value );
    
    SortIndex sortIndex = null;
    value = cli.getOptionValue( "sort" );
    if( value != null ) {
      sortIndex = SortIndex.fromString( value );
    }

    int maxOpen = 4;
    value = cli.getOptionValue( "maxOpen" );
    if( value != null ) {
      maxOpen = Integer.parseInt( value );
    }
    else {
      info( "No max open files was specified. Using default: -maxOpen 4" );
    }
    
    try {
      ts.join( dir, file, sortIndex, maxOpen );
    }
    catch( FileNotFoundException ex ) {
      usage( "" + ex );
    }
    catch( IOException ex ) {
      usage( "" + ex );
    }
    
  }
  
  
  private static void randomOrGeneric( CommandLine cli, File file, List<String> commands,
    boolean random ) throws IOException {
    
    int rowCount, columnCount;
    
    String value = cli.getOptionValue( "rows" );
    if( value == null ) {
      rowCount = 1000;
      info( "No row count was specified. Using default: -rows 1000" );
    } 
    else {
      rowCount = Integer.parseInt( value );
    }
    
    value = cli.getOptionValue( "cols" );
    if( value == null ) {
      columnCount = 20;
      info( "No row count was specified. Using default: -cols 20" );
    } 
    else {
      columnCount = Integer.parseInt( value );
    }
    
    if( random ) {
      ts.random( file, rowCount, columnCount );
    }
    else {
      ts.generic( file, rowCount, columnCount );
    }
    
  }


  private static void error( String message ) {
    System.out.println( message );
  }
  
  
  private static void info( String message ) {
    if( !ts.isQuiet() ) {
      System.out.println( message );
    }
  }


  private static void usage( String message ) {
    
    System.out.println( message + "\n" );
    HelpFormatter usage = new HelpFormatter();
    usage.printHelp( "tablestream [OPTIONS] COMMAND FILENAME\n\n\n", options );
    System.out.println( "\nCommands are:\n" );
    System.out.println( "split ................. split file into smaller files (use -sort <args> to created sorted files)" );
    System.out.println( "slice ................. write part of larger file to disk" );
    System.out.println( "join .................. join splitted files into one file (use -sort <args> to assume sorted files and create a sorted file)" );
    System.out.println( "sorted ................ test if file is sorted (needs sort index via -sort <args>)" );
    System.out.println( "count ................. count rows" );
    System.out.println( "sort .................. sort a file in memory" );
    System.out.println( "bigsort ............... sort a big file using sorted split + join" );
    System.out.println( "random ................ creates a file with random data" );
    System.out.println( "generic ............... creates a file with generic data" );
    
  }
  
  
}


