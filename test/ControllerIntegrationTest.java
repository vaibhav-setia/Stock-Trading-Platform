import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

import controller.Controller;
import controller.ControllerImpl;
import models.StockSetModel;
import models.StockSetModelImpl;
import models.UserSetModel;
import models.UserSetModelImpl;
import models.UserStockModel;
import models.UserStockModelImpl;
import views.TextBasedView;
import views.TextBasedViewImpl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

/**
 * Integration tests to verify
 * controller is properly integrated.
 */
public class ControllerIntegrationTest {
  private UserStockModel userStockModel;
  private TextBasedView textBasedView;
  private ByteArrayOutputStream bytes;
  private String rootDirectory;

  @Before
  public void initializeTest() throws IOException {
    bytes = new ByteArrayOutputStream();
    PrintStream out = new PrintStream(bytes);
    UserSetModel userSetModel = new UserSetModelImpl(new ArrayList<>());
    StockSetModel stockSetModel = new StockSetModelImpl(new HashMap<>());
    userStockModel = new UserStockModelImpl(userSetModel, stockSetModel);
    textBasedView = new TextBasedViewImpl(out, System.out);
    rootDirectory = "TestingHelper//TestUserSetModel";
    deleteFiles(Paths.get(rootDirectory).toFile());
    try {
      userSetModel.generateXML(rootDirectory);
    } catch (IOException | ParserConfigurationException | TransformerException e) {
      fail("Failed to initialize test");
    }
    fileWriteUp();
  }

  @After
  public void cleanupTest() {
    deleteFiles(Paths.get(rootDirectory).toFile());
  }

  private void deleteFiles(File file) {
    File[] list = file.listFiles();
    if (list != null) {
      for (File temp : list) {
        deleteFiles(temp);
      }
    }
    file.delete();
  }

  @Test
  public void testCreateNewUser() {
    String desiredOutput = "User create success\n";

    String input = "1"
            + "\nXYZ"
            + "\n8"
            + "\n3";

    InputStream in = new ByteArrayInputStream(input.getBytes());
    Controller controller = new ControllerImpl(this.userStockModel,
            this.textBasedView, in, rootDirectory);
    controller.start();
    assertEquals(desiredOutput, bytes.toString());
  }

  @Test
  public void testCreateNewUserWhenInvalidNameAdded() {
    String desiredOutput = "No special character allowed. "
            + "Please input only alphanumeric values and try again.\n"
            + "User create success\n";

    String input = "1"
            + "\n*(@)@"
            + "\nXYZ"
            + "\n8"
            + "\n3";

    InputStream in = new ByteArrayInputStream(input.getBytes());
    Controller controller = new ControllerImpl(this.userStockModel,
            this.textBasedView, in, rootDirectory);
    controller.start();
    assertEquals(desiredOutput, bytes.toString());
  }

  @Test
  public void testLoginUser() {
    String desiredOutput = "User create success\n"
            + "Log In Success\n";

    String input = "1"
            + "\nXYZ"
            + "\n8"
            + "\n2"
            + "\n1000"
            + "\n8"
            + "\n3";

    InputStream in = new ByteArrayInputStream(input.getBytes());
    Controller controller = new ControllerImpl(this.userStockModel,
            this.textBasedView, in, rootDirectory);
    controller.start();
    assertEquals(desiredOutput, bytes.toString());
  }

  @Test
  public void testLoginUserWhenNoUserPresent() {
    String desiredOutput = "No user found!\n"
            + "You might want to create a new user.\n";

    String input = "2"
            + "\n8"
            + "\n3";

    InputStream in = new ByteArrayInputStream(input.getBytes());
    Controller controller = new ControllerImpl(this.userStockModel,
            this.textBasedView, in, rootDirectory);
    controller.start();
    assertEquals(desiredOutput, bytes.toString());
  }

  @Test
  public void testLoginUserWhenIdNotPresent() {
    String desiredOutput = "User create success\n"
            + "No user found!\n"
            + "You might want to create a new user.\n"
            + "Log In Success\n";

    String input = "1"
            + "\nXYZ"
            + "\n8"
            + "\n2"
            + "\n1001"
            + "\n2"
            + "\n1000"
            + "\n8"
            + "\n3";

    InputStream in = new ByteArrayInputStream(input.getBytes());
    Controller controller = new ControllerImpl(this.userStockModel,
            this.textBasedView, in, rootDirectory);
    controller.start();
    assertEquals(desiredOutput, bytes.toString());
  }

  @Test
  public void addPortfolioManually() {
    String desiredOutput = "User create success\n"
            + "Portfolio 1 added successfully\n";

    String input = "1"
            + "\nXYZ"
            + "\n1"
            + "\n2"
            + "\nPortfolio 1"
            + "\n1"
            + "\n1"
            + "\nGOOG"
            + "\n2022-10-27"
            + "\n10"
            + "\n8"
            + "\n3";

    InputStream in = new ByteArrayInputStream(input.getBytes());
    Controller controller = new ControllerImpl(this.userStockModel,
            this.textBasedView, in, rootDirectory);
    controller.start();
    assertEquals(desiredOutput, bytes.toString());
  }

  @Test
  public void addMultipleStocksToPortfolioManually() {
    String desiredOutput = "User create success\n"
            + "Portfolio 1 added successfully\n";

    String input = "1"
            + "\nXYZ"
            + "\n1"
            + "\n2"
            + "\nPortfolio 1"
            + "\n1"
            + "\n2"
            + "\nGOOG"
            + "\n2022-10-27"
            + "\n10"
            + "\nAMZN"
            + "\n2022-10-27"
            + "\n10"
            + "\n8"
            + "\n3";

    InputStream in = new ByteArrayInputStream(input.getBytes());
    Controller controller = new ControllerImpl(this.userStockModel,
            this.textBasedView, in, rootDirectory);
    controller.start();
    assertEquals(desiredOutput, bytes.toString());
  }

  @Test
  public void addZeroStocksToPortfolioManually() {
    String desiredOutput = "User create success\n"
            + "Portfolio 1 added successfully\n";

    String input = "1"
            + "\nXYZ"
            + "\n1"
            + "\n2"
            + "\nPortfolio 1"
            + "\n1"
            + "\n0"
            + "\n8"
            + "\n3";

    InputStream in = new ByteArrayInputStream(input.getBytes());
    Controller controller = new ControllerImpl(this.userStockModel,
            this.textBasedView, in, rootDirectory);
    controller.start();
    assertEquals(desiredOutput, bytes.toString());
  }

  @Test
  public void addPortfolioWithSameNameAlreadyPresentManually() {
    String desiredOutput = "User create success\n"
            + "Portfolio 1 added successfully\n"
            + "Portfolio Name already exists. Please enter a different portfolio name.\n"
            + "Portfolio 2 added successfully\n";

    String input = "1"
            + "\nXYZ"
            + "\n1"
            + "\n2"
            + "\nPortfolio 1"
            + "\n1"
            + "\n1"
            + "\nGOOG"
            + "\n2022-10-27"
            + "\n10"
            + "\n1"
            + "\n2"
            + "\nPortfolio 1"
            + "\nPortfolio 2"
            + "\n1"
            + "\n1"
            + "\nGOOG"
            + "\n2022-10-27"
            + "\n10"
            + "\n8"
            + "\n3";

    InputStream in = new ByteArrayInputStream(input.getBytes());
    Controller controller = new ControllerImpl(this.userStockModel,
            this.textBasedView, in, rootDirectory);
    controller.start();
    assertEquals(desiredOutput, bytes.toString());
  }

  @Test
  public void testAddPortfolioManuallyWhenMoreThanOneSameStocksAdded() {
    String desiredOutput = "User create success\n"
            + "Portfolio 1 added successfully\n";

    String input = "1"
            + "\nXYZ"
            + "\n1"
            + "\n2"
            + "\nPortfolio 1"
            + "\n1"
            + "\n2"
            + "\nGOOG"
            + "\n2022-10-27"
            + "\n10"
            + "\nGOOG"
            + "\n2022-10-27"
            + "\n10"
            + "\n8"
            + "\n3";

    InputStream in = new ByteArrayInputStream(input.getBytes());
    Controller controller = new ControllerImpl(this.userStockModel,
            this.textBasedView, in, rootDirectory);
    controller.start();
    assertEquals(desiredOutput, bytes.toString());
  }

  @Test
  public void testAddPortfolioManuallyWhenMoreThanOneSameStocksAddedWhenOneIsShorted() {
    String desiredOutput = "User create success\n"
            + "Portfolio 1 added successfully\n";

    String input = "1"
            + "\nXYZ"
            + "\n1"
            + "\n2"
            + "\nPortfolio 1"
            + "\n1"
            + "\n2"
            + "\nGOOG"
            + "\n2022-10-27"
            + "\n10"
            + "\nGOOG"
            + "\n2022-10-27"
            + "\n-10"
            + "\n8"
            + "\n3";

    InputStream in = new ByteArrayInputStream(input.getBytes());
    Controller controller = new ControllerImpl(this.userStockModel,
            this.textBasedView, in, rootDirectory);
    controller.start();
    assertEquals(desiredOutput, bytes.toString());
  }

  @Test
  public void addPortfolioManuallyWhenQuantityIsNegative() {
    String desiredOutput = "User create success\n"
            + "Invalid input as resultant quantity for the stock turns out to be negative\n"
            + "Portfolio 1 added successfully\n";

    String input = "1"
            + "\nXYZ"
            + "\n1"
            + "\n2"
            + "\nPortfolio 1"
            + "\n1"
            + "\n1"
            + "\nGOOG"
            + "\n2022-10-27"
            + "\n-10"
            + "\n10"
            + "\n8"
            + "\n3";

    InputStream in = new ByteArrayInputStream(input.getBytes());
    Controller controller = new ControllerImpl(this.userStockModel,
            this.textBasedView, in, rootDirectory);
    controller.start();
    assertEquals(desiredOutput, bytes.toString());
  }

  @Test
  public void testInvalidTickerNameWhileAddingPortfolioRejected() {
    String desiredOutput = "User create success\n"
            + "Wrong input entered, try again!\n"
            + "Portfolio 1 added successfully\n";

    String input = "1"
            + "\nXYZ"
            + "\n1"
            + "\n2"
            + "\nPortfolio 1"
            + "\n1"
            + "\n1"
            + "\nXYZ"
            + "\nGOOG"
            + "\n2022-10-27"
            + "\n10"
            + "\n8"
            + "\n3";

    InputStream in = new ByteArrayInputStream(input.getBytes());
    Controller controller = new ControllerImpl(this.userStockModel,
            this.textBasedView, in, rootDirectory);
    controller.start();
    assertEquals(desiredOutput, bytes.toString());
  }

  @Test
  public void testInvalidDateWhileAddingPortfolioRejected() {
    String desiredOutput = "User create success\n"
            + "Wrong input entered, try again!\n"
            + "Portfolio 1 added successfully\n";

    String input = "1"
            + "\nXYZ"
            + "\n1"
            + "\n2"
            + "\nPortfolio 1"
            + "\n1"
            + "\n1"
            + "\nGOOG"
            + "\n27-10-2022"
            + "\n2022-10-27"
            + "\n10"
            + "\n8"
            + "\n3";

    InputStream in = new ByteArrayInputStream(input.getBytes());
    Controller controller = new ControllerImpl(this.userStockModel,
            this.textBasedView, in, rootDirectory);
    controller.start();
    assertEquals(desiredOutput, bytes.toString());
  }

  @Test
  public void testInvalidQuantityWhileAddingPortfolioRejected() {
    String desiredOutput = "User create success\n"
            + "Wrong input entered, try again!\n"
            + "Portfolio 1 added successfully\n";

    String input = "1"
            + "\nXYZ"
            + "\n1"
            + "\n2"
            + "\nPortfolio 1"
            + "\n1"
            + "\n1"
            + "\nGOOG"
            + "\n2022-10-27"
            + "\nFive"
            + "\n10"
            + "\n8"
            + "\n3";

    InputStream in = new ByteArrayInputStream(input.getBytes());
    Controller controller = new ControllerImpl(this.userStockModel,
            this.textBasedView, in, rootDirectory);
    controller.start();
    assertEquals(desiredOutput, bytes.toString());
  }

  @Test
  public void testZeroQuantityWhileAddingPortfolioWorks() {
    String desiredOutput = "User create success\n"
            + "Portfolio 1 added successfully\n";

    String input = "1"
            + "\nXYZ"
            + "\n1"
            + "\n2"
            + "\nPortfolio 1"
            + "\n1"
            + "\n1"
            + "\nGOOG"
            + "\n2022-10-27"
            + "\n0"
            + "\n8"
            + "\n3";

    InputStream in = new ByteArrayInputStream(input.getBytes());
    Controller controller = new ControllerImpl(this.userStockModel,
            this.textBasedView, in, rootDirectory);
    controller.start();
    assertEquals(desiredOutput, bytes.toString());
  }


  @Test
  public void testNegativeQuantityWhileAddingPortfolioDoesNotWorks() {
    String desiredOutput = "User create success\n"
            + "Invalid input as resultant quantity for the stock turns out to be negative\n"
            + "Portfolio 1 added successfully\n";

    String input = "1"
            + "\nXYZ"
            + "\n1"
            + "\n2"
            + "\nPortfolio 1"
            + "\n1"
            + "\n1"
            + "\nGOOG"
            + "\n2022-10-27"
            + "\n-10"
            + "\n10"
            + "\n8"
            + "\n3";

    InputStream in = new ByteArrayInputStream(input.getBytes());
    Controller controller = new ControllerImpl(this.userStockModel,
            this.textBasedView, in, rootDirectory);
    controller.start();
    assertEquals(desiredOutput, bytes.toString());
  }

  @Test
  public void testDecimalQuantityWhileAddingPortfolioRejected() {
    String desiredOutput = "User create success\n"
            + "Wrong input entered, try again!\n"
            + "Portfolio 1 added successfully\n";

    String input = "1"
            + "\nXYZ"
            + "\n1"
            + "\n2"
            + "\nPortfolio 1"
            + "\n1"
            + "\n1"
            + "\nGOOG"
            + "\n2022-10-27"
            + "\n10.15"
            + "\n10"
            + "\n8"
            + "\n3";

    InputStream in = new ByteArrayInputStream(input.getBytes());
    Controller controller = new ControllerImpl(this.userStockModel,
            this.textBasedView, in, rootDirectory);
    controller.start();
    assertEquals(desiredOutput, bytes.toString());
  }

  @Test
  public void testDateWithoutDataAddedWhileAddingPortfolioRejected() {
    String desiredOutput = "User create success\n"
            + "No data found for the date. "
            + "Please input a valid date on which the stock was actually traded\n"
            + "Portfolio 1 added successfully\n";

    String input = "1"
            + "\nXYZ"
            + "\n1"
            + "\n2"
            + "\nPortfolio 1"
            + "\n1"
            + "\n1"
            + "\nGOOG"
            + "\n2022-10-29"
            + "\n2022-10-27"
            + "\n10"
            + "\n8"
            + "\n3";

    InputStream in = new ByteArrayInputStream(input.getBytes());
    Controller controller = new ControllerImpl(this.userStockModel,
            this.textBasedView, in, rootDirectory);
    controller.start();
    assertEquals(desiredOutput, bytes.toString());
  }

  @Test
  public void addPortfolioViaFile() {
    String desiredOutput = "User create success\n"
            + "Portfolio 1 added successfully\n";

    String input = "1"
            + "\nXYZ"
            + "\n1"
            + "\n2"
            + "\nPortfolio 1"
            + "\n2"
            + "\nTestingHelper/singlePortfolioElementCorrectFormat.xml"
            + "\n8"
            + "\n3";

    InputStream in = new ByteArrayInputStream(input.getBytes());
    Controller controller = new ControllerImpl(this.userStockModel,
            this.textBasedView, in, rootDirectory);
    controller.start();
    assertEquals(desiredOutput, bytes.toString());
  }

  @Test
  public void addPortfolioWithInvalidFileFormatFailsViaFile() {
    String desiredOutput = "User create success\n"
            + "Error uploading XML file to portfolio. Please try again!\n"
            + "Portfolio 1 added successfully\n";

    String input = "1"
            + "\nXYZ"
            + "\n1"
            + "\n2"
            + "\nPortfolio 1"
            + "\n2"
            + "\nTestingHelper/invalidPortfolioElementFormat.xml"
            + "\nTestingHelper/multiplePortfolioElementCorrectFormat.xml"
            + "\n8"
            + "\n3";

    InputStream in = new ByteArrayInputStream(input.getBytes());
    Controller controller = new ControllerImpl(this.userStockModel,
            this.textBasedView, in, rootDirectory);
    controller.start();
    assertEquals(desiredOutput, bytes.toString());
  }

  @Test
  public void addPortfolioWithWrongFilePathFailsViaFile() {
    String desiredOutput = "User create success\n"
            + "Error uploading XML file to portfolio. Please try again!\n"
            + "Portfolio 1 added successfully\n";

    String input = "1"
            + "\nXYZ"
            + "\n1"
            + "\n2"
            + "\nPortfolio 1"
            + "\n2"
            + "\nTestingHelper/xyz.xml"
            + "\nTestingHelper/multiplePortfolioElementCorrectFormat.xml"
            + "\n8"
            + "\n3";

    InputStream in = new ByteArrayInputStream(input.getBytes());
    Controller controller = new ControllerImpl(this.userStockModel,
            this.textBasedView, in, rootDirectory);
    controller.start();
    assertEquals(desiredOutput, bytes.toString());
  }

  @Test
  public void addPortfolioWithMultipleStocksViaFile() {
    String desiredOutput = "User create success\n"
            + "Portfolio 1 added successfully\n";

    String input = "1"
            + "\nXYZ"
            + "\n1"
            + "\n2"
            + "\nPortfolio 1"
            + "\n2"
            + "\nTestingHelper/multiplePortfolioElementCorrectFormat.xml"
            + "\n8"
            + "\n3";

    InputStream in = new ByteArrayInputStream(input.getBytes());
    Controller controller = new ControllerImpl(this.userStockModel,
            this.textBasedView, in, rootDirectory);
    controller.start();
    assertEquals(desiredOutput, bytes.toString());
  }

  @Test
  public void addPortfolioWithMultipleSameStocksViaFile() {
    String desiredOutput = "User create success\n"
            + "Portfolio 1 added successfully\n";

    String input = "1"
            + "\nXYZ"
            + "\n1"
            + "\n2"
            + "\nPortfolio 1"
            + "\n2"
            + "\nTestingHelper/multipleSamePortfolioElementCorrectFormat.xml"
            + "\n8"
            + "\n3";

    InputStream in = new ByteArrayInputStream(input.getBytes());
    Controller controller = new ControllerImpl(this.userStockModel,
            this.textBasedView, in, rootDirectory);
    controller.start();
    assertEquals(desiredOutput, bytes.toString());
  }

  @Test
  public void addPortfolioWithZeroStocksViaFile() {
    String desiredOutput = "User create success\n"
            + "Portfolio 1 added successfully\n";

    String input = "1"
            + "\nXYZ"
            + "\n1"
            + "\n2"
            + "\nPortfolio 1"
            + "\n2"
            + "\nTestingHelper/zeroPortfolioElementCorrectFormat.xml"
            + "\n8"
            + "\n3";

    InputStream in = new ByteArrayInputStream(input.getBytes());
    Controller controller = new ControllerImpl(this.userStockModel,
            this.textBasedView, in, rootDirectory);
    controller.start();
    assertEquals(desiredOutput, bytes.toString());
  }

  @Test
  public void addPortfolioWithInvalidTickerFailsViaFile() {
    String desiredOutput = "User create success\n"
            + "Error uploading XML file to portfolio. Please try again!\n"
            + "Portfolio 1 added successfully\n";

    String input = "1"
            + "\nXYZ"
            + "\n1"
            + "\n2"
            + "\nPortfolio 1"
            + "\n2"
            + "\nTestingHelper/invalidTickerPortfolioElement.xml"
            + "\nTestingHelper/singlePortfolioElementCorrectFormat.xml"
            + "\n8"
            + "\n3";

    InputStream in = new ByteArrayInputStream(input.getBytes());
    Controller controller = new ControllerImpl(this.userStockModel,
            this.textBasedView, in, rootDirectory);
    controller.start();
    assertEquals(desiredOutput, bytes.toString());
  }

  @Test
  public void addPortfolioWithInvalidDateFormatFailsViaFile() {
    String desiredOutput = "User create success\n"
            + "Input file violates portfolio constraints."
            + "Please check all the inputs and try again.\n"
            + "Error uploading XML file to portfolio. Please try again!\n"
            + "Portfolio 1 added successfully\n";

    String input = "1"
            + "\nXYZ"
            + "\n1"
            + "\n2"
            + "\nPortfolio 1"
            + "\n2"
            + "\nTestingHelper/invalidDateFormatPortfolioElement.xml"
            + "\nTestingHelper/singlePortfolioElementCorrectFormat.xml"
            + "\n8"
            + "\n3";

    InputStream in = new ByteArrayInputStream(input.getBytes());
    Controller controller = new ControllerImpl(this.userStockModel,
            this.textBasedView, in, rootDirectory);
    controller.start();
    assertEquals(desiredOutput, bytes.toString());
  }

  @Test
  public void addPortfolioWithDataNotFoundForDateFailsViaFile() {
    String desiredOutput = "User create success\n"
            + "Input file violates portfolio constraints."
            + "Please check all the inputs and try again.\n"
            + "Error uploading XML file to portfolio. Please try again!\n"
            + "Portfolio 1 added successfully\n";

    String input = "1"
            + "\nXYZ"
            + "\n1"
            + "\n2"
            + "\nPortfolio 1"
            + "\n2"
            + "\nTestingHelper/dataNotFoundForDatePortfolioElement.xml"
            + "\nTestingHelper/singlePortfolioElementCorrectFormat.xml"
            + "\n8"
            + "\n3";

    InputStream in = new ByteArrayInputStream(input.getBytes());
    Controller controller = new ControllerImpl(this.userStockModel,
            this.textBasedView, in, rootDirectory);
    controller.start();
    assertEquals(desiredOutput, bytes.toString());
  }

  @Test
  public void addPortfolioWithWrongQuantityFailsViaFile() {
    String desiredOutput = "User create success\n"
            + "Input file violates portfolio constraints."
            + "Please check all the inputs and try again.\n"
            + "Error uploading XML file to portfolio. Please try again!\n"
            + "Portfolio 1 added successfully\n";

    String input = "1"
            + "\nXYZ"
            + "\n1"
            + "\n2"
            + "\nPortfolio 1"
            + "\n2"
            + "\nTestingHelper/wrongQuantityPortfolioElement.xml"
            + "\nTestingHelper/singlePortfolioElementCorrectFormat.xml"
            + "\n8"
            + "\n3";

    InputStream in = new ByteArrayInputStream(input.getBytes());
    Controller controller = new ControllerImpl(this.userStockModel,
            this.textBasedView, in, rootDirectory);
    controller.start();
    assertEquals(desiredOutput, bytes.toString());
  }

  @Test
  public void addPortfolioWithZeroQuantityWorksViaFile() {
    String desiredOutput = "User create success\n"
            + "Portfolio 1 added successfully\n";

    String input = "1"
            + "\nXYZ"
            + "\n1"
            + "\n2"
            + "\nPortfolio 1"
            + "\n2"
            + "\nTestingHelper/zeroQuantityFlexiblePortfolioElement.xml"
            + "\n8"
            + "\n3";

    InputStream in = new ByteArrayInputStream(input.getBytes());
    Controller controller = new ControllerImpl(this.userStockModel,
            this.textBasedView, in, rootDirectory);
    controller.start();
    assertEquals(desiredOutput, bytes.toString());
  }

  @Test
  public void addPortfolioWithNegativeQuantityDoesNotWorksViaFile() {
    String desiredOutput = "User create success\n"
            + "Input file violates portfolio constraints."
            + "Please check all the inputs and try again.\n"
            + "Error uploading XML file to portfolio. Please try again!\n"
            + "Portfolio 1 added successfully\n";

    String input = "1"
            + "\nXYZ"
            + "\n1"
            + "\n2"
            + "\nPortfolio 1"
            + "\n2"
            + "\nTestingHelper/negativeQuantityPortfolioElement.xml"
            + "\nTestingHelper/singlePortfolioElementCorrectFormat.xml"
            + "\n8"
            + "\n3";

    InputStream in = new ByteArrayInputStream(input.getBytes());
    Controller controller = new ControllerImpl(this.userStockModel,
            this.textBasedView, in, rootDirectory);
    controller.start();
    assertEquals(desiredOutput, bytes.toString());
  }

  @Test
  public void addPortfolioWithDecimalQuantityFailsViaFile() {
    String desiredOutput = "User create success\n"
            + "Input file violates portfolio constraints."
            + "Please check all the inputs and try again.\n"
            + "Error uploading XML file to portfolio. Please try again!\n"
            + "Portfolio 1 added successfully\n";

    String input = "1"
            + "\nXYZ"
            + "\n1"
            + "\n2"
            + "\nPortfolio 1"
            + "\n2"
            + "\nTestingHelper/decimalQuantityPortfolioElement.xml"
            + "\nTestingHelper/singlePortfolioElementCorrectFormat.xml"
            + "\n8"
            + "\n3";

    InputStream in = new ByteArrayInputStream(input.getBytes());
    Controller controller = new ControllerImpl(this.userStockModel,
            this.textBasedView, in, rootDirectory);
    controller.start();
    assertEquals(desiredOutput, bytes.toString());
  }

  @Test
  public void addPortfolioWithSameNameFailsViaFile() {
    String desiredOutput = "User create success\n"
            + "Portfolio 1 added successfully\n"
            + "Portfolio Name already exists. Please enter a different portfolio name.\n"
            + "Portfolio 2 added successfully\n";

    String input = "1"
            + "\nXYZ"
            + "\n1"
            + "\n2"
            + "\nPortfolio 1"
            + "\n2"
            + "\nTestingHelper/singlePortfolioElementCorrectFormat.xml"
            + "\n1"
            + "\n2"
            + "\nPortfolio 1"
            + "\nPortfolio 2"
            + "\n2"
            + "\nTestingHelper/singlePortfolioElementCorrectFormat.xml"
            + "\n8"
            + "\n3";

    InputStream in = new ByteArrayInputStream(input.getBytes());
    Controller controller = new ControllerImpl(this.userStockModel,
            this.textBasedView, in, rootDirectory);
    controller.start();
    assertEquals(desiredOutput, bytes.toString());
  }

  @Test
  public void testViewPortfolio() {
    String desiredOutput = "User create success\n"
            + "Portfolio 1 added successfully\n"
            + "Portfolio Details:\n"
            + "Portfolio Name: Portfolio 1\n"
            + "Ticker Symbol Name            "
            + "Stock Name                    "
            + "Total Quantity                "
            + "Average Price                 "
            + "Latest transaction Date       \n"
            + "GOOG                          "
            + "Alphabet Inc - Class C        "
            + "10.0                          "
            + "92.6                          "
            + "2022-10-27                    "
            + "\n"
            + "\n"
            + "\n"
            + "\n";

    String input = "1"
            + "\nXYZ"
            + "\n1"
            + "\n2"
            + "\nPortfolio 1"
            + "\n1"
            + "\n1"
            + "\nGOOG"
            + "\n2022-10-27"
            + "\n10"
            + "\n2"
            + "\n1"
            + "\n2022-10-28"
            + "\n8"
            + "\n3";

    InputStream in = new ByteArrayInputStream(input.getBytes());
    Controller controller = new ControllerImpl(this.userStockModel,
            this.textBasedView, in, rootDirectory);
    controller.start();
    assertEquals(desiredOutput, bytes.toString());
  }

  @Test
  public void testViewPortfolioWhenMultiplePresent() {
    String desiredOutput = "User create success\n"
            + "Portfolio 1 added successfully\n"
            + "Portfolio Details:\n"
            + "Portfolio Name: Portfolio 1\n"
            + "Ticker Symbol Name            "
            + "Stock Name                    "
            + "Total Quantity                "
            + "Average Price                 "
            + "Latest transaction Date       \n"
            + "GOOG                          "
            + "Alphabet Inc - Class C        "
            + "10.0                          "
            + "92.6                          "
            + "2022-10-27                    \n"
            + "AMZN                          "
            + "Amazon.com Inc                "
            + "10.0                          "
            + "110.96                        "
            + "2022-10-27                    \n"
            + "\n"
            + "\n"
            + "\n";

    String input = "1"
            + "\nXYZ"
            + "\n1"
            + "\n2"
            + "\nPortfolio 1"
            + "\n1"
            + "\n2"
            + "\nGOOG"
            + "\n2022-10-27"
            + "\n10"
            + "\nAMZN"
            + "\n2022-10-27"
            + "\n10"
            + "\n2"
            + "\n1"
            + "\n2022-10-28"
            + "\n8"
            + "\n3";

    InputStream in = new ByteArrayInputStream(input.getBytes());
    Controller controller = new ControllerImpl(this.userStockModel,
            this.textBasedView, in, rootDirectory);
    controller.start();
    assertEquals(desiredOutput, bytes.toString());
  }

  @Test
  public void testViewPortfolioWhenNoStocksPresent() {
    String desiredOutput = "User create success\n"
            + "Portfolio 1 added successfully\n"
            + "Portfolio Details:\n"
            + "Portfolio Name: Portfolio 1\n"
            + "Ticker Symbol Name            "
            + "Stock Name                    "
            + "Total Quantity                "
            + "Average Price                 "
            + "Latest transaction Date       "
            + "\n\n"
            + "\n\n";

    String input = "1"
            + "\nXYZ"
            + "\n1"
            + "\n2"
            + "\nPortfolio 1"
            + "\n1"
            + "\n0"
            + "\n2"
            + "\n1"
            + "\n2022-10-27"
            + "\n8"
            + "\n3";

    InputStream in = new ByteArrayInputStream(input.getBytes());
    Controller controller = new ControllerImpl(this.userStockModel,
            this.textBasedView, in, rootDirectory);
    controller.start();
    assertEquals(desiredOutput, bytes.toString());
  }

  @Test
  public void testWrongSelectionRejectedWhileViewPortfolio() {
    String desiredOutput = "User create success\n"
            + "Portfolio 1 added successfully\n"
            + "Wrong input entered, try again!\n"
            + "Portfolio Details:\n"
            + "Portfolio Name: Portfolio 1\n"
            + "Ticker Symbol Name            "
            + "Stock Name                    "
            + "Total Quantity                "
            + "Average Price                 "
            + "Latest transaction Date       \n"
            + "GOOG                          "
            + "Alphabet Inc - Class C        "
            + "10.0                          "
            + "92.6                          "
            + "2022-10-27                    "
            + "\n"
            + "\n"
            + "\n"
            + "\n";

    String input = "1"
            + "\nXYZ"
            + "\n1"
            + "\n2"
            + "\nPortfolio 1"
            + "\n1"
            + "\n1"
            + "\nGOOG"
            + "\n2022-10-27"
            + "\n10"
            + "\n2"
            + "\n2"
            + "\n1"
            + "\n2022-10-29"
            + "\n8"
            + "\n3";

    InputStream in = new ByteArrayInputStream(input.getBytes());
    Controller controller = new ControllerImpl(this.userStockModel,
            this.textBasedView, in, rootDirectory);
    controller.start();
    assertEquals(desiredOutput, bytes.toString());
  }

  @Test
  public void testViewPortfolioWhenNoPortfolioPresent() {
    String desiredOutput = "User create success\n"
            + "No portfolios found\n";

    String input = "1"
            + "\nXYZ"
            + "\n2"
            + "\n8"
            + "\n3";

    InputStream in = new ByteArrayInputStream(input.getBytes());
    Controller controller = new ControllerImpl(this.userStockModel,
            this.textBasedView, in, rootDirectory);
    controller.start();
    assertEquals(desiredOutput, bytes.toString());
  }

  @Test
  public void testDeterminePortfolioForADate() {
    String desiredOutput = "User create success\n"
            + "Portfolio 1 added successfully\n"
            + "Portfolio value for 2022-10-28 as of close is 965.8\n";

    String input = "1"
            + "\nXYZ"
            + "\n1"
            + "\n2"
            + "\nPortfolio 1"
            + "\n1"
            + "\n1"
            + "\nGOOG"
            + "\n2022-10-27"
            + "\n10"
            + "\n6"
            + "\n1"
            + "\n2022-10-28"
            + "\n8"
            + "\n3";

    InputStream in = new ByteArrayInputStream(input.getBytes());
    Controller controller = new ControllerImpl(this.userStockModel,
            this.textBasedView, in, rootDirectory);
    controller.start();
    assertEquals(desiredOutput, bytes.toString());
  }

  @Test
  public void testDeterminePortfolioForADateWhenPortfolioHasNoStocks() {
    String desiredOutput = "User create success\n"
            + "Portfolio 1 added successfully\n"
            + "Portfolio value for 2022-10-26 as of close is 0.0\n";

    String input = "1"
            + "\nXYZ"
            + "\n1"
            + "\n2"
            + "\nPortfolio 1"
            + "\n1"
            + "\n0"
            + "\n6"
            + "\n1"
            + "\n2022-10-26"
            + "\n8"
            + "\n3";

    InputStream in = new ByteArrayInputStream(input.getBytes());
    Controller controller = new ControllerImpl(this.userStockModel,
            this.textBasedView, in, rootDirectory);
    controller.start();
    assertEquals(desiredOutput, bytes.toString());
  }

  @Test
  public void testDeterminePortfolioForADateWhenPortfolioHasMultipleStocks() {
    String desiredOutput = "User create success\n"
            + "Portfolio 1 added successfully\n"
            + "Portfolio value for 2022-10-28 as of close is 3034.0\n";

    String input = "1"
            + "\nXYZ"
            + "\n1"
            + "\n2"
            + "\nPortfolio 1"
            + "\n1"
            + "\n2"
            + "\nGOOG"
            + "\n2022-10-27"
            + "\n10"
            + "\nAMZN"
            + "\n2022-10-27"
            + "\n20"
            + "\n6"
            + "\n1"
            + "\n2022-10-28"
            + "\n8"
            + "\n3";

    InputStream in = new ByteArrayInputStream(input.getBytes());
    Controller controller = new ControllerImpl(this.userStockModel,
            this.textBasedView, in, rootDirectory);
    controller.start();
    assertEquals(desiredOutput, bytes.toString());
  }

  @Test
  public void testDeterminePortfolioForADateWhenNoPortfoliosFound() {
    String desiredOutput = "User create success\n"
            + "No portfolios found\n";

    String input = "1"
            + "\nXYZ"
            + "\n6"
            + "\n8"
            + "\n3";

    InputStream in = new ByteArrayInputStream(input.getBytes());
    Controller controller = new ControllerImpl(this.userStockModel,
            this.textBasedView, in, rootDirectory);
    controller.start();
    assertEquals(desiredOutput, bytes.toString());
  }

  @Test
  public void testInvalidDateRejectedWhenDeterminePortfolioForADate() {
    String desiredOutput = "User create success\n"
            + "Portfolio 1 added successfully\n"
            + "Wrong input entered, try again!\n"
            + "Portfolio value for 2022-10-28 as of close is 965.8\n";

    String input = "1"
            + "\nXYZ"
            + "\n1"
            + "\n2"
            + "\nPortfolio 1"
            + "\n1"
            + "\n1"
            + "\nGOOG"
            + "\n2022-10-27"
            + "\n10"
            + "\n6"
            + "\n1"
            + "\n28-10-2022"
            + "\n2022-10-28"
            + "\n8"
            + "\n3";

    InputStream in = new ByteArrayInputStream(input.getBytes());
    Controller controller = new ControllerImpl(this.userStockModel,
            this.textBasedView, in, rootDirectory);
    controller.start();
    assertEquals(desiredOutput, bytes.toString());
  }

  @Test
  public void testDateForNoDataAvailableAddedWorksWhenDeterminePortfolioForADate() {
    String desiredOutput = "User create success\n"
            + "Portfolio 1 added successfully\n"
            + "Portfolio value for 2022-10-29 as of close is 965.8\n";

    String input = "1"
            + "\nXYZ"
            + "\n1"
            + "\n2"
            + "\nPortfolio 1"
            + "\n1"
            + "\n1"
            + "\nGOOG"
            + "\n2022-10-27"
            + "\n10"
            + "\n6"
            + "\n1"
            + "\n2022-10-29"
            + "\n8"
            + "\n3";

    InputStream in = new ByteArrayInputStream(input.getBytes());
    Controller controller = new ControllerImpl(this.userStockModel,
            this.textBasedView, in, rootDirectory);
    controller.start();
    assertEquals(desiredOutput, bytes.toString());
  }

  @Test
  public void testDownloadPortfolio() {
    String desiredOutput = "User create success\n"
            + "Portfolio 1 added successfully\n"
            + "Saved portfolio at path : ";

    String input = "1"
            + "\nXYZ"
            + "\n1"
            + "\n2"
            + "\nPortfolio 1"
            + "\n1"
            + "\n1"
            + "\nGOOG"
            + "\n2022-10-27"
            + "\n10"
            + "\n7"
            + "\n1"
            + "\n8"
            + "\n3";

    InputStream in = new ByteArrayInputStream(input.getBytes());
    Controller controller = new ControllerImpl(this.userStockModel,
            this.textBasedView, in, rootDirectory);
    controller.start();
    String[] output = bytes.toString().split("\n");
    String[] desired = desiredOutput.split("\n");
    assertEquals(output[0], desired[0]);
    assertEquals(output[1], desired[1]);
    assertTrue(output[2].contains(desired[2]));
  }

  @Test
  public void testWrongSelectionRejectedDuringDownloadPortfolio() {
    String desiredOutput = "User create success\n"
            + "Portfolio 1 added successfully\n"
            + "Wrong input entered, try again!\n"
            + "Saved portfolio at path : ";

    String input = "1"
            + "\nXYZ"
            + "\n1"
            + "\n2"
            + "\nPortfolio 1"
            + "\n1"
            + "\n1"
            + "\nGOOG"
            + "\n2022-10-27"
            + "\n10"
            + "\n7"
            + "\n2"
            + "\n1"
            + "\n8"
            + "\n3";

    InputStream in = new ByteArrayInputStream(input.getBytes());
    Controller controller = new ControllerImpl(this.userStockModel,
            this.textBasedView, in, rootDirectory);
    controller.start();
    String[] output = bytes.toString().split("\n");
    String[] desired = desiredOutput.split("\n");
    assertEquals(output[0], desired[0]);
    assertEquals(output[1], desired[1]);
    assertEquals(output[2], desired[2]);
    assertTrue(output[3].contains(desired[3]));
  }

  @Test
  public void testDownloadPortfolioWhenNoPortfolioPresent() {
    String desiredOutput = "User create success\n"
            + "No portfolios found\n";

    String input = "1"
            + "\nXYZ"
            + "\n4"
            + "\n8"
            + "\n3";

    InputStream in = new ByteArrayInputStream(input.getBytes());
    Controller controller = new ControllerImpl(this.userStockModel,
            this.textBasedView, in, rootDirectory);
    controller.start();
    assertEquals(desiredOutput, bytes.toString());
  }

  //Flexible Portfolio Tests

  @Test
  public void addFlexiblePortfolioManually() {
    String desiredOutput = "User create success\n"
            + "Portfolio 1 added successfully\n";

    String input = "1"
            + "\nXYZ"
            + "\n1"
            + "\n1"
            + "\nPortfolio 1"
            + "\n1"
            + "\n1"
            + "\nGOOG"
            + "\n2022-10-27"
            + "\n10"
            + "\n1"
            + "\n8"
            + "\n3";

    InputStream in = new ByteArrayInputStream(input.getBytes());
    Controller controller = new ControllerImpl(this.userStockModel,
            this.textBasedView, in, rootDirectory);
    controller.start();
    assertEquals(desiredOutput, bytes.toString());
  }

  @Test
  public void addMultipleStocksToFlexiblePortfolioManually() {
    String desiredOutput = "User create success\n"
            + "Portfolio 1 added successfully\n";

    String input = "1"
            + "\nXYZ"
            + "\n1"
            + "\n1"
            + "\nPortfolio 1"
            + "\n1"
            + "\n2"
            + "\nGOOG"
            + "\n2022-10-27"
            + "\n10"
            + "\n1"
            + "\nAMZN"
            + "\n2022-10-27"
            + "\n10"
            + "\n1"
            + "\n8"
            + "\n3";

    InputStream in = new ByteArrayInputStream(input.getBytes());
    Controller controller = new ControllerImpl(this.userStockModel,
            this.textBasedView, in, rootDirectory);
    controller.start();
    assertEquals(desiredOutput, bytes.toString());
  }

  @Test
  public void testNegativeCommissionDoesNotWorkForAddingPortfolio() {
    String desiredOutput = "User create success\n"
            + "Wrong input entered, try again!\n"
            + "Portfolio 1 added successfully\n";

    String input = "1"
            + "\nXYZ"
            + "\n1"
            + "\n1"
            + "\nPortfolio 1"
            + "\n1"
            + "\n1"
            + "\nGOOG"
            + "\n2022-10-27"
            + "\n10"
            + "\n-2"
            + "\n2"
            + "\n8"
            + "\n3";

    InputStream in = new ByteArrayInputStream(input.getBytes());
    Controller controller = new ControllerImpl(this.userStockModel,
            this.textBasedView, in, rootDirectory);
    controller.start();
    assertEquals(desiredOutput, bytes.toString());
  }

  @Test
  public void addZeroStocksToFlexiblePortfolioManually() {
    String desiredOutput = "User create success\n"
            + "Portfolio 1 added successfully\n";

    String input = "1"
            + "\nXYZ"
            + "\n1"
            + "\n1"
            + "\nPortfolio 1"
            + "\n1"
            + "\n0"
            + "\n8"
            + "\n3";

    InputStream in = new ByteArrayInputStream(input.getBytes());
    Controller controller = new ControllerImpl(this.userStockModel,
            this.textBasedView, in, rootDirectory);
    controller.start();
    assertEquals(desiredOutput, bytes.toString());
  }

  @Test
  public void addFlexiblePortfolioWithSameNameAlreadyPresentManually() {
    String desiredOutput = "User create success\n"
            + "Portfolio 1 added successfully\n"
            + "Portfolio Name already exists. Please enter a different portfolio name.\n"
            + "Portfolio 2 added successfully\n";

    String input = "1"
            + "\nXYZ"
            + "\n1"
            + "\n1"
            + "\nPortfolio 1"
            + "\n1"
            + "\n1"
            + "\nGOOG"
            + "\n2022-10-27"
            + "\n10"
            + "\n1"
            + "\n1"
            + "\n1"
            + "\nPortfolio 1"
            + "\nPortfolio 2"
            + "\n1"
            + "\n1"
            + "\nGOOG"
            + "\n2022-10-27"
            + "\n10"
            + "\n1"
            + "\n8"
            + "\n3";

    InputStream in = new ByteArrayInputStream(input.getBytes());
    Controller controller = new ControllerImpl(this.userStockModel,
            this.textBasedView, in, rootDirectory);
    controller.start();
    assertEquals(desiredOutput, bytes.toString());
  }

  @Test
  public void testAddFlexiblePortfolioManuallyWhenMoreThanOneSameStocksAdded() {
    String desiredOutput = "User create success\n"
            + "Portfolio 1 added successfully\n";

    String input = "1"
            + "\nXYZ"
            + "\n1"
            + "\n1"
            + "\nPortfolio 1"
            + "\n1"
            + "\n2"
            + "\nGOOG"
            + "\n2022-10-27"
            + "\n10"
            + "\n1"
            + "\nGOOG"
            + "\n2022-10-27"
            + "\n10"
            + "\n1"
            + "\n8"
            + "\n3";

    InputStream in = new ByteArrayInputStream(input.getBytes());
    Controller controller = new ControllerImpl(this.userStockModel,
            this.textBasedView, in, rootDirectory);
    controller.start();
    assertEquals(desiredOutput, bytes.toString());
  }

  @Test
  public void testAddFlexiblePortfolioManuallyWhenMoreThanOneSameStocksAddedWhenOneIsShorted() {
    String desiredOutput = "User create success\n"
            + "Portfolio 1 added successfully\n";

    String input = "1"
            + "\nXYZ"
            + "\n1"
            + "\n1"
            + "\nPortfolio 1"
            + "\n1"
            + "\n2"
            + "\nGOOG"
            + "\n2022-10-27"
            + "\n10"
            + "\n1"
            + "\nGOOG"
            + "\n2022-10-27"
            + "\n-10"
            + "\n1"
            + "\n8"
            + "\n3";

    InputStream in = new ByteArrayInputStream(input.getBytes());
    Controller controller = new ControllerImpl(this.userStockModel,
            this.textBasedView, in, rootDirectory);
    controller.start();
    assertEquals(desiredOutput, bytes.toString());
  }

  @Test
  public void addFlexiblePortfolioManuallyWhenQuantityIsNegativeFails() {
    String desiredOutput = "User create success\n"
            + "Invalid input as resultant quantity for the stock turns out to be negative\n"
            + "Portfolio 1 added successfully\n";

    String input = "1"
            + "\nXYZ"
            + "\n1"
            + "\n1"
            + "\nPortfolio 1"
            + "\n1"
            + "\n1"
            + "\nGOOG"
            + "\n2022-10-27"
            + "\n-10"
            + "\n10"
            + "\n1"
            + "\n8"
            + "\n3";

    InputStream in = new ByteArrayInputStream(input.getBytes());
    Controller controller = new ControllerImpl(this.userStockModel,
            this.textBasedView, in, rootDirectory);
    controller.start();
    assertEquals(desiredOutput, bytes.toString());
  }

  @Test
  public void testInvalidTickerNameWhileAddingFlexiblePortfolioRejected() {
    String desiredOutput = "User create success\n"
            + "Wrong input entered, try again!\n"
            + "Portfolio 1 added successfully\n";

    String input = "1"
            + "\nXYZ"
            + "\n1"
            + "\n1"
            + "\nPortfolio 1"
            + "\n1"
            + "\n1"
            + "\nXYZ"
            + "\nGOOG"
            + "\n2022-10-27"
            + "\n10"
            + "\n1"
            + "\n8"
            + "\n3";

    InputStream in = new ByteArrayInputStream(input.getBytes());
    Controller controller = new ControllerImpl(this.userStockModel,
            this.textBasedView, in, rootDirectory);
    controller.start();
    assertEquals(desiredOutput, bytes.toString());
  }

  @Test
  public void testInvalidDateFormatWhileAddingFlexiblePortfolioRejected() {
    String desiredOutput = "User create success\n"
            + "Wrong input entered, try again!\n"
            + "Portfolio 1 added successfully\n";

    String input = "1"
            + "\nXYZ"
            + "\n1"
            + "\n1"
            + "\nPortfolio 1"
            + "\n1"
            + "\n1"
            + "\nGOOG"
            + "\n27-10-2022"
            + "\n2022-10-27"
            + "\n10"
            + "\n1"
            + "\n8"
            + "\n3";

    InputStream in = new ByteArrayInputStream(input.getBytes());
    Controller controller = new ControllerImpl(this.userStockModel,
            this.textBasedView, in, rootDirectory);
    controller.start();
    assertEquals(desiredOutput, bytes.toString());
  }

  @Test
  public void testInvalidDateWhileAddingFlexiblePortfolioRejected() {
    String desiredOutput = "User create success\n"
            + "Wrong input entered, try again!\n"
            + "Portfolio 1 added successfully\n";

    String input = "1"
            + "\nXYZ"
            + "\n1"
            + "\n1"
            + "\nPortfolio 1"
            + "\n1"
            + "\n1"
            + "\nGOOG"
            + "\n27June2022Monday"
            + "\n2022-10-27"
            + "\n10"
            + "\n1"
            + "\n8"
            + "\n3";

    InputStream in = new ByteArrayInputStream(input.getBytes());
    Controller controller = new ControllerImpl(this.userStockModel,
            this.textBasedView, in, rootDirectory);
    controller.start();
    assertEquals(desiredOutput, bytes.toString());
  }

  @Test
  public void testInvalidQuantityWhileAddingFlexiblePortfolioRejected() {
    String desiredOutput = "User create success\n"
            + "Wrong input entered, try again!\n"
            + "Portfolio 1 added successfully\n";

    String input = "1"
            + "\nXYZ"
            + "\n1"
            + "\n1"
            + "\nPortfolio 1"
            + "\n1"
            + "\n1"
            + "\nGOOG"
            + "\n2022-10-27"
            + "\nFive"
            + "\n10"
            + "\n2"
            + "\n8"
            + "\n3";

    InputStream in = new ByteArrayInputStream(input.getBytes());
    Controller controller = new ControllerImpl(this.userStockModel,
            this.textBasedView, in, rootDirectory);
    controller.start();
    assertEquals(desiredOutput, bytes.toString());
  }

  @Test
  public void testZeroQuantityWhileAddingFlexiblePortfolioWorks() {
    String desiredOutput = "User create success\n"
            + "Portfolio 1 added successfully\n";

    String input = "1"
            + "\nXYZ"
            + "\n1"
            + "\n1"
            + "\nPortfolio 1"
            + "\n1"
            + "\n1"
            + "\nGOOG"
            + "\n2022-10-27"
            + "\n0"
            + "\n0"
            + "\n8"
            + "\n3";

    InputStream in = new ByteArrayInputStream(input.getBytes());
    Controller controller = new ControllerImpl(this.userStockModel,
            this.textBasedView, in, rootDirectory);
    controller.start();
    assertEquals(desiredOutput, bytes.toString());
  }


  @Test
  public void testNegativeQuantityWhileAddingFlexiblePortfolioDoesNotWorks() {
    String desiredOutput = "User create success\n"
            + "Invalid input as resultant quantity for the stock turns out to be negative\n"
            + "Portfolio 1 added successfully\n";

    String input = "1"
            + "\nXYZ"
            + "\n1"
            + "\n1"
            + "\nPortfolio 1"
            + "\n1"
            + "\n1"
            + "\nGOOG"
            + "\n2022-10-27"
            + "\n-10"
            + "\n10"
            + "\n1"
            + "\n8"
            + "\n3";

    InputStream in = new ByteArrayInputStream(input.getBytes());
    Controller controller = new ControllerImpl(this.userStockModel,
            this.textBasedView, in, rootDirectory);
    controller.start();
    assertEquals(desiredOutput, bytes.toString());
  }

  @Test
  public void testDecimalQuantityWhileAddingFlexiblePortfolioRejected() {
    String desiredOutput = "User create success\n"
            + "Wrong input entered, try again!\n"
            + "Portfolio 1 added successfully\n";

    String input = "1"
            + "\nXYZ"
            + "\n1"
            + "\n1"
            + "\nPortfolio 1"
            + "\n1"
            + "\n1"
            + "\nGOOG"
            + "\n2022-10-27"
            + "\n10.15"
            + "\n10"
            + "\n2"
            + "\n8"
            + "\n3";

    InputStream in = new ByteArrayInputStream(input.getBytes());
    Controller controller = new ControllerImpl(this.userStockModel,
            this.textBasedView, in, rootDirectory);
    controller.start();
    assertEquals(desiredOutput, bytes.toString());
  }

  @Test
  public void testDateWithoutDataAddedWhileAddingFlexiblePortfolioRejected() {
    String desiredOutput = "User create success\n"
            + "No data found for the date. "
            + "Please input a valid date on which the stock was actually traded\n"
            + "Portfolio 1 added successfully\n";

    String input = "1"
            + "\nXYZ"
            + "\n1"
            + "\n1"
            + "\nPortfolio 1"
            + "\n1"
            + "\n1"
            + "\nGOOG"
            + "\n2022-10-29"
            + "\n2022-10-27"
            + "\n10"
            + "\n1"
            + "\n8"
            + "\n3";

    InputStream in = new ByteArrayInputStream(input.getBytes());
    Controller controller = new ControllerImpl(this.userStockModel,
            this.textBasedView, in, rootDirectory);
    controller.start();
    assertEquals(desiredOutput, bytes.toString());
  }

  @Test
  public void addFlexiblePortfolioViaFile() {
    String desiredOutput = "User create success\n"
            + "Portfolio 1 added successfully\n";

    String input = "1"
            + "\nXYZ"
            + "\n1"
            + "\n1"
            + "\nPortfolio 1"
            + "\n2"
            + "\nTestingHelper/singleFlexiblePortfolioElementCorrectFormat.xml"
            + "\n8"
            + "\n3";

    InputStream in = new ByteArrayInputStream(input.getBytes());
    Controller controller = new ControllerImpl(this.userStockModel,
            this.textBasedView, in, rootDirectory);
    controller.start();
    assertEquals(desiredOutput, bytes.toString());
  }

  @Test
  public void addFlexiblePortfolioWithInvalidFileFormatFailsViaFile() {
    String desiredOutput = "User create success\n"
            + "Error uploading XML file to portfolio. Please try again!\n"
            + "Portfolio 1 added successfully\n";

    String input = "1"
            + "\nXYZ"
            + "\n1"
            + "\n1"
            + "\nPortfolio 1"
            + "\n2"
            + "\nTestingHelper/invalidPortfolioElementFormat.xml"
            + "\nTestingHelper/multipleSameFlexiblePortfolioElementCorrectFormat.xml"
            + "\n8"
            + "\n3";

    InputStream in = new ByteArrayInputStream(input.getBytes());
    Controller controller = new ControllerImpl(this.userStockModel,
            this.textBasedView, in, rootDirectory);
    controller.start();
    assertEquals(desiredOutput, bytes.toString());
  }

  @Test
  public void addFlexiblePortfolioWithMultipleStocksViaFile() {
    String desiredOutput = "User create success\n"
            + "Portfolio 1 added successfully\n";

    String input = "1"
            + "\nXYZ"
            + "\n1"
            + "\n1"
            + "\nPortfolio 1"
            + "\n2"
            + "\nTestingHelper/multipleSameFlexiblePortfolioElementCorrectFormat.xml"
            + "\n8"
            + "\n3";

    InputStream in = new ByteArrayInputStream(input.getBytes());
    Controller controller = new ControllerImpl(this.userStockModel,
            this.textBasedView, in, rootDirectory);
    controller.start();
    assertEquals(desiredOutput, bytes.toString());
  }

  @Test
  public void addFlexiblePortfolioWithMultipleSameStocksViaFile() {
    String desiredOutput = "User create success\n"
            + "Portfolio 1 added successfully\n";

    String input = "1"
            + "\nXYZ"
            + "\n1"
            + "\n1"
            + "\nPortfolio 1"
            + "\n2"
            + "\nTestingHelper/multipleSameFlexiblePortfolioElementCorrectFormat.xml"
            + "\n8"
            + "\n3";

    InputStream in = new ByteArrayInputStream(input.getBytes());
    Controller controller = new ControllerImpl(this.userStockModel,
            this.textBasedView, in, rootDirectory);
    controller.start();
    assertEquals(desiredOutput, bytes.toString());
  }

  @Test
  public void addFlexiblePortfolioWithZeroStocksViaFile() {
    String desiredOutput = "User create success\n"
            + "Portfolio 1 added successfully\n";

    String input = "1"
            + "\nXYZ"
            + "\n1"
            + "\n1"
            + "\nPortfolio 1"
            + "\n2"
            + "\nTestingHelper/zeroPortfolioElementCorrectFormat.xml"
            + "\n8"
            + "\n3";

    InputStream in = new ByteArrayInputStream(input.getBytes());
    Controller controller = new ControllerImpl(this.userStockModel,
            this.textBasedView, in, rootDirectory);
    controller.start();
    assertEquals(desiredOutput, bytes.toString());
  }

  @Test
  public void addFlexiblePortfolioWithInvalidTickerFailsViaFile() {
    String desiredOutput = "User create success\n"
            + "Error uploading XML file to portfolio. Please try again!\n"
            + "Portfolio 1 added successfully\n";

    String input = "1"
            + "\nXYZ"
            + "\n1"
            + "\n1"
            + "\nPortfolio 1"
            + "\n2"
            + "\nTestingHelper/invalidTickerPortfolioElement.xml"
            + "\nTestingHelper/singleFlexiblePortfolioElementCorrectFormat.xml"
            + "\n8"
            + "\n3";

    InputStream in = new ByteArrayInputStream(input.getBytes());
    Controller controller = new ControllerImpl(this.userStockModel,
            this.textBasedView, in, rootDirectory);
    controller.start();
    assertEquals(desiredOutput, bytes.toString());
  }

  @Test
  public void addFlexiblePortfolioWithInvalidDateFormatFailsViaFile() {
    String desiredOutput = "User create success\n"
            + "Input file violates portfolio constraints."
            + "Please check all the inputs and try again.\n"
            + "Error uploading XML file to portfolio. Please try again!\n"
            + "Portfolio 1 added successfully\n";

    String input = "1"
            + "\nXYZ"
            + "\n1"
            + "\n1"
            + "\nPortfolio 1"
            + "\n2"
            + "\nTestingHelper/invalidDateFormatPortfolioElement.xml"
            + "\nTestingHelper/singleFlexiblePortfolioElementCorrectFormat.xml"
            + "\n8"
            + "\n3";

    InputStream in = new ByteArrayInputStream(input.getBytes());
    Controller controller = new ControllerImpl(this.userStockModel,
            this.textBasedView, in, rootDirectory);
    controller.start();
    assertEquals(desiredOutput, bytes.toString());
  }

  @Test
  public void addFlexiblePortfolioWithDataNotFoundForDateFailsViaFile() {
    String desiredOutput = "User create success\n"
            + "Input file violates portfolio constraints."
            + "Please check all the inputs and try again.\n"
            + "Error uploading XML file to portfolio. Please try again!\n"
            + "Portfolio 1 added successfully\n";

    String input = "1"
            + "\nXYZ"
            + "\n1"
            + "\n1"
            + "\nPortfolio 1"
            + "\n2"
            + "\nTestingHelper/dataNotFoundForDatePortfolioElement.xml"
            + "\nTestingHelper/singleFlexiblePortfolioElementCorrectFormat.xml"
            + "\n8"
            + "\n3";

    InputStream in = new ByteArrayInputStream(input.getBytes());
    Controller controller = new ControllerImpl(this.userStockModel,
            this.textBasedView, in, rootDirectory);
    controller.start();
    assertEquals(desiredOutput, bytes.toString());
  }

  @Test
  public void addFlexiblePortfolioWithWrongQuantityFailsViaFile() {
    String desiredOutput = "User create success\n"
            + "Input file violates portfolio constraints."
            + "Please check all the inputs and try again.\n"
            + "Error uploading XML file to portfolio. Please try again!\n"
            + "Portfolio 1 added successfully\n";

    String input = "1"
            + "\nXYZ"
            + "\n1"
            + "\n1"
            + "\nPortfolio 1"
            + "\n2"
            + "\nTestingHelper/wrongQuantityPortfolioElement.xml"
            + "\nTestingHelper/singleFlexiblePortfolioElementCorrectFormat.xml"
            + "\n8"
            + "\n3";

    InputStream in = new ByteArrayInputStream(input.getBytes());
    Controller controller = new ControllerImpl(this.userStockModel,
            this.textBasedView, in, rootDirectory);
    controller.start();
    assertEquals(desiredOutput, bytes.toString());
  }

  @Test
  public void addFlexiblePortfolioWithZeroQuantityWorksViaFile() {
    String desiredOutput = "User create success\n"
            + "Portfolio 1 added successfully\n";

    String input = "1"
            + "\nXYZ"
            + "\n1"
            + "\n1"
            + "\nPortfolio 1"
            + "\n2"
            + "\nTestingHelper/zeroQuantityFlexiblePortfolioElement.xml"
            + "\n8"
            + "\n3";

    InputStream in = new ByteArrayInputStream(input.getBytes());
    Controller controller = new ControllerImpl(this.userStockModel,
            this.textBasedView, in, rootDirectory);
    controller.start();
    assertEquals(desiredOutput, bytes.toString());
  }

  @Test
  public void addFlexiblePortfolioWithNegativeQuantityDoesNotWorksViaFile() {
    String desiredOutput = "User create success\n"
            + "Input file violates portfolio constraints."
            + "Please check all the inputs and try again.\n"
            + "Error uploading XML file to portfolio. Please try again!\n"
            + "Portfolio 1 added successfully\n";

    String input = "1"
            + "\nXYZ"
            + "\n1"
            + "\n1"
            + "\nPortfolio 1"
            + "\n2"
            + "\nTestingHelper/negativeQuantityFlexiblePortfolioElement.xml"
            + "\nTestingHelper/singleFlexiblePortfolioElementCorrectFormat.xml"
            + "\n8"
            + "\n3";

    InputStream in = new ByteArrayInputStream(input.getBytes());
    Controller controller = new ControllerImpl(this.userStockModel,
            this.textBasedView, in, rootDirectory);
    controller.start();
    assertEquals(desiredOutput, bytes.toString());
  }

  @Test
  public void addFlexiblePortfolioWithDecimalQuantityFailsViaFile() {
    String desiredOutput = "User create success\n"
            + "Input file violates portfolio constraints."
            + "Please check all the inputs and try again.\n"
            + "Error uploading XML file to portfolio. Please try again!\n"
            + "Portfolio 1 added successfully\n";

    String input = "1"
            + "\nXYZ"
            + "\n1"
            + "\n1"
            + "\nPortfolio 1"
            + "\n2"
            + "\nTestingHelper/decimalQuantityPortfolioElement.xml"
            + "\nTestingHelper/singleFlexiblePortfolioElementCorrectFormat.xml"
            + "\n8"
            + "\n3";

    InputStream in = new ByteArrayInputStream(input.getBytes());
    Controller controller = new ControllerImpl(this.userStockModel,
            this.textBasedView, in, rootDirectory);
    controller.start();
    assertEquals(desiredOutput, bytes.toString());
  }

  @Test
  public void addFlexiblePortfolioWithSameNameFailsViaFile() {
    String desiredOutput = "User create success\n"
            + "Portfolio 1 added successfully\n"
            + "Portfolio Name already exists. Please enter a different portfolio name.\n"
            + "Portfolio 2 added successfully\n";

    String input = "1"
            + "\nXYZ"
            + "\n1"
            + "\n1"
            + "\nPortfolio 1"
            + "\n2"
            + "\nTestingHelper/singleFlexiblePortfolioElementCorrectFormat.xml"
            + "\n1"
            + "\n1"
            + "\nPortfolio 1"
            + "\nPortfolio 2"
            + "\n2"
            + "\nTestingHelper/singleFlexiblePortfolioElementCorrectFormat.xml"
            + "\n8"
            + "\n3";

    InputStream in = new ByteArrayInputStream(input.getBytes());
    Controller controller = new ControllerImpl(this.userStockModel,
            this.textBasedView, in, rootDirectory);
    controller.start();
    assertEquals(desiredOutput, bytes.toString());
  }

  @Test
  public void testViewFlexiblePortfolio() {
    String desiredOutput = "User create success\n"
            + "Portfolio 1 added successfully\n"
            + "Portfolio Details:\n"
            + "Portfolio Name: Portfolio 1\n"
            + "Ticker Symbol Name            "
            + "Stock Name                    "
            + "Total Quantity                "
            + "Average Price                 "
            + "Latest transaction Date       \n"
            + "GOOG                          "
            + "Alphabet Inc - Class C        "
            + "10.0                          "
            + "92.6                          "
            + "2022-10-27                    "
            + "\n"
            + "\n"
            + "\n"
            + "\n";

    String input = "1"
            + "\nXYZ"
            + "\n1"
            + "\n1"
            + "\nPortfolio 1"
            + "\n1"
            + "\n1"
            + "\nGOOG"
            + "\n2022-10-27"
            + "\n10"
            + "\n100"
            + "\n2"
            + "\n1"
            + "\n2022-10-28"
            + "\n8"
            + "\n3";

    InputStream in = new ByteArrayInputStream(input.getBytes());
    Controller controller = new ControllerImpl(this.userStockModel,
            this.textBasedView, in, rootDirectory);
    controller.start();
    assertEquals(desiredOutput, bytes.toString());
  }

  @Test
  public void testDetermineFlexiblePortfolioForADate() {
    String desiredOutput = "User create success\n"
            + "Portfolio 1 added successfully\n"
            + "Portfolio value for 2022-10-28 as of close is 965.8\n";

    String input = "1"
            + "\nXYZ"
            + "\n1"
            + "\n1"
            + "\nPortfolio 1"
            + "\n1"
            + "\n1"
            + "\nGOOG"
            + "\n2022-10-27"
            + "\n10"
            + "\n1"
            + "\n6"
            + "\n1"
            + "\n2022-10-28"
            + "\n8"
            + "\n3";

    InputStream in = new ByteArrayInputStream(input.getBytes());
    Controller controller = new ControllerImpl(this.userStockModel,
            this.textBasedView, in, rootDirectory);
    controller.start();
    assertEquals(desiredOutput, bytes.toString());
  }

  @Test
  public void testDetermineFlexiblePortfolioForADateWhenPortfolioHasNoStocks() {
    String desiredOutput = "User create success\n"
            + "Portfolio 1 added successfully\n"
            + "Portfolio value for 2022-10-26 as of close is 0.0\n";

    String input = "1"
            + "\nXYZ"
            + "\n1"
            + "\n1"
            + "\nPortfolio 1"
            + "\n1"
            + "\n0"
            + "\n6"
            + "\n1"
            + "\n2022-10-26"
            + "\n8"
            + "\n3";

    InputStream in = new ByteArrayInputStream(input.getBytes());
    Controller controller = new ControllerImpl(this.userStockModel,
            this.textBasedView, in, rootDirectory);
    controller.start();
    assertEquals(desiredOutput, bytes.toString());
  }

  @Test
  public void testDetermineFlexiblePortfolioForADateWhenPortfolioHasMultipleStocks() {
    String desiredOutput = "User create success\n"
            + "Portfolio 1 added successfully\n"
            + "Portfolio value for 2022-10-28 as of close is 3034.0\n";

    String input = "1"
            + "\nXYZ"
            + "\n1"
            + "\n1"
            + "\nPortfolio 1"
            + "\n1"
            + "\n2"
            + "\nGOOG"
            + "\n2022-10-27"
            + "\n10"
            + "\n1"
            + "\nAMZN"
            + "\n2022-10-27"
            + "\n20"
            + "\n2"
            + "\n6"
            + "\n1"
            + "\n2022-10-28"
            + "\n8"
            + "\n3";

    InputStream in = new ByteArrayInputStream(input.getBytes());
    Controller controller = new ControllerImpl(this.userStockModel,
            this.textBasedView, in, rootDirectory);
    controller.start();
    assertEquals(desiredOutput, bytes.toString());
  }

  @Test
  public void testDetermineFlexiblePortfolioForADateWhenNoPortfoliosFound() {
    String desiredOutput = "User create success\n"
            + "No portfolios found\n";

    String input = "1"
            + "\nXYZ"
            + "\n6"
            + "\n8"
            + "\n3";

    InputStream in = new ByteArrayInputStream(input.getBytes());
    Controller controller = new ControllerImpl(this.userStockModel,
            this.textBasedView, in, rootDirectory);
    controller.start();
    assertEquals(desiredOutput, bytes.toString());
  }

  @Test
  public void testInvalidDateRejectedWhenDetermineFlexiblePortfolioForADate() {
    String desiredOutput = "User create success\n"
            + "Portfolio 1 added successfully\n"
            + "Wrong input entered, try again!\n"
            + "Portfolio value for 2022-10-28 as of close is 965.8\n";

    String input = "1"
            + "\nXYZ"
            + "\n1"
            + "\n1"
            + "\nPortfolio 1"
            + "\n1"
            + "\n1"
            + "\nGOOG"
            + "\n2022-10-27"
            + "\n10"
            + "\n1"
            + "\n6"
            + "\n1"
            + "\n28-10-2022"
            + "\n2022-10-28"
            + "\n8"
            + "\n3";

    InputStream in = new ByteArrayInputStream(input.getBytes());
    Controller controller = new ControllerImpl(this.userStockModel,
            this.textBasedView, in, rootDirectory);
    controller.start();
    assertEquals(desiredOutput, bytes.toString());
  }

  @Test
  public void testDateForNoDataAvailableAddedWorksWhenDetermineFlexiblePortfolioForADate() {
    String desiredOutput = "User create success\n"
            + "Portfolio 1 added successfully\n"
            + "Portfolio value for 2022-10-29 as of close is 965.8\n";

    String input = "1"
            + "\nXYZ"
            + "\n1"
            + "\n1"
            + "\nPortfolio 1"
            + "\n1"
            + "\n1"
            + "\nGOOG"
            + "\n2022-10-27"
            + "\n10"
            + "\n1"
            + "\n6"
            + "\n1"
            + "\n2022-10-29"
            + "\n8"
            + "\n3";

    InputStream in = new ByteArrayInputStream(input.getBytes());
    Controller controller = new ControllerImpl(this.userStockModel,
            this.textBasedView, in, rootDirectory);
    controller.start();
    assertEquals(desiredOutput, bytes.toString());
  }

  @Test
  public void testDownloadFlexiblePortfolio() {
    String desiredOutput = "User create success\n"
            + "Portfolio 1 added successfully\n"
            + "Saved portfolio at path : ";

    String input = "1"
            + "\nXYZ"
            + "\n1"
            + "\n1"
            + "\nPortfolio 1"
            + "\n1"
            + "\n1"
            + "\nGOOG"
            + "\n2022-10-27"
            + "\n10"
            + "\n1"
            + "\n7"
            + "\n1"
            + "\n8"
            + "\n3";

    InputStream in = new ByteArrayInputStream(input.getBytes());
    Controller controller = new ControllerImpl(this.userStockModel,
            this.textBasedView, in, rootDirectory);
    controller.start();
    String[] output = bytes.toString().split("\n");
    String[] desired = desiredOutput.split("\n");
    assertEquals(output[0], desired[0]);
    assertEquals(output[1], desired[1]);
    assertTrue(output[2].contains(desired[2]));
  }

  @Test
  public void testCostBasisOfFlexiblePortfolio() {
    String desiredOutput = "User create success\n"
            + "Portfolio 1 added successfully\n"
            + "Portfolio cost basis for 2022-11-10 is 927.0\n";

    String input = "1"
            + "\nXYZ"
            + "\n1"
            + "\n1"
            + "\nPortfolio 1"
            + "\n1"
            + "\n1"
            + "\nGOOG"
            + "\n2022-10-27"
            + "\n10"
            + "\n1"
            + "\n4"
            + "\n1"
            + "\n2022-11-10"
            + "\n8"
            + "\n3";

    InputStream in = new ByteArrayInputStream(input.getBytes());
    Controller controller = new ControllerImpl(this.userStockModel,
            this.textBasedView, in, rootDirectory);
    controller.start();
    assertEquals(desiredOutput, bytes.toString());
  }

  @Test
  public void testCostBasisOfInflexiblePortfolio() {
    String desiredOutput = "User create success\n"
            + "Portfolio 1 added successfully\n"
            + "Portfolio cost basis for 2022-11-10 is 926.0\n";

    String input = "1"
            + "\nXYZ"
            + "\n1"
            + "\n2"
            + "\nPortfolio 1"
            + "\n1"
            + "\n1"
            + "\nGOOG"
            + "\n2022-10-27"
            + "\n10"
            + "\n4"
            + "\n1"
            + "\n2022-11-10"
            + "\n8"
            + "\n3";

    InputStream in = new ByteArrayInputStream(input.getBytes());
    Controller controller = new ControllerImpl(this.userStockModel,
            this.textBasedView, in, rootDirectory);
    controller.start();
    assertEquals(desiredOutput, bytes.toString());
  }

  @Test
  public void testCannotModifyInflexiblePortfolio() {
    String desiredOutput = "User create success\n"
            + "Portfolio 1 added successfully\n"
            + "No portfolios found\n";

    String input = "1"
            + "\nXYZ"
            + "\n1"
            + "\n2"
            + "\nPortfolio 1"
            + "\n1"
            + "\n1"
            + "\nGOOG"
            + "\n2022-10-27"
            + "\n10"
            + "\n3"
            + "\n8"
            + "\n3";

    InputStream in = new ByteArrayInputStream(input.getBytes());
    Controller controller = new ControllerImpl(this.userStockModel,
            this.textBasedView, in, rootDirectory);
    controller.start();
    assertEquals(desiredOutput, bytes.toString());
  }

  @Test
  public void testCannotModifyFlexiblePortfolio() {
    String desiredOutput = "User create success\n"
            + "Portfolio 1 added successfully\n"
            + "Portfolio modified successfully.\n";

    String input = "1"
            + "\nXYZ"
            + "\n1"
            + "\n1"
            + "\nPortfolio 1"
            + "\n1"
            + "\n1"
            + "\nGOOG"
            + "\n2022-10-27"
            + "\n10"
            + "\n1"
            + "\n3"
            + "\n1"
            + "\nGOOG"
            + "\n2022-10-28"
            + "\n20"
            + "\n2"
            + "\n8"
            + "\n3";

    InputStream in = new ByteArrayInputStream(input.getBytes());
    Controller controller = new ControllerImpl(this.userStockModel,
            this.textBasedView, in, rootDirectory);
    controller.start();
    assertEquals(desiredOutput, bytes.toString());
  }

  @Test
  public void testCannotModifyFlexiblePortfolioBuyInvalidDateFails() {
    String desiredOutput = "User create success\n"
            + "Portfolio 1 added successfully\n"
            + "Wrong input entered, try again!\n"
            + "Portfolio modified successfully.\n";

    String input = "1"
            + "\nXYZ"
            + "\n1"
            + "\n1"
            + "\nPortfolio 1"
            + "\n1"
            + "\n1"
            + "\nGOOG"
            + "\n2022-10-27"
            + "\n10"
            + "\n1"
            + "\n3"
            + "\n1"
            + "\nGOOG"
            + "\n28JuneMonday2022"
            + "\n2022-10-28"
            + "\n20"
            + "\n2"
            + "\n8"
            + "\n3";

    InputStream in = new ByteArrayInputStream(input.getBytes());
    Controller controller = new ControllerImpl(this.userStockModel,
            this.textBasedView, in, rootDirectory);
    controller.start();
    assertEquals(desiredOutput, bytes.toString());
  }

  @Test
  public void testCannotModifyFlexiblePortfolioSellInvalidDateFails() {
    String desiredOutput = "User create success\n"
            + "Portfolio 1 added successfully\n"
            + "Wrong input entered, try again!\n"
            + "Portfolio modified successfully.\n";

    String input = "1"
            + "\nXYZ"
            + "\n1"
            + "\n1"
            + "\nPortfolio 1"
            + "\n1"
            + "\n1"
            + "\nGOOG"
            + "\n2022-10-27"
            + "\n10"
            + "\n1"
            + "\n3"
            + "\n1"
            + "\nGOOG"
            + "\n28JuneMonday2022"
            + "\n2022-10-28"
            + "\n-5"
            + "\n2"
            + "\n8"
            + "\n3";

    InputStream in = new ByteArrayInputStream(input.getBytes());
    Controller controller = new ControllerImpl(this.userStockModel,
            this.textBasedView, in, rootDirectory);
    controller.start();
    assertEquals(desiredOutput, bytes.toString());
  }

  @Test
  public void testCannotModifyFlexiblePortfolioSoThatQuantityDropsToNegative() {
    String desiredOutput = "User create success\n"
            + "Portfolio 1 added successfully\n"
            + "Invalid input as resultant quantity for the stock turns out to be negative\n"
            + "Portfolio modified successfully.\n";

    String input = "1"
            + "\nXYZ"
            + "\n1"
            + "\n1"
            + "\nPortfolio 1"
            + "\n1"
            + "\n1"
            + "\nGOOG"
            + "\n2022-10-27"
            + "\n10"
            + "\n1"
            + "\n3"
            + "\n1"
            + "\nGOOG"
            + "\n2022-10-28"
            + "\n-20"
            + "\n10"
            + "\n2"
            + "\n8"
            + "\n3";

    InputStream in = new ByteArrayInputStream(input.getBytes());
    Controller controller = new ControllerImpl(this.userStockModel,
            this.textBasedView, in, rootDirectory);
    controller.start();
    assertEquals(desiredOutput, bytes.toString());
  }

  @Test
  public void testShowFlexiblePortfolioPerformanceOverTime() {
    String desiredOutput = "User create success\n"
            + "Portfolio 1 added successfully\n"
            + "Performance of portfolio Portfolio 1 from 01 Nov 2022 to 10 Nov 2022\n"
            + "01 Nov 2022: ********************************\n"
            + "02 Nov 2022: *****************\n"
            + "03 Nov 2022: *\n"
            + "04 Nov 2022: ***************\n"
            + "07 Nov 2022: ************************\n"
            + "08 Nov 2022: *************************\n"
            + "09 Nov 2022: ******************\n"
            + "10 Nov 2022: *************************************************\n"
            + "Relative scale: First * = 834 and following * = 2.2291666666666665\n"
            + "\n";

    String input = "1"
            + "\nXYZ"
            + "\n1"
            + "\n1"
            + "\nPortfolio 1"
            + "\n1"
            + "\n1"
            + "\nGOOG"
            + "\n2022-10-27"
            + "\n10"
            + "\n1"
            + "\n5"
            + "\n1"
            + "\n2022-11-01"
            + "\n2022-11-10"
            + "\n8"
            + "\n3";

    InputStream in = new ByteArrayInputStream(input.getBytes());
    Controller controller = new ControllerImpl(this.userStockModel,
            this.textBasedView, in, rootDirectory);
    controller.start();
    assertEquals(desiredOutput, bytes.toString());
  }

  @Test
  public void testShowInflexiblePortfolioPerformanceOverTime() {
    String desiredOutput = "User create success\n"
            + "Portfolio 1 added successfully\n"
            + "Performance of portfolio Portfolio 1 from 01 Nov 2022 to 10 Nov 2022\n"
            + "01 Nov 2022: ********************************\n"
            + "02 Nov 2022: *****************\n"
            + "03 Nov 2022: *\n"
            + "04 Nov 2022: ***************\n"
            + "07 Nov 2022: ************************\n"
            + "08 Nov 2022: *************************\n"
            + "09 Nov 2022: ******************\n"
            + "10 Nov 2022: *************************************************\n"
            + "Relative scale: First * = 834 and following * = 2.2291666666666665\n"
            + "\n";

    String input = "1"
            + "\nXYZ"
            + "\n1"
            + "\n2"
            + "\nPortfolio 1"
            + "\n1"
            + "\n1"
            + "\nGOOG"
            + "\n2022-10-27"
            + "\n10"
            + "\n5"
            + "\n1"
            + "\n2022-11-01"
            + "\n2022-11-10"
            + "\n8"
            + "\n3";

    InputStream in = new ByteArrayInputStream(input.getBytes());
    Controller controller = new ControllerImpl(this.userStockModel,
            this.textBasedView, in, rootDirectory);
    controller.start();
    assertEquals(desiredOutput, bytes.toString());
  }

  @Test
  public void testCannotShowPortfolioPerformanceOverTimeWhenEndDateBeforeStartDate() {
    String desiredOutput = "User create success\n"
            + "Portfolio 1 added successfully\n"
            + "End date cannot be before start date.\n"
            + "Performance of portfolio Portfolio 1 from 01 Nov 2022 to 10 Nov 2022\n"
            + "01 Nov 2022: ********************************\n"
            + "02 Nov 2022: *****************\n"
            + "03 Nov 2022: *\n"
            + "04 Nov 2022: ***************\n"
            + "07 Nov 2022: ************************\n"
            + "08 Nov 2022: *************************\n"
            + "09 Nov 2022: ******************\n"
            + "10 Nov 2022: *************************************************\n"
            + "Relative scale: First * = 834 and following * = 2.2291666666666665\n"
            + "\n";

    String input = "1"
            + "\nXYZ"
            + "\n1"
            + "\n2"
            + "\nPortfolio 1"
            + "\n1"
            + "\n1"
            + "\nGOOG"
            + "\n2022-10-27"
            + "\n10"
            + "\n5"
            + "\n1"
            + "\n2022-11-01"
            + "\n2022-10-29"
            + "\n2022-11-01"
            + "\n2022-11-10"
            + "\n8"
            + "\n3";

    InputStream in = new ByteArrayInputStream(input.getBytes());
    Controller controller = new ControllerImpl(this.userStockModel,
            this.textBasedView, in, rootDirectory);
    controller.start();
    assertEquals(desiredOutput, bytes.toString());
  }

  private void fileWriteUp() throws IOException {
    Files.createDirectories(Paths.get("TestingHelper"));
    String dataNotFoundForDatePortfolioElement = "<PortfolioModel>\n"
            + "<PortfolioElementModel>\n"
            + "<StockTicker> GOOG </StockTicker>\n"
            + "<totalQuantity> 1000 </totalQuantity>\n"
            + "<Date> 2022-10-29 </Date>\n"
            + "</PortfolioElementModel>\n"
            + "</PortfolioModel>";
    java.io.FileWriter fw = new java.io.FileWriter(
            "TestingHelper//dataNotFoundForDatePortfolioElement.xml");
    fw.write(dataNotFoundForDatePortfolioElement);
    fw.close();
    String decimalQuantityPortfolioElement = "<PortfolioModel>\n"
            + "<PortfolioElementModel>\n"
            + "<StockTicker> GOOG </StockTicker>\n"
            + "<totalQuantity> 1.2 </totalQuantity>\n"
            + "<Date> 2022-10-27 </Date>\n"
            + "</PortfolioElementModel>\n"
            + "</PortfolioModel>";
    fw = new java.io.FileWriter("TestingHelper//decimalQuantityPortfolioElement.xml");
    fw.write(decimalQuantityPortfolioElement);
    fw.close();
    String invalidDateFormatPortfolioElement = "<PortfolioModel>\n"
            + "<PortfolioElementModel>\n"
            + "<StockTicker> GOOG </StockTicker>\n"
            + "<totalQuantity> 1000 </totalQuantity>\n"
            + "<Date> 27-10-2022 </Date>\n"
            + "</PortfolioElementModel>\n"
            + "</PortfolioModel>";
    fw = new java.io.FileWriter("TestingHelper//invalidDateFormatPortfolioElement.xml");
    fw.write(invalidDateFormatPortfolioElement);
    fw.close();
    String invalidPortfolioElementFormat = "<PortfolioModel>\n"
            + "    <PortfolioElementModel>\n"
            + "</PortfolioModel>";
    fw = new java.io.FileWriter("TestingHelper//invalidPortfolioElementFormat.xml");
    fw.write(invalidPortfolioElementFormat);
    fw.close();
    String invalidTickerPortfolioElement = "<PortfolioModel>\n"
            + "<PortfolioElementModel>\n"
            + "<StockTicker> XYZ </StockTicker>\n"
            + "<totalQuantity> 1000 </totalQuantity>\n"
            + "<Date> 2022-10-27 </Date>\n"
            + "</PortfolioElementModel>\n"
            + "</PortfolioModel>";
    fw = new java.io.FileWriter("TestingHelper//invalidTickerPortfolioElement.xml");
    fw.write(invalidTickerPortfolioElement);
    fw.close();
    String multiplePortfolioElementCorrectFormat = "<PortfolioModel>\n"
            + "<PortfolioElementModel>\n"
            + "<StockTicker> GOOG </StockTicker>\n"
            + "<totalQuantity> 1000 </totalQuantity>\n"
            + "<Date> 2022-10-27 </Date>\n"
            + "</PortfolioElementModel>\n"
            + "<PortfolioElementModel>\n"
            + "<StockTicker> AMZN </StockTicker>\n"
            + "<totalQuantity> 1000 </totalQuantity>\n"
            + "<Date> 2022-10-27 </Date>\n"
            + "</PortfolioElementModel>\n"
            + "</PortfolioModel>";
    fw = new java.io.FileWriter("TestingHelper//multiplePortfolioElementCorrectFormat.xml");
    fw.write(multiplePortfolioElementCorrectFormat);
    fw.close();
    String portfolioModelTest = "<PortfolioModel>\n"
            + "<PortfolioElementModel>\n"
            + "<StockTicker>GOOG</StockTicker>\n"
            + "<totalQuantity>100</totalQuantity>\n"
            + "<Date>2022-09-22</Date>\n"
            + "</PortfolioElementModel>\n"
            + "<PortfolioElementModel>\n"
            + "<StockTicker>GOOG</StockTicker>\n"
            + "<totalQuantity>200</totalQuantity>\n"
            + "<Date>2021-09-22</Date>\n"
            + "</PortfolioElementModel>\n"
            + "<PortfolioElementModel>\n"
            + "<StockTicker>MSFT</StockTicker>\n"
            + "<totalQuantity>10</totalQuantity>\n"
            + "<Date>2022-09-15</Date>\n"
            + "</PortfolioElementModel>\n"
            + "</PortfolioModel>";
    fw = new java.io.FileWriter("TestingHelper//portfolioModelTest.xml");
    fw.write(portfolioModelTest);
    fw.close();
    String multipleSamePortfolioElementCorrectFormat = "<PortfolioModel>\n"
            + "<PortfolioElementModel>\n"
            + "<StockTicker> GOOG </StockTicker>\n"
            + "<totalQuantity> 1000 </totalQuantity>\n"
            + "<Date> 2022-10-27 </Date>\n"
            + "</PortfolioElementModel>\n"
            + "<PortfolioElementModel>\n"
            + "<StockTicker> GOOG </StockTicker>\n"
            + "<totalQuantity> 2000 </totalQuantity>\n"
            + "<Date> 2022-10-27 </Date>\n"
            + "</PortfolioElementModel>\n"
            + "</PortfolioModel>";
    fw = new java.io.FileWriter("TestingHelper//multipleSamePortfolioElementCorrectFormat.xml");
    fw.write(multipleSamePortfolioElementCorrectFormat);
    fw.close();
    String negativeQuantityPortfolioElement = "<PortfolioModel>\n"
            + "<PortfolioElementModel>\n"
            + "<StockTicker> GOOG </StockTicker>\n"
            + "<totalQuantity> -10 </totalQuantity>\n"
            + "<Date> 2022-10-27 </Date>\n"
            + "</PortfolioElementModel>\n"
            + "</PortfolioModel>";
    fw = new java.io.FileWriter("TestingHelper//negativeQuantityPortfolioElement.xml");
    fw.write(negativeQuantityPortfolioElement);
    fw.close();
    String singlePortfolioElementCorrectFormat = "<PortfolioModel>\n"
            + "<PortfolioElementModel>\n"
            + "<StockTicker> GOOG </StockTicker>\n"
            + "<totalQuantity> 1000 </totalQuantity>\n"
            + "<Date> 2022-10-27 </Date>\n"
            + "</PortfolioElementModel>\n"
            + "</PortfolioModel>";
    fw = new java.io.FileWriter("TestingHelper//singlePortfolioElementCorrectFormat.xml");
    fw.write(singlePortfolioElementCorrectFormat);
    fw.close();
    String userSetModel = "<UserSetModel>\n"
            + "<UserModel id=\"1000\"/>\n"
            + "</UserSetModel>";
    fw = new java.io.FileWriter("TestingHelper//UserSetModel.xml");
    fw.write(userSetModel);
    fw.close();
    String wrongQuantityPortfolioElement = "<PortfolioModel>\n"
            + "<PortfolioElementModel>\n"
            + "<StockTicker> GOOG </StockTicker>\n"
            + "<totalQuantity> Five </totalQuantity>\n"
            + "<Date> 2022-10-27 </Date>\n"
            + "</PortfolioElementModel>\n"
            + "</PortfolioModel>";
    fw = new java.io.FileWriter("TestingHelper//wrongQuantityPortfolioElement.xml");
    fw.write(wrongQuantityPortfolioElement);
    fw.close();
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
    fw = new java.io.FileWriter("TestingHelper//xmlParserTest.xml");
    fw.write(xmlParserTest);
    fw.close();
    String zeroPortfolioElementCorrectFormat = "\n"
            + "<PortfolioModel> </PortfolioModel>";
    fw = new java.io.FileWriter("TestingHelper//zeroPortfolioElementCorrectFormat.xml");
    fw.write(zeroPortfolioElementCorrectFormat);
    fw.close();
    String zeroQuantityPortfolioElement = "<PortfolioModel>\n"
            + "<PortfolioElementModel>\n"
            + "<StockTicker> GOOG </StockTicker>\n"
            + "<totalQuantity> 0 </totalQuantity>\n"
            + "<Date> 2022-10-27 </Date>\n"
            + "</PortfolioElementModel>\n"
            + "</PortfolioModel>";
    fw = new java.io.FileWriter("TestingHelper//zeroQuantityPortfolioElement.xml");
    fw.write(zeroQuantityPortfolioElement);
    fw.close();
    String singlePortfolioElementCorrectFormatss = "";
    fw = new java.io.FileWriter("TestingHelper//zeroQuantityPortfolioElement.xml");
    fw.write(singlePortfolioElementCorrectFormatss);
    fw.close();
    String singleFlexiblePortfolioElementCorrectFormat = "<PortfolioModel>\n"
            + "<PortfolioElementModel>\n"
            + "<StockTicker> GOOG </StockTicker>\n"
            + "<totalQuantity> 1000 </totalQuantity>\n"
            + "<Date> 2022-10-27 </Date>\n"
            + "<BrokerCommission> 100.9</BrokerCommission>\n"
            + "</PortfolioElementModel>\n"
            + "</PortfolioModel>";
    fw = new java.io.FileWriter("TestingHelper//singleFlexiblePortfolioElementCorrectFormat.xml");
    fw.write(singleFlexiblePortfolioElementCorrectFormat);
    fw.close();

    String zeroQuantityFlexiblePortfolioElement = "<PortfolioModel>\n"
            + "<PortfolioElementModel>\n"
            + "<StockTicker> GOOG </StockTicker>\n"
            + "<totalQuantity> 0 </totalQuantity>\n"
            + "<Date> 2022-10-27 </Date>\n"
            + "<BrokerCommission> 100.9</BrokerCommission>\n"
            + "</PortfolioElementModel>\n"
            + "</PortfolioModel>";
    fw = new java.io.FileWriter("TestingHelper//zeroQuantityFlexiblePortfolioElement.xml");
    fw.write(zeroQuantityFlexiblePortfolioElement);
    fw.close();

    String multipleSameFlexiblePortfolioElementCorrectFormat = "\n"
            + "<PortfolioModel>\n"
            + "<PortfolioElementModel>\n"
            + "<StockTicker>GOOG</StockTicker>\n"
            + "<totalQuantity>100</totalQuantity>\n"
            + "<Date>2022-09-22</Date>\n"
            + "<BrokerCommission> 50.9</BrokerCommission>\n"
            + "</PortfolioElementModel>\n"
            + "<PortfolioElementModel>\n"
            + "<StockTicker>GOOG</StockTicker>\n"
            + "<totalQuantity>200</totalQuantity>\n"
            + "<Date>2021-09-22</Date>\n"
            + "<BrokerCommission> 100.9</BrokerCommission>\n"
            + "</PortfolioElementModel>\n"
            + "<PortfolioElementModel>\n"
            + "<StockTicker>MSFT</StockTicker>\n"
            + "<totalQuantity>10</totalQuantity>\n"
            + "<Date>2022-09-15</Date>\n"
            + "<BrokerCommission>70.0</BrokerCommission>\n"
            + "</PortfolioElementModel>\n"
            + "</PortfolioModel>";
    fw = new java.io.FileWriter("TestingHelper//"
            + "multipleSameFlexiblePortfolioElementCorrectFormat.xml");
    fw.write(multipleSameFlexiblePortfolioElementCorrectFormat);
    fw.close();

    String negativeQuantityFlexiblePortfolioElement = "<PortfolioModel>\n"
            + "<PortfolioElementModel>\n"
            + "<StockTicker> GOOG </StockTicker>\n"
            + "<totalQuantity> -1000 </totalQuantity>\n"
            + "<Date> 2022-10-27 </Date>\n"
            + "<BrokerCommission> 100.9</BrokerCommission>\n"
            + "</PortfolioElementModel>\n"
            + "</PortfolioModel>";
    fw = new java.io.FileWriter("TestingHelper//negativeQuantityFlexiblePortfolioElement.xml");
    fw.write(negativeQuantityFlexiblePortfolioElement);
    fw.close();
  }
}
