package models;

import org.xml.sax.SAXException;

import java.io.IOException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPathExpressionException;

import utils.DataParser;
import utils.XMLParserImpl;

import static models.AbstractPortfolioModel.addElementToMap;
import static models.AbstractPortfolioModel.checkShort;

/**
 * Abstract class to build various builders pertaining to
 * different portfolios.It contains all the methods being used by different
 * builders of each portfolio implementation.
 *
 * @param <T> type of builder to be made.
 */
public abstract class AbstractPortfolioBuilder<T extends AbstractPortfolioBuilder<T>> {

  protected String portfolioName;

  protected Timestamp timestamp;


  protected Map<String, PortfolioElementModel> portfolioElementHashMap;

  /**
   * Constructor to assign initial values to various attributes of the Portfolio Model.
   */
  public AbstractPortfolioBuilder() {
    this.portfolioName = "";
    this.timestamp = new Timestamp(System.currentTimeMillis());
    this.portfolioElementHashMap = new HashMap<>();
  }

  /**
   * Set timestamp to the builder object of portfolio model.
   *
   * @param timestamp timestamp of portfolio to be set to builder object.
   * @return the builder object after assigning a timestamp.
   */
  public T timestamp(Timestamp timestamp) {
    this.timestamp = timestamp;
    return returnBuilder();
  }

  /**
   * Set portfolio name to the builder object of portfolio model.
   *
   * @param portfolioName name of portfolio to be set to builder object.
   * @return the builder object after assigning a portfolio name.
   */
  public T portfolioName(String portfolioName) {
    this.portfolioName = portfolioName;
    return returnBuilder();
  }

  /**
   * Assign a map of portfolio elements to the portfolio.
   *
   * @param portfolioHashMap all the portfolio elements to be assigned to portfolio.
   * @return a builder object of the portfolio builder only.
   */
  public T portfolioHashMap(
          Map<String, PortfolioElementModel> portfolioHashMap) {
    this.portfolioElementHashMap = portfolioHashMap;
    return returnBuilder();
  }

  /**
   * Set strategyList to the builder object of portfolio model.
   *
   * @param strategyList to be set to the builder object.
   * @return the resulting builder object of particular portfolio.
   * @throws UnsupportedOperationException if method called on portfolio without a strategy list.
   */
  public T strategyList(List<StrategyModel> strategyList) throws UnsupportedOperationException {
    throw new UnsupportedOperationException();
  }

  /**
   * Build a particular portfolio from its builder object.
   *
   * @return the corresponding portfolio model object only.
   */
  public abstract PortfolioModel build();

  /**
   * Return the builder object of the corresponding portfolio.
   *
   * @return the builder object of portfolio only.
   */
  public abstract T returnBuilder();

  /**
   * Constructs a portfolio model builder object by reading the contents from xml file
   * provided.
   *
   * @param path          relative path to source folder from where the xml file needs to be read.
   * @param portfolioName name of portfolio to specify folder to read from.
   * @return an object of PortfolioModelBuilder with all the attributes assigned from XML file.
   * @throws IOException                  if xml is not correctly read from file.
   * @throws ParserConfigurationException if the xml parser is configured incorrectly.
   * @throws SAXException                 exception thrown if xml violates integrity constraints.
   * @throws XPathExpressionException     if the Xpath to filter xml is incorrect.
   * @throws ParseException               if xml string is not correctly parsed as per contract.
   */
  public abstract T readXML(String path, String portfolioName)
          throws XPathExpressionException, IOException,
          ParserConfigurationException, SAXException, ParseException;

  /**
   * Build portfolio from XML provided by the user.
   * Location of filer is relative to the place program is running from.
   *
   * @param path          path of the xml file to upload.
   * @param portfolioName name of portfolio being uploaded.
   * @param stockSetModel set of all the stocks currently available.
   * @return an object of PortfolioModelBuilder with all the attributes assigned from XML file.
   * @throws IOException                  if xml is not correctly read from file.
   * @throws ParserConfigurationException if the xml parser is configured incorrectly.
   * @throws SAXException                 exception thrown if xml violates integrity constraints.
   * @throws XPathExpressionException     if the Xpath to filter xml is incorrect.
   * @throws ParseException               if xml string is not correctly parsed as per contract.
   * @throws IllegalArgumentException     if the date provided does not have any price on market.
   * @throws NullPointerException         if the given stock does not exist.
   */
  public T buildPortfolioFromXML(String path, String portfolioName,
                                 StockSetModel stockSetModel)
          throws XPathExpressionException, IOException, ParserConfigurationException,
          SAXException, ParseException, IllegalArgumentException, NullPointerException {
    DataParser<String> dataParser = new XMLParserImpl<>();
    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
    this.portfolioName = portfolioName;
    List<String> attributeNames = Arrays.asList("StockTicker", "totalQuantity",
            "Date", "BrokerCommission");
    List<List<String>> stocks = dataParser.getAllAttributes(path,
            "//PortfolioModel/PortfolioElementModel", attributeNames);
    for (List<String> stock : stocks) {
      addElementToMap(stockSetModel, format, stock, this.portfolioElementHashMap);
    }
    checkShort(this.portfolioElementHashMap);
    return returnBuilder();
  }

}
