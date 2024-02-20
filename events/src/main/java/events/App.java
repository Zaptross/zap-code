package events;

import io.javalin.Javalin;
import io.javalin.http.sse.SseClient;
import java.util.ArrayList;
import java.util.concurrent.Executors;

public class App {
    public String getGreeting() {
        return "Hello World";
    }

    public static void main(String[] args) {
        var app = Javalin.create().start(7071);
        var clients = new ArrayList<SseClient>();
        var running = true;

        var es = Executors.newFixedThreadPool(1);
        es.execute(() -> {
            while (running) {
                try {
                    for (var client : clients) {
                        if (client.terminated()) {
                            client.close();
                            clients.remove(client);
                        } else {
                            client.sendEvent("ping", "Hello, SSE");
                        }
                    }
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    // explicitly ignore the exception
                }
            }
        });

        app.sse(
                "/sse",
                client -> {
                    System.err.println("Client connected");
                    client.sendEvent("connected", "Hello, SSE");
                    System.err.println("Event sent");
                    client.sendComment("Hello, SSE");
                    System.err.println("Comment sent");
                    client.onClose(() -> {
                        System.out.println("Client disconnected");
                    });

                    clients.add(client);
                    client.keepAlive();
                });
    }
}
