package org.taskmanagementapp.project.exception;

import jakarta.ws.rs.core.Response;

public abstract class BaseException extends RuntimeException {

  public BaseException(String message) {
    super(message);
  }

  public abstract Response.Status getStatus();
}