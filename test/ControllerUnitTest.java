import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Date;

import controller.Controller;
import controller.ControllerImpl;
import fakes.FakeStockSetModel;
import fakes.FakeTextBasedView;
import fakes.FakeUserSetModel;
import models.StockModelImpl;
import models.StockSetModel;
import models.UserSetModel;
import models.UserStockModel;
import models.UserStockModelImpl;
import views.TextBasedView;

import static org.junit.Assert.assertEquals;

/**
 * Unit test class to ensure controller is
 * working correctly.
 */
public class ControllerUnitTest {
  private final String rootDirectory;
  private UserSetModel userSetModel;
  private StockSetModel stockSetModel;
  private UserStockModel userStockModel;
  private TextBasedView textBasedView;
  private StringBuilder builder;

  public ControllerUnitTest() {
    this.rootDirectory = "TestUserSetModel";
  }

  @Before
  public void initializeTest() throws IOException {
    builder = new StringBuilder();
    userSetModel = new FakeUserSetModel(builder);
    stockSetModel = new FakeStockSetModel(builder);
    userStockModel = new UserStockModelImpl(userSetModel, stockSetModel);
    textBasedView = new FakeTextBasedView(builder);
    fileWriteUp();
  }

  @Test
  public void testControllerInitialization() {
    String desiredOutput = "[Stock Set Model]Load stocks from database\n"
            + "[User Set Model]Load user set from database, root: TestUserSetModel\n"
            + "[Text Based View]Show welcome dialog\n";

    String input = "3";

    InputStream in = new ByteArrayInputStream(input.getBytes());
    Controller controller = new ControllerImpl(userStockModel, textBasedView, in, rootDirectory);
    controller.start();
    assertEquals(desiredOutput, builder.toString());
  }

  @Test
  public void testCreateUser() {
    String desiredOutput = "[Stock Set Model]Load stocks from database\n"
            + "[User Set Model]Load user set from database, root: TestUserSetModel\n"
            + "[Text Based View]Show welcome dialog\n"
            + "[Text Based View]Show create user dialog\n"
            + "[User Set Model]Get List Size\n"
            + "[User Set Model]Get user list\n"
            + "[User Set Model]Get user list\n"
            + "[User Set Model]Generate xml: TestUserSetModel\n"
            + "[Text Based View]Show user create success prompt\n"
            + "[Text Based View]Show welcome user prompt: Test user\n"
            + "[Text Based View]Show main menu\n"
            + "[Text Based View]Show welcome dialog\n";

    String input = "1\n"
            + "Test user\n"
            + "8\n"
            + "3\n";

    InputStream in = new ByteArrayInputStream(input.getBytes());
    Controller controller = new ControllerImpl(userStockModel, textBasedView, in, rootDirectory);
    controller.start();
    assertEquals(desiredOutput, builder.toString());
  }

  @Test
  public void testCreateNewUserWhenInvalidNameAdded() {
    String desiredOutput = "[Stock Set Model]Load stocks from database\n"
            + "[User Set Model]Load user set from database, root: TestUserSetModel\n"
            + "[Text Based View]Show welcome dialog\n"
            + "[Text Based View]Show create user dialog\n"
            + "[Text Based View]Show no special characters allowed prompt\n"
            + "[User Set Model]Get List Size\n"
            + "[User Set Model]Get user list\n"
            + "[User Set Model]Get user list\n"
            + "[User Set Model]Generate xml: TestUserSetModel\n"
            + "[Text Based View]Show user create success prompt\n"
            + "[Text Based View]Show welcome user prompt: XYZ\n"
            + "[Text Based View]Show main menu\n"
            + "[Text Based View]Show welcome dialog\n";

    String input = "1"
            + "\n*(@)@"
            + "\nXYZ"
            + "\n8"
            + "\n3";

    InputStream in = new ByteArrayInputStream(input.getBytes());
    Controller controller = new ControllerImpl(userStockModel,
            this.textBasedView, in, rootDirectory);
    controller.start();
    assertEquals(desiredOutput, builder.toString());
  }

  @Test
  public void testLoginUser() {
    String desiredOutput = "[Stock Set Model]Load stocks from database\n"
            + "[User Set Model]Load user set from database, root: TestUserSetModel\n"
            + "[Text Based View]Show welcome dialog\n"
            + "[Text Based View]Show create user dialog\n"
            + "[User Set Model]Get List Size\n"
            + "[User Set Model]Get user list\n"
            + "[User Set Model]Get user list\n"
            + "[User Set Model]Generate xml: TestUserSetModel\n"
            + "[Text Based View]Show user create success prompt\n"
            + "[Text Based View]Show welcome user prompt: XYZ\n"
            + "[Text Based View]Show main menu\n"
            + "[Text Based View]Show welcome dialog\n"
            + "[Text Based View]Show enter user id prompt\n"
            + "[User Set Model]Get user list\n"
            + "[User Set Model]Get user list\n"
            + "[Text Based View]Show login success prompt\n"
            + "[Text Based View]Show welcome user prompt: Test user\n"
            + "[Text Based View]Show main menu\n"
            + "[Text Based View]Show welcome dialog\n";

    String input = "1"
            + "\nXYZ"
            + "\n8"
            + "\n2"
            + "\n1000"
            + "\n8"
            + "\n3";

    InputStream in = new ByteArrayInputStream(input.getBytes());
    Controller controller = new ControllerImpl(userStockModel,
            textBasedView, in, rootDirectory);
    controller.start();
    assertEquals(desiredOutput, builder.toString());
  }

  @Test
  public void testLoginUserWhenNoUserPresent() {
    String desiredOutput = "[Stock Set Model]Load stocks from database\n"
            + "[User Set Model]Load user set from database, root: TestUserSetModel\n"
            + "[Text Based View]Show welcome dialog\n"
            + "[Text Based View]Show enter user id prompt\n"
            + "[Text Based View]Show no user found prompt\n"
            + "[Text Based View]Show welcome dialog\n";

    String input = "2"
            + "\n8"
            + "\n3";

    InputStream in = new ByteArrayInputStream(input.getBytes());
    Controller controller = new ControllerImpl(this.userStockModel,
            this.textBasedView, in, rootDirectory);
    controller.start();
    assertEquals(desiredOutput, builder.toString());
  }

  @Test
  public void testLoginUserWhenIdNotPresent() {
    String desiredOutput = "[Stock Set Model]Load stocks from database\n"
            + "[User Set Model]Load user set from database, root: TestUserSetModel\n"
            + "[Text Based View]Show welcome dialog\n"
            + "[Text Based View]Show create user dialog\n"
            + "[User Set Model]Get List Size\n"
            + "[User Set Model]Get user list\n"
            + "[User Set Model]Get user list\n"
            + "[User Set Model]Generate xml: TestUserSetModel\n"
            + "[Text Based View]Show user create success prompt\n"
            + "[Text Based View]Show welcome user prompt: XYZ\n"
            + "[Text Based View]Show main menu\n"
            + "[Text Based View]Show welcome dialog\n"
            + "[Text Based View]Show enter user id prompt\n"
            + "[User Set Model]Get user list\n"
            + "[Text Based View]Show no user found prompt\n"
            + "[Text Based View]Show welcome dialog\n"
            + "[Text Based View]Show enter user id prompt\n"
            + "[User Set Model]Get user list\n"
            + "[User Set Model]Get user list\n"
            + "[Text Based View]Show login success prompt\n"
            + "[Text Based View]Show welcome user prompt: Test user\n"
            + "[Text Based View]Show main menu\n"
            + "[Text Based View]Show welcome dialog\n";

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
    assertEquals(desiredOutput, builder.toString());
  }

  @Test
  public void addPortfolioManually() {
    String desiredOutput = "[Stock Set Model]Load stocks from database\n"
            + "[User Set Model]Load user set from database, root: TestUserSetModel\n"
            + "[Text Based View]Show welcome dialog\n"
            + "[Text Based View]Show create user dialog\n"
            + "[User Set Model]Get List Size\n"
            + "[User Set Model]Get user list\n"
            + "[User Set Model]Get user list\n"
            + "[User Set Model]Generate xml: TestUserSetModel\n"
            + "[Text Based View]Show user create success prompt\n"
            + "[Text Based View]Show welcome user prompt: XYZ\n"
            + "[Text Based View]Show main menu\n"
            + "[Text Based View]Show Different Add Portfolio type options called.\n"
            + "[Text Based View]Show add portfolio name prompt\n"
            + "[Text Based View]Show portfolio options\n"
            + "[Text Based View]Show total number of stocks to be added prompt\n"
            + "[Text Based View]Show enter stock ticker prompt\n"
            + "[Stock Set Model]Get stock: GOOG\n"
            + "[Stock Set Model]Get stock: GOOG\n"
            + "[Text Based View]Show enter date prompt\n"
            + "[Text Based View]Show enter quantity prompt\n"
            + "[Stock Set Model]Get stock: GOOG\n"
            + "[User Set Model]Get user list\n"
            + "[User Set Model]Get user list\n"
            + "[User Set Model]Generate xml: TestUserSetModel\n"
            + "[Text Based View]Show portfolio add success message prompt: Portfolio 1\n"
            + "[Text Based View]Show main menu\n"
            + "[Text Based View]Show welcome dialog\n";

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

    stockSetModel = new FakeStockSetModel(builder, StockModelImpl.getBuilder()
            .stockTicker("GOOG")
            .ipoDate(new Date())
            .stockName("Google")
            .exchangeName("NASDAQ")
            .build());

    this.userStockModel = new UserStockModelImpl(this.userSetModel, this.stockSetModel);

    InputStream in = new ByteArrayInputStream(input.getBytes());
    Controller controller = new ControllerImpl(this.userStockModel,
            this.textBasedView, in, rootDirectory);
    controller.start();
    assertEquals(desiredOutput, builder.toString());
  }

  @Test
  public void addMultipleStocksToPortfolioManually() {
    String desiredOutput = "[Stock Set Model]Load stocks from database\n"
            + "[User Set Model]Load user set from database, root: TestUserSetModel\n"
            + "[Text Based View]Show welcome dialog\n"
            + "[Text Based View]Show create user dialog\n"
            + "[User Set Model]Get List Size\n"
            + "[User Set Model]Get user list\n"
            + "[User Set Model]Get user list\n"
            + "[User Set Model]Generate xml: TestUserSetModel\n"
            + "[Text Based View]Show user create success prompt\n"
            + "[Text Based View]Show welcome user prompt: XYZ\n"
            + "[Text Based View]Show main menu\n"
            + "[Text Based View]Show Different Add Portfolio type options called.\n"
            + "[Text Based View]Show add portfolio name prompt\n"
            + "[Text Based View]Show portfolio options\n"
            + "[Text Based View]Show total number of stocks to be added prompt\n"
            + "[Text Based View]Show enter stock ticker prompt\n"
            + "[Stock Set Model]Get stock: GOOG\n"
            + "[Stock Set Model]Get stock: GOOG\n"
            + "[Text Based View]Show enter date prompt\n"
            + "[Text Based View]Show enter quantity prompt\n"
            + "[Text Based View]Show enter stock ticker prompt\n"
            + "[Stock Set Model]Get stock: AMZN\n"
            + "[Stock Set Model]Get stock: AMZN\n"
            + "[Text Based View]Show enter date prompt\n"
            + "[Text Based View]Show enter quantity prompt\n"
            + "[Stock Set Model]Get stock: GOOG\n"
            + "[User Set Model]Get user list\n"
            + "[User Set Model]Get user list\n"
            + "[User Set Model]Generate xml: TestUserSetModel\n"
            + "[Text Based View]Show portfolio add success message prompt: Portfolio 1\n"
            + "[Text Based View]Show main menu\n"
            + "[Text Based View]Show welcome dialog\n";

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

    stockSetModel = new FakeStockSetModel(builder, StockModelImpl.getBuilder()
            .stockTicker("GOOG")
            .ipoDate(new Date())
            .stockName("Google")
            .exchangeName("NASDAQ")
            .build(),
            StockModelImpl.getBuilder()
                    .stockTicker("AMZN")
                    .ipoDate(new Date())
                    .stockName("Amazon")
                    .exchangeName("NASDAQ")
                    .build());

    this.userStockModel = new UserStockModelImpl(this.userSetModel, this.stockSetModel);

    InputStream in = new ByteArrayInputStream(input.getBytes());
    Controller controller = new ControllerImpl(this.userStockModel,
            this.textBasedView, in, rootDirectory);
    controller.start();
    assertEquals(desiredOutput, builder.toString());
  }

  @Test
  public void addZeroStocksToPortfolioManually() {
    String desiredOutput = "[Stock Set Model]Load stocks from database\n"
            + "[User Set Model]Load user set from database, root: TestUserSetModel\n"
            + "[Text Based View]Show welcome dialog\n"
            + "[Text Based View]Show create user dialog\n"
            + "[User Set Model]Get List Size\n"
            + "[User Set Model]Get user list\n"
            + "[User Set Model]Get user list\n"
            + "[User Set Model]Generate xml: TestUserSetModel\n"
            + "[Text Based View]Show user create success prompt\n"
            + "[Text Based View]Show welcome user prompt: XYZ\n"
            + "[Text Based View]Show main menu\n"
            + "[Text Based View]Show Different Add Portfolio type options called.\n"
            + "[Text Based View]Show add portfolio name prompt\n"
            + "[Text Based View]Show portfolio options\n"
            + "[Text Based View]Show total number of stocks to be added prompt\n"
            + "[User Set Model]Get user list\n"
            + "[User Set Model]Get user list\n"
            + "[User Set Model]Generate xml: TestUserSetModel\n"
            + "[Text Based View]Show portfolio add success message prompt: Portfolio 1\n"
            + "[Text Based View]Show main menu\n"
            + "[Text Based View]Show welcome dialog\n";

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
    assertEquals(desiredOutput, builder.toString());
  }

  @Test
  public void addPortfolioWithSameNameAlreadyPresentManually() {
    String desiredOutput = "[Stock Set Model]Load stocks from database\n"
            + "[User Set Model]Load user set from database, root: TestUserSetModel\n"
            + "[Text Based View]Show welcome dialog\n"
            + "[Text Based View]Show create user dialog\n"
            + "[User Set Model]Get List Size\n"
            + "[User Set Model]Get user list\n"
            + "[User Set Model]Get user list\n"
            + "[User Set Model]Generate xml: TestUserSetModel\n"
            + "[Text Based View]Show user create success prompt\n"
            + "[Text Based View]Show welcome user prompt: XYZ\n"
            + "[Text Based View]Show main menu\n"
            + "[Text Based View]Show Different Add Portfolio type options called.\n"
            + "[Text Based View]Show add portfolio name prompt\n"
            + "[Text Based View]Show portfolio options\n"
            + "[Text Based View]Show total number of stocks to be added prompt\n"
            + "[Text Based View]Show enter stock ticker prompt\n"
            + "[Stock Set Model]Get stock: GOOG\n"
            + "[Stock Set Model]Get stock: GOOG\n"
            + "[Text Based View]Show enter date prompt\n"
            + "[Text Based View]Show enter quantity prompt\n"
            + "[Stock Set Model]Get stock: GOOG\n"
            + "[User Set Model]Get user list\n"
            + "[User Set Model]Get user list\n"
            + "[User Set Model]Generate xml: TestUserSetModel\n"
            + "[Text Based View]Show portfolio add success message prompt: Portfolio 1\n"
            + "[Text Based View]Show main menu\n"
            + "[Text Based View]Show Different Add Portfolio type options called.\n"
            + "[Text Based View]Show add portfolio name prompt\n"
            + "[Text Based View]Show portfolio name already exists prompt\n"
            + "[Text Based View]Show add portfolio name prompt\n"
            + "[Text Based View]Show portfolio options\n"
            + "[Text Based View]Show total number of stocks to be added prompt\n"
            + "[Text Based View]Show enter stock ticker prompt\n"
            + "[Stock Set Model]Get stock: GOOG\n"
            + "[Stock Set Model]Get stock: GOOG\n"
            + "[Text Based View]Show enter date prompt\n"
            + "[Text Based View]Show enter quantity prompt\n"
            + "[Stock Set Model]Get stock: GOOG\n"
            + "[User Set Model]Get user list\n"
            + "[User Set Model]Get user list\n"
            + "[User Set Model]Generate xml: TestUserSetModel\n"
            + "[Text Based View]Show portfolio add success message prompt: Portfolio 2\n"
            + "[Text Based View]Show main menu\n"
            + "[Text Based View]Show welcome dialog\n";

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

    stockSetModel = new FakeStockSetModel(builder, StockModelImpl.getBuilder()
            .stockTicker("GOOG")
            .ipoDate(new Date())
            .stockName("Google")
            .exchangeName("NASDAQ")
            .build());

    this.userStockModel = new UserStockModelImpl(this.userSetModel, this.stockSetModel);

    InputStream in = new ByteArrayInputStream(input.getBytes());
    Controller controller = new ControllerImpl(this.userStockModel,
            this.textBasedView, in, rootDirectory);
    controller.start();
    assertEquals(desiredOutput, builder.toString());
  }

  @Test
  public void testAddPortfolioManuallyWhenMoreThanOneSameStocksAdded() {
    String desiredOutput = "[Stock Set Model]Load stocks from database\n"
            + "[User Set Model]Load user set from database, root: TestUserSetModel\n"
            + "[Text Based View]Show welcome dialog\n"
            + "[Text Based View]Show create user dialog\n"
            + "[User Set Model]Get List Size\n"
            + "[User Set Model]Get user list\n"
            + "[User Set Model]Get user list\n"
            + "[User Set Model]Generate xml: TestUserSetModel\n"
            + "[Text Based View]Show user create success prompt\n"
            + "[Text Based View]Show welcome user prompt: XYZ\n"
            + "[Text Based View]Show main menu\n"
            + "[Text Based View]Show Different Add Portfolio type options called.\n"
            + "[Text Based View]Show add portfolio name prompt\n"
            + "[Text Based View]Show portfolio options\n"
            + "[Text Based View]Show total number of stocks to be added prompt\n"
            + "[Text Based View]Show enter stock ticker prompt\n"
            + "[Stock Set Model]Get stock: GOOG\n"
            + "[Stock Set Model]Get stock: GOOG\n"
            + "[Text Based View]Show enter date prompt\n"
            + "[Text Based View]Show enter quantity prompt\n"
            + "[Text Based View]Show enter stock ticker prompt\n"
            + "[Stock Set Model]Get stock: GOOG\n"
            + "[Stock Set Model]Get stock: GOOG\n"
            + "[Text Based View]Show enter date prompt\n"
            + "[Text Based View]Show enter quantity prompt\n"
            + "[Stock Set Model]Get stock: GOOG\n"
            + "[User Set Model]Get user list\n"
            + "[User Set Model]Get user list\n"
            + "[User Set Model]Generate xml: TestUserSetModel\n"
            + "[Text Based View]Show portfolio add success message prompt: Portfolio 1\n"
            + "[Text Based View]Show main menu\n"
            + "[Text Based View]Show welcome dialog\n";

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


    stockSetModel = new FakeStockSetModel(builder, StockModelImpl.getBuilder()
            .stockTicker("GOOG")
            .ipoDate(new Date())
            .stockName("Google")
            .exchangeName("NASDAQ")
            .build());

    this.userStockModel = new UserStockModelImpl(this.userSetModel, this.stockSetModel);

    InputStream in = new ByteArrayInputStream(input.getBytes());
    Controller controller = new ControllerImpl(this.userStockModel,
            this.textBasedView, in, rootDirectory);
    controller.start();
    assertEquals(desiredOutput, builder.toString());
  }

  @Test
  public void testAddPortfolioManuallyWhenMoreThanOneSameStocksAddedWhenOneIsShorted() {
    String desiredOutput = "[Stock Set Model]Load stocks from database\n"
            + "[User Set Model]Load user set from database, root: TestUserSetModel\n"
            + "[Text Based View]Show welcome dialog\n"
            + "[Text Based View]Show create user dialog\n"
            + "[User Set Model]Get List Size\n"
            + "[User Set Model]Get user list\n"
            + "[User Set Model]Get user list\n"
            + "[User Set Model]Generate xml: TestUserSetModel\n"
            + "[Text Based View]Show user create success prompt\n"
            + "[Text Based View]Show welcome user prompt: XYZ\n"
            + "[Text Based View]Show main menu\n"
            + "[Text Based View]Show Different Add Portfolio type options called.\n"
            + "[Text Based View]Show add portfolio name prompt\n"
            + "[Text Based View]Show portfolio options\n"
            + "[Text Based View]Show total number of stocks to be added prompt\n"
            + "[Text Based View]Show enter stock ticker prompt\n"
            + "[Stock Set Model]Get stock: GOOG\n"
            + "[Stock Set Model]Get stock: GOOG\n"
            + "[Text Based View]Show enter date prompt\n"
            + "[Text Based View]Show enter quantity prompt\n"
            + "[Text Based View]Show enter stock ticker prompt\n"
            + "[Stock Set Model]Get stock: GOOG\n"
            + "[Stock Set Model]Get stock: GOOG\n"
            + "[Text Based View]Show enter date prompt\n"
            + "[Text Based View]Show enter quantity prompt\n"
            + "[Stock Set Model]Get stock: GOOG\n"
            + "[User Set Model]Get user list\n"
            + "[User Set Model]Get user list\n"
            + "[User Set Model]Generate xml: TestUserSetModel\n"
            + "[Text Based View]Show portfolio add success message prompt: Portfolio 1\n"
            + "[Text Based View]Show main menu\n"
            + "[Text Based View]Show welcome dialog\n";

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


    stockSetModel = new FakeStockSetModel(builder, StockModelImpl.getBuilder()
            .stockTicker("GOOG")
            .ipoDate(new Date())
            .stockName("Google")
            .exchangeName("NASDAQ")
            .build());

    this.userStockModel = new UserStockModelImpl(this.userSetModel, this.stockSetModel);

    InputStream in = new ByteArrayInputStream(input.getBytes());
    Controller controller = new ControllerImpl(this.userStockModel,
            this.textBasedView, in, rootDirectory);
    controller.start();
    assertEquals(desiredOutput, builder.toString());
  }

  @Test
  public void addPortfolioManuallyWhenQuantityIsNegative() {
    String desiredOutput = "[Stock Set Model]Load stocks from database\n"
            + "[User Set Model]Load user set from database, root: TestUserSetModel\n"
            + "[Text Based View]Show welcome dialog\n"
            + "[Text Based View]Show create user dialog\n"
            + "[User Set Model]Get List Size\n"
            + "[User Set Model]Get user list\n"
            + "[User Set Model]Get user list\n"
            + "[User Set Model]Generate xml: TestUserSetModel\n"
            + "[Text Based View]Show user create success prompt\n"
            + "[Text Based View]Show welcome user prompt: XYZ\n"
            + "[Text Based View]Show main menu\n"
            + "[Text Based View]Show Different Add Portfolio type options called.\n"
            + "[Text Based View]Show add portfolio name prompt\n"
            + "[Text Based View]Show portfolio options\n"
            + "[Text Based View]Show total number of stocks to be added prompt\n"
            + "[Text Based View]Show enter stock ticker prompt\n"
            + "[Stock Set Model]Get stock: GOOG\n"
            + "[Stock Set Model]Get stock: GOOG\n"
            + "[Text Based View]Show enter date prompt\n"
            + "[Text Based View]Show enter quantity prompt\n"
            + "[Text Based View]Show Invalid transaction as quantity negative called.\n"
            + "[Text Based View]Show enter quantity prompt\n"
            + "[Stock Set Model]Get stock: GOOG\n"
            + "[User Set Model]Get user list\n"
            + "[User Set Model]Get user list\n"
            + "[User Set Model]Generate xml: TestUserSetModel\n"
            + "[Text Based View]Show portfolio add success message prompt: Portfolio 1\n"
            + "[Text Based View]Show main menu\n"
            + "[Text Based View]Show welcome dialog\n";

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

    stockSetModel = new FakeStockSetModel(builder, StockModelImpl.getBuilder()
            .stockTicker("GOOG")
            .ipoDate(new Date())
            .stockName("Google")
            .exchangeName("NASDAQ")
            .build());

    this.userStockModel = new UserStockModelImpl(this.userSetModel, this.stockSetModel);

    InputStream in = new ByteArrayInputStream(input.getBytes());
    Controller controller = new ControllerImpl(this.userStockModel,
            this.textBasedView, in, rootDirectory);
    controller.start();
    assertEquals(desiredOutput, builder.toString());
  }

  @Test
  public void testInvalidTickerNameWhileAddingPortfolioRejected() {
    String desiredOutput = "[Stock Set Model]Load stocks from database\n"
            + "[User Set Model]Load user set from database, root: TestUserSetModel\n"
            + "[Text Based View]Show welcome dialog\n"
            + "[Text Based View]Show create user dialog\n"
            + "[User Set Model]Get List Size\n"
            + "[User Set Model]Get user list\n"
            + "[User Set Model]Get user list\n"
            + "[User Set Model]Generate xml: TestUserSetModel\n"
            + "[Text Based View]Show user create success prompt\n"
            + "[Text Based View]Show welcome user prompt: XYZ\n"
            + "[Text Based View]Show main menu\n"
            + "[Text Based View]Show Different Add Portfolio type options called.\n"
            + "[Text Based View]Show add portfolio name prompt\n"
            + "[Text Based View]Show portfolio options\n"
            + "[Text Based View]Show total number of stocks to be added prompt\n"
            + "[Text Based View]Show enter stock ticker prompt\n"
            + "[Stock Set Model]Get stock: XYZ\n"
            + "[Stock Set Model]Get stock: XYZ\n"
            + "[Text Based View]Show enter date prompt\n"
            + "[Text Based View]Show wrong input prompt\n"
            + "[Text Based View]Show enter date prompt\n"
            + "[Text Based View]Show enter quantity prompt\n"
            + "[Stock Set Model]Get stock: GOOG\n"
            + "[User Set Model]Get user list\n"
            + "[User Set Model]Get user list\n"
            + "[User Set Model]Generate xml: TestUserSetModel\n"
            + "[Text Based View]Show portfolio add success message prompt: Portfolio 1\n"
            + "[Text Based View]Show main menu\n"
            + "[Text Based View]Show welcome dialog\n";

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

    stockSetModel = new FakeStockSetModel(builder, StockModelImpl.getBuilder()
            .stockTicker("GOOG")
            .ipoDate(new Date())
            .stockName("Google")
            .exchangeName("NASDAQ")
            .build());

    this.userStockModel = new UserStockModelImpl(this.userSetModel, this.stockSetModel);

    InputStream in = new ByteArrayInputStream(input.getBytes());
    Controller controller = new ControllerImpl(this.userStockModel,
            this.textBasedView, in, rootDirectory);
    controller.start();
    assertEquals(desiredOutput, builder.toString());
  }

  @Test
  public void testInvalidDateWhileAddingPortfolioRejected() {
    String desiredOutput = "[Stock Set Model]Load stocks from database\n"
            + "[User Set Model]Load user set from database, root: TestUserSetModel\n"
            + "[Text Based View]Show welcome dialog\n"
            + "[Text Based View]Show create user dialog\n"
            + "[User Set Model]Get List Size\n"
            + "[User Set Model]Get user list\n"
            + "[User Set Model]Get user list\n"
            + "[User Set Model]Generate xml: TestUserSetModel\n"
            + "[Text Based View]Show user create success prompt\n"
            + "[Text Based View]Show welcome user prompt: XYZ\n"
            + "[Text Based View]Show main menu\n"
            + "[Text Based View]Show Different Add Portfolio type options called.\n"
            + "[Text Based View]Show add portfolio name prompt\n"
            + "[Text Based View]Show portfolio options\n"
            + "[Text Based View]Show total number of stocks to be added prompt\n"
            + "[Text Based View]Show enter stock ticker prompt\n"
            + "[Stock Set Model]Get stock: GOOG\n"
            + "[Stock Set Model]Get stock: GOOG\n"
            + "[Text Based View]Show enter date prompt\n"
            + "[Text Based View]Show wrong input prompt\n"
            + "[Text Based View]Show enter date prompt\n"
            + "[Text Based View]Show enter quantity prompt\n"
            + "[Stock Set Model]Get stock: GOOG\n"
            + "[User Set Model]Get user list\n"
            + "[User Set Model]Get user list\n"
            + "[User Set Model]Generate xml: TestUserSetModel\n"
            + "[Text Based View]Show portfolio add success message prompt: Portfolio 1\n"
            + "[Text Based View]Show main menu\n"
            + "[Text Based View]Show welcome dialog\n";

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

    stockSetModel = new FakeStockSetModel(builder, StockModelImpl.getBuilder()
            .stockTicker("GOOG")
            .ipoDate(new Date())
            .stockName("Google")
            .exchangeName("NASDAQ")
            .build());

    this.userStockModel = new UserStockModelImpl(this.userSetModel, this.stockSetModel);

    InputStream in = new ByteArrayInputStream(input.getBytes());
    Controller controller = new ControllerImpl(this.userStockModel,
            this.textBasedView, in, rootDirectory);
    controller.start();
    assertEquals(desiredOutput, builder.toString());
  }

  @Test
  public void testInvalidQuantityWhileAddingPortfolioRejected() {
    String desiredOutput = "[Stock Set Model]Load stocks from database\n"
            + "[User Set Model]Load user set from database, root: TestUserSetModel\n"
            + "[Text Based View]Show welcome dialog\n"
            + "[Text Based View]Show create user dialog\n"
            + "[User Set Model]Get List Size\n"
            + "[User Set Model]Get user list\n"
            + "[User Set Model]Get user list\n"
            + "[User Set Model]Generate xml: TestUserSetModel\n"
            + "[Text Based View]Show user create success prompt\n"
            + "[Text Based View]Show welcome user prompt: XYZ\n"
            + "[Text Based View]Show main menu\n"
            + "[Text Based View]Show Different Add Portfolio type options called.\n"
            + "[Text Based View]Show add portfolio name prompt\n"
            + "[Text Based View]Show portfolio options\n"
            + "[Text Based View]Show total number of stocks to be added prompt\n"
            + "[Text Based View]Show enter stock ticker prompt\n"
            + "[Stock Set Model]Get stock: GOOG\n"
            + "[Stock Set Model]Get stock: GOOG\n"
            + "[Text Based View]Show enter date prompt\n"
            + "[Text Based View]Show enter quantity prompt\n"
            + "[Text Based View]Show wrong input prompt\n"
            + "[Text Based View]Show enter quantity prompt\n"
            + "[Stock Set Model]Get stock: GOOG\n"
            + "[User Set Model]Get user list\n"
            + "[User Set Model]Get user list\n"
            + "[User Set Model]Generate xml: TestUserSetModel\n"
            + "[Text Based View]Show portfolio add success message prompt: Portfolio 1\n"
            + "[Text Based View]Show main menu\n"
            + "[Text Based View]Show welcome dialog\n";

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

    stockSetModel = new FakeStockSetModel(builder, StockModelImpl.getBuilder()
            .stockTicker("GOOG")
            .ipoDate(new Date())
            .stockName("Google")
            .exchangeName("NASDAQ")
            .build());

    this.userStockModel = new UserStockModelImpl(this.userSetModel, this.stockSetModel);

    InputStream in = new ByteArrayInputStream(input.getBytes());
    Controller controller = new ControllerImpl(this.userStockModel,
            this.textBasedView, in, rootDirectory);
    controller.start();
    assertEquals(desiredOutput, builder.toString());
  }

  @Test
  public void testZeroQuantityWhileAddingPortfolioWorks() {
    String desiredOutput = "[Stock Set Model]Load stocks from database\n"
            + "[User Set Model]Load user set from database, root: TestUserSetModel\n"
            + "[Text Based View]Show welcome dialog\n"
            + "[Text Based View]Show create user dialog\n"
            + "[User Set Model]Get List Size\n"
            + "[User Set Model]Get user list\n"
            + "[User Set Model]Get user list\n"
            + "[User Set Model]Generate xml: TestUserSetModel\n"
            + "[Text Based View]Show user create success prompt\n"
            + "[Text Based View]Show welcome user prompt: XYZ\n"
            + "[Text Based View]Show main menu\n"
            + "[Text Based View]Show Different Add Portfolio type options called.\n"
            + "[Text Based View]Show add portfolio name prompt\n"
            + "[Text Based View]Show portfolio options\n"
            + "[Text Based View]Show total number of stocks to be added prompt\n"
            + "[Text Based View]Show enter stock ticker prompt\n"
            + "[Stock Set Model]Get stock: GOOG\n"
            + "[Stock Set Model]Get stock: GOOG\n"
            + "[Text Based View]Show enter date prompt\n"
            + "[Text Based View]Show enter quantity prompt\n"
            + "[Stock Set Model]Get stock: GOOG\n"
            + "[User Set Model]Get user list\n"
            + "[User Set Model]Get user list\n"
            + "[User Set Model]Generate xml: TestUserSetModel\n"
            + "[Text Based View]Show portfolio add success message prompt: Portfolio 1\n"
            + "[Text Based View]Show main menu\n"
            + "[Text Based View]Show welcome dialog\n";

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

    stockSetModel = new FakeStockSetModel(builder, StockModelImpl.getBuilder()
            .stockTicker("GOOG")
            .ipoDate(new Date())
            .stockName("Google")
            .exchangeName("NASDAQ")
            .build());

    this.userStockModel = new UserStockModelImpl(this.userSetModel, this.stockSetModel);

    InputStream in = new ByteArrayInputStream(input.getBytes());
    Controller controller = new ControllerImpl(this.userStockModel,
            this.textBasedView, in, rootDirectory);
    controller.start();
    assertEquals(desiredOutput, builder.toString());
  }

  @Test
  public void testNegativeQuantityWhileAddingPortfolioDoesNotWork() {
    String desiredOutput = "[Stock Set Model]Load stocks from database\n"
            + "[User Set Model]Load user set from database, root: TestUserSetModel\n"
            + "[Text Based View]Show welcome dialog\n"
            + "[Text Based View]Show create user dialog\n"
            + "[User Set Model]Get List Size\n"
            + "[User Set Model]Get user list\n"
            + "[User Set Model]Get user list\n"
            + "[User Set Model]Generate xml: TestUserSetModel\n"
            + "[Text Based View]Show user create success prompt\n"
            + "[Text Based View]Show welcome user prompt: XYZ\n"
            + "[Text Based View]Show main menu\n"
            + "[Text Based View]Show Different Add Portfolio type options called.\n"
            + "[Text Based View]Show add portfolio name prompt\n"
            + "[Text Based View]Show portfolio options\n"
            + "[Text Based View]Show total number of stocks to be added prompt\n"
            + "[Text Based View]Show enter stock ticker prompt\n"
            + "[Stock Set Model]Get stock: GOOG\n"
            + "[Stock Set Model]Get stock: GOOG\n"
            + "[Text Based View]Show enter date prompt\n"
            + "[Text Based View]Show enter quantity prompt\n"
            + "[Text Based View]Show Invalid transaction as quantity negative called.\n"
            + "[Text Based View]Show enter quantity prompt\n"
            + "[Stock Set Model]Get stock: GOOG\n"
            + "[User Set Model]Get user list\n"
            + "[User Set Model]Get user list\n"
            + "[User Set Model]Generate xml: TestUserSetModel\n"
            + "[Text Based View]Show portfolio add success message prompt: Portfolio 1\n"
            + "[Text Based View]Show main menu\n"
            + "[Text Based View]Show welcome dialog\n";

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

    stockSetModel = new FakeStockSetModel(builder, StockModelImpl.getBuilder()
            .stockTicker("GOOG")
            .ipoDate(new Date())
            .stockName("Google")
            .exchangeName("NASDAQ")
            .build());

    this.userStockModel = new UserStockModelImpl(this.userSetModel, this.stockSetModel);

    InputStream in = new ByteArrayInputStream(input.getBytes());
    Controller controller = new ControllerImpl(this.userStockModel,
            this.textBasedView, in, rootDirectory);
    controller.start();
    assertEquals(desiredOutput, builder.toString());
  }

  @Test
  public void testDecimalQuantityWhileAddingPortfolioRejected() {
    String desiredOutput = "[Stock Set Model]Load stocks from database\n"
            + "[User Set Model]Load user set from database, root: TestUserSetModel\n"
            + "[Text Based View]Show welcome dialog\n"
            + "[Text Based View]Show create user dialog\n"
            + "[User Set Model]Get List Size\n"
            + "[User Set Model]Get user list\n"
            + "[User Set Model]Get user list\n"
            + "[User Set Model]Generate xml: TestUserSetModel\n"
            + "[Text Based View]Show user create success prompt\n"
            + "[Text Based View]Show welcome user prompt: XYZ\n"
            + "[Text Based View]Show main menu\n"
            + "[Text Based View]Show Different Add Portfolio type options called.\n"
            + "[Text Based View]Show add portfolio name prompt\n"
            + "[Text Based View]Show portfolio options\n"
            + "[Text Based View]Show total number of stocks to be added prompt\n"
            + "[Text Based View]Show enter stock ticker prompt\n"
            + "[Stock Set Model]Get stock: GOOG\n"
            + "[Stock Set Model]Get stock: GOOG\n"
            + "[Text Based View]Show enter date prompt\n"
            + "[Text Based View]Show enter quantity prompt\n"
            + "[Text Based View]Show wrong input prompt\n"
            + "[Text Based View]Show enter quantity prompt\n"
            + "[Stock Set Model]Get stock: GOOG\n"
            + "[User Set Model]Get user list\n"
            + "[User Set Model]Get user list\n"
            + "[User Set Model]Generate xml: TestUserSetModel\n"
            + "[Text Based View]Show portfolio add success message prompt: Portfolio 1\n"
            + "[Text Based View]Show main menu\n"
            + "[Text Based View]Show welcome dialog\n";

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

    stockSetModel = new FakeStockSetModel(builder, StockModelImpl.getBuilder()
            .stockTicker("GOOG")
            .ipoDate(new Date())
            .stockName("Google")
            .exchangeName("NASDAQ")
            .build());

    this.userStockModel = new UserStockModelImpl(this.userSetModel, this.stockSetModel);

    InputStream in = new ByteArrayInputStream(input.getBytes());
    Controller controller = new ControllerImpl(this.userStockModel,
            this.textBasedView, in, rootDirectory);
    controller.start();
    assertEquals(desiredOutput, builder.toString());
  }

  @Test
  public void testDateWithoutDataAddedWhileAddingPortfolioRejected() {
    String desiredOutput = "[Stock Set Model]Load stocks from database\n"
            + "[User Set Model]Load user set from database, root: TestUserSetModel\n"
            + "[Text Based View]Show welcome dialog\n"
            + "[Text Based View]Show create user dialog\n"
            + "[User Set Model]Get List Size\n"
            + "[User Set Model]Get user list\n"
            + "[User Set Model]Get user list\n"
            + "[User Set Model]Generate xml: TestUserSetModel\n"
            + "[Text Based View]Show user create success prompt\n"
            + "[Text Based View]Show welcome user prompt: XYZ\n"
            + "[Text Based View]Show main menu\n"
            + "[Text Based View]Show Different Add Portfolio type options called.\n"
            + "[Text Based View]Show add portfolio name prompt\n"
            + "[Text Based View]Show portfolio options\n"
            + "[Text Based View]Show total number of stocks to be added prompt\n"
            + "[Text Based View]Show enter stock ticker prompt\n"
            + "[Stock Set Model]Get stock: GOOG\n"
            + "[Stock Set Model]Get stock: GOOG\n"
            + "[Text Based View]Show enter date prompt\n"
            + "[Text Based View]Show no data for date prompt\n"
            + "[Text Based View]Show enter date prompt\n"
            + "[Text Based View]Show enter quantity prompt\n"
            + "[Stock Set Model]Get stock: GOOG\n"
            + "[User Set Model]Get user list\n"
            + "[User Set Model]Get user list\n"
            + "[User Set Model]Generate xml: TestUserSetModel\n"
            + "[Text Based View]Show portfolio add success message prompt: Portfolio 1\n"
            + "[Text Based View]Show main menu\n"
            + "[Text Based View]Show welcome dialog\n";

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

    stockSetModel = new FakeStockSetModel(builder, StockModelImpl.getBuilder()
            .stockTicker("GOOG")
            .ipoDate(new Date())
            .stockName("Google")
            .exchangeName("NASDAQ")
            .build());

    this.userStockModel = new UserStockModelImpl(this.userSetModel, this.stockSetModel);

    InputStream in = new ByteArrayInputStream(input.getBytes());
    Controller controller = new ControllerImpl(this.userStockModel,
            this.textBasedView, in, rootDirectory);
    controller.start();
    assertEquals(desiredOutput, builder.toString());
  }

  @Test
  public void addPortfolioViaFile() {
    String desiredOutput = "[Stock Set Model]Load stocks from database\n"
            + "[User Set Model]Load user set from database, root: TestUserSetModel\n"
            + "[Text Based View]Show welcome dialog\n"
            + "[Text Based View]Show create user dialog\n"
            + "[User Set Model]Get List Size\n"
            + "[User Set Model]Get user list\n"
            + "[User Set Model]Get user list\n"
            + "[User Set Model]Generate xml: TestUserSetModel\n"
            + "[Text Based View]Show user create success prompt\n"
            + "[Text Based View]Show welcome user prompt: XYZ\n"
            + "[Text Based View]Show main menu\n"
            + "[Text Based View]Show Different Add Portfolio type options called.\n"
            + "[Text Based View]Show add portfolio name prompt\n"
            + "[Text Based View]Show portfolio options\n"
            + "[Text Based View]Show enter file path prompt\n"
            + "[Stock Set Model]Get stock: GOOG\n"
            + "[Stock Set Model]Get stock: GOOG\n"
            + "[Stock Set Model]Get stock: GOOG\n"
            + "[Stock Set Model]Get stock: GOOG\n"
            + "[User Set Model]Get user list\n"
            + "[User Set Model]Get user list\n"
            + "[User Set Model]Get user list\n"
            + "[User Set Model]Generate xml: TestUserSetModel\n"
            + "[Text Based View]Show portfolio add success message prompt: Portfolio 1\n"
            + "[Text Based View]Show main menu\n"
            + "[Text Based View]Show welcome dialog\n";

    String input = "1"
            + "\nXYZ"
            + "\n1"
            + "\n2"
            + "\nPortfolio 1"
            + "\n2"
            + "\nTestingHelper/singlePortfolioElementCorrectFormat.xml"
            + "\n8"
            + "\n3";

    stockSetModel = new FakeStockSetModel(builder, StockModelImpl.getBuilder()
            .stockTicker("GOOG")
            .ipoDate(new Date())
            .stockName("Google")
            .exchangeName("NASDAQ")
            .build());

    this.userStockModel = new UserStockModelImpl(this.userSetModel, this.stockSetModel);

    InputStream in = new ByteArrayInputStream(input.getBytes());
    Controller controller = new ControllerImpl(this.userStockModel,
            this.textBasedView, in, rootDirectory);
    controller.start();
    assertEquals(desiredOutput, builder.toString());
  }

  @Test
  public void addPortfolioWithInvalidFileFormatFailsViaFile() {
    String desiredOutput = "[Stock Set Model]Load stocks from database\n"
            + "[User Set Model]Load user set from database, root: TestUserSetModel\n"
            + "[Text Based View]Show welcome dialog\n"
            + "[Text Based View]Show create user dialog\n"
            + "[User Set Model]Get List Size\n"
            + "[User Set Model]Get user list\n"
            + "[User Set Model]Get user list\n"
            + "[User Set Model]Generate xml: TestUserSetModel\n"
            + "[Text Based View]Show user create success prompt\n"
            + "[Text Based View]Show welcome user prompt: XYZ\n"
            + "[Text Based View]Show main menu\n"
            + "[Text Based View]Show Different Add Portfolio type options called.\n"
            + "[Text Based View]Show add portfolio name prompt\n"
            + "[Text Based View]Show portfolio options\n"
            + "[Text Based View]Show enter file path prompt\n"
            + "[Text Based View]Show upload portfolio by xml error prompt\n"
            + "[Text Based View]Show enter file path prompt\n"
            + "[Stock Set Model]Get stock: GOOG\n"
            + "[Stock Set Model]Get stock: GOOG\n"
            + "[Stock Set Model]Get stock: GOOG\n"
            + "[Stock Set Model]Get stock: GOOG\n"
            + "[Stock Set Model]Get stock: AMZN\n"
            + "[Stock Set Model]Get stock: AMZN\n"
            + "[Stock Set Model]Get stock: AMZN\n"
            + "[Stock Set Model]Get stock: AMZN\n"
            + "[User Set Model]Get user list\n"
            + "[User Set Model]Get user list\n"
            + "[User Set Model]Get user list\n"
            + "[User Set Model]Generate xml: TestUserSetModel\n"
            + "[Text Based View]Show portfolio add success message prompt: Portfolio 1\n"
            + "[Text Based View]Show main menu\n"
            + "[Text Based View]Show welcome dialog\n";

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

    stockSetModel = new FakeStockSetModel(builder, StockModelImpl.getBuilder()
            .stockTicker("GOOG")
            .ipoDate(new Date())
            .stockName("Google")
            .exchangeName("NASDAQ")
            .build(),
            StockModelImpl.getBuilder()
                    .stockTicker("AMZN")
                    .ipoDate(new Date())
                    .stockName("Amazon")
                    .exchangeName("NASDAQ")
                    .build());

    this.userStockModel = new UserStockModelImpl(this.userSetModel, this.stockSetModel);

    InputStream in = new ByteArrayInputStream(input.getBytes());
    Controller controller = new ControllerImpl(this.userStockModel,
            this.textBasedView, in, rootDirectory);
    controller.start();
    assertEquals(desiredOutput, builder.toString());
  }

  @Test
  public void addPortfolioWithWrongFilePathFailsViaFile() {
    String desiredOutput = "[Stock Set Model]Load stocks from database\n"
            + "[User Set Model]Load user set from database, root: TestUserSetModel\n"
            + "[Text Based View]Show welcome dialog\n"
            + "[Text Based View]Show create user dialog\n"
            + "[User Set Model]Get List Size\n"
            + "[User Set Model]Get user list\n"
            + "[User Set Model]Get user list\n"
            + "[User Set Model]Generate xml: TestUserSetModel\n"
            + "[Text Based View]Show user create success prompt\n"
            + "[Text Based View]Show welcome user prompt: XYZ\n"
            + "[Text Based View]Show main menu\n"
            + "[Text Based View]Show Different Add Portfolio type options called.\n"
            + "[Text Based View]Show add portfolio name prompt\n"
            + "[Text Based View]Show portfolio options\n"
            + "[Text Based View]Show enter file path prompt\n"
            + "[Text Based View]Show upload portfolio by xml error prompt\n"
            + "[Text Based View]Show enter file path prompt\n"
            + "[Stock Set Model]Get stock: GOOG\n"
            + "[Stock Set Model]Get stock: GOOG\n"
            + "[Stock Set Model]Get stock: GOOG\n"
            + "[Stock Set Model]Get stock: GOOG\n"
            + "[Stock Set Model]Get stock: AMZN\n"
            + "[Stock Set Model]Get stock: AMZN\n"
            + "[Stock Set Model]Get stock: AMZN\n"
            + "[Stock Set Model]Get stock: AMZN\n"
            + "[User Set Model]Get user list\n"
            + "[User Set Model]Get user list\n"
            + "[User Set Model]Get user list\n"
            + "[User Set Model]Generate xml: TestUserSetModel\n"
            + "[Text Based View]Show portfolio add success message prompt: Portfolio 1\n"
            + "[Text Based View]Show main menu\n"
            + "[Text Based View]Show welcome dialog\n";

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


    stockSetModel = new FakeStockSetModel(builder, StockModelImpl.getBuilder()
            .stockTicker("GOOG")
            .ipoDate(new Date())
            .stockName("Google")
            .exchangeName("NASDAQ")
            .build(),
            StockModelImpl.getBuilder()
                    .stockTicker("AMZN")
                    .ipoDate(new Date())
                    .stockName("Amazon")
                    .exchangeName("NASDAQ")
                    .build());

    this.userStockModel = new UserStockModelImpl(this.userSetModel, this.stockSetModel);

    InputStream in = new ByteArrayInputStream(input.getBytes());
    Controller controller = new ControllerImpl(this.userStockModel,
            this.textBasedView, in, rootDirectory);
    controller.start();
    assertEquals(desiredOutput, builder.toString());
  }

  @Test
  public void addPortfolioWithMultipleStocksViaFile() {
    String desiredOutput = "[Stock Set Model]Load stocks from database\n"
            + "[User Set Model]Load user set from database, root: TestUserSetModel\n"
            + "[Text Based View]Show welcome dialog\n"
            + "[Text Based View]Show create user dialog\n"
            + "[User Set Model]Get List Size\n"
            + "[User Set Model]Get user list\n"
            + "[User Set Model]Get user list\n"
            + "[User Set Model]Generate xml: TestUserSetModel\n"
            + "[Text Based View]Show user create success prompt\n"
            + "[Text Based View]Show welcome user prompt: XYZ\n"
            + "[Text Based View]Show main menu\n"
            + "[Text Based View]Show Different Add Portfolio type options called.\n"
            + "[Text Based View]Show add portfolio name prompt\n"
            + "[Text Based View]Show portfolio options\n"
            + "[Text Based View]Show enter file path prompt\n"
            + "[Stock Set Model]Get stock: GOOG\n"
            + "[Stock Set Model]Get stock: GOOG\n"
            + "[Stock Set Model]Get stock: GOOG\n"
            + "[Stock Set Model]Get stock: GOOG\n"
            + "[Stock Set Model]Get stock: AMZN\n"
            + "[Stock Set Model]Get stock: AMZN\n"
            + "[Stock Set Model]Get stock: AMZN\n"
            + "[Stock Set Model]Get stock: AMZN\n"
            + "[User Set Model]Get user list\n"
            + "[User Set Model]Get user list\n"
            + "[User Set Model]Get user list\n"
            + "[User Set Model]Generate xml: TestUserSetModel\n"
            + "[Text Based View]Show portfolio add success message prompt: Portfolio 1\n"
            + "[Text Based View]Show main menu\n"
            + "[Text Based View]Show welcome dialog\n";

    String input = "1"
            + "\nXYZ"
            + "\n1"
            + "\n2"
            + "\nPortfolio 1"
            + "\n2"
            + "\nTestingHelper/multiplePortfolioElementCorrectFormat.xml"
            + "\n8"
            + "\n3";


    stockSetModel = new FakeStockSetModel(builder, StockModelImpl.getBuilder()
            .stockTicker("GOOG")
            .ipoDate(new Date())
            .stockName("Google")
            .exchangeName("NASDAQ")
            .build(),
            StockModelImpl.getBuilder()
                    .stockTicker("AMZN")
                    .ipoDate(new Date())
                    .stockName("Amazon")
                    .exchangeName("NASDAQ")
                    .build());

    this.userStockModel = new UserStockModelImpl(this.userSetModel, this.stockSetModel);

    InputStream in = new ByteArrayInputStream(input.getBytes());
    Controller controller = new ControllerImpl(this.userStockModel,
            this.textBasedView, in, rootDirectory);
    controller.start();
    assertEquals(desiredOutput, builder.toString());
  }

  @Test
  public void addPortfolioWithMultipleSameStocksViaFile() {
    String desiredOutput = "[Stock Set Model]Load stocks from database\n"
            + "[User Set Model]Load user set from database, root: TestUserSetModel\n"
            + "[Text Based View]Show welcome dialog\n"
            + "[Text Based View]Show create user dialog\n"
            + "[User Set Model]Get List Size\n"
            + "[User Set Model]Get user list\n"
            + "[User Set Model]Get user list\n"
            + "[User Set Model]Generate xml: TestUserSetModel\n"
            + "[Text Based View]Show user create success prompt\n"
            + "[Text Based View]Show welcome user prompt: XYZ\n"
            + "[Text Based View]Show main menu\n"
            + "[Text Based View]Show Different Add Portfolio type options called.\n"
            + "[Text Based View]Show add portfolio name prompt\n"
            + "[Text Based View]Show portfolio options\n"
            + "[Text Based View]Show enter file path prompt\n"
            + "[Stock Set Model]Get stock: GOOG\n"
            + "[Stock Set Model]Get stock: GOOG\n"
            + "[Stock Set Model]Get stock: GOOG\n"
            + "[Stock Set Model]Get stock: GOOG\n"
            + "[Stock Set Model]Get stock: GOOG\n"
            + "[Stock Set Model]Get stock: GOOG\n"
            + "[Stock Set Model]Get stock: GOOG\n"
            + "[User Set Model]Get user list\n"
            + "[User Set Model]Get user list\n"
            + "[User Set Model]Get user list\n"
            + "[User Set Model]Generate xml: TestUserSetModel\n"
            + "[Text Based View]Show portfolio add success message prompt: Portfolio 1\n"
            + "[Text Based View]Show main menu\n"
            + "[Text Based View]Show welcome dialog\n";

    String input = "1"
            + "\nXYZ"
            + "\n1"
            + "\n2"
            + "\nPortfolio 1"
            + "\n2"
            + "\nTestingHelper/multipleSamePortfolioElementCorrectFormat.xml"
            + "\n8"
            + "\n3";

    stockSetModel = new FakeStockSetModel(builder, StockModelImpl.getBuilder()
            .stockTicker("GOOG")
            .ipoDate(new Date())
            .stockName("Google")
            .exchangeName("NASDAQ")
            .build());

    this.userStockModel = new UserStockModelImpl(this.userSetModel, this.stockSetModel);

    InputStream in = new ByteArrayInputStream(input.getBytes());
    Controller controller = new ControllerImpl(this.userStockModel,
            this.textBasedView, in, rootDirectory);
    controller.start();
    assertEquals(desiredOutput, builder.toString());
  }

  @Test
  public void addPortfolioWithZeroStocksViaFile() {
    String desiredOutput = "[Stock Set Model]Load stocks from database\n"
            + "[User Set Model]Load user set from database, root: TestUserSetModel\n"
            + "[Text Based View]Show welcome dialog\n"
            + "[Text Based View]Show create user dialog\n"
            + "[User Set Model]Get List Size\n"
            + "[User Set Model]Get user list\n"
            + "[User Set Model]Get user list\n"
            + "[User Set Model]Generate xml: TestUserSetModel\n"
            + "[Text Based View]Show user create success prompt\n"
            + "[Text Based View]Show welcome user prompt: XYZ\n"
            + "[Text Based View]Show main menu\n"
            + "[Text Based View]Show Different Add Portfolio type options called.\n"
            + "[Text Based View]Show add portfolio name prompt\n"
            + "[Text Based View]Show portfolio options\n"
            + "[Text Based View]Show enter file path prompt\n"
            + "[User Set Model]Get user list\n"
            + "[User Set Model]Get user list\n"
            + "[User Set Model]Get user list\n"
            + "[User Set Model]Generate xml: TestUserSetModel\n"
            + "[Text Based View]Show portfolio add success message prompt: Portfolio 1\n"
            + "[Text Based View]Show main menu\n"
            + "[Text Based View]Show welcome dialog\n";

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
    assertEquals(desiredOutput, builder.toString());
  }

  @Test
  public void addPortfolioWithInvalidTickerFailsViaFile() {
    String desiredOutput = "[Stock Set Model]Load stocks from database\n"
            + "[User Set Model]Load user set from database, root: TestUserSetModel\n"
            + "[Text Based View]Show welcome dialog\n"
            + "[Text Based View]Show create user dialog\n"
            + "[User Set Model]Get List Size\n"
            + "[User Set Model]Get user list\n"
            + "[User Set Model]Get user list\n"
            + "[User Set Model]Generate xml: TestUserSetModel\n"
            + "[Text Based View]Show user create success prompt\n"
            + "[Text Based View]Show welcome user prompt: XYZ\n"
            + "[Text Based View]Show main menu\n"
            + "[Text Based View]Show Different Add Portfolio type options called.\n"
            + "[Text Based View]Show add portfolio name prompt\n"
            + "[Text Based View]Show portfolio options\n"
            + "[Text Based View]Show enter file path prompt\n"
            + "[Stock Set Model]Get stock: XYZ\n"
            + "[Stock Set Model]Get stock: XYZ\n"
            + "[Stock Set Model]Get stock: XYZ\n"
            + "[Stock Set Model]Get stock: XYZ\n"
            + "[User Set Model]Get user list\n"
            + "[User Set Model]Get user list\n"
            + "[User Set Model]Get user list\n"
            + "[User Set Model]Generate xml: TestUserSetModel\n"
            + "[Text Based View]Show portfolio add success message prompt: Portfolio 1\n"
            + "[Text Based View]Show main menu\n"
            + "[Text Based View]Show wrong input prompt\n"
            + "[Text Based View]Show main menu\n"
            + "[Text Based View]Show welcome dialog\n";

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

    stockSetModel = new FakeStockSetModel(builder, StockModelImpl.getBuilder()
            .stockTicker("GOOG")
            .ipoDate(new Date())
            .stockName("Google")
            .exchangeName("NASDAQ")
            .build());

    this.userStockModel = new UserStockModelImpl(this.userSetModel, this.stockSetModel);

    InputStream in = new ByteArrayInputStream(input.getBytes());
    Controller controller = new ControllerImpl(this.userStockModel,
            this.textBasedView, in, rootDirectory);
    controller.start();
    assertEquals(desiredOutput, builder.toString());
  }

  @Test
  public void addPortfolioWithInvalidDateFormatFailsViaFile() {
    String desiredOutput = "[Stock Set Model]Load stocks from database\n"
            + "[User Set Model]Load user set from database, root: TestUserSetModel\n"
            + "[Text Based View]Show welcome dialog\n"
            + "[Text Based View]Show create user dialog\n"
            + "[User Set Model]Get List Size\n"
            + "[User Set Model]Get user list\n"
            + "[User Set Model]Get user list\n"
            + "[User Set Model]Generate xml: TestUserSetModel\n"
            + "[Text Based View]Show user create success prompt\n"
            + "[Text Based View]Show welcome user prompt: XYZ\n"
            + "[Text Based View]Show main menu\n"
            + "[Text Based View]Show Different Add Portfolio type options called.\n"
            + "[Text Based View]Show add portfolio name prompt\n"
            + "[Text Based View]Show portfolio options\n"
            + "[Text Based View]Show enter file path prompt\n"
            + "[Stock Set Model]Get stock: GOOG\n"
            + "[Text Based View]Show input file violates portfolio constraints called.\n"
            + "[Text Based View]Show upload portfolio by xml error prompt\n"
            + "[Text Based View]Show enter file path prompt\n"
            + "[Stock Set Model]Get stock: GOOG\n"
            + "[Stock Set Model]Get stock: GOOG\n"
            + "[Stock Set Model]Get stock: GOOG\n"
            + "[Stock Set Model]Get stock: GOOG\n"
            + "[User Set Model]Get user list\n"
            + "[User Set Model]Get user list\n"
            + "[User Set Model]Get user list\n"
            + "[User Set Model]Generate xml: TestUserSetModel\n"
            + "[Text Based View]Show portfolio add success message prompt: Portfolio 1\n"
            + "[Text Based View]Show main menu\n"
            + "[Text Based View]Show welcome dialog\n";

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

    stockSetModel = new FakeStockSetModel(builder, StockModelImpl.getBuilder()
            .stockTicker("GOOG")
            .ipoDate(new Date())
            .stockName("Google")
            .exchangeName("NASDAQ")
            .build());

    this.userStockModel = new UserStockModelImpl(this.userSetModel, this.stockSetModel);

    InputStream in = new ByteArrayInputStream(input.getBytes());
    Controller controller = new ControllerImpl(this.userStockModel,
            this.textBasedView, in, rootDirectory);
    controller.start();
    assertEquals(desiredOutput, builder.toString());
  }

  @Test
  public void addPortfolioWithDataNotFoundForDateFailsViaFile() {
    String desiredOutput = "[Stock Set Model]Load stocks from database\n"
            + "[User Set Model]Load user set from database, root: TestUserSetModel\n"
            + "[Text Based View]Show welcome dialog\n"
            + "[Text Based View]Show create user dialog\n"
            + "[User Set Model]Get List Size\n"
            + "[User Set Model]Get user list\n"
            + "[User Set Model]Get user list\n"
            + "[User Set Model]Generate xml: TestUserSetModel\n"
            + "[Text Based View]Show user create success prompt\n"
            + "[Text Based View]Show welcome user prompt: XYZ\n"
            + "[Text Based View]Show main menu\n"
            + "[Text Based View]Show Different Add Portfolio type options called.\n"
            + "[Text Based View]Show add portfolio name prompt\n"
            + "[Text Based View]Show portfolio options\n"
            + "[Text Based View]Show enter file path prompt\n"
            + "[Stock Set Model]Get stock: GOOG\n"
            + "[Text Based View]Show input file violates portfolio constraints called.\n"
            + "[Text Based View]Show upload portfolio by xml error prompt\n"
            + "[Text Based View]Show enter file path prompt\n"
            + "[Stock Set Model]Get stock: GOOG\n"
            + "[Stock Set Model]Get stock: GOOG\n"
            + "[Stock Set Model]Get stock: GOOG\n"
            + "[Stock Set Model]Get stock: GOOG\n"
            + "[User Set Model]Get user list\n"
            + "[User Set Model]Get user list\n"
            + "[User Set Model]Get user list\n"
            + "[User Set Model]Generate xml: TestUserSetModel\n"
            + "[Text Based View]Show portfolio add success message prompt: Portfolio 1\n"
            + "[Text Based View]Show main menu\n"
            + "[Text Based View]Show welcome dialog\n";

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

    stockSetModel = new FakeStockSetModel(builder, StockModelImpl.getBuilder()
            .stockTicker("GOOG")
            .ipoDate(new Date())
            .stockName("Google")
            .exchangeName("NASDAQ")
            .build());

    this.userStockModel = new UserStockModelImpl(this.userSetModel, this.stockSetModel);

    InputStream in = new ByteArrayInputStream(input.getBytes());
    Controller controller = new ControllerImpl(this.userStockModel,
            this.textBasedView, in, rootDirectory);
    controller.start();
    assertEquals(desiredOutput, builder.toString());
  }

  @Test
  public void addPortfolioWithWrongQuantityFailsViaFile() {
    String desiredOutput = "[Stock Set Model]Load stocks from database\n"
            + "[User Set Model]Load user set from database, root: TestUserSetModel\n"
            + "[Text Based View]Show welcome dialog\n"
            + "[Text Based View]Show create user dialog\n"
            + "[User Set Model]Get List Size\n"
            + "[User Set Model]Get user list\n"
            + "[User Set Model]Get user list\n"
            + "[User Set Model]Generate xml: TestUserSetModel\n"
            + "[Text Based View]Show user create success prompt\n"
            + "[Text Based View]Show welcome user prompt: XYZ\n"
            + "[Text Based View]Show main menu\n"
            + "[Text Based View]Show Different Add Portfolio type options called.\n"
            + "[Text Based View]Show add portfolio name prompt\n"
            + "[Text Based View]Show portfolio options\n"
            + "[Text Based View]Show enter file path prompt\n"
            + "[Stock Set Model]Get stock: GOOG\n"
            + "[Stock Set Model]Get stock: GOOG\n"
            + "[Text Based View]Show input file violates portfolio constraints called.\n"
            + "[Text Based View]Show upload portfolio by xml error prompt\n"
            + "[Text Based View]Show enter file path prompt\n"
            + "[Stock Set Model]Get stock: GOOG\n"
            + "[Stock Set Model]Get stock: GOOG\n"
            + "[Stock Set Model]Get stock: GOOG\n"
            + "[Stock Set Model]Get stock: GOOG\n"
            + "[User Set Model]Get user list\n"
            + "[User Set Model]Get user list\n"
            + "[User Set Model]Get user list\n"
            + "[User Set Model]Generate xml: TestUserSetModel\n"
            + "[Text Based View]Show portfolio add success message prompt: Portfolio 1\n"
            + "[Text Based View]Show main menu\n"
            + "[Text Based View]Show welcome dialog\n";

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

    stockSetModel = new FakeStockSetModel(builder, StockModelImpl.getBuilder()
            .stockTicker("GOOG")
            .ipoDate(new Date())
            .stockName("Google")
            .exchangeName("NASDAQ")
            .build());

    this.userStockModel = new UserStockModelImpl(this.userSetModel, this.stockSetModel);

    InputStream in = new ByteArrayInputStream(input.getBytes());
    Controller controller = new ControllerImpl(this.userStockModel,
            this.textBasedView, in, rootDirectory);
    controller.start();
    assertEquals(desiredOutput, builder.toString());
  }

  @Test
  public void addPortfolioWithZeroQuantityWorksViaFile() {
    String desiredOutput = "[Stock Set Model]Load stocks from database\n"
            + "[User Set Model]Load user set from database, root: TestUserSetModel\n"
            + "[Text Based View]Show welcome dialog\n"
            + "[Text Based View]Show create user dialog\n"
            + "[User Set Model]Get List Size\n"
            + "[User Set Model]Get user list\n"
            + "[User Set Model]Get user list\n"
            + "[User Set Model]Generate xml: TestUserSetModel\n"
            + "[Text Based View]Show user create success prompt\n"
            + "[Text Based View]Show welcome user prompt: XYZ\n"
            + "[Text Based View]Show main menu\n"
            + "[Text Based View]Show Different Add Portfolio type options called.\n"
            + "[Text Based View]Show add portfolio name prompt\n"
            + "[Text Based View]Show portfolio options\n"
            + "[Text Based View]Show enter file path prompt\n"
            + "[Stock Set Model]Get stock: GOOG\n"
            + "[Stock Set Model]Get stock: GOOG\n"
            + "[Stock Set Model]Get stock: GOOG\n"
            + "[Stock Set Model]Get stock: GOOG\n"
            + "[User Set Model]Get user list\n"
            + "[User Set Model]Get user list\n"
            + "[User Set Model]Get user list\n"
            + "[User Set Model]Generate xml: TestUserSetModel\n"
            + "[Text Based View]Show portfolio add success message prompt: Portfolio 1\n"
            + "[Text Based View]Show main menu\n"
            + "[Text Based View]Show welcome dialog\n";

    String input = "1"
            + "\nXYZ"
            + "\n1"
            + "\n2"
            + "\nPortfolio 1"
            + "\n2"
            + "\nTestingHelper/zeroQuantityPortfolioElement.xml"
            + "\n8"
            + "\n3";

    stockSetModel = new FakeStockSetModel(builder, StockModelImpl.getBuilder()
            .stockTicker("GOOG")
            .ipoDate(new Date())
            .stockName("Google")
            .exchangeName("NASDAQ")
            .build());

    this.userStockModel = new UserStockModelImpl(this.userSetModel, this.stockSetModel);

    InputStream in = new ByteArrayInputStream(input.getBytes());
    Controller controller = new ControllerImpl(this.userStockModel,
            this.textBasedView, in, rootDirectory);
    controller.start();
    assertEquals(desiredOutput, builder.toString());
  }

  @Test
  public void addPortfolioWithNegativeQuantityFailsViaFile() {
    String desiredOutput = "[Stock Set Model]Load stocks from database\n"
            + "[User Set Model]Load user set from database, root: TestUserSetModel\n"
            + "[Text Based View]Show welcome dialog\n"
            + "[Text Based View]Show create user dialog\n"
            + "[User Set Model]Get List Size\n"
            + "[User Set Model]Get user list\n"
            + "[User Set Model]Get user list\n"
            + "[User Set Model]Generate xml: TestUserSetModel\n"
            + "[Text Based View]Show user create success prompt\n"
            + "[Text Based View]Show welcome user prompt: XYZ\n"
            + "[Text Based View]Show main menu\n"
            + "[Text Based View]Show Different Add Portfolio type options called.\n"
            + "[Text Based View]Show add portfolio name prompt\n"
            + "[Text Based View]Show portfolio options\n"
            + "[Text Based View]Show enter file path prompt\n"
            + "[Stock Set Model]Get stock: GOOG\n"
            + "[Stock Set Model]Get stock: GOOG\n"
            + "[Stock Set Model]Get stock: GOOG\n"
            + "[Stock Set Model]Get stock: GOOG\n"
            + "[Text Based View]Show input file violates portfolio constraints called.\n"
            + "[Text Based View]Show upload portfolio by xml error prompt\n"
            + "[Text Based View]Show enter file path prompt\n"
            + "[Stock Set Model]Get stock: GOOG\n"
            + "[Stock Set Model]Get stock: GOOG\n"
            + "[Stock Set Model]Get stock: GOOG\n"
            + "[Stock Set Model]Get stock: GOOG\n"
            + "[User Set Model]Get user list\n"
            + "[User Set Model]Get user list\n"
            + "[User Set Model]Get user list\n"
            + "[User Set Model]Generate xml: TestUserSetModel\n"
            + "[Text Based View]Show portfolio add success message prompt: Portfolio 1\n"
            + "[Text Based View]Show main menu\n"
            + "[Text Based View]Show welcome dialog\n";

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

    stockSetModel = new FakeStockSetModel(builder, StockModelImpl.getBuilder()
            .stockTicker("GOOG")
            .ipoDate(new Date())
            .stockName("Google")
            .exchangeName("NASDAQ")
            .build());

    this.userStockModel = new UserStockModelImpl(this.userSetModel, this.stockSetModel);

    InputStream in = new ByteArrayInputStream(input.getBytes());
    Controller controller = new ControllerImpl(this.userStockModel,
            this.textBasedView, in, rootDirectory);
    controller.start();
    assertEquals(desiredOutput, builder.toString());
  }

  @Test
  public void addPortfolioWithDecimalQuantityFailsViaFile() {
    String desiredOutput = "[Stock Set Model]Load stocks from database\n"
            + "[User Set Model]Load user set from database, root: TestUserSetModel\n"
            + "[Text Based View]Show welcome dialog\n"
            + "[Text Based View]Show create user dialog\n"
            + "[User Set Model]Get List Size\n"
            + "[User Set Model]Get user list\n"
            + "[User Set Model]Get user list\n"
            + "[User Set Model]Generate xml: TestUserSetModel\n"
            + "[Text Based View]Show user create success prompt\n"
            + "[Text Based View]Show welcome user prompt: XYZ\n"
            + "[Text Based View]Show main menu\n"
            + "[Text Based View]Show Different Add Portfolio type options called.\n"
            + "[Text Based View]Show add portfolio name prompt\n"
            + "[Text Based View]Show portfolio options\n"
            + "[Text Based View]Show enter file path prompt\n"
            + "[Stock Set Model]Get stock: GOOG\n"
            + "[Stock Set Model]Get stock: GOOG\n"
            + "[Text Based View]Show input file violates portfolio constraints called.\n"
            + "[Text Based View]Show upload portfolio by xml error prompt\n"
            + "[Text Based View]Show enter file path prompt\n"
            + "[Stock Set Model]Get stock: GOOG\n"
            + "[Stock Set Model]Get stock: GOOG\n"
            + "[Stock Set Model]Get stock: GOOG\n"
            + "[Stock Set Model]Get stock: GOOG\n"
            + "[User Set Model]Get user list\n"
            + "[User Set Model]Get user list\n"
            + "[User Set Model]Get user list\n"
            + "[User Set Model]Generate xml: TestUserSetModel\n"
            + "[Text Based View]Show portfolio add success message prompt: Portfolio 1\n"
            + "[Text Based View]Show main menu\n"
            + "[Text Based View]Show welcome dialog\n";

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

    stockSetModel = new FakeStockSetModel(builder, StockModelImpl.getBuilder()
            .stockTicker("GOOG")
            .ipoDate(new Date())
            .stockName("Google")
            .exchangeName("NASDAQ")
            .build());

    this.userStockModel = new UserStockModelImpl(this.userSetModel, this.stockSetModel);

    InputStream in = new ByteArrayInputStream(input.getBytes());
    Controller controller = new ControllerImpl(this.userStockModel,
            this.textBasedView, in, rootDirectory);
    controller.start();
    assertEquals(desiredOutput, builder.toString());
  }

  @Test
  public void addPortfolioWithSameNameFailsViaFile() {
    String desiredOutput = "[Stock Set Model]Load stocks from database\n"
            + "[User Set Model]Load user set from database, root: TestUserSetModel\n"
            + "[Text Based View]Show welcome dialog\n"
            + "[Text Based View]Show create user dialog\n"
            + "[User Set Model]Get List Size\n"
            + "[User Set Model]Get user list\n"
            + "[User Set Model]Get user list\n"
            + "[User Set Model]Generate xml: TestUserSetModel\n"
            + "[Text Based View]Show user create success prompt\n"
            + "[Text Based View]Show welcome user prompt: XYZ\n"
            + "[Text Based View]Show main menu\n"
            + "[Text Based View]Show Different Add Portfolio type options called.\n"
            + "[Text Based View]Show add portfolio name prompt\n"
            + "[Text Based View]Show portfolio options\n"
            + "[Text Based View]Show enter file path prompt\n"
            + "[Stock Set Model]Get stock: GOOG\n"
            + "[Stock Set Model]Get stock: GOOG\n"
            + "[Stock Set Model]Get stock: GOOG\n"
            + "[Stock Set Model]Get stock: GOOG\n"
            + "[User Set Model]Get user list\n"
            + "[User Set Model]Get user list\n"
            + "[User Set Model]Get user list\n"
            + "[User Set Model]Generate xml: TestUserSetModel\n"
            + "[Text Based View]Show portfolio add success message prompt: Portfolio 1\n"
            + "[Text Based View]Show main menu\n"
            + "[Text Based View]Show Different Add Portfolio type options called.\n"
            + "[Text Based View]Show add portfolio name prompt\n"
            + "[Text Based View]Show portfolio name already exists prompt\n"
            + "[Text Based View]Show add portfolio name prompt\n"
            + "[Text Based View]Show portfolio options\n"
            + "[Text Based View]Show enter file path prompt\n"
            + "[Stock Set Model]Get stock: GOOG\n"
            + "[Stock Set Model]Get stock: GOOG\n"
            + "[Stock Set Model]Get stock: GOOG\n"
            + "[Stock Set Model]Get stock: GOOG\n"
            + "[User Set Model]Get user list\n"
            + "[User Set Model]Get user list\n"
            + "[User Set Model]Get user list\n"
            + "[User Set Model]Generate xml: TestUserSetModel\n"
            + "[Text Based View]Show portfolio add success message prompt: Portfolio 2\n"
            + "[Text Based View]Show main menu\n"
            + "[Text Based View]Show welcome dialog\n";

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

    stockSetModel = new FakeStockSetModel(builder, StockModelImpl.getBuilder()
            .stockTicker("GOOG")
            .ipoDate(new Date())
            .stockName("Google")
            .exchangeName("NASDAQ")
            .build());

    this.userStockModel = new UserStockModelImpl(this.userSetModel, this.stockSetModel);

    InputStream in = new ByteArrayInputStream(input.getBytes());
    Controller controller = new ControllerImpl(this.userStockModel,
            this.textBasedView, in, rootDirectory);
    controller.start();
    assertEquals(desiredOutput, builder.toString());
  }


  @Test
  public void viewPortfolio() {
    String desiredOutput = "[Stock Set Model]Load stocks from database\n"
            + "[User Set Model]Load user set from database, root: TestUserSetModel\n"
            + "[Text Based View]Show welcome dialog\n"
            + "[Text Based View]Show create user dialog\n"
            + "[User Set Model]Get List Size\n"
            + "[User Set Model]Get user list\n"
            + "[User Set Model]Get user list\n"
            + "[User Set Model]Generate xml: TestUserSetModel\n"
            + "[Text Based View]Show user create success prompt\n"
            + "[Text Based View]Show welcome user prompt: XYZ\n"
            + "[Text Based View]Show main menu\n"
            + "[Text Based View]Show Different Add Portfolio type options called.\n"
            + "[Text Based View]Show add portfolio name prompt\n"
            + "[Text Based View]Show portfolio options\n"
            + "[Text Based View]Show total number of stocks to be added prompt\n"
            + "[Text Based View]Show enter stock ticker prompt\n"
            + "[Stock Set Model]Get stock: GOOG\n"
            + "[Stock Set Model]Get stock: GOOG\n"
            + "[Text Based View]Show enter date prompt\n"
            + "[Text Based View]Show enter quantity prompt\n"
            + "[Stock Set Model]Get stock: GOOG\n"
            + "[User Set Model]Get user list\n"
            + "[User Set Model]Get user list\n"
            + "[User Set Model]Generate xml: TestUserSetModel\n"
            + "[Text Based View]Show portfolio add success message prompt: Portfolio 1\n"
            + "[Text Based View]Show main menu\n"
            + "[Text Based View]Show select portfolio prompt: 1\n"
            + "[Text Based View]Show enter date prompt\n"
            + "[Text Based View]Show portfolio details prompt: Portfolio 1\n"
            + "[Text Based View]Show main menu\n"
            + "[Text Based View]Show welcome dialog\n";

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

    stockSetModel = new FakeStockSetModel(builder, StockModelImpl.getBuilder()
            .stockTicker("GOOG")
            .ipoDate(new Date())
            .stockName("Google")
            .exchangeName("NASDAQ")
            .build());

    this.userStockModel = new UserStockModelImpl(this.userSetModel, this.stockSetModel);

    InputStream in = new ByteArrayInputStream(input.getBytes());
    Controller controller = new ControllerImpl(this.userStockModel,
            this.textBasedView, in, rootDirectory);
    controller.start();
    assertEquals(desiredOutput, builder.toString());
  }

  @Test
  public void testViewPortfolioWhenMultiplePresent() {
    String desiredOutput = "[Stock Set Model]Load stocks from database\n"
            + "[User Set Model]Load user set from database, root: TestUserSetModel\n"
            + "[Text Based View]Show welcome dialog\n"
            + "[Text Based View]Show create user dialog\n"
            + "[User Set Model]Get List Size\n"
            + "[User Set Model]Get user list\n"
            + "[User Set Model]Get user list\n"
            + "[User Set Model]Generate xml: TestUserSetModel\n"
            + "[Text Based View]Show user create success prompt\n"
            + "[Text Based View]Show welcome user prompt: XYZ\n"
            + "[Text Based View]Show main menu\n"
            + "[Text Based View]Show Different Add Portfolio type options called.\n"
            + "[Text Based View]Show add portfolio name prompt\n"
            + "[Text Based View]Show portfolio options\n"
            + "[Text Based View]Show total number of stocks to be added prompt\n"
            + "[Text Based View]Show enter stock ticker prompt\n"
            + "[Stock Set Model]Get stock: GOOG\n"
            + "[Stock Set Model]Get stock: GOOG\n"
            + "[Text Based View]Show enter date prompt\n"
            + "[Text Based View]Show enter quantity prompt\n"
            + "[Text Based View]Show enter stock ticker prompt\n"
            + "[Stock Set Model]Get stock: AMZN\n"
            + "[Stock Set Model]Get stock: AMZN\n"
            + "[Text Based View]Show enter date prompt\n"
            + "[Text Based View]Show enter quantity prompt\n"
            + "[Stock Set Model]Get stock: GOOG\n"
            + "[User Set Model]Get user list\n"
            + "[User Set Model]Get user list\n"
            + "[User Set Model]Generate xml: TestUserSetModel\n"
            + "[Text Based View]Show portfolio add success message prompt: Portfolio 1\n"
            + "[Text Based View]Show main menu\n"
            + "[Text Based View]Show select portfolio prompt: 1\n"
            + "[Text Based View]Show enter date prompt\n"
            + "[Text Based View]Show portfolio details prompt: Portfolio 1\n"
            + "[Text Based View]Show main menu\n"
            + "[Text Based View]Show welcome dialog\n";

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

    stockSetModel = new FakeStockSetModel(builder, StockModelImpl.getBuilder()
            .stockTicker("GOOG")
            .ipoDate(new Date())
            .stockName("Google")
            .exchangeName("NASDAQ")
            .build(),
            StockModelImpl.getBuilder()
                    .stockTicker("AMZN")
                    .ipoDate(new Date())
                    .stockName("Amazon")
                    .exchangeName("NASDAQ")
                    .build());

    this.userStockModel = new UserStockModelImpl(this.userSetModel, this.stockSetModel);

    InputStream in = new ByteArrayInputStream(input.getBytes());
    Controller controller = new ControllerImpl(this.userStockModel,
            this.textBasedView, in, rootDirectory);
    controller.start();
    assertEquals(desiredOutput, builder.toString());
  }

  @Test
  public void testViewPortfolioWhenNoStocksPresent() {
    String desiredOutput = "[Stock Set Model]Load stocks from database\n"
            + "[User Set Model]Load user set from database, root: TestUserSetModel\n"
            + "[Text Based View]Show welcome dialog\n"
            + "[Text Based View]Show create user dialog\n"
            + "[User Set Model]Get List Size\n"
            + "[User Set Model]Get user list\n"
            + "[User Set Model]Get user list\n"
            + "[User Set Model]Generate xml: TestUserSetModel\n"
            + "[Text Based View]Show user create success prompt\n"
            + "[Text Based View]Show welcome user prompt: XYZ\n"
            + "[Text Based View]Show main menu\n"
            + "[Text Based View]Show Different Add Portfolio type options called.\n"
            + "[Text Based View]Show add portfolio name prompt\n"
            + "[Text Based View]Show portfolio options\n"
            + "[Text Based View]Show total number of stocks to be added prompt\n"
            + "[User Set Model]Get user list\n"
            + "[User Set Model]Get user list\n"
            + "[User Set Model]Generate xml: TestUserSetModel\n"
            + "[Text Based View]Show portfolio add success message prompt: Portfolio 1\n"
            + "[Text Based View]Show main menu\n"
            + "[Text Based View]Show select portfolio prompt: 1\n"
            + "[Text Based View]Show enter date prompt\n"
            + "[Text Based View]Show portfolio details prompt: Portfolio 1\n"
            + "[Text Based View]Show main menu\n"
            + "[Text Based View]Show welcome dialog\n";

    String input = "1"
            + "\nXYZ"
            + "\n1"
            + "\n2"
            + "\nPortfolio 1"
            + "\n1"
            + "\n0"
            + "\n2"
            + "\n1"
            + "\n2022-10-28"
            + "\n8"
            + "\n3";

    InputStream in = new ByteArrayInputStream(input.getBytes());
    Controller controller = new ControllerImpl(this.userStockModel,
            this.textBasedView, in, rootDirectory);
    controller.start();
    assertEquals(desiredOutput, builder.toString());
  }

  @Test
  public void testWrongSelectionRejectedWhileViewPortfolio() {
    String desiredOutput = "[Stock Set Model]Load stocks from database\n"
            + "[User Set Model]Load user set from database, root: TestUserSetModel\n"
            + "[Text Based View]Show welcome dialog\n"
            + "[Text Based View]Show create user dialog\n"
            + "[User Set Model]Get List Size\n"
            + "[User Set Model]Get user list\n"
            + "[User Set Model]Get user list\n"
            + "[User Set Model]Generate xml: TestUserSetModel\n"
            + "[Text Based View]Show user create success prompt\n"
            + "[Text Based View]Show welcome user prompt: XYZ\n"
            + "[Text Based View]Show main menu\n"
            + "[Text Based View]Show Different Add Portfolio type options called.\n"
            + "[Text Based View]Show add portfolio name prompt\n"
            + "[Text Based View]Show portfolio options\n"
            + "[Text Based View]Show total number of stocks to be added prompt\n"
            + "[Text Based View]Show enter stock ticker prompt\n"
            + "[Stock Set Model]Get stock: GOOG\n"
            + "[Stock Set Model]Get stock: GOOG\n"
            + "[Text Based View]Show enter date prompt\n"
            + "[Text Based View]Show enter quantity prompt\n"
            + "[Stock Set Model]Get stock: GOOG\n"
            + "[User Set Model]Get user list\n"
            + "[User Set Model]Get user list\n"
            + "[User Set Model]Generate xml: TestUserSetModel\n"
            + "[Text Based View]Show portfolio add success message prompt: Portfolio 1\n"
            + "[Text Based View]Show main menu\n"
            + "[Text Based View]Show select portfolio prompt: 1\n"
            + "[Text Based View]Show wrong input prompt\n"
            + "[Text Based View]Show select portfolio prompt: 1\n"
            + "[Text Based View]Show enter date prompt\n"
            + "[Text Based View]Show portfolio details prompt: Portfolio 1\n"
            + "[Text Based View]Show main menu\n"
            + "[Text Based View]Show welcome dialog\n";

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
            + "\n2022-10-28"
            + "\n8"
            + "\n3";

    stockSetModel = new FakeStockSetModel(builder, StockModelImpl.getBuilder()
            .stockTicker("GOOG")
            .ipoDate(new Date())
            .stockName("Google")
            .exchangeName("NASDAQ")
            .build());

    this.userStockModel = new UserStockModelImpl(this.userSetModel, this.stockSetModel);

    InputStream in = new ByteArrayInputStream(input.getBytes());
    Controller controller = new ControllerImpl(this.userStockModel,
            this.textBasedView, in, rootDirectory);
    controller.start();
    assertEquals(desiredOutput, builder.toString());
  }

  @Test
  public void testViewPortfolioWhenNoPortfolioPresent() {
    String desiredOutput = "[Stock Set Model]Load stocks from database\n"
            + "[User Set Model]Load user set from database, root: TestUserSetModel\n"
            + "[Text Based View]Show welcome dialog\n"
            + "[Text Based View]Show create user dialog\n"
            + "[User Set Model]Get List Size\n"
            + "[User Set Model]Get user list\n"
            + "[User Set Model]Get user list\n"
            + "[User Set Model]Generate xml: TestUserSetModel\n"
            + "[Text Based View]Show user create success prompt\n"
            + "[Text Based View]Show welcome user prompt: XYZ\n"
            + "[Text Based View]Show main menu\n"
            + "[Text Based View]Show no portfolios prompt\n"
            + "[Text Based View]Show main menu\n"
            + "[Text Based View]Show welcome dialog\n";

    String input = "1"
            + "\nXYZ"
            + "\n2"
            + "\n8"
            + "\n3";

    InputStream in = new ByteArrayInputStream(input.getBytes());
    Controller controller = new ControllerImpl(this.userStockModel,
            this.textBasedView, in, rootDirectory);
    controller.start();
    assertEquals(desiredOutput, builder.toString());
  }

  @Test
  public void testDeterminePortfolioForADate() {
    String desiredOutput = "[Stock Set Model]Load stocks from database\n"
            + "[User Set Model]Load user set from database, root: TestUserSetModel\n"
            + "[Text Based View]Show welcome dialog\n"
            + "[Text Based View]Show create user dialog\n"
            + "[User Set Model]Get List Size\n"
            + "[User Set Model]Get user list\n"
            + "[User Set Model]Get user list\n"
            + "[User Set Model]Generate xml: TestUserSetModel\n"
            + "[Text Based View]Show user create success prompt\n"
            + "[Text Based View]Show welcome user prompt: XYZ\n"
            + "[Text Based View]Show main menu\n"
            + "[Text Based View]Show Different Add Portfolio type options called.\n"
            + "[Text Based View]Show add portfolio name prompt\n"
            + "[Text Based View]Show portfolio options\n"
            + "[Text Based View]Show total number of stocks to be added prompt\n"
            + "[Text Based View]Show enter stock ticker prompt\n"
            + "[Stock Set Model]Get stock: GOOG\n"
            + "[Stock Set Model]Get stock: GOOG\n"
            + "[Text Based View]Show enter date prompt\n"
            + "[Text Based View]Show enter quantity prompt\n"
            + "[Stock Set Model]Get stock: GOOG\n"
            + "[User Set Model]Get user list\n"
            + "[User Set Model]Get user list\n"
            + "[User Set Model]Generate xml: TestUserSetModel\n"
            + "[Text Based View]Show portfolio add success message prompt: Portfolio 1\n"
            + "[Text Based View]Show main menu\n"
            + "[Text Based View]Show select portfolio prompt: 1\n"
            + "[Text Based View]Show enter date prompt\n"
            + "[Text Based View]Show portfolio value for date prompt:"
            + " Fri Oct 28 00:00:00 EDT 2022 965.8\n"
            + "[Text Based View]Show main menu\n"
            + "[Text Based View]Show welcome dialog\n";

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

    stockSetModel = new FakeStockSetModel(builder, StockModelImpl.getBuilder()
            .stockTicker("GOOG")
            .ipoDate(new Date())
            .stockName("Google")
            .exchangeName("NASDAQ")
            .build());

    this.userStockModel = new UserStockModelImpl(this.userSetModel, this.stockSetModel);

    InputStream in = new ByteArrayInputStream(input.getBytes());
    Controller controller = new ControllerImpl(this.userStockModel,
            this.textBasedView, in, rootDirectory);
    controller.start();
    assertEquals(desiredOutput, builder.toString());
  }

  @Test
  public void testDeterminePortfolioForADateWhenPortfolioHasNoStocks() {
    String desiredOutput = "[Stock Set Model]Load stocks from database\n"
            + "[User Set Model]Load user set from database, root: TestUserSetModel\n"
            + "[Text Based View]Show welcome dialog\n"
            + "[Text Based View]Show create user dialog\n"
            + "[User Set Model]Get List Size\n"
            + "[User Set Model]Get user list\n"
            + "[User Set Model]Get user list\n"
            + "[User Set Model]Generate xml: TestUserSetModel\n"
            + "[Text Based View]Show user create success prompt\n"
            + "[Text Based View]Show welcome user prompt: XYZ\n"
            + "[Text Based View]Show main menu\n"
            + "[Text Based View]Show Different Add Portfolio type options called.\n"
            + "[Text Based View]Show add portfolio name prompt\n"
            + "[Text Based View]Show portfolio options\n"
            + "[Text Based View]Show total number of stocks to be added prompt\n"
            + "[User Set Model]Get user list\n"
            + "[User Set Model]Get user list\n"
            + "[User Set Model]Generate xml: TestUserSetModel\n"
            + "[Text Based View]Show portfolio add success message prompt: Portfolio 1\n"
            + "[Text Based View]Show main menu\n"
            + "[Text Based View]Show select portfolio prompt: 1\n"
            + "[Text Based View]Show enter date prompt\n"
            + "[Text Based View]Show portfolio value for date prompt:"
            + " Wed Oct 26 00:00:00 EDT 2022 0.0\n"
            + "[Text Based View]Show main menu\n"
            + "[Text Based View]Show welcome dialog\n";

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
    assertEquals(desiredOutput, builder.toString());
  }

  @Test
  public void testDeterminePortfolioForADateWhenPortfolioHasMultipleStocks() {
    String desiredOutput = "[Stock Set Model]Load stocks from database\n"
            + "[User Set Model]Load user set from database, root: TestUserSetModel\n"
            + "[Text Based View]Show welcome dialog\n"
            + "[Text Based View]Show create user dialog\n"
            + "[User Set Model]Get List Size\n"
            + "[User Set Model]Get user list\n"
            + "[User Set Model]Get user list\n"
            + "[User Set Model]Generate xml: TestUserSetModel\n"
            + "[Text Based View]Show user create success prompt\n"
            + "[Text Based View]Show welcome user prompt: XYZ\n"
            + "[Text Based View]Show main menu\n"
            + "[Text Based View]Show Different Add Portfolio type options called.\n"
            + "[Text Based View]Show add portfolio name prompt\n"
            + "[Text Based View]Show portfolio options\n"
            + "[Text Based View]Show total number of stocks to be added prompt\n"
            + "[Text Based View]Show enter stock ticker prompt\n"
            + "[Stock Set Model]Get stock: GOOG\n"
            + "[Stock Set Model]Get stock: GOOG\n"
            + "[Text Based View]Show enter date prompt\n"
            + "[Text Based View]Show enter quantity prompt\n"
            + "[Text Based View]Show enter stock ticker prompt\n"
            + "[Stock Set Model]Get stock: AMZN\n"
            + "[Stock Set Model]Get stock: AMZN\n"
            + "[Text Based View]Show enter date prompt\n"
            + "[Text Based View]Show enter quantity prompt\n"
            + "[Stock Set Model]Get stock: GOOG\n"
            + "[User Set Model]Get user list\n"
            + "[User Set Model]Get user list\n"
            + "[User Set Model]Generate xml: TestUserSetModel\n"
            + "[Text Based View]Show portfolio add success message prompt: Portfolio 1\n"
            + "[Text Based View]Show main menu\n"
            + "[Text Based View]Show select portfolio prompt: 1\n"
            + "[Text Based View]Show enter date prompt\n"
            + "[Text Based View]Show portfolio value for date prompt: "
            + "Wed Oct 26 00:00:00 EDT 2022 0.0\n"
            + "[Text Based View]Show main menu\n"
            + "[Text Based View]Show welcome dialog\n";

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
            + "\n2022-10-26"
            + "\n8"
            + "\n3";

    stockSetModel = new FakeStockSetModel(builder, StockModelImpl.getBuilder()
            .stockTicker("GOOG")
            .ipoDate(new Date())
            .stockName("Google")
            .exchangeName("NASDAQ")
            .build(),
            StockModelImpl.getBuilder()
                    .stockTicker("AMZN")
                    .ipoDate(new Date())
                    .stockName("Amazon")
                    .exchangeName("NASDAQ")
                    .build());

    this.userStockModel = new UserStockModelImpl(this.userSetModel, this.stockSetModel);

    InputStream in = new ByteArrayInputStream(input.getBytes());
    Controller controller = new ControllerImpl(this.userStockModel,
            this.textBasedView, in, rootDirectory);
    controller.start();
    assertEquals(desiredOutput, builder.toString());
  }

  @Test
  public void testDeterminePortfolioForADateWhenNoPortfoliosFound() {
    String desiredOutput = "[Stock Set Model]Load stocks from database\n"
            + "[User Set Model]Load user set from database, root: TestUserSetModel\n"
            + "[Text Based View]Show welcome dialog\n"
            + "[Text Based View]Show create user dialog\n"
            + "[User Set Model]Get List Size\n"
            + "[User Set Model]Get user list\n"
            + "[User Set Model]Get user list\n"
            + "[User Set Model]Generate xml: TestUserSetModel\n"
            + "[Text Based View]Show user create success prompt\n"
            + "[Text Based View]Show welcome user prompt: XYZ\n"
            + "[Text Based View]Show main menu\n"
            + "[Text Based View]Show no portfolios prompt\n"
            + "[Text Based View]Show main menu\n"
            + "[Text Based View]Show welcome dialog\n";

    String input = "1"
            + "\nXYZ"
            + "\n3"
            + "\n8"
            + "\n3";

    InputStream in = new ByteArrayInputStream(input.getBytes());
    Controller controller = new ControllerImpl(this.userStockModel,
            this.textBasedView, in, rootDirectory);
    controller.start();
    assertEquals(desiredOutput, builder.toString());
  }

  @Test
  public void testInvalidDateRejectedWhenDeterminePortfolioForADate() {
    String desiredOutput = "[Stock Set Model]Load stocks from database\n"
            + "[User Set Model]Load user set from database, root: TestUserSetModel\n"
            + "[Text Based View]Show welcome dialog\n"
            + "[Text Based View]Show create user dialog\n"
            + "[User Set Model]Get List Size\n"
            + "[User Set Model]Get user list\n"
            + "[User Set Model]Get user list\n"
            + "[User Set Model]Generate xml: TestUserSetModel\n"
            + "[Text Based View]Show user create success prompt\n"
            + "[Text Based View]Show welcome user prompt: XYZ\n"
            + "[Text Based View]Show main menu\n"
            + "[Text Based View]Show Different Add Portfolio type options called.\n"
            + "[Text Based View]Show add portfolio name prompt\n"
            + "[Text Based View]Show portfolio options\n"
            + "[Text Based View]Show total number of stocks to be added prompt\n"
            + "[Text Based View]Show enter stock ticker prompt\n"
            + "[Stock Set Model]Get stock: GOOG\n"
            + "[Stock Set Model]Get stock: GOOG\n"
            + "[Text Based View]Show enter date prompt\n"
            + "[Text Based View]Show enter quantity prompt\n"
            + "[Stock Set Model]Get stock: GOOG\n"
            + "[User Set Model]Get user list\n"
            + "[User Set Model]Get user list\n"
            + "[User Set Model]Generate xml: TestUserSetModel\n"
            + "[Text Based View]Show portfolio add success message prompt: Portfolio 1\n"
            + "[Text Based View]Show main menu\n"
            + "[Text Based View]Show select portfolio prompt: 1\n"
            + "[Text Based View]Show enter date prompt\n"
            + "[Text Based View]Show wrong input prompt\n"
            + "[Text Based View]Show enter date prompt\n"
            + "[Text Based View]Show portfolio value for date prompt:"
            + " Fri Oct 28 00:00:00 EDT 2022 965.8\n"
            + "[Text Based View]Show main menu\n"
            + "[Text Based View]Show welcome dialog\n";

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
            + "\n26-10-2022"
            + "\n2022-10-28"
            + "\n8"
            + "\n3";

    stockSetModel = new FakeStockSetModel(builder, StockModelImpl.getBuilder()
            .stockTicker("GOOG")
            .ipoDate(new Date())
            .stockName("Google")
            .exchangeName("NASDAQ")
            .build());

    this.userStockModel = new UserStockModelImpl(this.userSetModel, this.stockSetModel);

    InputStream in = new ByteArrayInputStream(input.getBytes());
    Controller controller = new ControllerImpl(this.userStockModel,
            this.textBasedView, in, rootDirectory);
    controller.start();
    assertEquals(desiredOutput, builder.toString());
  }

  @Test
  public void testDateForNoDataAvailableAddedRejectedWhenDeterminePortfolioForADate() {
    String desiredOutput = "[Stock Set Model]Load stocks from database\n"
            + "[User Set Model]Load user set from database, root: TestUserSetModel\n"
            + "[Text Based View]Show welcome dialog\n"
            + "[Text Based View]Show create user dialog\n"
            + "[User Set Model]Get List Size\n"
            + "[User Set Model]Get user list\n"
            + "[User Set Model]Get user list\n"
            + "[User Set Model]Generate xml: TestUserSetModel\n"
            + "[Text Based View]Show user create success prompt\n"
            + "[Text Based View]Show welcome user prompt: XYZ\n"
            + "[Text Based View]Show main menu\n"
            + "[Text Based View]Show Different Add Portfolio type options called.\n"
            + "[Text Based View]Show add portfolio name prompt\n"
            + "[Text Based View]Show portfolio options\n"
            + "[Text Based View]Show total number of stocks to be added prompt\n"
            + "[Text Based View]Show enter stock ticker prompt\n"
            + "[Stock Set Model]Get stock: GOOG\n"
            + "[Stock Set Model]Get stock: GOOG\n"
            + "[Text Based View]Show enter date prompt\n"
            + "[Text Based View]Show enter quantity prompt\n"
            + "[Stock Set Model]Get stock: GOOG\n"
            + "[User Set Model]Get user list\n"
            + "[User Set Model]Get user list\n"
            + "[User Set Model]Generate xml: TestUserSetModel\n"
            + "[Text Based View]Show portfolio add success message prompt: Portfolio 1\n"
            + "[Text Based View]Show main menu\n"
            + "[Text Based View]Show select portfolio prompt: 1\n"
            + "[Text Based View]Show enter date prompt\n"
            + "[Text Based View]Show portfolio value for date prompt: "
            + "Sat Oct 29 00:00:00 EDT 2022 965.8\n"
            + "[Text Based View]Show main menu\n"
            + "[Text Based View]Show welcome dialog\n";

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

    stockSetModel = new FakeStockSetModel(builder, StockModelImpl.getBuilder()
            .stockTicker("GOOG")
            .ipoDate(new Date())
            .stockName("Google")
            .exchangeName("NASDAQ")
            .build());

    this.userStockModel = new UserStockModelImpl(this.userSetModel, this.stockSetModel);

    InputStream in = new ByteArrayInputStream(input.getBytes());
    Controller controller = new ControllerImpl(this.userStockModel,
            this.textBasedView, in, rootDirectory);
    controller.start();
    assertEquals(desiredOutput, builder.toString());
  }

  @Test
  public void testDownloadPortfolio() {
    String desiredOutput = "[Stock Set Model]Load stocks from database\n"
            + "[User Set Model]Load user set from database, root: TestUserSetModel\n"
            + "[Text Based View]Show welcome dialog\n"
            + "[Text Based View]Show create user dialog\n"
            + "[User Set Model]Get List Size\n"
            + "[User Set Model]Get user list\n"
            + "[User Set Model]Get user list\n"
            + "[User Set Model]Generate xml: TestUserSetModel\n"
            + "[Text Based View]Show user create success prompt\n"
            + "[Text Based View]Show welcome user prompt: XYZ\n"
            + "[Text Based View]Show main menu\n"
            + "[Text Based View]Show Different Add Portfolio type options called.\n"
            + "[Text Based View]Show add portfolio name prompt\n"
            + "[Text Based View]Show portfolio options\n"
            + "[Text Based View]Show total number of stocks to be added prompt\n"
            + "[Text Based View]Show enter stock ticker prompt\n"
            + "[Stock Set Model]Get stock: GOOG\n"
            + "[Stock Set Model]Get stock: GOOG\n"
            + "[Text Based View]Show enter date prompt\n"
            + "[Text Based View]Show enter quantity prompt\n"
            + "[Stock Set Model]Get stock: GOOG\n"
            + "[User Set Model]Get user list\n"
            + "[User Set Model]Get user list\n"
            + "[User Set Model]Generate xml: TestUserSetModel\n"
            + "[Text Based View]Show portfolio add success message prompt: Portfolio 1\n"
            + "[Text Based View]Show main menu\n"
            + "[Text Based View]Show select portfolio prompt: 1\n"
            + "[Text Based View]Show save portfolio success message prompt\n"
            + "[Text Based View]Show main menu\n"
            + "[Text Based View]Show welcome dialog\n";

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

    stockSetModel = new FakeStockSetModel(builder, StockModelImpl.getBuilder()
            .stockTicker("GOOG")
            .ipoDate(new Date())
            .stockName("Google")
            .exchangeName("NASDAQ")
            .build());

    this.userStockModel = new UserStockModelImpl(this.userSetModel, this.stockSetModel);

    InputStream in = new ByteArrayInputStream(input.getBytes());
    Controller controller = new ControllerImpl(this.userStockModel,
            this.textBasedView, in, rootDirectory);
    controller.start();
    assertEquals(desiredOutput, builder.toString());
  }

  @Test
  public void testWrongSelectionRejectedDuringDownloadPortfolio() {
    String desiredOutput = "[Stock Set Model]Load stocks from database\n"
            + "[User Set Model]Load user set from database, root: TestUserSetModel\n"
            + "[Text Based View]Show welcome dialog\n"
            + "[Text Based View]Show create user dialog\n"
            + "[User Set Model]Get List Size\n"
            + "[User Set Model]Get user list\n"
            + "[User Set Model]Get user list\n"
            + "[User Set Model]Generate xml: TestUserSetModel\n"
            + "[Text Based View]Show user create success prompt\n"
            + "[Text Based View]Show welcome user prompt: XYZ\n"
            + "[Text Based View]Show main menu\n"
            + "[Text Based View]Show Different Add Portfolio type options called.\n"
            + "[Text Based View]Show add portfolio name prompt\n"
            + "[Text Based View]Show portfolio options\n"
            + "[Text Based View]Show total number of stocks to be added prompt\n"
            + "[Text Based View]Show enter stock ticker prompt\n"
            + "[Stock Set Model]Get stock: GOOG\n"
            + "[Stock Set Model]Get stock: GOOG\n"
            + "[Text Based View]Show enter date prompt\n"
            + "[Text Based View]Show enter quantity prompt\n"
            + "[Stock Set Model]Get stock: GOOG\n"
            + "[User Set Model]Get user list\n"
            + "[User Set Model]Get user list\n"
            + "[User Set Model]Generate xml: TestUserSetModel\n"
            + "[Text Based View]Show portfolio add success message prompt: Portfolio 1\n"
            + "[Text Based View]Show main menu\n"
            + "[Text Based View]Show select portfolio prompt: 1\n"
            + "[Text Based View]Show wrong input prompt\n"
            + "[Text Based View]Show select portfolio prompt: 1\n"
            + "[Text Based View]Show save portfolio success message prompt\n"
            + "[Text Based View]Show main menu\n"
            + "[Text Based View]Show welcome dialog\n";

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

    stockSetModel = new FakeStockSetModel(builder, StockModelImpl.getBuilder()
            .stockTicker("GOOG")
            .ipoDate(new Date())
            .stockName("Google")
            .exchangeName("NASDAQ")
            .build());

    this.userStockModel = new UserStockModelImpl(this.userSetModel, this.stockSetModel);

    InputStream in = new ByteArrayInputStream(input.getBytes());
    Controller controller = new ControllerImpl(this.userStockModel,
            this.textBasedView, in, rootDirectory);
    controller.start();
    assertEquals(desiredOutput, builder.toString());
  }

  @Test
  public void testDownloadPortfolioWhenNoPortfolioPresent() {
    String desiredOutput = "[Stock Set Model]Load stocks from database\n"
            + "[User Set Model]Load user set from database, root: TestUserSetModel\n"
            + "[Text Based View]Show welcome dialog\n"
            + "[Text Based View]Show create user dialog\n"
            + "[User Set Model]Get List Size\n"
            + "[User Set Model]Get user list\n"
            + "[User Set Model]Get user list\n"
            + "[User Set Model]Generate xml: TestUserSetModel\n"
            + "[Text Based View]Show user create success prompt\n"
            + "[Text Based View]Show welcome user prompt: XYZ\n"
            + "[Text Based View]Show main menu\n"
            + "[Text Based View]Show no portfolios prompt\n"
            + "[Text Based View]Show main menu\n"
            + "[Text Based View]Show welcome dialog\n";

    String input = "1"
            + "\nXYZ"
            + "\n4"
            + "\n8"
            + "\n3";

    InputStream in = new ByteArrayInputStream(input.getBytes());
    Controller controller = new ControllerImpl(this.userStockModel,
            this.textBasedView, in, rootDirectory);
    controller.start();
    assertEquals(desiredOutput, builder.toString());
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
    fw = new java.io.FileWriter(
            "TestingHelper//decimalQuantityPortfolioElement.xml");
    fw.write(decimalQuantityPortfolioElement);
    fw.close();
    String invalidDateFormatPortfolioElement = "<PortfolioModel>\n"
            + "<PortfolioElementModel>\n"
            + "<StockTicker> GOOG </StockTicker>\n"
            + "<totalQuantity> 1000 </totalQuantity>\n"
            + "<Date> 27-10-2022 </Date>\n"
            + "</PortfolioElementModel>\n"
            + "</PortfolioModel>";
    fw = new java.io.FileWriter(
            "TestingHelper//invalidDateFormatPortfolioElement.xml");
    fw.write(invalidDateFormatPortfolioElement);
    fw.close();
    String invalidPortfolioElementFormat = "<PortfolioModel>\n"
            + "    <PortfolioElementModel>\n"
            + "</PortfolioModel>";
    fw = new java.io.FileWriter(
            "TestingHelper//invalidPortfolioElementFormat.xml");
    fw.write(invalidPortfolioElementFormat);
    fw.close();
    String invalidTickerPortfolioElement = "<PortfolioModel>\n"
            + "<PortfolioElementModel>\n"
            + "<StockTicker> XYZ </StockTicker>\n"
            + "<totalQuantity> 1000 </totalQuantity>\n"
            + "<Date> 2022-10-27 </Date>\n"
            + "</PortfolioElementModel>\n"
            + "</PortfolioModel>";
    fw = new java.io.FileWriter(
            "TestingHelper//invalidTickerPortfolioElement.xml");
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
    fw = new java.io.FileWriter(
            "TestingHelper//multiplePortfolioElementCorrectFormat.xml");
    fw.write(multiplePortfolioElementCorrectFormat);
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
    fw = new java.io.FileWriter(
            "TestingHelper//multipleSamePortfolioElementCorrectFormat.xml");
    fw.write(multipleSamePortfolioElementCorrectFormat);
    fw.close();
    String negativeQuantityPortfolioElement = "<PortfolioModel>\n"
            + "<PortfolioElementModel>\n"
            + "<StockTicker> GOOG </StockTicker>\n"
            + "<totalQuantity> -10 </totalQuantity>\n"
            + "<Date> 2022-10-27 </Date>\n"
            + "</PortfolioElementModel>\n"
            + "</PortfolioModel>";
    fw = new java.io.FileWriter(
            "TestingHelper//negativeQuantityPortfolioElement.xml");
    fw.write(negativeQuantityPortfolioElement);
    fw.close();
    String singlePortfolioElementCorrectFormat = "<PortfolioModel>\n"
            + "<PortfolioElementModel>\n"
            + "<StockTicker> GOOG </StockTicker>\n"
            + "<totalQuantity> 1000 </totalQuantity>\n"
            + "<Date> 2022-10-27 </Date>\n"
            + "</PortfolioElementModel>\n"
            + "</PortfolioModel>";
    fw = new java.io.FileWriter(
            "TestingHelper//singlePortfolioElementCorrectFormat.xml");
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
    fw = new java.io.FileWriter(
            "TestingHelper//wrongQuantityPortfolioElement.xml");
    fw.write(wrongQuantityPortfolioElement);
    fw.close();
    String zeroPortfolioElementCorrectFormat = "\n"
            + "<PortfolioModel> </PortfolioModel>";
    fw = new java.io.FileWriter(
            "TestingHelper//zeroPortfolioElementCorrectFormat.xml");
    fw.write(zeroPortfolioElementCorrectFormat);
    fw.close();
    String zeroQuantityPortfolioElement = "<PortfolioModel>\n"
            + "<PortfolioElementModel>\n"
            + "<StockTicker> GOOG </StockTicker>\n"
            + "<totalQuantity> 0 </totalQuantity>\n"
            + "<Date> 2022-10-27 </Date>\n"
            + "</PortfolioElementModel>\n"
            + "</PortfolioModel>";
    fw = new java.io.FileWriter(
            "TestingHelper//zeroQuantityPortfolioElement.xml");
    fw.write(zeroQuantityPortfolioElement);
    fw.close();
  }
}
