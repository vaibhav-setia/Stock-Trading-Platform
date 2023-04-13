package models;

import java.io.IOException;
import java.util.Date;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

/**
 * This interface represents one fof the portfolio element
 * of a particular portfolio of a user in the system. This portfolio is
 * a part of User Model. This interface contains various
 * operations that can be applied to a single portfolio element
 * of a portfolio.
 */
public interface PortfolioElementModel {
  /**
   * Get the stock of the portfolio element.
   *
   * @return A stock model object which makes the portfolio element.
   */
  StockModel getStockModel();

  /**
   * Get the current average price of the stock amount
   * present in the portfolio element.
   *
   * @return the average price of stock as a double only.
   */
  double getStockCurrentAvgPrice();

  /**
   * Get the latest transaction date for this stock in a
   * particular portfolio.
   *
   * @return the lastest transaction date as a Date object.
   */
  Date getTransactionDate();

  /**
   * Get the total amount of this stock in the portfolio.
   *
   * @return total stock quantity as integer only.
   */
  double getStockTotalQuantity();

  /**
   * Converts the Portfolio Element Model to an XML file.
   * Saves the XML file in relative folder specified.
   *
   * @param path file path where a particular Portfolio Element Model needs to be written.
   * @throws IOException                  if not able to write the file.
   * @throws ParserConfigurationException if the input data is not correctly parsed.
   * @throws TransformerException         if the object cannot be converted to the
   *                                      specified XML type.
   */
  void generateXML(String path) throws IOException, ParserConfigurationException,
          TransformerException;

  /**
   * Adds a transaction object to the portfolio element.
   *
   * @param transaction transaction object to be added.
   */
  void addTransaction(TransactionModel transaction);

  /**
   * Get value of a portfolio element on a particular date.
   *
   * @param date date on which value needs to be calculated.
   * @return the value of the element as a double only.
   */
  double getPortfolioElementValueOnDate(Date date) throws IllegalArgumentException;

  /**
   * Get cost basis of a portfolio element on a particular date.
   *
   * @param date date on which cost basis needs to be calculated.
   * @return the value of cost basis as a double only.
   */
  double getPortfolioCostBasis(Date date) throws IllegalArgumentException;

  /**
   * Get all the transactions of an element.
   *
   * @return a list of transaction model objects of an element.
   */
  List<TransactionModel> getAllTransactions();

  /**
   * Get the composition of an element on a date.
   *
   * @param date date on which composition needs to be calculated.
   * @return a list of string with all the composition fields.
   */
  List<String> getCompositionOnDate(Date date) throws IllegalArgumentException;
}
