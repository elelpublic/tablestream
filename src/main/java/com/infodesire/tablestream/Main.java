// (C) 1998-2014 Information Desire Software GmbH
// www.infodesire.com

package com.infodesire.tablestream;

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
      .withDescription( "Name of file" ) //
      .create( "file" ) // 
      );
    options.addOption( OptionBuilder //
      .hasArg() //
      .withDescription( "Name of directory" ) //
      .create( "dir" ) // 
      );
  }
  

  public static void main( String[] args ) {
    
    CommandLineParser parser = new PosixParser();
    try {
      
      CommandLine cli = parser.parse( options, args );
      
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
      
      String command = commands.get( 0 );
      
      if( command.equals( "random" ) ) {
        commands.remove( 0 );
        randomOrGeneric( cli, commands, true );
      }
      else if( command.equals( "generic" ) ) {
        commands.remove( 0 );
        randomOrGeneric( cli, commands, false );
      }
      else if( command.equals( "split" ) ) {
        commands.remove( 0 );
        split( cli, commands );
      }
      else if( command.equals( "sort" ) ) {
        commands.remove( 0 );
        split( cli, commands );
      }
      else if( command.equals( "bigsort" ) ) {
        commands.remove( 0 );
        System.out.println( "Not implemented yet." );
      }
      else {
        usage( "Unknwon command '" + command + "'" );
        return;
      }
      
    }
    catch( ParseException ex ) {
      usage( "" + ex );
    }
    catch( IOException ex ) {
      usage( "" + ex );
    }
    
  }


  private static void split( CommandLine cli, List<String> commands ) {

    String value = cli.getOptionValue( "file" );
    if( value == null ) {
      usage( "No file specified (-file)" );
      return;
    }

    File file = new File( value );
    
    value = cli.getOptionValue( "dir" );
    if( value == null ) {
      usage( "No dir specified (-dir)" );
      return;
    }
    
    File dir = new File( value );

    int rowCount;

    value = cli.getOptionValue( "rows" );
    
    if( value == null ) {
      rowCount = 1000;
      log( "No row count was specified (-rows). Using default value of 1000." );
    } 
    else {
      rowCount = Integer.parseInt( value );
    }
    
    try {
      TS.split( file, rowCount, dir );
    }
    catch( FileNotFoundException ex ) {
      usage( "" + ex );
    }
    catch( IOException ex ) {
      usage( "" + ex );
    }

  }


  private static void randomOrGeneric( CommandLine cli, List<String> commands,
    boolean random ) throws IOException {
    
    int rowCount, columnCount;
    File file;
    
    String value = cli.getOptionValue( "rows" );
    if( value == null ) {
      rowCount = 1000;
      log( "No row count was specified (-rows). Using default value of 1000." );
    } 
    else {
      rowCount = Integer.parseInt( value );
    }
    
    value = cli.getOptionValue( "cols" );
    if( value == null ) {
      columnCount = 20;
      log( "No row count was specified (-cols). Using default value of 20." );
    } 
    else {
      columnCount = Integer.parseInt( value );
    }
    
    value = cli.getOptionValue( "file" );
    if( value == null ) {
      file = File.createTempFile( random ? "random-" : "generic-", ".ts" );
      log( "No file name was specified. Using temp file '" + file.getAbsolutePath() + "'" );
    } 
    else {
      file = new File( value );
    }
    
    long t0 = System.currentTimeMillis();
    
    if( random ) {
      TS.random( file, rowCount, columnCount );
    }
    else {
      TS.generic( file, rowCount, columnCount );
    }
    
    log( "Generated " + rowCount + " rows in " + ( (double) ( System.currentTimeMillis() - t0 ) / 1000 ) + "s" );
    
  }

  private static void log( String message ) {
    System.out.println( message );
  }

  private static void usage( String message ) {
    
    System.out.println( message + "\n" );
    HelpFormatter usage = new HelpFormatter();
    usage.printHelp( "tablestream [OPTIONS] COMMAND [PARAMETERS]\n\n\n", options );
    System.out.println( "\nCommands are:\n" );
    System.out.println( "sort .................. sort a file in memory" );
    System.out.println( "bigsort ............... sort a big file using split to disk" );
    System.out.println( "random ................ creates a file with random data" );
    System.out.println( "generic ............... creates a file with generic data" );
    System.out.println( "split ................. split file into smaller files" );
    
  }
  
  
}


