package views;

import org.jdatepicker.impl.DateComponentFormatter;
import org.jdatepicker.impl.JDatePickerImpl;


import java.awt.GridLayout;
import java.util.Collections;
import java.util.List;
import java.util.Objects;


import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import utils.DateRangeCalculatorUtil;

/**
 *  Panel class to show the stock item panel. It extends JPanel ad all its features.
 */
public class StockItemPanel extends JPanel {
  JLabel lStockName;
  JLabel lQuantity;
  JLabel lCommission;
  JLabel lDate;

  JComboBox<String> cbStocks;
  JTextField tQuantity;
  JTextField tCommission;
  List<String> stocks;
  private JDatePickerImpl dpDate;

  /**
   * Public constructor to show the stock item panel.
   * @param stocks list of stocks available.
   */
  public StockItemPanel(List<String> stocks) {
    this.stocks = stocks;
    initializeViews();
  }

  private void initializeViews() {
    lStockName = new JLabel("Stock Name");
    lQuantity = new JLabel("Quantity");
    lCommission = new JLabel("Commission");
    lDate = new JLabel("Date");

    Collections.sort(this.stocks);
    cbStocks = new JComboBox<>(this.stocks.toArray(new String[0]));

    tQuantity = new JTextField();
    tCommission = new JTextField("0");
    dpDate = new JDatePickerImpl(CustomJDatePanel.getCustomJDatePanel(),
            new DateComponentFormatter());

    this.setLayout(new GridLayout(0, 4));

    this.add(lStockName);
    this.add(lQuantity);
    this.add(lCommission);
    this.add(lDate);

    this.add(cbStocks);
    this.add(tQuantity);
    this.add(tCommission);
    this.add(dpDate);
  }

  protected String getStockName() {
    return Objects.requireNonNull(this.cbStocks.getSelectedItem()).toString();
  }

  protected String getQuantity() {
    return this.tQuantity.getText();
  }

  protected String getCommission() {
    return this.tCommission.getText();
  }

  protected String getDate() {
    if (this.dpDate.getModel() != null && this.dpDate.getModel().getValue() != null) {
      String date = this.dpDate.getModel().getValue().toString();
      return DateRangeCalculatorUtil.dateConverter(date,
              "EEE MMM dd HH:mm:ss zzzz yyyy", "yyyy-MM-dd");
    } else {
      return "";
    }
  }
}
