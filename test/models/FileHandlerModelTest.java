package models;

import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

import utils.FileHandlerModelImpl;

import static org.junit.Assert.assertEquals;

/**
 * Test Class to write junits to ensure File Handler class is working correctly.
 */
public class FileHandlerModelTest {

  @Before
  public void writeFile() throws IOException {
    String tickers_supported = "AAPL,Apple Inc,NASDAQ,12/12/80\n"
            + "ADBE,Adobe Inc,NASDAQ,14/08/86\n"
            + "AMZN,Amazon.com Inc,NASDAQ,15/05/97\n"
            + "CSCO,Cisco Systems Inc,NASDAQ,26/03/90\n"
            + "GOOG,Alphabet Inc - Class C,NASDAQ,27/03/14\n"
            + "MSFT,Microsoft Corporation,NASDAQ,13/03/86\n"
            + "NFLX,Netflix Inc,NASDAQ,23/05/02\n"
            + "PEP,PepsiCo Inc,NASDAQ,01/06/72\n"
            + "SBUX,Starbucks Corp,NASDAQ,26/06/92\n"
            + "TSLA,Tesla Inc,NASDAQ,29/06/10";
    java.io.FileWriter fw = new java.io.FileWriter("TestingHelper//tickers_supported.csv");
    fw.write(tickers_supported);
    fw.close();
  }

  @Test
  public void testReadFileDataAsString() throws IOException {
    String fileData = new FileHandlerModelImpl()
            .readFileDataAsString("TestingHelper/tickers_supported.csv");
    assertEquals("ADBE,Adobe Inc,NASDAQ,14/08/86",
            fileData.split("\r\n")[1]);
  }

  @Test
  public void testGetFilePathForATicker() {
    String tickerFilePath = "StockSetModel//tickers/GOOG.csv";
    assertEquals(tickerFilePath, new FileHandlerModelImpl().getFilePathForATicker("GOOG"));
  }

  @Test
  public void testGetFilePathForTickerList() {
    assertEquals("StockSetModel//tickers_supported.csv",
            new FileHandlerModelImpl().getFilePathForTickerList());
  }
}
