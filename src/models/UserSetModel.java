package models;

import org.xml.sax.SAXException;

import java.io.IOException;
import java.text.ParseException;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.xpath.XPathExpressionException;

/**
 * This interface represents the complete set of users
 * registered in the portfolio system. This interface
 * contains various operations that can be applied to a
 * collection of Users. Each user further has different
 * portfolios associated and other properties.
 */
public interface UserSetModel {

  /**
   * Inserts a new user to UserSetModel.
   *
   * @param userModel a userModel object to be added.
   */
  void addUser(UserModel userModel);

  /**
   * Gets the number of users created in the UserSetModel.
   *
   * @return number of users in the model list as integer only.
   */
  int getListSize();

  /**
   * Fetches all the users in the UserSetModel.
   *
   * @return list of all the users as {@link UserModel}class.
   */
  List<UserModel> getUserList();

  /**
   * Converts the UserSetModel to an XML file.
   * Saves the XML file in root folder specified.
   *
   * @param root folder where the UserSetModel is stored as XML.
   * @throws IOException                  if not able to write the file.
   * @throws ParserConfigurationException if the input data is not correctly parsed.
   * @throws TransformerException         if the object cannot be converted
   *                                      to the specified XML type.
   */
  void generateXML(String root) throws IOException, ParserConfigurationException,
          TransformerException;

  /**
   * Load users from the user set model xml file on startup. All the users
   * are iteratively read and stored as a list of {@link UserModel}.
   *
   * @param root root directory from where user set data is loaded on startup.
   * @return A user set model with all the users created in the stock system.
   * @throws XPathExpressionException     if the Xpath to filter xml is incorrect.
   * @throws IOException                  if xml is not correctly read from file.
   * @throws ParserConfigurationException if the xml parser is configured incorrectly.
   * @throws SAXException                 exception thrown if xml violates integrity constraints.
   * @throws ParseException               if xml string is not correctly parsed as per contract.
   */
  UserSetModel loadUserSetFromDatabase(String root) throws XPathExpressionException, IOException,
          ParserConfigurationException, SAXException, ParseException;

  void runAllStrategies();

}
