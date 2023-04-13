package views;



import java.awt.Dimension;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import controller.Features;

/**
 * Panel class to show the login panel. It extends JPanel ad all its features.
 */
public class LoginPanel extends JPanel {
  private JTextField tUserID;
  private JTextField tUserName;

  private JButton bLogin;
  private JButton bSignIn;
  private JButton bExit;

  /**
   * Public constructor to initialize login panel.
   */
  public LoginPanel() {
    initializeView();
  }

  private void initializeView() {
    BoxLayout boxLayout = new BoxLayout(this, BoxLayout.PAGE_AXIS);
    this.setLayout(boxLayout);

    this.add(Box.createVerticalGlue());
    JLabel lLoginPrompt1 = new JLabel("Already a user?", JLabel.CENTER);
    lLoginPrompt1.setAlignmentX(CENTER_ALIGNMENT);
    this.add(lLoginPrompt1);
    JLabel lLoginPrompt2 = new JLabel("Login with user ID", JLabel.CENTER);
    lLoginPrompt2.setAlignmentX(CENTER_ALIGNMENT);
    this.add(lLoginPrompt2);
    this.add(Box.createRigidArea(new Dimension(0, 10)));

    tUserID = new JTextField(10);
    tUserID.setMaximumSize(tUserID.getPreferredSize());
    this.add(tUserID);
    this.add(Box.createRigidArea(new Dimension(0, 10)));

    bLogin = new JButton("Login");
    bLogin.setAlignmentX(CENTER_ALIGNMENT);
    this.add(bLogin);
    this.add(Box.createRigidArea(new Dimension(0, 10)));

    JLabel lSignInPrompt1 = new JLabel("Create a new account?", JLabel.CENTER);
    lSignInPrompt1.setAlignmentX(CENTER_ALIGNMENT);
    this.add(lSignInPrompt1);

    JLabel lSignInPrompt2 = new JLabel("Enter your username", JLabel.CENTER);
    lSignInPrompt2.setAlignmentX(CENTER_ALIGNMENT);
    this.add(lSignInPrompt2);
    this.add(Box.createRigidArea(new Dimension(0, 10)));

    tUserName = new JTextField(10);
    tUserName.setMaximumSize(tUserName.getPreferredSize());
    tUserName.setAlignmentX(CENTER_ALIGNMENT);
    this.add(tUserName);
    this.add(Box.createRigidArea(new Dimension(0, 10)));

    bSignIn = new JButton("Create Account");
    bSignIn.setAlignmentX(CENTER_ALIGNMENT);
    this.add(bSignIn);

    this.add(Box.createRigidArea(new Dimension(0, 30)));

    bExit = new JButton("Exit");
    bExit.setAlignmentX(CENTER_ALIGNMENT);
    this.add(bExit);

    this.add(Box.createVerticalGlue());
  }

  protected void addFeatures(Features features) {
    bLogin.addActionListener((e) -> features.login());
    bSignIn.addActionListener((e) -> features.signUp());
    bExit.addActionListener((e) -> features.exitProgram());
  }

  protected String getUserId() {
    return tUserID.getText();
  }

  protected String getUserName() {
    return tUserName.getText();
  }

  protected void clearInputs() {
    this.tUserName.setText("");
    this.tUserID.setText("");
  }
}
