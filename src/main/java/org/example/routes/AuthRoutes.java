package org.example.routes;

import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.mongo.MongoClient;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import org.example.services.UserService;

public class AuthRoutes {

    private final UserService userService;

    public AuthRoutes(Vertx vertx, MongoClient mongoClient) {
        this.userService = new UserService(vertx, mongoClient);
    }

    public void init(Router mainRouter, Vertx vertx) {
        Router authRouter = Router.router(vertx);  // ✅ No more getVertx()

        // Register endpoint
        authRouter.post("/register").handler(this::handleRegister);

        // Login endpoint
        authRouter.post("/login").handler(this::handleLogin);

        // ✅ Use new recommended method instead of deprecated mountSubRouter
        mainRouter.route("/api/*").subRouter(authRouter);
    }

    private void handleRegister(RoutingContext ctx) {
        JsonObject body = ctx.body().asJsonObject();
        String name = body.getString("name");
        String email = body.getString("email");

        userService.registerUser(name, email, res -> {
            if (res.succeeded()) {
                ctx.response()
                        .putHeader("Content-Type", "application/json")
                        .setStatusCode(200)
                        .end(new JsonObject().put("message", res.result()).encode());
            } else {
                ctx.response()
                        .putHeader("Content-Type", "application/json")
                        .setStatusCode(500)
                        .end(new JsonObject().put("error", res.cause().getMessage()).encode());
            }
        });
    }

    private void handleLogin(RoutingContext ctx) {
        JsonObject body = ctx.body().asJsonObject();
        String email = body.getString("email");
        String password = body.getString("password");

        userService.loginUser(email, password, res -> {
            if (res.succeeded()) {
                ctx.response()
                        .putHeader("Content-Type", "application/json")
                        .setStatusCode(200)
                        .end(res.result().encode());
            } else {
                ctx.response()
                        .putHeader("Content-Type", "application/json")
                        .setStatusCode(401)
                        .end(new JsonObject().put("error", res.cause().getMessage()).encode());
            }
        });
    }
}
