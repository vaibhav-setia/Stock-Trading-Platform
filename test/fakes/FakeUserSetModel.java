package fakes;

import java.util.ArrayList;
import java.util.List;

import models.UserModel;
import models.UserModelImpl;
import models.UserSetModel;

/**
 * A Fake User Set model implementation which is created only for the purpose of testing.
 * This implementation does nothing but logs the calls made to its methods.
 */
public class FakeUserSetModel implements UserSetModel {
  private final StringBuilder log;

  /**
   * Construct a Fake User set model given a String builder.
   *
   * @param log the string builder used for generating logs.
   */
  public FakeUserSetModel(StringBuilder log) {
    this.log = log;
  }

  /**
   * A Fake Add User method which just logs the call made.
   *
   * @param userModel a userModel object to be added.
   */
  @Override
  public void addUser(UserModel userModel) {
    log.append("[User Set Model]Add user:").append(userModel.getUserName()).append("\n");
  }

  /**
   * A fake Get List Size method which just logs the call made.
   *
   * @return Zero as default value as fake user set is empty.
   */
  @Override
  public int getListSize() {
    log.append("[User Set Model]Get List Size\n");
    return 0;
  }

  /**
   * A fake get user list method which just logs the call made.
   *
   * @return a dummy list of user models with one entry.
   */
  @Override
  public List<UserModel> getUserList() {
    log.append("[User Set Model]Get user list\n");
    List<UserModel> userModels = new ArrayList<>();
    userModels.add(UserModelImpl.getBuilder().userId(1000).name("Test user").build());
    return userModels;
  }

  /**
   * A fake generate xml method which just logs the call made.
   *
   * @param root folder where the UserSetModel is stored as XML.
   */
  @Override
  public void generateXML(String root) {
    log.append("[User Set Model]Generate xml: ").append(root).append("\n");
  }

  /**
   * A fake load user set from database method which just logs the call made to the method.
   *
   * @param root root directory from where user set data is loaded on startup.
   * @return a fake user set model given the log string builder.
   */
  @Override
  public UserSetModel loadUserSetFromDatabase(String root) {
    log.append("[User Set Model]Load user set from database, root: ").append(root).append("\n");
    return new FakeUserSetModel(log);
  }

  @Override
  public void runAllStrategies() {
    log.append("[User Set Model]Run all strategies called").append("\n");
  }
}
