package org.taskmanagementapp.notification;

import io.quarkus.runtime.Quarkus;
import io.quarkus.runtime.QuarkusApplication;
import io.quarkus.runtime.annotations.QuarkusMain;

@QuarkusMain
public class NotificationServiceMain implements QuarkusApplication {

  public static void main(String... args) {
    Quarkus.run(NotificationServiceMain.class, args);
  }

  @Override
  public int run(String... args) throws Exception {
    System.out.println("Notification Service started...");
    Quarkus.waitForExit();
    return 0;
  }
}
