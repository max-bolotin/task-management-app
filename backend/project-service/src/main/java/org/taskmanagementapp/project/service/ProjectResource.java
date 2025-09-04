package org.taskmanagementapp.project.service;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

@Path("/ping")
@Tag(name = "Health")
public class ProjectResource {

  @GET
  @Produces(MediaType.TEXT_PLAIN)
  @Operation(summary = "Health check for Project Service")
  public String ping() {
    return "project-service: OK";
  }
}
