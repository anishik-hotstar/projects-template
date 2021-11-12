FROM golang:1.16-alpine

WORKDIR /app

COPY go.mod .
RUN go mod download

COPY src/main/go/com/hotstar/boilerplate/*.go .

RUN go build -o /hello-world-go

EXPOSE 8080

# CMD [ "go", "run", "." ]

CMD [ "/hello-world-go" ]

# docker build --tag hello-world-go .
# docker run -d -p  8080:8080 --name example hello-world-go
