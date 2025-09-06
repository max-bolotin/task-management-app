package org.taskmanagementapp.project.exception;

import jakarta.ws.rs.core.Response;

public class ValidationException extends BaseException {

  public ValidationException(String message) {
    super(message);
  }

  @Override
  public Response.Status getStatus() {
    return Response.Status.BAD_REQUEST;
  }
}