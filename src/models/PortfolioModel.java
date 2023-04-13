package models;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

import utils.ChartPlot;

/**
 * This interface represents one portfolio
 * of a particular user in the system. This portfolio is
 * a part of User Model. This interface contains various
 * operations that can be applied to a single portfolio.
 */
public interface PortfolioModel {
  /**
   * Get the name of the portfolio given by the user.
   *
   * @return the name of portfolio as a string only.
   */
  String getName();

  /**
   * Return the combined value of all the stocks of a portfolio
   * on a particular date. Date format should be YYYY-MM-DD.
   * No rounding off is done on returned result.
   *
   * @param date date on which portfolio's is to be calculated.
   * @return the combined value of all the stocks of a portfolio as a double only.
   */
  double getPortfolioValueByDate(Date date) throws IllegalArgumentException;

  /**
   * Get the timestamp when the portfolio was created.
   *
   * @return the time when portfolio was created as a timestamp object only.
   */
  Timestamp getPortfolioAddDateTime();

  Timestamp getTimestamp();


  /**
   * Get a map of all the portfolio elements of a user, each element is uniquely identified
   * by the stock ticker symbol.
   *
   * @return all the portfolios and various elements in them as a map only.
   */
  Map<String, PortfolioElementModel> getPortfolioElements();

  /**
   * Converts the Portfolio Model to an XML file.
   * Saves the XML file in the relative path specified.
   *
   * @param path file path where a particular portfolio model needs to be written.
   * @throws IOException                  if not able to write the file.
   * @throws ParserConfigurationException if the input data is not correctly parsed.
   * @throws TransformerException         if the object cannot be converted
   *                                      to the specified XML type.
   */
  void generateXML(String path) throws IOException, ParserConfigurationException,
          TransformerException;

  /**
   * Get the portfolio if it is inflexible.
   *
   * @return a portfolio model object if it is inflexible
   *         else return null.
   */
  PortfolioModel getConcretePortfolio();

  /**
   * Get the portfolio if it is flexible.
   *
   * @return a portfolio model object if it is flexible
   *         else return null.
   */
  PortfolioModel getFlexiblePortfolio();

  PortfolioModel getStrategyFlexiblePortfolio();

  /**
   * Get the performance of a portfolio over a start date and end date.
   *
   * @param startDate start date of the performance.
   * @param endDate   end date of the performance.
   * @return a chart plot object with all the performance.
   */
  ChartPlot getPerformanceForPortfolio(Date startDate, Date endDate);

  /**
   * Modify a portfolio by adding/selling stocks in it.
   *
   * @param stockModel  stock which needs to be modified.
   * @param transaction transaction details of the stock.
   * @throws IllegalStateException if method is called on an object not open for modification.
   */
  void modifyPortfolio(StockModel stockModel,
                       TransactionModel transaction) throws IllegalStateException;

  /**
   * Modify a portfolio by adding a new portfolio element to it.
   *
   * @param portfolioElement portfolio element which needs to be added.
   * @throws IllegalStateException if method is called on an object not open for modification.
   */
  void addPortfolioElement(PortfolioElementModel portfolioElement) throws IllegalStateException;

  /**
   * Get cost basis of a portfolio on a particular date.
   *
   * @param date date on which cost basis needs to be calculated.
   * @return the cost basis value as a double only.
   */
  double getPortfolioCostBasis(Date date);

  /**
   * Get all the transactions of an element.
   *
   * @return a list of transaction model objects of an element.
   */
  List<TransactionModel> getAllTransactions();

  /**
   * Get the composition of a portfolio on a date.
   *
   * @param date date on which composition needs to be calculated.
   * @return a list of string lists with all the composition fields.
   */
  List<List<String>> getPortfolioComposition(Date date);

  void runAllStrategies() throws UnsupportedOperationException;

  List<StrategyModel> getAllStrategies() throws UnsupportedOperationException;

  void addStrategy(StrategyModel strategyModel) throws UnsupportedOperationException;
}
