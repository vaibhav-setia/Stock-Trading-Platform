package models;

import java.io.IOException;
import java.util.Date;
import java.util.Map;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

/**
 * This interface contains all the operations that can be
 * applied on long term investing strategies.
 */
public interface StrategyModel {

  String getStrategyName();

  int getDateFrequency();

  Date getStartDate();

  Date getEndDate();

  Map<StockModel, Double> getStockMapPercent();

  double getBrokerCommission();

  double getAmount();

  void generateXML(String path) throws IOException, ParserConfigurationException,
          TransformerException;

  void run(PortfolioModel portfolioModel);
}
