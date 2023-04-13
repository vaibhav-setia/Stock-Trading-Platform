package models;

import org.xml.sax.SAXException;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.xpath.XPathExpressionException;

import utils.DataParser;
import utils.DataPersister;
import utils.XMLParserImpl;
import utils.XMLWriterImpl;

/**
 * This class implements UserSetModel and all its functionality.
 * This class stores all the users as an arraylist of {@link UserModel} class.
 */
public class UserSetModelImpl implements UserSetModel {

  private final List<UserModel> userList;

  /**
   * Public constructor to assign a userList to
   * UserSetModel.
   *
   * @param userList list of users to be assigned to userModel.
   */
  public UserSetModelImpl(List<UserModel> userList) {
    this.userList = userList;
  }

  /**
   * Static class to get a builder to build
   * UserSetModel class.
   *
   * @return a new builder object for User Set Model.
   */
  public static UserSetModelBuilder getBuilder() {
    return new UserSetModelBuilder();
  }

  @Override
  public void addUser(UserModel userModel) {
    this.userList.add(userModel);
  }

  @Override
  public int getListSize() {
    return this.userList.size();
  }

  @Override
  public List<UserModel> getUserList() {
    return this.userList;
  }

  @Override
  public void generateXML(String root) throws IOException, ParserConfigurationException,
          TransformerException {
    DataPersister persister = new XMLWriterImpl();
    persister.writeUserSetModel(root, this);
  }

  @Override
  public UserSetModel loadUserSetFromDatabase(String root) throws XPathExpressionException,
          IOException, ParserConfigurationException, SAXException, ParseException {

    File fileToFetch = new File("UserSetModel/UserSetModel.xml");
    if (!fileToFetch.exists()) {
      Files.createDirectories(Paths
              .get("UserSetModel"));
      String baseUserSetModel = "<UserSetModel> </UserSetModel>";
      java.io.FileWriter writer = new java.io.FileWriter(
              "UserSetModel//UserSetModel.xml");
      writer.write(baseUserSetModel);
      writer.close();
    }
    return UserSetModelImpl.getBuilder().readXML(root).build();
  }

  @Override
  public void runAllStrategies() {
    for (UserModel userModel : userList) {
      userModel.runAllStrategies();
    }
  }

  /**
   * Builder helper class to build an object of User Set Model class.
   * It contains functionality to set different fields of User Set Model.
   */
  public static class UserSetModelBuilder {
    private List<UserModel> userList;

    /**
     * Initialise a new builder object with an empty list.
     */
    private UserSetModelBuilder() {
      this.userList = new ArrayList<>();
    }

    /**
     * Set user list of builder object to be assigned
     * to User Set Model.
     *
     * @param userList List of User Model objects to be assigned.
     * @return the resulting builder object with user list assigned.
     */
    public UserSetModelBuilder userList(List<UserModel> userList) {
      this.userList = userList;
      return this;
    }

    /**
     * Read and build a User Set Model object from the xml file.
     *
     * @param path relative path to source folder from where the xml file needs to be read.
     * @return an object of UserSetModelBuilder with all the attributes assigned from XML file.
     * @throws IOException                  if xml is not correctly read from file.
     * @throws ParserConfigurationException if the xml parser is configured incorrectly.
     * @throws SAXException                 exception thrown if xml violates integrity constraints.
     * @throws XPathExpressionException     if the Xpath to filter xml is incorrect.
     * @throws ParseException               if xml string is not correctly parsed as per contract.
     */
    private UserSetModelBuilder readXML(String path) throws
            IOException, ParserConfigurationException,
            SAXException, XPathExpressionException, ParseException {
      DataParser<UserSetModelBuilder> dataParserUserSetModel = new XMLParserImpl<>();
      return dataParserUserSetModel.getUserSetModelBuilder(path);
    }

    /**
     * Build a User Set Model builder object to get User Set Model.
     *
     * @return a new User Set Model object with all users appended
     *         in the list.
     */
    public UserSetModelImpl build() {
      return new UserSetModelImpl(new ArrayList<>(this.userList));
    }
  }
}
