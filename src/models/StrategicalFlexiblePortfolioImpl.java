package models;

import org.xml.sax.SAXException;

import java.io.IOException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.util.ArrayList;
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
 * by the stock ticker symbol. It extends the flexible portfolio model
 * which contains various common functionalities across flexible types
 * of portfolios. This class allows modification to an existing portfolio.
 * Stocks can be bought and sold in a particular portfolio. In addition it also
 * allows various strategies to be applied to this class
 */
public class StrategicalFlexiblePortfolioImpl extends FlexiblePortfolioModelImpl {

  private final List<StrategyModel> strategyList;

  public StrategicalFlexiblePortfolioImpl(String portfolioName,
                                          Timestamp timestampAdded, Map<String,
          PortfolioElementModel> portfolioElementHashMap, List<StrategyModel> strategyList) {
    super(portfolioName, timestampAdded, portfolioElementHashMap);
    this.strategyList = strategyList;
  }

  @Override
  public List<StrategyModel> getAllStrategies() throws UnsupportedOperationException {
    return this.strategyList;
  }

  @Override
  public void addStrategy(StrategyModel strategyModel) {
    this.strategyList.add(strategyModel);
  }


  @Override
  public PortfolioModel getStrategyFlexiblePortfolio() {
    return this;
  }

  @Override
  public void runAllStrategies() throws UnsupportedOperationException {
    for (StrategyModel strategyModel : strategyList) {
      strategyModel.run(this);
    }
  }

  @Override
  public void generateXML(String path) throws IOException,
          ParserConfigurationException, TransformerException {
    DataPersister persister = new XMLWriterImpl();
    persister.writePortfolioModel(path, this, "Strategic Flexible");
  }

  /**
   * Builder object to construct a strategical model object.
   * It extends the abstract builder class and implements all the
   * required functionality on a portfolio builder.
   */
  public static class StrategicalFlexiblePortfolioBuilder
          extends AbstractPortfolioBuilder<StrategicalFlexiblePortfolioBuilder> {

    private List<StrategyModel> strategyList;

    public StrategicalFlexiblePortfolioBuilder() {
      super();
      this.strategyList = new ArrayList<>();
    }

    @Override
    public StrategicalFlexiblePortfolioBuilder strategyList(List<StrategyModel> strategyList) {
      this.strategyList = strategyList;
      return this;
    }

    @Override
    public StrategicalFlexiblePortfolioBuilder returnBuilder() {
      return this;
    }

    @Override
    public StrategicalFlexiblePortfolioImpl build() {
      return new StrategicalFlexiblePortfolioImpl(this.portfolioName, this.timestamp,
              this.portfolioElementHashMap, this.strategyList);
    }

    @Override
    public StrategicalFlexiblePortfolioBuilder readXML(String path, String portfolioName)
            throws XPathExpressionException, IOException,
            ParserConfigurationException, SAXException, ParseException {
      DataParser<StrategicalFlexiblePortfolioBuilder> dataParserStrategicalFlexiblePortfolioModel
              = new XMLParserImpl<>();
      return dataParserStrategicalFlexiblePortfolioModel
              .getStrategicalFlexiblePortfolioModelBuilder(path, portfolioName);
    }
  }


}
