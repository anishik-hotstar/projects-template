package main

import (
	"fmt"
	"github.com/prometheus/client_golang/prometheus/promhttp"
	"net/http"
)

func HelloServer(w http.ResponseWriter, r *http.Request) {
	fmt.Fprintf(w, "Hello, world!!!")
}

func main() {
	http.HandleFunc("/healthy", HelloServer)
	http.ListenAndServe(":8080", nil)
	http.Handle("/metrics", promhttp.Handler())
	http.ListenAndServe(":4001", nil)
}
