package org.taskmanagementapp.project.exception;

import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

@Provider
public class GlobalExceptionMapper implements ExceptionMapper<BaseException> {

  @Override
  public Response toResponse(BaseException exception) {
    return Response.status(exception.getStatus())
        .entity(exception.getMessage())
        .build();
  }
}