FROM golang:1.15.6-alpine AS build-env

WORKDIR /app

COPY . .

RUN go build -o /server

FROM alpine

EXPOSE 8000

WORKDIR /

COPY --from=build-env /server /

CMD ["/server"]
