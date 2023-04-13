package utils;

import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import models.PortfolioElementModel;
import models.PortfolioModel;
import models.StockDataModel;
import models.StockModel;
import models.StrategyModel;
import models.TransactionModel;
import models.UserModel;
import models.UserSetModel;

/**
 * This class implements the DataPersister class and all its methods.
 * This class is used to persis all the obejcts in an XML format.
 */
public class XMLWriterImpl implements DataPersister {
  /**
   * Placeholder method to write persist stock tickers in xml format if required in future.
   *
   * @param stockModelList list of all the stock models to be written.
   * @param path           path where the object needs be persisted.
   * @throws FileNotFoundException exception if the given directory path is not found.
   */
  @Override
  public void writeTickersData(List<StockModel> stockModelList,
                               String path) throws FileNotFoundException {
    return ;
  }

  /**
   * Placeholder method to write persist stock data models in xml format if required in future.
   *
   * @param stockDataModelList list of all the stock data models to be written.
   * @param path               path where the object needs be persisted.
   * @throws FileNotFoundException exception if the given directory path is not found.
   */
  @Override
  public void writeTimeSeriesDataForTicker(List<StockDataModel> stockDataModelList,
                                           String path) throws FileNotFoundException {
    return ;
  }

  @Override
  public void writePortfolioElement(String path,
                                    PortfolioElementModel portfolioElementModel)
          throws IOException, ParserConfigurationException, TransformerException {
    Files.createDirectories(Paths.get(path));
    Document document = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
    Element portfolioElementModelXML = document.createElement("PortfolioElementModel");
    document.appendChild(portfolioElementModelXML);
    Element avgPrice = document.createElement("avgPrice");
    avgPrice.appendChild(document.createTextNode(String.valueOf(portfolioElementModel
            .getStockCurrentAvgPrice())));
    portfolioElementModelXML.appendChild(avgPrice);
    Element totalQuantity = document.createElement("totalQuantity");
    totalQuantity.appendChild(document.createTextNode(String.valueOf(portfolioElementModel
            .getStockTotalQuantity())));
    portfolioElementModelXML.appendChild(totalQuantity);
    Element transactionDate = document.createElement("transactionDate");
    transactionDate.appendChild(document.createTextNode(String.valueOf(portfolioElementModel
            .getTransactionDate())));
    portfolioElementModelXML.appendChild(transactionDate);
    Element stockModel = document.createElement("StockModel");
    Attr attr = document.createAttribute("id");
    attr.setValue(portfolioElementModel.getStockModel().getStockName());
    stockModel.setAttributeNode(attr);
    portfolioElementModelXML.appendChild(stockModel);
    portfolioElementModel.getStockModel().generateXML(path + "//" + portfolioElementModel
            .getStockModel().getStockName() + "//");
    for (int i = 0; i < portfolioElementModel.getAllTransactions().size(); i++) {
      Element transaction = document.createElement("Transaction");
      Attr transactionAttr = document.createAttribute("id");
      transactionAttr.setValue(String.valueOf(i + 1));
      transaction.setAttributeNode(transactionAttr);
      portfolioElementModelXML.appendChild(transaction);
      portfolioElementModel.getAllTransactions().get(i).generateXML(path + "//"
              + "Transactions" + "//", i + 1);
    }
    TransformerFactory.newInstance().newTransformer().transform(new DOMSource(document),
            new StreamResult(new File(path + "//" + portfolioElementModel
                    .getStockModel().getStockTicker() + ".xml")));
  }

  @Override
  public void writePortfolioModel(String path, PortfolioModel portfolioModel,
                                  String typeOfPortfolio) throws IOException,
          ParserConfigurationException, TransformerException {
    Files.createDirectories(Paths.get(path));
    Document document = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
    Element portfolioModelXML = document.createElement("PortfolioModel");
    document.appendChild(portfolioModelXML);
    Element portfolioName = document.createElement("portfolioName");
    portfolioName.appendChild(document.createTextNode(String.valueOf(portfolioModel.getName())));
    portfolioModelXML.appendChild(portfolioName);
    Element portfolioType = document.createElement("portfolioType");
    portfolioType.appendChild(document.createTextNode(typeOfPortfolio));
    portfolioModelXML.appendChild(portfolioType);
    Element timestamp = document.createElement("timestamp");
    timestamp.appendChild(document.createTextNode(String.valueOf(portfolioModel.getTimestamp())));
    portfolioModelXML.appendChild(timestamp);
    for (String ticker : portfolioModel.getPortfolioElements().keySet()) {
      Element portfolioElementModelXML = document.createElement("PortfolioElementModel");
      portfolioModelXML.appendChild(portfolioElementModelXML);
      Attr attr = document.createAttribute("id");
      attr.setValue(ticker);
      portfolioElementModelXML.setAttributeNode(attr);
      PortfolioElementModel portfolioElementModel = portfolioModel.getPortfolioElements()
              .get(ticker);
      portfolioElementModel.generateXML(path + "//" + ticker + "//");
    }

    if (typeOfPortfolio.equals("Strategic Flexible")) {
      List<StrategyModel> strategyModelList = portfolioModel.getAllStrategies();
      for (StrategyModel strategyModel : strategyModelList) {
        Element strategyModelXML = document.createElement("StrategyModel");
        portfolioModelXML.appendChild(strategyModelXML);
        Attr attr = document.createAttribute("name");
        attr.setValue(strategyModel.getStrategyName());
        strategyModelXML.setAttributeNode(attr);
        strategyModel.generateXML(path + "//Strategies");
      }
    }
    TransformerFactory.newInstance().newTransformer().transform(new DOMSource(document),
            new StreamResult(new File(path + "//" + portfolioModel.getName() + ".xml")));
  }

  @Override
  public void writeStockModel(String path, StockModel stockModel) throws IOException,
          ParserConfigurationException, TransformerException {
    Files.createDirectories(Paths.get(path));
    Document document = DocumentBuilderFactory.newInstance().newDocumentBuilder()
            .newDocument();
    Element stockModelXML = document.createElement("StockModel");
    document.appendChild(stockModelXML);
    Element stockTicker = document.createElement("stockTicker");
    stockTicker.appendChild(document.createTextNode(String.valueOf(stockModel
            .getStockTicker())));
    stockModelXML.appendChild(stockTicker);
    Element stockName = document.createElement("stockName");
    stockName.appendChild(document.createTextNode(String.valueOf(stockModel
            .getStockName())));
    stockModelXML.appendChild(stockName);
    Element exchangeName = document.createElement("exchangeName");
    exchangeName.appendChild(document.createTextNode(String.valueOf(stockModel
            .getExchangeName())));
    stockModelXML.appendChild(exchangeName);
    Element ipoDate = document.createElement("ipoDate");
    ipoDate.appendChild(document.createTextNode(String.valueOf(stockModel.getIpoDate())));
    stockModelXML.appendChild(ipoDate);
    TransformerFactory.newInstance().newTransformer().transform(new DOMSource(document),
            new StreamResult(new File(path + "//" + stockModel.getStockName() + ".xml")));
  }

  @Override
  public void writeTransactionModel(String path, int id, TransactionModel transaction)
          throws IOException, ParserConfigurationException, TransformerException {
    Files.createDirectories(Paths.get(path));
    Document document = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
    Element portfolioElementTransaction = document.createElement("Transaction");
    document.appendChild(portfolioElementTransaction);
    Element transactionDate = document.createElement("transactionDate");
    transactionDate.appendChild(document.createTextNode(
            String.valueOf(transaction.getTransactionDate())));
    portfolioElementTransaction.appendChild(transactionDate);
    Element brokerCommission = document.createElement("brokerCommission");
    brokerCommission.appendChild(document.createTextNode(
            String.valueOf(transaction.getBrokerCommission())));
    portfolioElementTransaction.appendChild(brokerCommission);
    Element transactionSource = document.createElement("transactionSource");
    transactionSource.appendChild(document.createTextNode(
            String.valueOf(transaction.getTransactionSource())));
    portfolioElementTransaction.appendChild(transactionSource);
    Element qty = document.createElement("qty");
    qty.appendChild(document.createTextNode(String.valueOf(transaction.getQty())));
    portfolioElementTransaction.appendChild(qty);
    Element price = document.createElement("price");
    price.appendChild(document.createTextNode(String.valueOf(transaction.getPrice())));
    portfolioElementTransaction.appendChild(price);
    Element exchange = document.createElement("exchange");
    exchange.appendChild(document.createTextNode(String.valueOf(transaction.getExchange())));
    portfolioElementTransaction.appendChild(exchange);
    TransformerFactory.newInstance().newTransformer().transform(new DOMSource(document),
            new StreamResult(new File(path + "//" + id + ".xml")));
  }

  @Override
  public void writeUserModel(String path, UserModel userModel) throws IOException,
          ParserConfigurationException, TransformerException {
    Files.createDirectories(Paths.get(path));
    Document document = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
    Element userModelXML = document.createElement("UserModel");
    document.appendChild(userModelXML);
    Element userId = document.createElement("userId");
    userId.appendChild(document.createTextNode(String.valueOf(userModel.getUserId())));
    userModelXML.appendChild(userId);
    Element name = document.createElement("name");
    name.appendChild(document.createTextNode(String.valueOf(userModel.getUserName())));
    userModelXML.appendChild(name);

    for (String portfolioName : userModel.getAllPortfolios().keySet()) {
      Element portfolioXML = document.createElement("Portfolio");
      userModelXML.appendChild(portfolioXML);
      Attr attr = document.createAttribute("id");
      attr.setValue(portfolioName);
      portfolioXML.setAttributeNode(attr);
      PortfolioModel portfolioModel = userModel.getAllPortfolios().get(portfolioName);
      portfolioModel.generateXML(path + "//" + portfolioName + "//");
    }

    TransformerFactory.newInstance().newTransformer().transform(new DOMSource(document),
            new StreamResult(new File(path + "//" + userModel.getUserId() + ".xml")));
  }

  @Override
  public void writeUserSetModel(String root, UserSetModel userSetModel) throws IOException,
          ParserConfigurationException, TransformerException {
    Files.createDirectories(Paths.get(root));
    Document document = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
    Element userSetModelXML = document.createElement("UserSetModel");
    document.appendChild(userSetModelXML);
    for (int i = 0; i < userSetModel.getUserList().size(); i++) {
      Element userModelXML = document.createElement("UserModel");
      userSetModelXML.appendChild(userModelXML);
      Attr attr = document.createAttribute("id");
      attr.setValue(String.valueOf(i + 1000));
      userModelXML.setAttributeNode(attr);
      UserModel userModel = userSetModel.getUserList().get(i);
      userModel.generateXML(root + "//" + (i + 1000) + "//");
    }
    TransformerFactory.newInstance().newTransformer().transform(new DOMSource(document),
            new StreamResult(new File(root + "//UserSetModel.xml")));
  }

  @Override
  public void writeStrategyModel(String path, StrategyModel strategyModel)
          throws IOException, ParserConfigurationException, TransformerException {
    Files.createDirectories(Paths.get(path));
    Document document = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
    Element strategyModelXML = document.createElement("StrategyModel");
    document.appendChild(strategyModelXML);
    Element strategyName = document.createElement("StrategyName");
    strategyName.appendChild(document.createTextNode(
            String.valueOf(strategyModel.getStrategyName())));
    strategyModelXML.appendChild(strategyName);
    Element amount = document.createElement("Amount");
    amount.appendChild(document.createTextNode(
            String.valueOf(strategyModel.getAmount())));
    strategyModelXML.appendChild(amount);
    Element startDate = document.createElement("StartDate");
    startDate.appendChild(document.createTextNode(String.valueOf(strategyModel.getStartDate())));
    strategyModelXML.appendChild(startDate);
    Element endDate = document.createElement("EndDate");
    endDate.appendChild(document.createTextNode(String.valueOf(strategyModel.getEndDate())));
    strategyModelXML.appendChild(endDate);
    Element dateFrequency = document.createElement("DateFrequency");
    dateFrequency.appendChild(document.createTextNode(String.valueOf(strategyModel
            .getDateFrequency())));
    strategyModelXML.appendChild(dateFrequency);
    Element brokerCommission = document.createElement("BrokerCommission");
    brokerCommission.appendChild(document.createTextNode(
            String.valueOf(strategyModel.getBrokerCommission())));
    strategyModelXML.appendChild(brokerCommission);
    for (StockModel stockModel : strategyModel.getStockMapPercent().keySet()) {
      Element stockPercent = document.createElement("StockPercent");
      stockPercent.appendChild(document.createTextNode(String.valueOf(strategyModel
              .getStockMapPercent().get(stockModel))));
      Attr attr = document.createAttribute("stockName");
      attr.setValue(String.valueOf(stockModel.getStockName()));
      stockPercent.setAttributeNode(attr);
      strategyModelXML.appendChild(stockPercent);
      stockModel.generateXML(path + "//StockModel");
    }
    TransformerFactory.newInstance().newTransformer().transform(new DOMSource(document),
            new StreamResult(new File(path + "//" + strategyModel
                    .getStrategyName() + ".xml")));
  }
}
