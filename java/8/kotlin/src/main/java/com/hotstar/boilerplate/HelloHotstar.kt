package com.hotstar.boilerplate

import com.hotstar.bootcamp.HTTPMetricHandler
import com.sun.net.httpserver.HttpServer
import io.prometheus.client.CollectorRegistry
import java.net.InetSocketAddress
import io.prometheus.client.Counter
import io.prometheus.client.Summary
import io.prometheus.client.hotspot.DefaultExports


fun main(){

    val reqCounter = Counter.build()
            .name("http_request_count")
            .labelNames("method")
            .help("Total requests processed by the server")
            .register()
    val respStatusCounter = Counter.build()
            .name("http_response_status_count")
            .labelNames("status")
            .help("Response status as returned by the server")
            .register()
    val reqLatency = Summary.build()
            .quantile(0.5, 0.05)
            .quantile(0.9, 0.01)
            .name("requests_latency_seconds")
            .help("Request latency in seconds")
            .labelNames("method")
            .register()

    val server = HttpServer.create(InetSocketAddress(8080), 0)

    server.createContext("/healthy"){
        val response = """{"status" : "Welcome to hello! hotstar bootcamp"}""".toByteArray()
        it.responseHeaders.add("Content-type", "application/json")
        it.sendResponseHeaders(200, response.size.toLong())
        it.responseBody.use {
            it.write(response)
        }
    }
    DefaultExports.initialize()
    val metricsHandler = HTTPMetricHandler(CollectorRegistry.defaultRegistry)
    server.createContext("/metrics", metricsHandler)
    server.createContext("/-/healthy", metricsHandler)

    System.out.println("Starting server on port 8080")
    server.start()
}