package org.taskmanagementapp.project.service;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;
import org.taskmanagementapp.common.dto.HealthStatus;

@Path("/status")
@Tag(name = "Health", description = "Health check endpoints")
public class HealthResource {

  @GET
  @Produces(MediaType.APPLICATION_JSON)
  @Operation(summary = "Health check", description = "Returns the health status of the project service")
  public HealthStatus health() {
    return HealthStatus.up("project-service");
  }
}