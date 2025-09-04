package org.taskmanagementapp.project.service;

import io.quarkus.runtime.Quarkus;
import io.quarkus.runtime.annotations.QuarkusMain;

@QuarkusMain
public class ProjectServiceApplication {

  public static void main(String... args) {
    Quarkus.run(args);
  }
}
