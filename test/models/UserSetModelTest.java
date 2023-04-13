package models;

import org.junit.Before;
import org.junit.Test;
import org.xml.sax.SAXException;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.xpath.XPathExpressionException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Class to write Junit tests to verify correct working of UserSetModel.
 */
public class UserSetModelTest {
  @Before
  public void fileWriteUp() throws IOException {
    Files.createDirectories(Paths.get("TestingHelper//UserSetModel//1000//concrete"));
    Files.createDirectories(Paths.get("TestingHelper//UserSetModel//1000//concrete//AAL"));
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
    fw = new java.io.FileWriter("TestingHelper//UserSetModel//"
            + "1000//flexible//GOOG//GOOG.xml");
    fw.write(stockGOOG);
    fw.close();
    String stockAAL1 = "<StockModel>\n"
            + "<stockTicker>AAL</stockTicker>\n"
            + "<stockName>American Airlines Group Inc</stockName>\n"
            + "<exchangeName>NASDAQ</exchangeName>\n"
            + "<ipoDate>Tue Sep 27 00:00:00 EDT 2005</ipoDate>\n"
            + "</StockModel>";
    fw = new java.io.FileWriter("TestingHelper//UserSetModel//1000"
            + "//concrete//AAL//American Airlines Group Inc//American Airlines Group Inc.xml");
    fw.write(stockAAL1);
    fw.close();
    String stockGOOG1 = "<StockModel>\n"
            + "<stockTicker>GOOG</stockTicker>\n"
            + "<stockName>Alphabet Inc - Class C</stockName>\n"
            + "<exchangeName>NASDAQ</exchangeName>\n"
            + "<ipoDate>Thu Mar 27 00:00:00 EDT 2014</ipoDate>\n"
            + "</StockModel>";
    fw = new java.io.FileWriter("TestingHelper//UserSetModel//1000//"
            + "flexible//GOOG//Alphabet Inc - Class C//Alphabet Inc - Class C.xml");
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
    fw = new java.io.FileWriter("TestingHelper//UserSetModel//1000//"
            + "flexible//GOOG//Transactions//1.xml");
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
  public void testInitializeUserSetModelHasEmptyUserList() {
    UserSetModel userSetModel = new UserSetModelImpl(new ArrayList<>());
    assertEquals(userSetModel.getListSize(), 0);
  }

  @Test
  public void testGetListSizeIncreasesWhenUserIsAdded() {
    UserModel userModel = UserModelImpl.getBuilder()
            .userId(1234)
            .name("Test user")
            .portfolioWallet(new HashMap<>())
            .build();
    UserSetModel userSetModel = new UserSetModelImpl(new ArrayList<>());
    assertEquals(userSetModel.getListSize(), 0);
    userSetModel.addUser(userModel);
    assertEquals(userSetModel.getListSize(), 1);
  }

  @Test
  public void getUserListGivesCorrectListWhenNotEmpty() {
    UserModel userModel1 = UserModelImpl.getBuilder()
            .userId(1234)
            .name("Test user")
            .portfolioWallet(new HashMap<>())
            .build();
    UserModel userModel2 = UserModelImpl.getBuilder()
            .userId(4567)
            .name("Test user 2")
            .portfolioWallet(new HashMap<>())
            .build();

    List<UserModel> userModels = new ArrayList<>();
    userModels.add(userModel1);
    userModels.add(userModel2);

    UserSetModel userSetModel = new UserSetModelImpl(userModels);
    assertEquals(userSetModel.getUserList().get(0).getUserName(), "Test user");
    assertEquals(userSetModel.getUserList().get(0).getUserId(), 1234);
    assertEquals(userSetModel.getUserList().get(1).getUserName(), "Test user 2");
    assertEquals(userSetModel.getUserList().get(1).getUserId(), 4567);
    assertEquals(userSetModel.getListSize(), 2);
  }

  @Test
  public void getUserListGivesCorrectListWhenEmpty() {
    List<UserModel> userModels = new ArrayList<>();
    UserSetModel userSetModel = new UserSetModelImpl(userModels);
    assertEquals(userSetModel.getUserList().size(), 0);
  }

  @Test
  public void testGenerateXML() throws IOException, ParserConfigurationException,
          TransformerException, XPathExpressionException, ParseException, SAXException {
    UserSetModel userSetModel = UserSetModelImpl.getBuilder().build()
            .loadUserSetFromDatabase("TestingHelper//UserSetModel");
    userSetModel.generateXML("TestingHelper//Write//UserSetModel");
    File file = new File("TestingHelper//Write//UserSetModel");
    assertTrue(file.exists());
  }

  @Test
  public void testLoadUserSetFromDatabase() throws XPathExpressionException,
          IOException, ParserConfigurationException, ParseException, SAXException {
    UserSetModel userSetModel = UserSetModelImpl.getBuilder().build()
            .loadUserSetFromDatabase("TestingHelper//UserSetModel");
    assertEquals(1, userSetModel.getListSize());
    assertEquals(1000, userSetModel.getUserList().get(0).getUserId());
    assertEquals("Vaibhav", userSetModel.getUserList().get(0).getUserName());
    assertEquals("concrete", userSetModel.getUserList().get(0).getAllPortfolios()
            .get("concrete").getName());
    assertEquals("AAL", userSetModel.getUserList().get(0).getAllPortfolios()
            .get("concrete").getPortfolioElements().get("AAL").getStockModel()
            .getStockTicker());

  }

  @Test
  public void testUserSetModelBuilder() {
    List<UserModel> userModels = new ArrayList<>();
    userModels.add(UserModelImpl.getBuilder()
            .name("Test user")
            .userId(1234)
            .portfolioWallet(new HashMap<>())
            .build());

    userModels.add(UserModelImpl.getBuilder()
            .name("Test user 2")
            .userId(4567)
            .portfolioWallet(new HashMap<>())
            .build());

    UserSetModel userSetModel = UserSetModelImpl.getBuilder()
            .userList(userModels)
            .build();

    assertEquals(userSetModel.getListSize(), 2);
    assertEquals(userSetModel.getUserList().get(0).getUserName(), "Test user");
    assertEquals(userSetModel.getUserList().get(0).getUserId(), 1234);
    assertEquals(userSetModel.getUserList().get(1).getUserName(), "Test user 2");
    assertEquals(userSetModel.getUserList().get(1).getUserId(), 4567);
    assertEquals(userSetModel.getUserList().size(), 2);
  }

}
