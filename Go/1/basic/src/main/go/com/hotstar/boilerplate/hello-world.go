package main

import (
    "fmt"
    "net/http"
)

func main() {
    http.HandleFunc("/healthy", HelloServer)
    http.ListenAndServe(":8080", nil)
}

func HelloServer(w http.ResponseWriter, r *http.Request) {
    fmt.Fprintf(w, "Hello, world!!!")
}
