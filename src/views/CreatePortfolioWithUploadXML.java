package views;


import java.awt.Component;
import java.io.File;

import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import controller.Features;

/**
 * Panel class to show Create Portfolio With Upload XML panel.
 * This panel extends the jpanel and all its features.
 */
public class CreatePortfolioWithUploadXML extends JPanel {
  private final JTextField tPortfolioName;
  private final CustomFileChooser fcXMLFile;
  private Features features;

  /**
   * Public constructor to create the panel.
   */
  public CreatePortfolioWithUploadXML() {
    this.setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
    JLabel lPortfolioName = new JLabel("Portfolio Name", SwingConstants.CENTER);
    lPortfolioName.setAlignmentX(Component.CENTER_ALIGNMENT);

    JLabel lXMLFile = new JLabel("XML File Upload", SwingConstants.CENTER);
    lXMLFile.setAlignmentX(Component.CENTER_ALIGNMENT);

    tPortfolioName = new JTextField();
    tPortfolioName.setAlignmentX(Component.CENTER_ALIGNMENT);

    fcXMLFile = new CustomFileChooser();
    fcXMLFile.setAlignmentX(Component.CENTER_ALIGNMENT);

    this.add(lPortfolioName);
    this.add(tPortfolioName);
    this.add(lXMLFile);
    this.add(fcXMLFile);
  }

  protected void addFeatures(Features features) {
    this.features = features;
    fcXMLFile.addFeatures(features);
  }

  protected void setSelectedFile(File file) {
    features.createFlexiblePortfolioWithXML(tPortfolioName.getText(), file);
  }
}
