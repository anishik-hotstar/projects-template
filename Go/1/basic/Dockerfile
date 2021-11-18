FROM 084690408984.dkr.ecr.us-east-1.amazonaws.com/go-1.14.2:v59.0

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
