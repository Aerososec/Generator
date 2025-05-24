FROM golang:1.24.1-alpine AS builder

WORKDIR /app
COPY go.mod go.sum ./

COPY . .
WORKDIR /app
RUN go build -o main ./cmd/main.go


FROM alpine:latest

WORKDIR /app

# только бинарник из предыдущей стадии
COPY --from=builder /app/main .

RUN apk add --no-cache ca-certificates

CMD ["./main"]
