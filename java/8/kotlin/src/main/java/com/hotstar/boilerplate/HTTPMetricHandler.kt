package com.hotstar.bootcamp

import com.sun.net.httpserver.HttpExchange
import com.sun.net.httpserver.HttpHandler
import java.net.URLDecoder
import java.util.HashSet
import java.io.IOException
import java.util.zip.GZIPOutputStream
import io.prometheus.client.exporter.common.TextFormat
import java.nio.charset.Charset
import java.io.OutputStreamWriter
import java.io.ByteArrayOutputStream
import io.prometheus.client.CollectorRegistry
import java.net.HttpURLConnection


class HTTPMetricHandler(private val registry: CollectorRegistry) : HttpHandler {

    private class LocalByteArray : ThreadLocal<ByteArrayOutputStream>() {
        override fun initialValue(): ByteArrayOutputStream {
            return ByteArrayOutputStream(1 shl 20)
        }
    }

    private val response: LocalByteArray = LocalByteArray()

    @Throws(IOException::class)
    override fun handle(t: HttpExchange) {
        val query = t.requestURI.rawQuery
        val contextPath = t.httpContext.path
        val response = response.get()
        response.reset()
        val osw = OutputStreamWriter(response, Charset.forName("UTF-8"))
        if ("/-/healthy" == contextPath) {
            osw.write(HEALTHY_RESPONSE)
        } else {
            val contentType = TextFormat.chooseContentType(t.requestHeaders.getFirst("Accept"))
            t.responseHeaders.set("Content-Type", contentType)
            TextFormat.writeFormat(contentType, osw,
                    registry.filteredMetricFamilySamples(parseQuery(query)))
        }
        osw.close()
        if (shouldUseCompression(t)) {
            t.responseHeaders.set("Content-Encoding", "gzip")
            t.sendResponseHeaders(HttpURLConnection.HTTP_OK, 0)
            val os = GZIPOutputStream(t.responseBody)
            try {
                response.writeTo(os)
            } finally {
                os.close()
            }
        } else {
            t.responseHeaders.set("Content-Length", response.size().toString())
            t.sendResponseHeaders(HttpURLConnection.HTTP_OK, response.size().toLong())
            response.writeTo(t.responseBody)
        }
        t.close()
    }

    companion object {
        private const val HEALTHY_RESPONSE = "Exporter is Healthy."
    }
}

fun shouldUseCompression(exchange: HttpExchange): Boolean {
    val encodingHeaders = exchange.getRequestHeaders().get("Accept-Encoding")
            ?: return false
    for (encodingHeader in encodingHeaders) {
        val encodings = encodingHeader.split(",".toRegex()).toTypedArray()
        for (encoding in encodings) {
            if (encoding.trim { it <= ' ' }.equals("gzip", ignoreCase = true)) {
                return true
            }
        }
    }
    return false
}

@Throws(IOException::class)
fun parseQuery(query: String?): Set<String>? {
    val names: MutableSet<String> = HashSet()
    if (query != null) {
        val pairs = query.split("&".toRegex()).toTypedArray()
        for (pair in pairs) {
            val idx = pair.indexOf("=")
            if (idx != -1 && URLDecoder.decode(pair.substring(0, idx), "UTF-8") == "name[]") {
                names.add(URLDecoder.decode(pair.substring(idx + 1), "UTF-8"))
            }
        }
    }
    return names
}