package com.cosmos.sine.server.http;

/**
 * HTTP Server Interface.
 *
 * @author BSD
 */
public interface HttpServer {

    /**
     * Start HTTP service.
     *
     * @return true if listen service established, false on error
     */
    Boolean start();

    /**
     * Keeping service and blocking until the service is shutdown.
     *
     * @throws InterruptedException interrupted exception
     */
    void join() throws InterruptedException;

    /**
     * Destroy service.
     */
    void shutdown();

}
