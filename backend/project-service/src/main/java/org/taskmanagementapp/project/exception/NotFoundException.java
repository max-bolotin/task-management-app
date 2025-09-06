package org.taskmanagementapp.project.exception;

import jakarta.ws.rs.core.Response;

public class NotFoundException extends BaseException {

  public NotFoundException(String message) {
    super(message);
  }

  @Override
  public Response.Status getStatus() {
    return Response.Status.NOT_FOUND;
  }
}