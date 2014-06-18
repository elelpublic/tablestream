// (C) 1998-2015 Information Desire Software GmbH
// www.infodesire.com

package com.infodesire.tablestream;



public class CountResult {
  
  
  private int rows;
  private Integer minCols;
  private Integer maxCols;
  
  
  /**
   * @return the rows
   */
  public int getRows() {
    return rows;
  }
  
  
  /**
   * @param rows the rows to set
   */
  public void setRows( int rows ) {
    this.rows = rows;
  }
  
  
  /**
   * @return the minCols
   */
  public Integer getMinCols() {
    return minCols;
  }
  
  
  /**
   * @param minCols the minCols to set
   */
  public void setMinCols( Integer minCols ) {
    this.minCols = minCols;
  }
  
  
  /**
   * @return the maxCols
   */
  public Integer getMaxCols() {
    return maxCols;
  }
  
  
  /**
   * @param maxCols the maxCols to set
   */
  public void setMaxCols( Integer maxCols ) {
    this.maxCols = maxCols;
  }
  
  
  public String toString() {
    return "Rows: " + rows //
      + ", Min columns: " + ( minCols == null ? "-" : minCols ) //
      + ", Max columns: " + ( maxCols == null ? "-" : maxCols ) //
      ;
  }

}
