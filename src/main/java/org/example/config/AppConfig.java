package org.example.config;

import io.vertx.core.json.JsonObject;

public class AppConfig {
    public static JsonObject getConfig() {
        return new JsonObject()
                .put("connection_string", "mongodb://localhost:27017")
                .put("db_name", "Event_Management")
                .put("http.port", 8888);
    }
}
