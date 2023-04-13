package views;


import java.awt.Component;
import java.awt.GridLayout;
import java.util.ArrayList;
import java.util.List;


import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import controller.Features;

/**
 * Panel class to show Create Portfolio Without Strategy panel.
 * This panel extends the jpanel and all its features.
 */
public class CreatePortfolioWithoutStrategyPanel extends JPanel {
  private JTextField tPortfolioName;
  private JButton bAddStock;
  private JButton bCreate;
  private JPanel pStockList;
  private final List<String> stocks;
  private List<StockItemPanel> stockItemPanels;

  /**
   * Constructor to initialise the panel.
   * @param stocks list of stocks available.
   */
  public CreatePortfolioWithoutStrategyPanel(List<String> stocks) {
    this.stocks = stocks;
    initializeViews();
  }

  private void initializeViews() {
    this.setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));

    stockItemPanels = new ArrayList<>();
    JLabel lPortfolioName = new JLabel("Portfolio Name", SwingConstants.CENTER);
    lPortfolioName.setAlignmentX(Component.CENTER_ALIGNMENT);

    tPortfolioName = new JTextField();

    bAddStock = new JButton("Add another Stock");
    bAddStock.setAlignmentX(Component.CENTER_ALIGNMENT);

    bCreate = new JButton("Create Portfolio");
    bCreate.setAlignmentX(Component.CENTER_ALIGNMENT);

    pStockList = new JPanel();
    pStockList.setLayout(new GridLayout(0, 1));
    StockItemPanel stockItemPanel = new StockItemPanel(stocks);
    pStockList.add(stockItemPanel);
    stockItemPanels.add(stockItemPanel);

    this.add(lPortfolioName);
    this.add(tPortfolioName);
    this.add(pStockList);
    this.add(bAddStock);
    this.add(bCreate);
  }

  protected void addFeatures(Features features) {
    bAddStock.addActionListener((e) -> features.requestMoreStockInput());
    bCreate.addActionListener((e) -> features
            .createFlexiblePortfolioWithoutStrategy(tPortfolioName.getText(),
            getStockNames(), getQuantities(), getCommissions(), getDates()));
  }

  protected void addStock() {
    StockItemPanel stockItemPanel = new StockItemPanel(this.stocks);
    this.pStockList.add(stockItemPanel);
    this.stockItemPanels.add(stockItemPanel);
    this.pStockList.validate();
  }

  private List<String> getStockNames() {
    List<String> stockNames = new ArrayList<>();
    for (StockItemPanel stockItemPanel : stockItemPanels) {
      stockNames.add(stockItemPanel.getStockName());
    }

    return stockNames;
  }

  private List<String> getQuantities() {
    List<String> quantities = new ArrayList<>();
    for (StockItemPanel stockItemPanel : stockItemPanels) {
      quantities.add(stockItemPanel.getQuantity());
    }

    return quantities;
  }

  private List<String> getDates() {
    List<String> dates = new ArrayList<>();
    for (StockItemPanel stockItemPanel : stockItemPanels) {
      dates.add(stockItemPanel.getDate());
    }

    return dates;
  }

  private List<String> getCommissions() {
    List<String> commissions = new ArrayList<>();
    for (StockItemPanel stockItemPanel : stockItemPanels) {
      commissions.add(stockItemPanel.getCommission());
    }

    return commissions;
  }
}
