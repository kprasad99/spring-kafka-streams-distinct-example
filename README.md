# Spring Cloud Kafka Streams Distinct Example

An example project that filters out duplicate messages by key using kafka streams.

## How to run

* Update the `application.yml` to point to your kafka cluster.
* Start the app.
```
mvn spring-boot:run
```

## How to test

* send a curl request(which in turn calls kafka producer and publishes message to `k-msg` topic)
```
curl --location --request POST 'http://localhost:8080/api/notify' \
--header 'Content-Type: application/json' \
--data-raw '{
    "id": "k-1",
    "text": "Hello from my subject"
}'
```
.output
```
2020-06-29 14:04:06.443  INFO 31735 --- [or-http-epoll-3] i.g.k.kafka.endpoint.MessageRestService  : Sending message k-1
2020-06-29 14:04:06.489  INFO 31735 --- [-StreamThread-1] i.g.k.k.c.StreamsConfigurations          : received message with id k-1
```

* send a second curl with different id
```
curl --location --request POST 'http://localhost:8080/api/notify' \
--header 'Content-Type: application/json' \
--data-raw '{
    "id": "k-2",
    "text": "Hello from my subject"
}'
```
.output
```
2020-06-29 14:06:37.830  INFO 31735 --- [or-http-epoll-3] i.g.k.kafka.endpoint.MessageRestService  : Sending message k-2
2020-06-29 14:06:37.900  INFO 31735 --- [-StreamThread-1] i.g.k.k.c.StreamsConfigurations          : received message with id k-2
```

* Now send a curl with same id, Now you can notice logs only sender is logged and not receiver.
```
curl --location --request POST 'http://localhost:8080/api/notify' \
--header 'Content-Type: application/json' \
--data-raw '{
    "id": "k-1",
    "text": "Hello from my subject"
}'
```
.output
```
2020-06-29 14:09:00.152  INFO 31735 --- [or-http-epoll-3] i.g.k.kafka.endpoint.MessageRestService  : Sending message k-1
```