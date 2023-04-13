package views;


import java.awt.CardLayout;
import java.awt.Color;
import java.io.File;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;


import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import controller.Features;
import models.Strategy;
import utils.ChartPlot;

/**
 * HomeView class to show the view. It implements
 * GUIView and extends the JFrame class.
 */
public class HomeView extends JFrame implements GUIView {
  private MainMenuPanel pMainMenuPanel;
  private LoginPanel pLogin;
  private JPanel pCards;
  private PortfolioDetailsPanel pPortfolioDetailsPanel;
  private Features features;
  private CreatePortfolioWithoutStrategyPanel createPortfolioWithoutStrategyPanel;
  private JDialog createPortfolioWithoutStrategyDialog;
  private AddStrategyPanel addStrategyPanel;
  private JDialog addStrategyDialog;
  private JDialog modifyPortfolioDialog;
  private CreatePortfolioWithStrategyPanel createPortfolioWithStrategyPanel;
  private JDialog createPortfolioWithStrategyDialog;
  private CreatePortfolioWithUploadXML createPortfolioWithUploadXML;
  private JDialog createPortfolioWithUploadXMLDialog;

  private JDialog processingDialog;

  /**
   * Public constructor to initialize home view.
   */
  public HomeView() {
    initializeLayout();
    initializeViews();
  }

  private void initializeViews() {
    pMainMenuPanel = new MainMenuPanel();
    pPortfolioDetailsPanel = new PortfolioDetailsPanel();
    pLogin = new LoginPanel();
    pCards = new JPanel(new CardLayout());
    pCards.add(pLogin, "Login");
    pCards.add(pMainMenuPanel, "Main Menu");
    pCards.add(pPortfolioDetailsPanel, "Portfolio Details");
    this.getContentPane().add(pCards);
    ((CardLayout) pCards.getLayout()).show(pCards, "Login");
    setVisible(true);
    this.pack();
  }

  private void initializeLayout() {
    setLocation(200, 200);
    setResizable(false);
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    JPanel processingPanel = new JPanel();
    processingPanel.setBackground(Color.yellow);
    JLabel lProcessing = new JLabel("Processing...Please wait");
    processingPanel.add(lProcessing);
    processingDialog = new JDialog(this,
            "Processing");
    processingDialog.setLocation(500, 300);
    processingDialog.setSize(500, 500);
    processingDialog.setUndecorated(true);
    processingDialog.setContentPane(processingPanel);
    processingDialog.pack();
    processingDialog.setResizable(false);
  }

  @Override
  public void addFeatures(Features features) {
    this.features = features;
    pLogin.addFeatures(features);
    pMainMenuPanel.addFeatures(features);
    pPortfolioDetailsPanel.addFeatures(features);
  }

  @Override
  public void switchLayout(String newLayout) {
    switchLayoutHelper(newLayout);
  }

  @Override
  public void packLayout() {
    pack();
  }

  @Override
  public void openMainMenuLayout(String userName, int userID, List<String> portfolios) {
    clearPortfolios();
    pPortfolioDetailsPanel.showBarChart(new ChartPlot());
    pMainMenuPanel.setUserFields(userID, userName);
    pMainMenuPanel.addPortfolios(portfolios);
    switchLayoutHelper("Main Menu");
  }

  private void clearPortfolios() {
    pMainMenuPanel.removeAllPortfolios();
  }

  private void switchLayoutHelper(String newLayout) {
    ((CardLayout) pCards.getLayout()).show(pCards, newLayout);
    features.packLayout();
  }

  @Override
  public String getUserId() {
    return pLogin.getUserId();
  }

  @Override
  public void showWrongUserIDPopup() {
    JOptionPane.showMessageDialog(null,
            "Wrong UserID input. Please try again");
  }

  @Override
  public void showInitializationFailedPopup() {
    JOptionPane.showMessageDialog(null,
            "Initialization failed. Please ensure the directory structure is as expected");
  }

  @Override
  public void showIncorrectStrategyPopup() {
    JOptionPane.showMessageDialog(null,
            "Incorrect strategy input. Please try again.");
  }

  @Override
  public void showNoUserFoundPrompt() {
    JOptionPane.showMessageDialog(null,
            "No user found. You might want to create a new user");
  }

  @Override
  public String getUserName() {
    return pLogin.getUserName();
  }

  @Override
  public void showNoSpecialCharsAllowedPopup() {
    JOptionPane.showMessageDialog(null,
            "No special characters allowed. Please try again.");
  }

  @Override
  public void showDBStoreErrorMessage() {
    JOptionPane.showMessageDialog(null,
            "Failed to persist to database."
                    + " Please ensure the directory structure is as expected.");
  }

  @Override
  public void requestCreatePortfolio() {
    JPanel panel = new JPanel();
    JComboBox<String> createTypeComboBox = new JComboBox<>(
            new String[]{"Create Portfolio Manually","Create Portfolio with Strategy",
                "Upload Portfolio"});

    panel.add(createTypeComboBox);

    int result = JOptionPane.showConfirmDialog(null, panel, "Create Portfolio",
            JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

    if (result == JOptionPane.OK_OPTION) {
      switch (Objects.requireNonNull(createTypeComboBox.getSelectedItem()).toString()) {
        case "Create Portfolio Manually":
          features.requestCreateFlexiblePortfolioWithoutStrategy();
          break;
        case "Create Portfolio with Strategy":
          features.requestCreateFlexiblePortfolioWithStrategy();
          break;
        case "Upload Portfolio":
          features.requestCreateFlexiblePortfolioWithXML();
          break;
        default:
          return ;
      }
    }
  }

  @Override
  public void requestCreateFlexiblePortfolioWithoutStrategy(List<String> stocks) {
    createPortfolioWithoutStrategyPanel = new CreatePortfolioWithoutStrategyPanel(stocks);
    createPortfolioWithoutStrategyPanel.addFeatures(this.features);
    createPortfolioWithoutStrategyDialog = new JDialog(this,
            "Create Portfolio Manually");
    createPortfolioWithoutStrategyDialog.setLocation(300, 200);
    createPortfolioWithoutStrategyDialog.setSize(500, 500);
    createPortfolioWithoutStrategyDialog.setContentPane(createPortfolioWithoutStrategyPanel);
    createPortfolioWithoutStrategyDialog.pack();
    createPortfolioWithoutStrategyDialog.setResizable(false);
    createPortfolioWithoutStrategyDialog.setVisible(true);
  }

  @Override
  public void requestCreateFlexiblePortfolioWithStrategy(List<String> stocks,
                                                         List<Strategy> strategyTypes) {
    createPortfolioWithStrategyPanel = new CreatePortfolioWithStrategyPanel(stocks, strategyTypes);
    createPortfolioWithStrategyPanel.addFeatures(this.features);
    createPortfolioWithStrategyDialog = new JDialog(this,
            "Create Portfolio with Strategy");
    createPortfolioWithStrategyDialog.setLocation(300, 200);
    createPortfolioWithStrategyDialog.setSize(500, 500);
    createPortfolioWithStrategyDialog.setContentPane(createPortfolioWithStrategyPanel);
    createPortfolioWithStrategyDialog.pack();
    createPortfolioWithStrategyDialog.setResizable(false);
    createPortfolioWithStrategyDialog.setVisible(true);
  }

  @Override
  public void requestCreateFlexiblePortfolioWithXML() {
    createPortfolioWithUploadXML = new CreatePortfolioWithUploadXML();
    createPortfolioWithUploadXML.addFeatures(this.features);
    createPortfolioWithUploadXMLDialog = new JDialog(this,
            "Upload Portfolio");
    createPortfolioWithUploadXMLDialog.setLocation(300, 200);
    createPortfolioWithUploadXMLDialog.setSize(500, 500);
    createPortfolioWithUploadXMLDialog.setContentPane(createPortfolioWithUploadXML);
    createPortfolioWithUploadXMLDialog.pack();
    createPortfolioWithUploadXMLDialog.setResizable(false);
    createPortfolioWithUploadXMLDialog.setVisible(true);
  }

  @Override
  public void closeAddFlexiblePortfolioWithUploadXMLPopup(List<String> portfolioNames) {
    this.createPortfolioWithUploadXMLDialog.setVisible(false);
    this.pMainMenuPanel.removeAllPortfolios();
    this.pMainMenuPanel.addPortfolios(portfolioNames);
  }

  @Override
  public void showIncorrectFileUploadPopup() {
    JOptionPane.showMessageDialog(null,
            "Format of the file uploaded is incorrect. Please try again.");
  }

  @Override
  public void setSelectedFile(File file) {
    this.createPortfolioWithUploadXML.setSelectedFile(file);
  }

  @Override
  public void closeAddFlexiblePortfolioWithStrategyPopup(List<String> portfolioNames) {
    createPortfolioWithStrategyDialog.setVisible(false);
    this.pMainMenuPanel.removeAllPortfolios();
    this.pMainMenuPanel.addPortfolios(portfolioNames);
  }

  @Override
  public void clearLoginPageInputs() {
    this.pLogin.clearInputs();
  }

  @Override
  public void clearPortfolioDetailsPage() {
    this.pPortfolioDetailsPanel.clearPage();
  }

  @Override
  public void showModifyPortfolioSuccessPopup() {
    JOptionPane.showMessageDialog(null,
            "Portfolio modified successfully");
  }

  @Override
  public void showAddStrategySuccessPopup() {
    JOptionPane.showMessageDialog(null,
            "Strategy added successfully");
  }

  @Override
  public void openPortfolioDetails(String portfolioName, List<List<String>> composition) {
    pPortfolioDetailsPanel.setPortfolioName(portfolioName);
    pPortfolioDetailsPanel.showBarChart(new ChartPlot());
    pPortfolioDetailsPanel.showComposition(composition);
    switchLayoutHelper("Portfolio Details");
  }

  @Override
  public String getDateInput() {
    DateInputPanel panel = new DateInputPanel();
    int result = JOptionPane.showConfirmDialog(null, panel, "Date",
            JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

    if (result == JOptionPane.OK_OPTION) {
      return panel.getDate();
    }

    return null;
  }

  @Override
  public String[] getStartEndDateInput() {
    StartEndDateInputPanel panel = new StartEndDateInputPanel();
    int result = JOptionPane.showConfirmDialog(null, panel, "Date",
            JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

    if (result == JOptionPane.OK_OPTION) {
      return new String[]{panel.getStartDate(), panel.getEndDate()};
    }

    return new String[0];
  }


  @Override
  public void showWrongDateInputPopup() {
    JOptionPane.showMessageDialog(null,
            "Incorrect date input. Please try again.");
  }

  @Override
  public void showValueOnADate(Date date, double value) {
    pPortfolioDetailsPanel.showBarChart(new ChartPlot());
    pPortfolioDetailsPanel.showValueOnADate(date, value);
    pack();
  }

  @Override
  public void showCostBasis(Date date, double value) {
    pPortfolioDetailsPanel.showBarChart(new ChartPlot());
    pPortfolioDetailsPanel.showCostBasis(date, value);
    pack();
  }

  @Override
  public void showComposition(List<List<String>> composition) {
    pPortfolioDetailsPanel.showBarChart(new ChartPlot());
    pPortfolioDetailsPanel.showComposition(composition);
    pack();
  }

  @Override
  public void showIncorrectQuantityPopup() {
    JOptionPane.showMessageDialog(null,
            "Incorrect Quantity input, please try again.");
  }

  @Override
  public void showBarChart(String startDate, String endDate,
                           String portfolioName, ChartPlot chartPlot) {
    pPortfolioDetailsPanel.showBarChart(chartPlot);
    pack();
  }

  @Override
  public void showIncorrectWeightPopup() {
    JOptionPane.showMessageDialog(null,
            "Please make sure stock weightage sums to exactly 100");
  }

  @Override
  public void showIncorrectCommissionPopup() {
    JOptionPane.showMessageDialog(null,
            "Incorrect commission input, please try again.");
  }

  @Override
  public void showIncorrectAmountPopup() {
    JOptionPane.showMessageDialog(null,
            "Incorrect amount input, please try again.");
  }

  @Override
  public void showIncorrectFrequencyPopup() {
    JOptionPane.showMessageDialog(null,
            "Incorrect frequency input, please try again.");
  }

  @Override
  public void requestMoreStockInput() {
    if (createPortfolioWithoutStrategyPanel != null) {
      this.createPortfolioWithoutStrategyPanel.addStock();
      this.createPortfolioWithoutStrategyDialog.pack();
    }
  }

  @Override
  public void requestMoreStockInputForStrategy() {
    if (addStrategyPanel != null) {
      this.addStrategyPanel.getAddStrategyComponentPanel().addStock();
      this.addStrategyDialog.pack();
    }
    if (createPortfolioWithStrategyPanel != null) {
      this.createPortfolioWithStrategyPanel.getAddStrategyComponentPanel().addStock();
      this.createPortfolioWithStrategyDialog.pack();
    }
  }

  @Override
  public void showPortfolioNameShouldOnlyBeAlphaNumericPopup() {
    JOptionPane.showMessageDialog(null,
            "Only alphanumeric characters permitted for portfolio name, please try again.");
  }

  @Override
  public void showStrategyNameShouldOnlyBeAlphaNumericPopup() {
    JOptionPane.showMessageDialog(null,
            "Only alphanumeric characters permitted for strategy name, please try again.");
  }

  @Override
  public void showPortfolioNameAlreadyExists() {
    JOptionPane.showMessageDialog(null,
            "Portfolio with same name already exists, please try again.");
  }

  @Override
  public void closeAddFlexiblePortfolioWithoutStrategyPopup(List<String> portfolios) {
    this.createPortfolioWithoutStrategyDialog.setVisible(false);
    this.pMainMenuPanel.removeAllPortfolios();
    this.pMainMenuPanel.addPortfolios(portfolios);
  }

  @Override
  public void showModifyPortfolioPopup(String portfolioName, Map<String, Double> portfolioElements,
                                       List<String> stocks) {
    ModifyPortfolioPanel modifyPortfolioPanel = new ModifyPortfolioPanel(portfolioName,
            portfolioElements, stocks);
    modifyPortfolioPanel.addFeatures(this.features);
    modifyPortfolioDialog = new JDialog(this,
            "Modify Portfolio");
    modifyPortfolioDialog.setLocation(300, 200);
    modifyPortfolioDialog.setSize(500, 500);
    modifyPortfolioDialog.setContentPane(modifyPortfolioPanel);
    modifyPortfolioDialog.pack();
    modifyPortfolioDialog.setResizable(false);
    modifyPortfolioDialog.setVisible(true);
  }

  @Override
  public void showNoPriceOnDatePopup(String stockName) {
    JOptionPane.showMessageDialog(null,
            String.format("No price found on the given date for stock - %s,"
                    + " please input correct date.", stockName));
  }

  @Override
  public void closeAddStrategyPopup() {
    this.addStrategyDialog.setVisible(false);
  }

  @Override
  public void closeModifyPortfolioPopup() {
    modifyPortfolioDialog.setVisible(false);
  }

  @Override
  public void showDownloadPortfolioPopup(String path) {
    JOptionPane.showMessageDialog(null, "Portfolio downloaded and saved at: "
            + path);
  }

  @Override
  public void showFailedToSavePortfolio() {
    JOptionPane.showMessageDialog(null, "Failed to save portfolio."
            + " Please try again.");
  }

  @Override
  public void showSameStrategyNameAlreadyExist() {
    JOptionPane.showMessageDialog(null,
            "Strategy with same name already exists for the portfolio, please try again.");
  }

  @Override
  public void showProcessingPopup() {
    processingDialog.setVisible(true);
    processingDialog.toFront();
    processingDialog.repaint();
  }

  @Override
  public void closeProcessingPopup() {
    processingDialog.setVisible(false);
  }

  @Override
  public void requestAddStrategy(String portfolioName, List<String> stocks,
                                 List<Strategy> strategyTypes) {
    addStrategyPanel = new AddStrategyPanel(portfolioName, stocks, strategyTypes);
    addStrategyPanel.addFeatures(this.features);
    addStrategyDialog = new JDialog(this,
            "Add Strategy");
    addStrategyDialog.setLocation(300, 200);
    addStrategyDialog.setSize(500, 500);
    addStrategyDialog.setContentPane(addStrategyPanel);
    addStrategyDialog.pack();
    addStrategyDialog.setResizable(false);
    addStrategyDialog.setVisible(true);
  }

}
