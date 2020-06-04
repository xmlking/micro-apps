FROM elasticsearch:6.8.10

LABEL maintainer="Said Sef <saidsef@gmail.com> (saidsef.co.uk)"

ENV cluster.name=spot
ENV node.name=ec2
ENV ES_JAVA_OPTS="-Xms1g -Xmx2g -XX:CMSInitiatingOccupancyFraction=75"
ENV bootstrap.memory_lock=true
ENV discovery.type=single-node
ENV xpack.security.enabled=false
ENV xpack.monitoring.enabled=true
ENV xpack.monitoring.collection.enabled=true
ENV xpack.watcher.enabled=true
ENV xpack.graph.enabled=true
ENV xpack.ml.enabled=true
ENV http.compression=true
ENV http.compression_level=9
ENV http.cors.enabled=true
ENV node.ingest=true

RUN cd /usr/share/elasticsearch \
    && bin/elasticsearch-plugin install -b analysis-icu \
    && bin/elasticsearch-plugin install -b analysis-phonetic \
    && bin/elasticsearch-plugin install -b discovery-ec2 \
    && bin/elasticsearch-plugin install -b discovery-file \
    && bin/elasticsearch-plugin install -b ingest-attachment \
    && bin/elasticsearch-plugin install -b mapper-annotated-text \
    && bin/elasticsearch-plugin install -b mapper-murmur3 \
    && bin/elasticsearch-plugin install -b mapper-size \
    && bin/elasticsearch-plugin install -b repository-s3

USER elasticsearch
