import controller.Controller;
import controller.ControllerImpl;
import controller.GUIController;
import models.UserStockModel;
import models.UserStockModelImpl;
import views.GUIView;
import views.HomeView;
import views.TextBasedView;
import views.TextBasedViewImpl;

/**
 * Runner class which starts the portfolio system.
 */
public class Runner {
  /**
   * Main class to start the program. It initialises
   * userSetModel, stockSetModel, textBasedView,
   * controller and calls controller to display options.
   *
   * @param args arguments required for java to run main.
   */
  public static void main(String[] args) {
    UserStockModel userStockModel = new UserStockModelImpl();
    String rootDirectory = "UserSetModel";
    if (args.length != 0 && args[0].equals("gui")) {
      GUIView guiView = new HomeView();
      Controller controller = new GUIController(userStockModel, guiView, rootDirectory);
    } else {
      TextBasedView textBasedView = new TextBasedViewImpl(System.out, System.out);
      Controller controller = new ControllerImpl(userStockModel,
              textBasedView, System.in, rootDirectory);
      controller.start();
    }
  }
}
