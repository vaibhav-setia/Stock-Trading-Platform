package utils;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.dom.DOMSource;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import models.DollarCostAveragingImpl;
import models.FlexiblePortfolioModelImpl;
import models.PortfolioElementModel;
import models.PortfolioElementModelImpl;
import models.PortfolioModel;
import models.PortfolioModelImpl;
import models.StockModel;
import models.StockModelImpl;
import models.StrategicalFlexiblePortfolioImpl;
import models.StrategyModel;
import models.TransactionModel;
import models.TransactionModelImpl;
import models.UserModel;
import models.UserModelImpl;
import models.UserSetModelImpl;

/**
 * This generic class implements the Data parser interface and all its functionalities.
 * T, here is a placeholder for the actual object type format in which the parsed xml
 * needs to be returned.
 *
 * @param <T> placeholder for the object type to be returned.
 */
public class XMLParserImpl<T> implements DataParser<T> {


  @Override
  public List<String> getIds(String xmlPath, String xPathRegex) throws IOException,
          XPathExpressionException, ParserConfigurationException, SAXException {
    InputStream in = new FileInputStream(xmlPath);
    return filterXML(in, xPathRegex);
  }

  @Override
  public String getAttribute(String xmlPath, String xPathRegex) throws IOException,
          ParserConfigurationException, SAXException, XPathExpressionException {
    InputStream in = new FileInputStream(xmlPath);
    Document doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(in);
    NodeList list = (NodeList) XPathFactory.newInstance().newXPath()
            .compile(xPathRegex).evaluate(doc, XPathConstants.NODESET);
    Node node = list.item(0);
    DOMSource source = new DOMSource(node);
    return source.getNode().getFirstChild().getNodeValue();
  }

  @Override
  public List<List<String>> getAllAttributes(String xmlPath, String xPathRegex,
                                             List<String> attributeNames) throws IOException,
          ParserConfigurationException, SAXException, XPathExpressionException {
    InputStream in = new FileInputStream(xmlPath);
    Document doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(in);
    List<List<String>> answer = new ArrayList<>();
    for (String attributeName : attributeNames) {
      NodeList nodes = (NodeList) XPathFactory.newInstance().newXPath()
              .compile(xPathRegex + "/" + attributeName + "/text()")
              .evaluate(doc, XPathConstants.NODESET);
      for (int i = 0; i < nodes.getLength(); i++) {
        if (i > answer.size() - 1) {
          answer.add(new ArrayList<>());
        }
        answer.get(i).add(nodes.item(i).getNodeValue());
      }
    }
    return answer;
  }

  @Override
  public T getTransactionBuilder(String path, int id) throws XPathExpressionException,
          IOException, ParserConfigurationException, SAXException, ParseException {
    DataParser<String> dataParser = new XMLParserImpl<>();
    DateFormat formatter = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzzz yyyy");

    TransactionModelImpl.TransactionBuilder transactionBuilder = TransactionModelImpl.getBuilder()
            .date(formatter.parse(dataParser.getAttribute(path + "//" + id + ".xml",
                    "//Transaction/transactionDate")))
            .brokerCommission(Double.parseDouble(dataParser
                    .getAttribute(path + "//" + id + ".xml",
                    "//Transaction/brokerCommission")))
            .qty(Double.parseDouble(dataParser.getAttribute(path + "//" + id + ".xml",
                    "//Transaction/qty")))
            .price(Double.parseDouble(dataParser.getAttribute(path + "//" + id + ".xml",
                    "//Transaction/price")))
            .exchange(dataParser.getAttribute(path + "//" + id + ".xml",
                    "//Transaction/exchange"))
            .transactionSource(dataParser.getAttribute(path + "//" + id + ".xml",
                    "//Transaction/transactionSource"));

    return (T) transactionBuilder;
  }

  @Override
  public T getUserSetModelBuilder(String path) throws XPathExpressionException,
          IOException, ParserConfigurationException, SAXException, ParseException {
    DataParser<String> dataParser = new XMLParserImpl<>();
    List<String> idList = dataParser.getIds(path + "//UserSetModel.xml",
            "//UserSetModel/UserModel");
    List<UserModel> userModelList = new ArrayList<>();
    for (String id : idList) {
      userModelList.add(UserModelImpl.getBuilder().readXML(path + "//" + id, id).build());
    }
    UserSetModelImpl.UserSetModelBuilder userSetModelBuilder = UserSetModelImpl.getBuilder()
            .userList(userModelList);
    return (T) userSetModelBuilder;
  }

  @Override
  public T getUserModelBuilder(String path, String id) throws XPathExpressionException,
          IOException, ParserConfigurationException, SAXException, ParseException {

    DataParser<String> dataParser = new XMLParserImpl<>();
    List<String> portfolioList = dataParser.getIds(path + "//" + id + ".xml",
            "//UserModel/Portfolio");
    int userId = Integer.parseInt(dataParser.getAttribute(path + "//" + id + ".xml",
            "//UserModel/userId"));
    String name = dataParser.getAttribute(path + "//" + id + ".xml",
            "//UserModel/name");

    DataParser<String> dataParser1 = new XMLParserImpl<>();
    Map<String, PortfolioModel> portfolioWallet = new HashMap<>();
    for (String portfolioName : portfolioList) {
      String pfolioType = dataParser1.getAttribute(path + "//"
                      + portfolioName + "//" + portfolioName + ".xml",
              "//PortfolioModel/portfolioType");
      if (pfolioType.equals("Inflexible")) {
        portfolioWallet.put(portfolioName,
                PortfolioModelImpl.getBuilder().readXML(path + "//" + portfolioName,
                        portfolioName).build());
      } else {
        portfolioWallet.put(portfolioName,
                new StrategicalFlexiblePortfolioImpl.StrategicalFlexiblePortfolioBuilder()
                        .readXML(path + "//" + portfolioName,
                        portfolioName).build());
      }
    }
    UserModelImpl.UserModelBuilder userModelBuilder = UserModelImpl.getBuilder()
            .userId(userId)
            .name(name)
            .portfolioWallet(portfolioWallet);
    return (T) userModelBuilder;
  }

  @Override
  public T getStockModelBuilder(String path, String ticker) throws XPathExpressionException,
          IOException, ParserConfigurationException, SAXException, ParseException {
    DataParser<String> dataParser = new XMLParserImpl<>();
    DateFormat formatter = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzzz yyyy");
    String stockTicker = dataParser.getAttribute(path + "//" + ticker + ".xml",
            "//StockModel/stockTicker");
    String stockName = dataParser.getAttribute(path + "//" + ticker + ".xml",
            "//StockModel/stockName");
    String exchangeName = dataParser.getAttribute(path + "//" + ticker + ".xml",
            "//StockModel/exchangeName");
    Date ipoDate = formatter.parse(dataParser.getAttribute(path + "//" + ticker + ".xml",
            "//StockModel/ipoDate"));

    StockModelImpl.StockModelBuilder stockModelBuilder = StockModelImpl.getBuilder()
            .stockName(stockName)
            .stockTicker(stockTicker)
            .exchangeName(exchangeName)
            .ipoDate(ipoDate);
    return (T) stockModelBuilder;
  }

  @Override
  public T getPortfolioModelBuilder(String path, String portfolioName)
          throws XPathExpressionException,
          IOException, ParserConfigurationException, SAXException, ParseException {
    DataParser<String> dataParser = new XMLParserImpl<>();
    List<String> tickerList = dataParser.getIds(path + "//" + portfolioName + ".xml",
            "//PortfolioModel/PortfolioElementModel");
    Map<String, PortfolioElementModel> portfolioElementHashMap = new HashMap<>();
    for (String ticker : tickerList) {
      portfolioElementHashMap.put(ticker, PortfolioElementModelImpl.getBuilder()
              .readXML(path + "//" + ticker, ticker).build());
    }

    PortfolioModelImpl.PortfolioModelBuilder portfolioModelBuilder = PortfolioModelImpl.getBuilder()
            .portfolioName(dataParser.getAttribute(path + "//" + portfolioName + ".xml",
                    "//PortfolioModel/portfolioName"))
            .portfolioHashMap(portfolioElementHashMap)
            .timestamp(Timestamp.valueOf(dataParser
                    .getAttribute(path + "//" + portfolioName + ".xml",
                            "//PortfolioModel/timestamp")));
    return (T) portfolioModelBuilder;
  }

  @Override
  public T getFlexiblePortfolioModelBuilder(String path, String portfolioName)
          throws XPathExpressionException,
          IOException, ParserConfigurationException, SAXException, ParseException {
    DataParser<String> dataParser = new XMLParserImpl<>();
    List<String> tickerList = dataParser.getIds(path + "//" + portfolioName + ".xml",
            "//PortfolioModel/PortfolioElementModel");
    Map<String, PortfolioElementModel> portfolioElementHashMap = new HashMap<>();
    for (String ticker : tickerList) {
      portfolioElementHashMap.put(ticker, PortfolioElementModelImpl.getBuilder()
              .readXML(path + "//" + ticker, ticker).build());
    }

    FlexiblePortfolioModelImpl.FlexiblePortfolioModelBuilder portfolioModelBuilder
            = FlexiblePortfolioModelImpl.getBuilder()
            .portfolioName(dataParser.getAttribute(path + "//" + portfolioName + ".xml",
                    "//PortfolioModel/portfolioName"))
            .portfolioHashMap(portfolioElementHashMap)
            .timestamp(Timestamp.valueOf(dataParser
                    .getAttribute(path + "//" + portfolioName + ".xml",
                            "//PortfolioModel/timestamp")));

    return (T) portfolioModelBuilder;
  }

  @Override
  public T getPortfolioElementModelBuilder(String path, String ticker)
          throws XPathExpressionException,
          IOException, ParserConfigurationException, SAXException, ParseException {
    DataParser<String> dataParser = new XMLParserImpl<>();
    DateFormat formatter = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzzz yyyy");
    Double avgPrice = Double.parseDouble(dataParser
            .getAttribute(path + "//" + ticker + ".xml",
            "//PortfolioElementModel/avgPrice"));
    Double totalQuantity = Double.parseDouble(dataParser
            .getAttribute(path + "//" + ticker + ".xml",
            "//PortfolioElementModel/totalQuantity"));
    String tickerId = dataParser.getIds(path + "//" + ticker + ".xml",
            "//PortfolioElementModel/StockModel").get(0);
    StockModel stockModel = StockModelImpl.getBuilder().readXML(path + "//" + tickerId,
                    tickerId)
            .build();
    Date transactionDate = formatter.parse(dataParser
            .getAttribute(path + "//" + ticker + ".xml",
            "//PortfolioElementModel/transactionDate"));
    List<String> transactionIds = dataParser.getIds(path + "//" + ticker + ".xml",
            "//PortfolioElementModel/Transaction");
    List<TransactionModel> transactionList = new ArrayList<>();
    for (String transactionId : transactionIds) {
      transactionList.add(TransactionModelImpl.getBuilder()
              .readXML(path + "//" + "Transactions",
              Integer.parseInt(transactionId)).build());
    }

    PortfolioElementModelImpl.PortfolioElementModelBuilder portfolioElementModelBuilder
            = PortfolioElementModelImpl
            .getBuilder()
            .stockModel(stockModel)
            .transactionList(transactionList)
            .transactionDate(transactionDate)
            .avgPrice(avgPrice)
            .totalQuantity(totalQuantity);
    return (T) portfolioElementModelBuilder;
  }

  @Override
  public T getStrategicalFlexiblePortfolioModelBuilder(String path, String portfolioName)
          throws XPathExpressionException, IOException, ParserConfigurationException,
          SAXException, ParseException {
    DataParser<String> dataParser = new XMLParserImpl<>();
    List<String> tickerList = dataParser.getIds(path + "//" + portfolioName + ".xml",
            "//PortfolioModel/PortfolioElementModel");
    Map<String, PortfolioElementModel> portfolioElementHashMap = new HashMap<>();
    for (String ticker : tickerList) {
      portfolioElementHashMap.put(ticker, PortfolioElementModelImpl.getBuilder()
              .readXML(path + "//" + ticker, ticker).build());
    }
    List<String> strategyList = dataParser.getIds(path + "//" + portfolioName + ".xml",
            "//PortfolioModel/StrategyModel");
    List<StrategyModel> strategyModelList = new ArrayList<>();
    for (String strategy : strategyList) {
      strategyModelList.add(DollarCostAveragingImpl.getBuilder().readXML(path + "//Strategies",
              strategy).build());
    }
    StrategicalFlexiblePortfolioImpl.StrategicalFlexiblePortfolioBuilder portfolioModelBuilder
            = new StrategicalFlexiblePortfolioImpl.StrategicalFlexiblePortfolioBuilder()
            .portfolioName(dataParser.getAttribute(path + "//" + portfolioName + ".xml",
                    "//PortfolioModel/portfolioName"))
            .portfolioHashMap(portfolioElementHashMap)
            .strategyList(strategyModelList)
            .timestamp(Timestamp.valueOf(dataParser
                    .getAttribute(path + "//" + portfolioName + ".xml",
                            "//PortfolioModel/timestamp")));
    return (T) portfolioModelBuilder;
  }

  @Override
  public T getDollarCostAveragingBuilder(String path, String strategyName)
          throws XPathExpressionException, IOException, ParserConfigurationException, SAXException,
          ParseException {
    DataParser<String> dataParser = new XMLParserImpl<>();
    DateFormat formatter = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzzz yyyy");
    Map<StockModel, Double> stockMapPercent = new HashMap<>();
    List<String> attribute = new ArrayList<>(Collections.singleton("StockPercent"));
    List<String> stockCent = dataParser.getIds(path + "//" + strategyName + ".xml",
            "//StrategyModel/StockPercent");
    List<List<String>> stockPercent = dataParser
            .getAllAttributes(path + "//" + strategyName + ".xml",
            "//StrategyModel/", attribute);
    for (int i = 0; i < stockCent.size(); i++) {
      stockMapPercent.put(StockModelImpl.getBuilder().readXML(path + "//StockModel",
                      stockCent.get(i)).build(),
               Double.parseDouble(stockPercent.get(i).get(0)));
    }

    String xmlEndDate = dataParser.getAttribute(path + "//" + strategyName + ".xml",
            "//StrategyModel/EndDate");
    Date endDate = xmlEndDate.equals("null") ? null : formatter.parse(xmlEndDate);

    DollarCostAveragingImpl.DollarCostAveragingBuilder dollarCostAveragingBuilder
            = DollarCostAveragingImpl.getBuilder()
            .strategyName(dataParser.getAttribute(path + "//" + strategyName + ".xml",
                    "//StrategyModel/StrategyName"))
            .amount(Double.valueOf(dataParser
                    .getAttribute(path + "//" + strategyName + ".xml",
                    "//StrategyModel/Amount")))
            .startDate(formatter.parse((dataParser
                    .getAttribute(path + "//" + strategyName + ".xml",
                    "//StrategyModel/StartDate"))))
            .endDate(endDate)
            .dateFrequency(Integer.parseInt((dataParser
                    .getAttribute(path + "//" + strategyName + ".xml",
                    "//StrategyModel/DateFrequency"))))
            .brokerCommission(Double.valueOf((dataParser
                    .getAttribute(path + "//" + strategyName + ".xml",
                    "//StrategyModel/BrokerCommission"))))
            .stockMapPercent(stockMapPercent);

    return (T) dollarCostAveragingBuilder;
  }


  /**
   * Private helper method to filter xml for a given regex pattern.
   *
   * @param in         input stream after reading the xml file.
   * @param xPathRegex the regex pattern to specify which tag ids to extract.
   * @return a list of T objects with each object storing the id of one tag.
   * @throws IOException                  if not able to read the XML file.
   * @throws XPathExpressionException     if the Xpath pattern to filter xml is incorrect.
   * @throws ParserConfigurationException if the xml parser is configured incorrectly.
   * @throws SAXException                 exception thrown if xml violates integrity constraints.
   */
  private List<String> filterXML(InputStream in, String xPathRegex) throws
          ParserConfigurationException, IOException, SAXException, XPathExpressionException {
    Document doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(in);
    NodeList list = (NodeList) XPathFactory.newInstance()
            .newXPath().compile(xPathRegex).evaluate(doc, XPathConstants.NODESET);
    List<String> answer = new ArrayList<>();
    for (int i = 0; i < list.getLength(); i++) {
      Node node = list.item(i);
      DOMSource source = new DOMSource(node);
      answer.add(source.getNode().getAttributes().item(0).getNodeValue());
    }
    return new ArrayList<>(answer);
  }
}
