package models;

import org.junit.Before;
import org.junit.Test;
import org.xml.sax.SAXException;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.ParseException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.xpath.XPathExpressionException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Class to write Junit tests to verify correct working of UserModel.
 */
public class UserModelTest {
  @Before
  public void fileWriteUp() throws IOException {
    Files.createDirectories(Paths.get("TestingHelper//UserSetModel//1000//concrete"));
    Files.createDirectories(Paths
            .get("TestingHelper//UserSetModel//1000//concrete//AAL"));
    Files.createDirectories(Paths
            .get("TestingHelper//UserSetModel//1000//flexible"));
    Files.createDirectories(Paths
            .get("TestingHelper//UserSetModel//1000//flexible//GOOG"));
    Files.createDirectories(Paths
            .get("TestingHelper//UserSetModel//1000//flexible//GOOG//Transactions"));
    Files.createDirectories(Paths
            .get("TestingHelper//UserSetModel//1000//concrete//AAL//Transactions"));
    Files.createDirectories(Paths
            .get("TestingHelper//UserSetModel//1000//concrete//AAL//American Airlines Group Inc"));
    Files.createDirectories(Paths
            .get("TestingHelper//UserSetModel//1000//flexible//GOOG//Alphabet Inc - Class C"));
    String userSetModel = "<UserSetModel>\n"
            + "<UserModel id=\"1000\"/>\n"
            + "</UserSetModel>";
    java.io.FileWriter fw = new java.io.FileWriter("TestingHelper//UserSetModel.xml");
    fw.write(userSetModel);
    fw.close();
    String concrete = "<PortfolioModel>\n"
            + "<portfolioName>concrete</portfolioName>\n"
            + "<portfolioType>Inflexible</portfolioType>\n"
            + "<timestamp>2022-11-12 05:38:54.164</timestamp>\n"
            + "<PortfolioElementModel id=\"AAL\"/>\n"
            + "</PortfolioModel>";
    fw = new java.io.FileWriter("TestingHelper//UserSetModel//1000//concrete//concrete.xml");
    fw.write(concrete);
    fw.close();
    String flexible = "<PortfolioModel>\n"
            + "<portfolioName>flexible</portfolioName>\n"
            + "<portfolioType>Flexible</portfolioType>\n"
            + "<timestamp>2022-11-12 05:38:03.911</timestamp>\n"
            + "<PortfolioElementModel id=\"GOOG\"/>\n"
            + "</PortfolioModel>";
    fw = new java.io.FileWriter("TestingHelper//UserSetModel//1000//flexible//flexible.xml");
    fw.write(flexible);
    fw.close();
    String stockAAL = "<PortfolioElementModel>\n"
            + "<avgPrice>19.53</avgPrice>\n"
            + "<totalQuantity>44.0</totalQuantity>\n"
            + "<transactionDate>Tue Oct 19 00:00:00 EDT 2021</transactionDate>\n"
            + "<StockModel id=\"American Airlines Group Inc\"/>\n"
            + "<Transaction id=\"1\"/>\n"
            + "</PortfolioElementModel>";
    fw = new java.io.FileWriter("TestingHelper//UserSetModel//1000//concrete//AAL//AAL.xml");
    fw.write(stockAAL);
    fw.close();
    String stockGOOG = "<PortfolioElementModel>\n"
            + "<avgPrice>2758.0</avgPrice>\n"
            + "<totalQuantity>22.0</totalQuantity>\n"
            + "<transactionDate>Wed Oct 13 00:00:00 EDT 2021</transactionDate>\n"
            + "<StockModel id=\"Alphabet Inc - Class C\"/>\n"
            + "<Transaction id=\"1\"/>\n"
            + "</PortfolioElementModel>";
    fw = new java.io.FileWriter("TestingHelper//UserSetModel//1000//flexible//GOOG//GOOG.xml");
    fw.write(stockGOOG);
    fw.close();
    String stockAAL1 = "<StockModel>\n"
            + "<stockTicker>AAL</stockTicker>\n"
            + "<stockName>American Airlines Group Inc</stockName>\n"
            + "<exchangeName>NASDAQ</exchangeName>\n"
            + "<ipoDate>Tue Sep 27 00:00:00 EDT 2005</ipoDate>\n"
            + "</StockModel>";
    fw = new java.io.FileWriter("TestingHelper//UserSetModel//1000//"
            + "concrete//AAL//American Airlines Group Inc//American Airlines Group Inc.xml");
    fw.write(stockAAL1);
    fw.close();
    String stockGOOG1 = "<StockModel>\n"
            + "<stockTicker>GOOG</stockTicker>\n"
            + "<stockName>Alphabet Inc - Class C</stockName>\n"
            + "<exchangeName>NASDAQ</exchangeName>\n"
            + "<ipoDate>Thu Mar 27 00:00:00 EDT 2014</ipoDate>\n"
            + "</StockModel>";
    fw = new java.io.FileWriter("TestingHelper//UserSetModel//1000//flexible"
            + "//GOOG//Alphabet Inc - Class C//Alphabet Inc - Class C.xml");
    fw.write(stockGOOG1);
    fw.close();
    String stockGOOG2 = "<Transaction>\n"
            + "<transactionDate>Wed Oct 13 00:00:00 EDT 2021</transactionDate>\n"
            + "<brokerCommission>12.0</brokerCommission>\n"
            + "<qty>22.0</qty>\n"
            + "<price>2758.0</price>\n"
            + "<transactionSource>Manual</transactionSource>\n"
            + "<exchange>NASDAQ</exchange>\n"
            + "</Transaction>";
    fw = new java.io.FileWriter("TestingHelper//UserSetModel//"
            + "1000//flexible//GOOG//Transactions//1.xml");
    fw.write(stockGOOG2);
    fw.close();
    String stockAAL2 = "<Transaction>\n"
            + "<transactionDate>Tue Oct 19 00:00:00 EDT 2021</transactionDate>\n"
            + "<brokerCommission>0.0</brokerCommission>\n"
            + "<qty>44.0</qty>\n"
            + "<price>19.53</price>\n"
            + "<transactionSource>Manual</transactionSource>\n"
            + "<exchange>NASDAQ</exchange>\n"
            + "</Transaction>";
    fw = new java.io.FileWriter("TestingHelper//UserSetModel//"
            + "1000//concrete//AAL//Transactions//1.xml");
    fw.write(stockAAL2);
    fw.close();
  }

  @Test
  public void testReadXML() throws XPathExpressionException, IOException,
          ParserConfigurationException, ParseException, SAXException {
    UserModel userModel = UserModelImpl.getBuilder()
            .readXML("TestingHelper//UserSetModel//1000",
                    "1000").build();
    assertEquals(1000, userModel.getUserId());
    assertEquals("Vaibhav", userModel.getUserName());
    assertEquals("concrete", userModel.getPortfolio("concrete").getName());
  }

  @Test
  public void testGenerateXML() throws IOException, ParserConfigurationException,
          TransformerException, XPathExpressionException, ParseException, SAXException {
    UserModel userModel = UserModelImpl.getBuilder()
            .readXML("TestingHelper//UserSetModel//1000",
                    "1000").build();
    userModel.generateXML("TestingHelper//Write//UserModel");
    File file = new File("TestingHelper//Write//UserModel");
    assertTrue(file.exists());
  }
}
