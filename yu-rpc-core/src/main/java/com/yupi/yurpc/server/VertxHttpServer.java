package com.yupi.yurpc.server;

import io.vertx.core.Vertx;
import io.vertx.core.http.HttpServerOptions;

public class VertxHttpServer implements HttpServer {

    /**
     * 启动服务器
     * @param port
     */
    @Override
    public void doStart(int port) {
        // 创建Vert.x 实例
        Vertx vertx = Vertx.vertx();
        // 设计vertx的timeout便于debug
        HttpServerOptions options = new HttpServerOptions().setIdleTimeout(600);
        // 创建HTTP服务器
        io.vertx.core.http.HttpServer server = vertx.createHttpServer(options);

        // 监听端口并处理请求
        server.requestHandler(new HttpServerHandler());

        // 启动HTTP服务器并监听指定端口
        server.listen(port, result -> {
           if (result.succeeded()) {
               System.out.println("Server is now listening on port " + port);
           } else {
               System.err.println("Failed to start server: " + result.cause());
           }
        });
    }
}
