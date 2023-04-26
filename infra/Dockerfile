ARG KSQL_VERSION=0.28.2
FROM confluentinc/ksqldb-server:${KSQL_VERSION}
ARG KSQL_VERSION

# set up plugins dir
USER root
RUN mkdir -p /usr/share/kafka/plugins
RUN chown appuser:appuser /usr/share/kafka/plugins
USER appuser

ENV KSQL_CONNECT_PLUGIN_PATH "/usr/share/kafka/plugins"

# plugin: datagen connector
RUN bin/confluent-hub install --no-prompt \
    --component-dir /usr/share/kafka/plugins \
    --worker-configs /etc/ksqldb/connect.properties -- \
    confluentinc/kafka-connect-datagen:0.6.0

# plugin: jdbc connector
RUN bin/confluent-hub install --no-prompt \
    --component-dir /usr/share/kafka/plugins \
    --worker-configs /etc/ksqldb/connect.properties -- \
    confluentinc/kafka-connect-jdbc:10.7.1

# plugin: debezium postgresql source connector
RUN bin/confluent-hub install --no-prompt \
    --component-dir /usr/share/kafka/plugins \
    --worker-configs /etc/ksqldb/connect.properties -- \
    debezium/debezium-connector-postgresql:2.1.4

# plugin: common SMTs
RUN bin/confluent-hub install --no-prompt \
    --component-dir /usr/share/kafka/plugins \
    --worker-configs /etc/ksqldb/connect.properties -- \
    jcustenborder/kafka-connect-transform-common:0.1.0.58


