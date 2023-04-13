package models;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

import utils.ChartPlot;

/**
 * This interface represents one user in the portfolio system.
 * This user is a part of UserSetModel. This interface
 * contains various operations that can be applied to a
 * single user.
 */
public interface UserModel {
  /**
   * Get the username of the user.
   *
   * @return username of the user as a string only.
   */
  String getUserName();

  /**
   * Get the unique userId of the user.
   * User id is automatically generated for a new user
   * to maintain uniqueness.
   *
   * @return returns the user id of user as an integer only.
   */
  int getUserId();

  /**
   * Add a new portfolio to a User Model.
   *
   * @param portfolioModel Portfolio with all the stocks
   *                       attributes that needs to be added.
   */
  void addPortfolio(PortfolioModel portfolioModel);

  void removePortfolio(String portfolioName);

  /**
   * Get all the portfolios for a given user.
   *
   * @return a map of portfolios and its ticker symbol
   *         for o particular user.
   */
  Map<String, PortfolioModel> getAllPortfolios();

  /**
   * Get all the flexible portfolios for a given user.
   *
   * @return a map of flexible portfolios and its ticker symbol
   *         for o particular user.
   */
  Map<String, PortfolioModel> getAllFlexiblePortfolios();

  /**
   * Get all the inflexible portfolios for a given user.
   *
   * @return a map of inflexible portfolios and its ticker symbol
   *         for o particular user.
   */
  Map<String, PortfolioModel> getAllInflexiblePortfolios();

  Map<String, PortfolioModel> getAllStrategyFlexiblePortfolios();

  /**
   * Get a particular portfolio of the user.
   *
   * @param portfolioName name of portfolio which needs to be returned.
   * @return A Portfolio model object with all the attributes of that portfolio.
   */
  PortfolioModel getPortfolio(String portfolioName);


  /**
   * Return the combined value of all the stocks of a portfolio
   * on a particular date. Date format should be YYYY-MM-DD.
   *
   * @param portfolio portfolio name whose value needs to be calculated.
   * @param date      date on which portfolio's is to be calculated.
   * @return the combined value of all the stocks of a portfolio as a double only.
   * @throws IllegalArgumentException if date is greater than equal to present date.
   */
  double getPortfolioValueByDate(String portfolio, Date date) throws IllegalArgumentException;

  /**
   * Download a portfolio for a given user to the system as a bunch
   * of xml files.
   *
   * @param portfolio name of portfolio to be downloaded.
   * @return path where the file has been saved as a string only.
   * @throws IOException                  if not able to save the portfolio correctly.
   * @throws ParserConfigurationException if the xml parser is configured incorrectly.
   * @throws TransformerException         if the object cannot be converted
   *                                      to the specified XML type.
   */
  String savePortfolio(String portfolio) throws IOException,
          ParserConfigurationException, TransformerException;

  /**
   * Converts the UserModel to an XML file.
   * Saves the XML file in root folder specified.
   *
   * @param path file path where a particular user model needs to be written.
   * @throws IOException                  if not able to write the file.
   * @throws ParserConfigurationException if the input data is not correctly parsed.
   * @throws TransformerException         if the object cannot be converted
   *                                      to the specified XML type.
   */
  void generateXML(String path) throws IOException, ParserConfigurationException,
          TransformerException;

  /**
   * Modify a portfolio by adding/selling a stock.
   *
   * @param portfolioName portfolio to be modified.
   * @param stockModel    stock model to be modified in the portfolio.
   * @param transaction   transaction object to be added.
   */
  void modifyPortfolio(String portfolioName, StockModel stockModel, TransactionModel transaction);

  /**
   * Get cost basis of a portfolio on a particular date.
   *
   * @param portfolioName name of portfolio to be queried.
   * @param date          date on which cost basis needs to be calculated.
   * @return the value of cost basis as a double only.
   * @throws IllegalArgumentException if date is greater than equal to present date.
   */
  double getPortfolioCostBasis(String portfolioName, Date date) throws IllegalArgumentException;

  /**
   * Get the performance of a portfolio over a start date and end date.
   *
   * @param startDate     start date of the performance.
   * @param endDate       end date of the performance.
   * @param portfolioName name of portfolio to be queried.
   * @return a chart plot object with all the performance.
   */
  ChartPlot getPerformanceForPortfolio(Date startDate, Date endDate, String portfolioName);

  /**
   * Get the composition of a portfolio on a date.
   *
   * @param portfolioName name of portfolio to be queried.
   * @param date          date on which composition needs to be calculated.
   * @return a list of string lists with all the composition fields.
   * @throws IllegalArgumentException if date is greater than equal to present date.
   */
  List<List<String>> getCompositionValueByDate(String portfolioName, Date date)
          throws IllegalArgumentException;

  /**
   * Method to run each portfolios strategy for all transactions not created yet.
   */
  void runAllStrategies();
}
