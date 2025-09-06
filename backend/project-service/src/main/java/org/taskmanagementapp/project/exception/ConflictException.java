package org.taskmanagementapp.project.exception;

import jakarta.ws.rs.core.Response;

public class ConflictException extends BaseException {

  public ConflictException(String message) {
    super(message);
  }

  @Override
  public Response.Status getStatus() {
    return Response.Status.CONFLICT;
  }
}