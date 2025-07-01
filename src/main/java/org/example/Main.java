package org.example;

import io.vertx.core.Vertx;

public class Main {
    public static void main(String[] args) {
        Vertx vertx = Vertx.vertx();
        vertx.deployVerticle(new MainVerticle(), res -> {
            if (res.succeeded()) {
                System.out.println("✅ MainVerticle deployed successfully!");
            } else {
                System.err.println("❌ Failed to deploy MainVerticle: " + res.cause());
            }
        });
    }
}
