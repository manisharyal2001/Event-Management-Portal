package org.example.services;

import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.mongo.MongoClient;
import io.vertx.ext.web.RoutingContext;

public class EventService {

    private final Vertx vertx;
    private final MongoClient mongoClient;

    public EventService(Vertx vertx, MongoClient mongoClient) {
        this.vertx = vertx;
        this.mongoClient = mongoClient;
    }

    public void getAllEvents(io.vertx.core.Handler<io.vertx.core.AsyncResult<io.vertx.core.json.JsonArray>> resultHandler) {
        mongoClient.find("event_ticket", new JsonObject(), ar -> {
            if (ar.succeeded()) {
                resultHandler.handle(io.vertx.core.Future.succeededFuture(new io.vertx.core.json.JsonArray(ar.result())));
            } else {
                resultHandler.handle(io.vertx.core.Future.failedFuture(ar.cause()));
            }
        });
    }

    public void bookEventToken(RoutingContext ctx) {
        JsonObject body = ctx.body().asJsonObject();
        String eventId = body.getString("eventId");
        String userEmail = body.getString("email");

        if (eventId == null || userEmail == null) {
            ctx.response()
                    .setStatusCode(400)
                    .end(new JsonObject().put("error", "Missing eventId or email").encode());
            return;
        }

        // Check if tokens are available
        JsonObject query = new JsonObject().put("_id", eventId);

        mongoClient.findOne("event_ticket", query, null, event -> {
            if (event.succeeded() && event.result() != null) {
                JsonObject eventDoc = event.result();
                int availableTokens = eventDoc.getInteger("available_tokens", 0);

                if (availableTokens > 0) {
                    // Reduce token count
                    JsonObject update = new JsonObject()
                            .put("$set", new JsonObject().put("available_tokens", availableTokens - 1));

                    mongoClient.updateCollection("event_ticket", query, update, updateResult -> {
                        if (updateResult.succeeded()) {
                            // Save booking
                            JsonObject booking = new JsonObject()
                                    .put("event_id", eventId)
                                    .put("email", userEmail);

                            mongoClient.insert("booking", booking, insertResult -> {
                                if (insertResult.succeeded()) {
                                    ctx.response()
                                            .putHeader("Content-Type", "application/json")
                                            .end(new JsonObject().put("message", "Token booked successfully!").encode());
                                } else {
                                    ctx.response()
                                            .setStatusCode(500)
                                            .end(new JsonObject().put("error", insertResult.cause().getMessage()).encode());
                                }
                            });
                        } else {
                            ctx.response()
                                    .setStatusCode(500)
                                    .end(new JsonObject().put("error", updateResult.cause().getMessage()).encode());
                        }
                    });
                } else {
                    ctx.response()
                            .setStatusCode(400)
                            .end(new JsonObject().put("error", "No tokens available").encode());
                }
            } else {
                ctx.response()
                        .setStatusCode(404)
                        .end(new JsonObject().put("error", "Event not found").encode());
            }
        });
    }
}
