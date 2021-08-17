# Benthos

**[Benthos](https://www.benthos.dev/)** is simple but powerful ETL tool.

## Setup

```bash
# Install
brew install benthos

# Make a config
benthos create nats/protobuf/aws_sqs > ./config.yaml

# Run
benthos -c ./config.yaml
```

Use **benthos** [Lab UX](https://lab.benthos.dev/l/wEeWzz0O1G0) to visualize the [config.yaml](./config.yaml)

## Raw Data

Download full NPI data from [cms](https://download.cms.gov/nppes/NPI_Files.html) to load into Redis.

Here I am including few records from downloaded **npidata_pfile_20050523-20210613.csv(8.27GB)** file as [npidata.csv](./npidata.csv)

## Commands

```bash
#echo config
benthos -c config.yaml echo

#count input lines. Should be 135
wc -l npidata.csv

# run job
benthos -c config.yaml

#count output lines. Should be 100
wc -l npidata.jsonl
```
