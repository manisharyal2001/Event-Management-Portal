package org.example.routes;

import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import org.example.services.EventService;

public class EventRoutes {
    private final EventService eventService;

    public EventRoutes(EventService eventService) {
        this.eventService = eventService;
    }

    public void init(Router mainRouter) {
        mainRouter.get("/api/events").handler(this::handleGetEvents);
        mainRouter.post("/api/book").handler(this::handleBookToken);
    }

    private void handleGetEvents(RoutingContext ctx) {
        eventService.getAllEvents(res -> {
            if (res.succeeded()) {
                ctx.response()
                        .putHeader("Content-Type", "application/json")
                        .end(res.result().encode());
            } else {
                ctx.response()
                        .setStatusCode(500)
                        .end(res.cause().getMessage());
            }
        });
    }

    private void handleBookToken(RoutingContext ctx) {
        eventService.bookEventToken(ctx);
    }
}
