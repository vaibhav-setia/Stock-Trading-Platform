package utils;

import org.xml.sax.SAXException;

import java.io.IOException;
import java.text.ParseException;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPathExpressionException;

/**
 * This interface represents a generic data parser.
 * It has various methods to parse data in different ways/files and return
 * objects accordingly.
 *
 * @param <T> a placeholder for an actual object to which data will be extracted to.
 */
public interface DataParser<T> {

  /**
   * Get all the id tags of a data for a given regex
   * patter of data file.
   *
   * @param dataPath   the path where data file is stored.
   * @param xPathRegex the regex pattern to specify which tag ids to extract.
   * @return a list of T objects with each object storing the id of one tag.
   * @throws IOException                  if not able to read the data file.
   * @throws XPathExpressionException     if the Xpath pattern to filter data is incorrect.
   * @throws ParserConfigurationException if the data parser is configured incorrectly.
   * @throws SAXException                 exception thrown if data violates integrity constraints.
   */
  List<String> getIds(String dataPath, String xPathRegex) throws IOException,
          XPathExpressionException, ParserConfigurationException, SAXException;

  /**
   * Get the data value of a particular attribute of a data file.
   *
   * @param dataPath   the path where data file is stored.
   * @param xPathRegex the regex pattern to specify which attribute to extract.
   * @return ab object of type T having the data value of that data tag.
   * @throws IOException                  if not able to read the data file.
   * @throws XPathExpressionException     if the Xpath pattern to filter data is incorrect.
   * @throws ParserConfigurationException if the xml parser is configured incorrectly.
   * @throws SAXException                 exception thrown if data violates integrity constraints.
   */
  String getAttribute(String dataPath, String xPathRegex) throws IOException,
          ParserConfigurationException, SAXException, XPathExpressionException;

  /**
   * Get all the attributes sent in a list, for all the tags available in XML.
   *
   * @param xmlPath        the path where xml file is stored.
   * @param xPathRegex     the regex pattern to specify which parent attribute to extract.
   * @param attributeNames list of attributes whose values need to be stored.
   * @return A 2D list of T objects which contains all the attributes of each of the parent
   *         attribute present in the xml file.
   * @throws IOException                  if not able to read the data file.
   * @throws XPathExpressionException     if the Xpath pattern to filter xml is incorrect.
   * @throws ParserConfigurationException if the data parser is configured incorrectly.
   * @throws SAXException                 exception thrown if data violates integrity constraints.
   */
  List<List<String>> getAllAttributes(String xmlPath, String xPathRegex,
                                      List<String> attributeNames)
          throws IOException, ParserConfigurationException,
          SAXException, XPathExpressionException;

  /**
   * Read a file which contains transaction details and return as an appropriate
   * transaction object.
   *
   * @param path file path from here data needs to be read.
   * @param id   transaction document id.
   * @return appropriate object with all the transaction details.
   * @throws IOException                  if not able to read the data file.
   * @throws XPathExpressionException     if the Xpath pattern to filter xml is incorrect.
   * @throws ParserConfigurationException if the data parser is configured incorrectly.
   * @throws SAXException                 exception thrown if data violates integrity constraints.
   * @throws ParseException               exception if there is error parsing through the file.
   */
  T getTransactionBuilder(String path, int id) throws
          XPathExpressionException, IOException,
          ParserConfigurationException, SAXException, ParseException;

  /**
   * Read a file which contains transaction details and return as an appropriate
   * user set model object.
   *
   * @param path file path from here data needs to be read.
   * @return appropriate object with all the userSetModel details.
   * @throws IOException                  if not able to read the data file.
   * @throws XPathExpressionException     if the Xpath pattern to filter xml is incorrect.
   * @throws ParserConfigurationException if the data parser is configured incorrectly.
   * @throws SAXException                 exception thrown if data violates integrity constraints.
   * @throws ParseException               exception if there is error parsing through the file.
   */
  T getUserSetModelBuilder(String path) throws XPathExpressionException,
          IOException, ParserConfigurationException, SAXException, ParseException;

  /**
   * Read a file which contains transaction details and return as an appropriate
   * user model object.
   *
   * @param path file path from here data needs to be read.
   * @param id   user model id to build the details.
   * @return appropriate object with all the userModel details.
   * @throws IOException                  if not able to read the data file.
   * @throws XPathExpressionException     if the Xpath pattern to filter xml is incorrect.
   * @throws ParserConfigurationException if the data parser is configured incorrectly.
   * @throws SAXException                 exception thrown if data violates integrity constraints.
   * @throws ParseException               exception if there is error parsing through the file.
   */
  T getUserModelBuilder(String path, String id) throws XPathExpressionException,
          IOException, ParserConfigurationException, SAXException, ParseException;

  /**
   * Read a file which contains transaction details and return as an appropriate
   * stock model object.
   *
   * @param path   file path from here data needs to be read.
   * @param ticker stock ticker for which data needs to be formed.
   * @return appropriate object with all the stockModel details.
   * @throws IOException                  if not able to read the data file.
   * @throws XPathExpressionException     if the Xpath pattern to filter xml is incorrect.
   * @throws ParserConfigurationException if the data parser is configured incorrectly.
   * @throws SAXException                 exception thrown if data violates integrity constraints.
   * @throws ParseException               exception if there is error parsing through the file.
   */
  T getStockModelBuilder(String path, String ticker) throws XPathExpressionException,
          IOException, ParserConfigurationException, SAXException, ParseException;

  /**
   * Read a file which contains transaction details and return as an appropriate
   * portfolio model object.
   *
   * @param path          file path from here data needs to be read.
   * @param portfolioName portfolio name for which data needs to be formed.
   * @return appropriate object with all the portfolio details.
   * @throws IOException                  if not able to read the data file.
   * @throws XPathExpressionException     if the Xpath pattern to filter xml is incorrect.
   * @throws ParserConfigurationException if the data parser is configured incorrectly.
   * @throws SAXException                 exception thrown if data violates integrity constraints.
   * @throws ParseException               exception if there is error parsing through the file.
   */
  T getPortfolioModelBuilder(String path, String portfolioName) throws
          XPathExpressionException, IOException, ParserConfigurationException,
          SAXException, ParseException;

  /**
   * Read a file which contains transaction details and return as an appropriate
   * flexible portfolio model object.
   *
   * @param path          file path from here data needs to be read.
   * @param portfolioName portfolio name for which data needs to be formed.
   * @return appropriate object with all the flexible portfolio details.
   * @throws IOException                  if not able to read the data file.
   * @throws XPathExpressionException     if the Xpath pattern to filter xml is incorrect.
   * @throws ParserConfigurationException if the data parser is configured incorrectly.
   * @throws SAXException                 exception thrown if data violates integrity constraints.
   * @throws ParseException               exception if there is error parsing through the file.
   */
  T getFlexiblePortfolioModelBuilder(String path, String portfolioName) throws
          XPathExpressionException, IOException, ParserConfigurationException,
          SAXException, ParseException;

  /**
   * Read a file which contains transaction details and return as an appropriate
   * portfolio element model object.
   *
   * @param path   file path from here data needs to be read.
   * @param ticker stock ticker for which data needs to be formed.
   * @return appropriate object with all the element model details.
   * @throws IOException                  if not able to read the data file.
   * @throws XPathExpressionException     if the Xpath pattern to filter xml is incorrect.
   * @throws ParserConfigurationException if the data parser is configured incorrectly.
   * @throws SAXException                 exception thrown if data violates integrity constraints.
   * @throws ParseException               exception if there is error parsing through the file.
   */
  T getPortfolioElementModelBuilder(String path, String ticker) throws
          XPathExpressionException, IOException, ParserConfigurationException,
          SAXException, ParseException;

  T getStrategicalFlexiblePortfolioModelBuilder(String path, String portfolioName) throws
          XPathExpressionException, IOException, ParserConfigurationException,
          SAXException, ParseException;

  T getDollarCostAveragingBuilder(String path, String strategyName)
          throws XPathExpressionException, IOException, ParserConfigurationException,
          SAXException, ParseException;
}
