# avro-tools

Refer [avro-tools](https://tsypuk.github.io/avro-tools) Docs

## Install Tools

you can also manually download the binary version of [hadoop-3.2.1.tar.gz](https://hadoop.apache.org/releases.html) and
set `HADOOP_HOME`

```bash
brew install hadoop 
brew install avro-tools
```

```bash
export HADOOP_HOME=/usr/local/Cellar/hadoop/3.2.1_1
export HADOOP_OPTS="-Djava.library.path=$HADOOP_HOME/lib"

# Retrieve Avro schema from binary Avro
avro-tools getschema person.avro
# avro-tools getschema person.avro > person.avsc

# Binary Avro to JSON
avro-tools tojson person.avro
# avro-tools tojson e2e/fixtures/person.avro > e2e/fixtures/person.json

# Reads JSON records and writes an Avro data file.
avro-tools fromjson --schema-file person.avsc person.json
# avro-tools fromjson --schema-file person.avsc person.json > person.avro

# Renders a JSON-encoded Avro datum as binary.
avro-tools jsontofrag --schema-file person.avsc person.json 
# avro-tools jsontofrag --schema-file person.avsc person.json > person-binary.avro

# Extract records from a group of files into single file
avro-tools cat /tmp/backward*.avro /tmp/newavro.avro

# Get metainformation
avro-tools getmeta person.avro

# To Generate random test data
avro-tools random person.avro --count 5 --schema-file person.avsc
avro-tools random person.snappy.avro --codec snappy --count 5 --schema-file person.avsc

avro-tools tojson person.avro > person.json
```
