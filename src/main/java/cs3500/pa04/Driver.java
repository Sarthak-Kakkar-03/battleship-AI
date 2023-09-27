package cs3500.pa04;

import cs3500.pa04.controller.Controller;
import cs3500.pa04.controller.ProxyController;
import cs3500.pa04.model.ArtIntel;
import cs3500.pa04.model.Player;
import java.io.IOException;
import java.net.Socket;

/**
 * This is the main driver of this project.
 */
public class Driver {
  /**
   * Project entry point
   *
   * @param args - no command line args required
   */
  public static void main(String[] args) {
    if (args.length == 0) {
      Controller controller = new Controller();
      controller.startGame();
      //controller.simulateWar();
    } else if (args.length == 2) {
      try {
        int port = Integer.parseInt(args[1]);
        runClient(args[0], port);
        //runClient("0.0.0.0", 35001);
      } catch (IOException e) {
        System.out.println(":(");
        System.out.println(e);
      }
    } else {
      System.out.println("You haven't provided a valid host and/or port!\n"
          + "Please provide a valid input in format: host port");
    }
  }


  private static void runClient(String host, int port)
      throws IOException, IllegalStateException {
    Socket server = new Socket(host, port);
    ArtIntel contender = new ArtIntel("pa04-font-gpt");
    ProxyController proxyController = new ProxyController(server, contender);
    proxyController.run();
  }
}
