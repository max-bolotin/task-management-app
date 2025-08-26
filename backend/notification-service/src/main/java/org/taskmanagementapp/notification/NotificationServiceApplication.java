package org.taskmanagementapp.notification;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.Response;

@Path("/")
public class NotificationServiceApplication {

    @GET
    @Path("/ping")
    public Response ping() {
        return Response.ok("Notification Service is up!").build();
    }
}
