package org.example;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import io.vertx.ext.mongo.MongoClient;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.BodyHandler;
import io.vertx.ext.web.handler.StaticHandler;
import org.example.routes.AuthRoutes;
import org.example.routes.EventRoutes;
import org.example.services.EventService;

public class MainVerticle extends AbstractVerticle {

    @Override
    public void start(Promise<Void> startPromise) {
        // MongoDB config
        String mongoUri = "mongodb://localhost:27017";
        String dbName = "Event_Management";

        MongoClient mongoClient = MongoClient.createShared(vertx, new io.vertx.core.json.JsonObject()
                .put("connection_string", mongoUri)
                .put("db_name", dbName)
        );

        Router router = Router.router(vertx);

        // Body handler (for JSON)
        router.route().handler(BodyHandler.create());

        // Serve static frontend files (HTML, JS, CSS)
        router.route("/*").handler(StaticHandler.create("web").setCachingEnabled(false));

        // Auth and Event routes
        AuthRoutes authRoutes = new AuthRoutes(vertx, mongoClient);
        authRoutes.init(router, vertx);

        EventService eventService = new EventService(vertx, mongoClient);
        EventRoutes eventRoutes = new EventRoutes(eventService);
        eventRoutes.init(router);

        // Start HTTP server
        vertx.createHttpServer()
                .requestHandler(router)
                .listen(8888)
                .onSuccess(server -> {
                    System.out.println("✅ Server running at http://localhost:8888/");
                    startPromise.complete();
                })
                .onFailure(startPromise::fail);
    }
}
