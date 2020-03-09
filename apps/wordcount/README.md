# WordCount

WordCount pipeline demo. 

### Run
#### Local Run  
```bash
gradle :apps:wordcount:run --args="--runner=DirectRunner --inputFile=./src/test/resources/data/input.txt --output=./build/output.txt"
```

### Test
```bash
gradle :apps:wordcount:test
```

### Build
```bash
# clean
gradle :apps:wordcount:clean
# make fatJar
gradle :apps:wordcount:build
```
