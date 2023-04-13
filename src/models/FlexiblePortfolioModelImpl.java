package models;

import org.xml.sax.SAXException;

import java.io.IOException;
import java.sql.Timestamp;
import java.text.ParseException;
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
 * of portfolios. This class allows modification to an existing portfolio.
 * Stocks can be bought and sold in a particular portfolio.
 */
public class FlexiblePortfolioModelImpl extends AbstractPortfolioModel {
  /**
   * Constructor to assign values to various attributes of the Flexible Portfolio Model.
   *
   * @param portfolioName           name of the portfolio as a String.
   * @param timestampAdded          time when the portfolio was created.
   * @param portfolioElementHashMap map of all the portfolios and various elements they have.
   */
  public FlexiblePortfolioModelImpl(String portfolioName, Timestamp timestampAdded,
                                    Map<String, PortfolioElementModel> portfolioElementHashMap) {
    super(portfolioName, timestampAdded, portfolioElementHashMap);
  }

  /**
   * Static class to get a builder to build
   * Flexible Portfolio Model class.
   *
   * @return a new builder object for Flexible Portfolio Model.
   */
  public static FlexiblePortfolioModelBuilder getBuilder() {
    return new FlexiblePortfolioModelBuilder();
  }

  @Override
  public PortfolioModel getFlexiblePortfolio() {
    return this;
  }

  @Override
  public PortfolioModel getStrategyFlexiblePortfolio() {
    return null;
  }

  @Override
  public void generateXML(String path) throws IOException, ParserConfigurationException,
          TransformerException {
    DataPersister persister = new XMLWriterImpl();
    persister.writePortfolioModel(path, this, "Flexible");
  }

  @Override
  public void modifyPortfolio(StockModel stockModel, TransactionModel transaction)
          throws IllegalStateException {
    String stockSymbol = stockModel.getStockTicker();
    PortfolioElementModel portfolioElementModel = portfolioElementHashMap
            .getOrDefault(stockSymbol, PortfolioElementModelImpl
                    .getBuilder()
                    .stockModel(stockModel)
                    .build());
    portfolioElementModel.addTransaction(transaction);
    this.portfolioElementHashMap.put(stockModel.getStockTicker(), portfolioElementModel);
  }

  @Override
  public void addPortfolioElement(PortfolioElementModel portfolioElement)
          throws IllegalStateException {
    if (!portfolioElementHashMap.containsKey(portfolioElement.getStockModel()
            .getStockTicker())) {
      this.portfolioElementHashMap.put(portfolioElement.getStockModel()
              .getStockTicker(), portfolioElement);
    }
  }

  /**
   * Builder helper class to build an object of Flexible Portfolio Model class.
   * It contains functionality to set different fields of Flexible Portfolio Model.
   */
  public static class FlexiblePortfolioModelBuilder extends
          AbstractPortfolioBuilder<FlexiblePortfolioModelBuilder> {

    /**
     * Build a Portfolio Model builder object to get Flexible Portfolio Model.
     *
     * @return a new Flexible Portfolio Model object with all portfolio
     *         elements, time stamps and portfolio name appended.
     */
    @Override
    public FlexiblePortfolioModelImpl build() {
      return new FlexiblePortfolioModelImpl(this.portfolioName, this.timestamp,
              this.portfolioElementHashMap);
    }

    @Override
    public FlexiblePortfolioModelBuilder returnBuilder() {
      return this;
    }

    @Override
    public FlexiblePortfolioModelBuilder readXML(String path, String portfolioName)
            throws XPathExpressionException, IOException,
            ParserConfigurationException, SAXException, ParseException {
      DataParser<FlexiblePortfolioModelBuilder> dataParserFlexiblePortfolioModel
              = new XMLParserImpl<>();
      return dataParserFlexiblePortfolioModel.getFlexiblePortfolioModelBuilder(path, portfolioName);
    }
  }
}
