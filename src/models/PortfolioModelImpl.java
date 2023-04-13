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
import javax.xml.transform.TransformerException;
import javax.xml.xpath.XPathExpressionException;

import utils.DataParser;
import utils.DataPersister;
import utils.XMLParserImpl;
import utils.XMLWriterImpl;

/**
 * This class stores various details of a particular portfolio
 * for a particular user. A portfolio consists of different stocks
 * and their attributes as a portfolio element uniquely identified
 * by the stock ticker symbol. It extends the abstract portfolio model
 * which contains various common functionalities across different types
 * of portfolios. The portfolio cannot be modified once created and this class
 * will throw errors if such an operation is tried.
 */
public class PortfolioModelImpl extends AbstractPortfolioModel {


  /**
   * Constructor to assign values to various attributes of the Portfolio Model.
   *
   * @param portfolioName           name of the portfolio as a String.
   * @param timestampAdded          time when the portfolio was created.
   * @param portfolioElementHashMap map of all the portfolios and various elements they have.
   */
  public PortfolioModelImpl(String portfolioName, Timestamp timestampAdded,
                            Map<String, PortfolioElementModel> portfolioElementHashMap) {
    super(portfolioName, timestampAdded, portfolioElementHashMap);
  }

  /**
   * Static class to get a builder to build
   * Portfolio Model class.
   *
   * @return a new builder object for Portfolio Model.
   */
  public static PortfolioModelBuilder getBuilder() {
    return new PortfolioModelBuilder();
  }

  @Override
  public PortfolioModel getConcretePortfolio() {
    return this;
  }

  @Override
  public Map<String, PortfolioElementModel> getPortfolioElements() {
    return this.portfolioElementHashMap;
  }

  @Override
  public void generateXML(String path) throws IOException,
          ParserConfigurationException, TransformerException {
    DataPersister persister = new XMLWriterImpl();
    persister.writePortfolioModel(path, this, "Inflexible");
  }

  /**
   * Builder helper class to build an object of Portfolio Model class.
   * It contains functionality to set different fields of Portfolio Model.
   */
  public static class PortfolioModelBuilder
          extends AbstractPortfolioBuilder<PortfolioModelBuilder> {
    /**
     * Add portfolio elements to a portfolio from a list.
     *
     * @param portfolioElementList list of all the portfolio elements to be added.
     * @param stockModel           set of all the stock models available.
     * @return the builder object after assigning all the portfolio elements to the
     *         portfolio hashmap.
     */
    public PortfolioModelBuilder addPortfolioList(List<PortfolioElementModel> portfolioElementList,
                                                  StockSetModel stockModel) {
      HashMap<String, PortfolioElementModel> portfolio = new HashMap<>();
      for (PortfolioElementModel portfolioElementModel : portfolioElementList) {
        String tickerName = portfolioElementModel.getStockModel().getStockTicker();
        TransactionModel transaction = TransactionModelImpl.getBuilder()
                .date(portfolioElementModel.getTransactionDate())
                .price(portfolioElementModel.getStockCurrentAvgPrice())
                .brokerCommission(0)
                .qty(portfolioElementModel.getStockTotalQuantity())
                .exchange(portfolioElementModel.getStockModel().getExchangeName())
                .build();
        if (portfolio.containsKey(tickerName)) {
          PortfolioElementModel existingElement =
                  portfolio.get(tickerName);
          existingElement.addTransaction(transaction);
          portfolio.put(tickerName, existingElement);
        } else {
          PortfolioElementModel newElement = PortfolioElementModelImpl
                  .getBuilder().stockModel(stockModel.getStock(tickerName))
                  .build();
          newElement.addTransaction(transaction);
          portfolio.put(tickerName, newElement);
        }
      }
      this.portfolioElementHashMap = portfolio;
      return this;
    }


    @Override
    public PortfolioModelBuilder readXML(String path, String portfolioName)
            throws XPathExpressionException, IOException,
            ParserConfigurationException, SAXException, ParseException {
      DataParser<PortfolioModelBuilder> dataParserPortfolioModel = new XMLParserImpl<>();
      return dataParserPortfolioModel.getPortfolioModelBuilder(path, portfolioName);
    }

    @Override
    public PortfolioModelBuilder buildPortfolioFromXML(String path, String portfolioName,
                                                       StockSetModel stockSetModel)
            throws XPathExpressionException, IOException, ParserConfigurationException,
            SAXException, ParseException, IllegalArgumentException, NullPointerException {
      DataParser<String> dataParser = new XMLParserImpl<>();
      SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
      this.portfolioName = portfolioName;
      List<String> attributeNames = Arrays.asList("StockTicker", "totalQuantity", "Date");
      List<List<String>> stocks = dataParser.getAllAttributes(path,
              "//PortfolioModel/PortfolioElementModel", attributeNames);
      for (List<String> stock : stocks) {
        stock.add("0");
        addElementToMap(stockSetModel, format, stock, this.portfolioElementHashMap);
      }
      checkShort(this.portfolioElementHashMap);
      return this;
    }

    /**
     * Build a User Model builder object to get Portfolio Model.
     *
     * @return a new Portfolio Model object with all portfolio
     *         elements, time stamps and portfolio name appended.
     */
    @Override
    public PortfolioModelImpl build() {
      return new PortfolioModelImpl(this.portfolioName,
              this.timestamp, this.portfolioElementHashMap);
    }

    @Override
    public PortfolioModelBuilder returnBuilder() {
      return this;
    }

  }
}
