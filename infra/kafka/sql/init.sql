CREATE STREAM emergencies (name VARCHAR, reason VARCHAR, area VARCHAR)
  WITH (kafka_topic='call-center', value_format='json', partitions=1);


CREATE TABLE location_of_interest AS
SELECT reason,
       count_distinct(area) AS distinct_pings,
       latest_by_offset(area) AS last_location
FROM emergencies
GROUP BY reason
    EMIT CHANGES;


CREATE TABLE call_record AS
SELECT name,
       count(reason) AS total_emergencies
FROM emergencies
GROUP BY name
    EMIT CHANGES;
