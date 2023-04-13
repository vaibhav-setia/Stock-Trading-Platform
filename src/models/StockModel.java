package models;

import java.io.IOException;
import java.util.Date;
import java.util.Map;
import java.util.Set;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

/**
 * This interface represents a stock model in the portfolio system.
 * This interface contains various operations that can be applied to
 * a particular stock.
 */
public interface StockModel {
  /**
   * Get the stock ticker symbol for a particular stock.
   *
   * @return a stock ticker symbol as string only.
   */
  String getStockTicker();

  /**
   * Get the stock name for a particular stock.
   *
   * @return a stock name as string only.
   */
  String getStockName();

  /**
   * Get the stock exchange name for a particular stock
   * where it is traded.
   *
   * @return a stock exchange name as string only.
   */
  String getExchangeName();

  /**
   * Get the stock IPO date for a particular stock.
   *
   * @return a Date object when stock got offered to public.
   */
  Date getIpoDate();

  /**
   * Get the stock delisting date for a particular stock.
   *
   * @return a Date object when stock got delisted to public.
   */
  Date getDelistingDate();

  /**
   * Get the status of a particular stock.
   *
   * @return a status of a stock as a string only.
   */
  String getStatus();

  /**
   * Get the asset type of particular stock.
   *
   * @return the asset type of stock as a string only.
   */
  String getAssetType();

  /**
   * Get the value of stock on a particular date.
   *
   * @param date the date at which price will be calculated.
   * @return a stock price value as double only.
   */
  double getPriceOnADate(Date date);

  /**
   * Check whether stock has a price on a particular date.
   *
   * @param date on which stock price needs to be checked.
   * @return a true/false boolean whether price exists.
   */
  boolean doesPriceExistOnADate(Date date);

  /**
   * Converts the Stock Model to an XML file.
   * Saves the XML file in relative folder path specified.
   *
   * @param path relative path where the Stock Model is stored as XML.
   * @throws IOException                  if not able to write the file.
   * @throws ParserConfigurationException if the input data is not correctly parsed.
   * @throws TransformerException         if the object cannot be
   *                                      converted to the specified XML type.
   */
  void generateXML(String path) throws IOException, ParserConfigurationException,
          TransformerException;

  Map<Date, Map<Integer, Double>> getPriceOnMultipleDates(Set<Date> dateSet, Date maxDate);

}
