FROM quay.io/strimzi/kafka:latest-kafka-2.8.0
USER root:root
RUN mkdir -p /opt/kafka/plugins/debezium
COPY ./debezium-connector-postgres.1.6.1/ /opt/kafka/plugins/debezium/
USER 1001