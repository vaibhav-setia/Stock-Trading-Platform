package models;

import org.xml.sax.SAXException;

import java.io.IOException;
import java.text.ParseException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.xpath.XPathExpressionException;

import utils.ChartPlot;
import utils.DataParser;
import utils.DataPersister;
import utils.XMLParserImpl;
import utils.XMLWriterImpl;

/**
 * This class implements User Model and all its functionality.
 * This class stores various details of a user including all of their portfolios.
 * All the portfolios have a unique name.
 */
public class UserModelImpl implements UserModel {

  private final int userId;

  private final String name;

  private final Map<String, PortfolioModel> portfolioWallet;

  /**
   * Constructor to assign values to various attributes of the UserModel.
   *
   * @param userId          userId of the user to be assigned.
   * @param name            name of the user.
   * @param portfolioWallet map of all the portfolios of a user
   *                        with portfolio name.
   */
  public UserModelImpl(int userId, String name, Map<String, PortfolioModel> portfolioWallet) {
    this.userId = userId;
    this.name = name;
    this.portfolioWallet = portfolioWallet;
  }

  /**
   * Static class to get a builder to build
   * User Model class.
   *
   * @return a new builder object for User Model.
   */
  public static UserModelBuilder getBuilder() {
    return new UserModelBuilder();
  }

  @Override
  public String getUserName() {
    return this.name;
  }

  @Override
  public int getUserId() {
    return this.userId;
  }

  @Override
  public void addPortfolio(PortfolioModel portfolioModel) {
    this.portfolioWallet.put(portfolioModel.getName(), portfolioModel);
  }

  @Override
  public void removePortfolio(String portfolioName) {
    this.portfolioWallet.remove(portfolioName);
  }

  @Override
  public Map<String, PortfolioModel> getAllPortfolios() {
    return this.portfolioWallet;
  }

  @Override
  public Map<String, PortfolioModel> getAllFlexiblePortfolios() {
    Map<String, PortfolioModel> portfolioModelMap = new HashMap<>();
    for (Map.Entry<String, PortfolioModel> entry : portfolioWallet.entrySet()) {
      PortfolioModel portfolioModel = entry.getValue().getFlexiblePortfolio();
      if (portfolioModel != null) {
        portfolioModelMap.put(entry.getKey(), portfolioModel);
      }
    }
    return portfolioModelMap;
  }

  @Override
  public Map<String, PortfolioModel> getAllInflexiblePortfolios() {
    Map<String, PortfolioModel> portfolioModelMap = new HashMap<>();
    for (Map.Entry<String, PortfolioModel> entry : portfolioWallet.entrySet()) {
      PortfolioModel portfolioModel = entry.getValue().getConcretePortfolio();
      if (portfolioModel != null) {
        portfolioModelMap.put(entry.getKey(), portfolioModel);
      }
    }
    return portfolioModelMap;
  }

  @Override
  public Map<String, PortfolioModel> getAllStrategyFlexiblePortfolios() {
    Map<String, PortfolioModel> portfolioModelMap = new HashMap<>();
    for (Map.Entry<String, PortfolioModel> entry : portfolioWallet.entrySet()) {
      PortfolioModel portfolioModel = entry.getValue().getStrategyFlexiblePortfolio();
      if (portfolioModel != null) {
        portfolioModelMap.put(entry.getKey(), portfolioModel);
      }
    }
    return portfolioModelMap;
  }

  @Override
  public PortfolioModel getPortfolio(String portfolioName) {
    return this.portfolioWallet.get(portfolioName);
  }

  @Override
  public double getPortfolioValueByDate(String portfolio, Date date)
          throws IllegalArgumentException {
    if (date.compareTo(new Date()) > 0) {
      throw new IllegalArgumentException();
    }
    PortfolioModel portfolioModel = portfolioWallet.get(portfolio);
    return portfolioModel.getPortfolioValueByDate(date);
  }

  @Override
  public String savePortfolio(String portfolio) throws IOException,
          ParserConfigurationException, TransformerException {
    PortfolioModel portfolioModel = this.portfolioWallet.get(portfolio);
    String path = System.getProperty("user.dir") + "//downloads//" + portfolio;
    portfolioModel.generateXML(path);
    return path;
  }

  @Override
  public void generateXML(String path) throws IOException,
          ParserConfigurationException, TransformerException {
    DataPersister persister = new XMLWriterImpl();
    persister.writeUserModel(path, this);
  }

  @Override
  public void modifyPortfolio(String portfolioName, StockModel stockModel,
                              TransactionModel transaction) {
    PortfolioModel portfolioModel = portfolioWallet.get(portfolioName);
    portfolioModel.modifyPortfolio(stockModel, transaction);
  }

  @Override
  public double getPortfolioCostBasis(String portfolioName, Date date) {
    if (date.compareTo(new Date()) > 0) {
      throw new IllegalArgumentException();
    }
    return portfolioWallet.get(portfolioName).getPortfolioCostBasis(date);
  }

  @Override
  public ChartPlot getPerformanceForPortfolio(Date startDate, Date endDate,
                                              String portfolioName) {
    return portfolioWallet.get(portfolioName)
            .getPerformanceForPortfolio(startDate, endDate);
  }

  @Override
  public List<List<String>> getCompositionValueByDate(String portfolioName, Date date)
          throws IllegalArgumentException {
    if (date.compareTo(new Date()) > 0) {
      throw new IllegalArgumentException();
    }

    return portfolioWallet.get(portfolioName).getPortfolioComposition(date);
  }

  @Override
  public void runAllStrategies() {
    for (PortfolioModel portfolioModel : portfolioWallet.values()) {
      if (portfolioModel.getStrategyFlexiblePortfolio() != null) {
        portfolioModel.runAllStrategies();
      }
    }
  }

  /**
   * Builder helper class to build an object of User Model class.
   * It contains functionality to set different fields of User Model.
   */
  public static class UserModelBuilder {
    private int userId;

    private String name;

    private Map<String, PortfolioModel> portfolioWallet;

    /**
     * Initialise a new builder object with an empty portfolio map,
     * 0 as userId and no name.
     */
    private UserModelBuilder() {
      this.userId = 0;
      this.name = "";
      this.portfolioWallet = new HashMap<>();
    }

    /**
     * Set name to the builder object of user model.
     *
     * @param name name of user to be set to builder object.
     * @return the builder object after assigning a username.
     */
    public UserModelBuilder name(String name) {
      this.name = name;
      return this;
    }

    /**
     * Set a complete portfolio wallet to the builder object of user model
     * which contains all the portfolios of a user.
     *
     * @param portfolioWallet Map of portfolios and its name to assigned
     *                        to the builder object
     * @return the builder object after assigning the portfolios to it.
     */
    public UserModelBuilder portfolioWallet(Map<String, PortfolioModel> portfolioWallet) {
      this.portfolioWallet = portfolioWallet;
      return this;
    }

    /**
     * Set userId to the builder object of user model.
     *
     * @param userId userId of user to be set to builder object.
     * @return the builder object after assigning a userId.
     */
    public UserModelBuilder userId(int userId) {
      this.userId = userId;
      return this;
    }

    /**
     * Build a User Set Model builder object to get User Set Model.
     *
     * @return a new User Set Model object with all users appended
     *         in the list.
     */
    public UserModelImpl build() {
      return new UserModelImpl(this.userId, this.name, this.portfolioWallet);
    }

    /**
     * Constructs a user model builder object by reading the contents from xml file
     * provided.
     *
     * @param path relative path to source folder from where the xml file needs to be read.
     * @param id   userid of the user.
     * @return an object of UserModelBuilder with all the attributes assigned from XML file.
     * @throws IOException                  if xml is not correctly read from file.
     * @throws ParserConfigurationException if the xml parser is configured incorrectly.
     * @throws SAXException                 exception thrown if xml violates integrity constraints.
     * @throws XPathExpressionException     if the Xpath to filter xml is incorrect.
     * @throws ParseException               if xml string is not correctly parsed as per contract.
     */
    public UserModelBuilder readXML(String path, String id)
            throws XPathExpressionException, IOException,
            ParserConfigurationException, SAXException, ParseException {
      DataParser<UserModelBuilder> dataParserUserModel = new XMLParserImpl<>();
      return dataParserUserModel.getUserModelBuilder(path, id);
    }
  }
}
