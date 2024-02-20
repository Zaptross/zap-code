package api;

import io.javalin.Javalin;

public class App {
    public String getGreeting() {
        return "Hello World";
    }

    public static void main(String[] args) {
        var app = Javalin.create().start(7070);

        app.get("/", ctx -> ctx.result("Hello World"));
        app.get("/<name>", ctx -> ctx.result("Hello " + ctx.pathParam("name")));
    }
}