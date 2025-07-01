package org.example.services;

import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.mongo.MongoClient;
import org.example.utils.EmailService;
import org.example.utils.PasswordGenerator;

public class UserService {

    private final MongoClient mongoClient;
    private final Vertx vertx;

    public UserService(Vertx vertx, MongoClient mongoClient) {
        this.vertx = vertx;
        this.mongoClient = mongoClient;
    }

    public void registerUser(String name, String email, io.vertx.core.Handler<io.vertx.core.AsyncResult<String>> resultHandler) {
        String plainPassword = PasswordGenerator.generateRandomPassword(8);  // fixed to 8 characters

        JsonObject user = new JsonObject()
                .put("name", name)
                .put("email", email)
                .put("password", plainPassword);  // Store plain for now (bcrypt version optional)

        System.out.println("Registering user: " + email);

        mongoClient.insert("users", user, insertRes -> {
            if (insertRes.succeeded()) {
                System.out.println("User inserted into DB: " + email);

                // Send email in a blocking-safe worker thread
                vertx.executeBlocking(
                        promise -> {
                            try {
                                EmailService.sendEmail(
                                        email,
                                        "Your Event App Password",
                                        "Hello " + name + ",\n\nYour password is: " + plainPassword + "\n\nThank you for registering!"
                                );
                                promise.complete();
                            } catch (Exception e) {
                                promise.fail(e);
                            }
                        },
                        emailRes -> {
                            if (emailRes.succeeded()) {
                                System.out.println("✅ Email sent to " + email);
                            } else {
                                System.err.println("❌ Email failed to " + email + ": " + emailRes.cause());
                            }
                        }
                );

                resultHandler.handle(io.vertx.core.Future.succeededFuture("User registered successfully"));
            } else {
                resultHandler.handle(io.vertx.core.Future.failedFuture("Failed to register user: " + insertRes.cause()));
            }
        });
    }

    public void loginUser(String email, String password, io.vertx.core.Handler<io.vertx.core.AsyncResult<JsonObject>> resultHandler) {
        JsonObject query = new JsonObject()
                .put("email", email)
                .put("password", password); // Plain password comparison for now

        mongoClient.findOne("users", query, null, res -> {
            if (res.succeeded()) {
                JsonObject user = res.result();
                if (user != null) {
                    resultHandler.handle(io.vertx.core.Future.succeededFuture(user));
                } else {
                    resultHandler.handle(io.vertx.core.Future.failedFuture("Invalid email or password"));
                }
            } else {
                resultHandler.handle(io.vertx.core.Future.failedFuture("Login failed: " + res.cause()));
            }
        });
    }
}
