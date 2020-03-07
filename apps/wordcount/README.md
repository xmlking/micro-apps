# WordCount

wordcount pipeline demo. 

### Test
```bash
gradle :apps:samples:wordcount:test
```

### Run

#### Local Run  
```bash
gradle :apps:samples:wordcount:run --args="--runner=DirectRunner --inputFile=./src/test/resources/data/input.txt --output=./build/output.txt"
```
 

### Build
```bash
# display version
gradle :versionDisplay
# clean
gradle :apps:samples:wordcount:clean
# make fatJar
gradle :apps:samples:wordcount:build
# docker build
gradle :apps:samples:wordcount:jibDockerBuild
```
