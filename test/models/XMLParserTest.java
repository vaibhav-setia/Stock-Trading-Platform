package models;

import org.junit.Before;
import org.junit.Test;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPathExpressionException;

import utils.DataParser;
import utils.XMLParserImpl;

import static org.junit.Assert.assertEquals;

/**
 * Unit test class to verify correct working of XML Parser class.
 */
public class XMLParserTest {

  @Before
  public void createFile() throws IOException {
    String xmlParserTest = "<PortfolioModel>\n"
            + "<StockTicker>GOOG</StockTicker>\n"
            + "<totalQuantity>1000</totalQuantity>\n"
            + "<Date>2022-10-27</Date>\n"
            + "<UserSetModel>\n"
            + "<UserModel id=\"1000\"/>\n"
            + "<UserModel id=\"1001\"/>\n"
            + "<UserModel id=\"1002\"/>\n"
            + "</UserSetModel>\n"
            + "</PortfolioModel>";
    java.io.FileWriter fw = new java.io.FileWriter("TestingHelper//xmlParserTest.xml");
    fw.write(xmlParserTest);
    fw.close();
  }

  @Test
  public void testGetAttributes() throws XPathExpressionException,
          IOException, ParserConfigurationException, SAXException {
    DataParser<String> dataParser = new XMLParserImpl<>();
    String actual = dataParser.getAttribute("TestingHelper/xmlParserTest.xml",
            "//PortfolioModel/StockTicker");
    assertEquals("GOOG", actual);
  }

  @Test
  public void testGetAllAttributes() throws XPathExpressionException,
          IOException, ParserConfigurationException, SAXException {
    DataParser<String> dataParser = new XMLParserImpl<>();
    List<List<String>> actual = dataParser
            .getAllAttributes("TestingHelper/xmlParserTest.xml",
                    "//PortfolioModel", Arrays.asList("totalQuantity", "StockTicker"));
    for (List<String> strings : actual) {
      assertEquals("GOOG", strings.get(1));
      assertEquals("1000", strings.get(0));
    }
  }

  @Test
  public void testGetIds() throws XPathExpressionException, IOException,
          ParserConfigurationException, SAXException {
    DataParser<String> dataParser = new XMLParserImpl<>();
    List<String> actual = dataParser.getIds("TestingHelper/xmlParserTest.xml",
            "//UserSetModel/UserModel");
    assertEquals("1000", actual.get(0));
    assertEquals("1001", actual.get(1));
    assertEquals("1002", actual.get(2));

  }

}
