package models;

import java.io.IOException;
import java.util.Date;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

/**
 * This interface represents all the methods that can be executed on a transaction object.
 */
public interface TransactionModel {
  /**
   * Get the date on which transaction happened.
   *
   * @return a date object on when the transaction happened.
   */
  Date getTransactionDate();

  /**
   * Get the commission paid to broker on a transaction.
   *
   * @return the commission value as a double only.
   */
  double getBrokerCommission();

  /**
   * Get the stock quantity of a transaction.
   *
   * @return the quantity traded as a double only.
   */
  double getQty();

  /**
   * Get the price on which stock was traded in the transaction.
   *
   * @return the stock price as a double only.
   */
  double getPrice();

  /**
   * Get the stock exchange where the transaction occured.
   *
   * @return the exchange name as a string only.
   */
  String getExchange();

  String getTransactionSource();

  /**
   * Converts the TransactionModel to an XML file.
   * Saves the XML file in relative folder path specified.
   *
   * @param path relative path where the transaction Model is stored as XML.
   * @param id id of the transaction to be written as XML.
   * @throws IOException                  if not able to write the file.
   * @throws ParserConfigurationException if the input data is not correctly parsed.
   * @throws TransformerException         if the object cannot be
   *                                      converted to the specified XML type.
   */
  void generateXML(String path, int id) throws IOException,
          ParserConfigurationException, TransformerException;
}
