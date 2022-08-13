
via docker-compose

```bash
# start local redis and grafana 
docker compose -f infra/redis.yml up
# (Or) start only redis
docker compose -f infra/redis.yml up redis
# stop local redis before restart again
docker compose -f infra/redis.yml down
# this will stop redis and remove all volumes
docker compose -f infra/redis.yml down -v 
```

gradle :apps:redis-service:kaptKotlin
gradle :apps:redis-service:bootRun

http :8080/api/users/name/Wilk
http :8080/api/users/start/Wil
http :8080/api/users/role roleName==GUITAR
http :8080/api/users/exists email==brad@ratm.com
http --offline :8080/api/users/exists email==brad@ratm.com

http :8080/api/users/q firstName==Brad lastName==Wilk

http :8080/api/people/search/sumo
http :8080/api/people/search/@name_first:sumo

```bash
FLUSHDB
FT.INFO PersonIdx

FT.SEARCH PersonIdx "sumo" 
FT.SEARCH PersonIdx "@name_last:demo" 
FT.SEARCH PersonIdx "@name_first:sumo" RETURN 1 name_first
FT.SEARCH PersonIdx "@name_first:sumo " RETURN 1 address_country

FT.SEARCH PersonIdx "@address_state:{CA}"
FT.SEARCH PersonIdx "@address_city:{riverside}"
FT.SEARCH PersonIdx "@address_country:{usa}"
FT.SEARCH PersonIdx "@address_street:fourt"
FT.SEARCH PersonIdx "@address_street:Jambri"

FT.SEARCH PersonIdx "@name_first:sumo @address_city:{riverside}"
FT.SEARCH PersonIdx "@name_first:sumo @name_last:demo"

FT.EXPLAINCLI PersonIdx "@name_first:{sumo}"
FT.EXPLAINCLI PersonIdx "@name_first:sumo "
FT.EXPLAINCLI PersonIdx "@name_first:(sumo deno)"

FT.PROFILE PersonIdx SEARCH QUERY "@name_first:{sumo}"

FT.INFO UserIdx
FT.SEARCH RoleIdx "@roleName:{GUITAR}"
FT.SEARCH UserIdx "@firstName:{Brad} @lastName:Wilk "
```

```log
1641754524.277569 [0 10.4.1.1:33576] "FT.CREATE" "PersonIdx" "ON" "JSON" "PREFIX" "1" "micro.apps.service.Person:" "SCORE_FIELD" "$.score" "SCHEMA" "$.name.first" "AS" "name_first" "TEXT" "$.name.last" "AS" "name_last" "TEXT" "$.address.street" "AS" "address_street" "TEXT" "NOSTEM" "$.address.city" "AS" "address_city" "TAG" "$.address.state" "AS" "address_state" "TAG" "$.address.country" "AS" "address_country" "TAG" "$.address.location" "AS" "address_location" "GEO" "$.email" "AS" "email" "TAG"
1641754524.289234 [0 10.4.1.1:33576] "FT.CREATE" "RoleIdx" "ON" "HASH" "PREFIX" "1" "micro.apps.service.Role:" "SCHEMA" "roleName" "AS" "roleName" "TAG"
1641754524.291635 [0 10.4.1.1:33576] "FT.CREATE" "UserIdx" "ON" "HASH" "PREFIX" "1" "micro.apps.service.User:" "SCHEMA" "firstName" "AS" "firstName" "TAG" "middleName" "AS" "middleName" "TAG" "lastName" "AS" "lastName" "TEXT" "email" "AS" "email" "TAG"
1641754524.302476 [0 10.4.1.1:33576] "BF.RESERVE" "bf_person_email" "0.001" "100000"
1641754524.303347 [0 10.4.1.1:33576] "BF.RESERVE" "bf_user_email" "0.001" "100000"
1641754524.357686 [0 10.4.1.1:33576] "SISMEMBER" "micro.apps.service.Role" "01FS033ZM1QMHD3C900QFS2M10"
1641754524.376939 [0 10.4.1.1:33576] "DEL" "micro.apps.service.Role:01FS033ZM1QMHD3C900QFS2M10"
1641754524.378661 [0 10.4.1.1:33576] "HMSET" "micro.apps.service.Role:01FS033ZM1QMHD3C900QFS2M10" "_class" "micro.apps.service.Role" "id" "01FS033ZM1QMHD3C900QFS2M10" "roleName" "BASS"
1641754524.379954 [0 10.4.1.1:33576] "SADD" "micro.apps.service.Role" "01FS033ZM1QMHD3C900QFS2M10"
1641754524.381655 [0 10.4.1.1:33576] "SISMEMBER" "micro.apps.service.Role" "01FS033ZNQV6KVZTVE8FGE8QD8"
1641754524.383090 [0 10.4.1.1:33576] "DEL" "micro.apps.service.Role:01FS033ZNQV6KVZTVE8FGE8QD8"
1641754524.384095 [0 10.4.1.1:33576] "HMSET" "micro.apps.service.Role:01FS033ZNQV6KVZTVE8FGE8QD8" "_class" "micro.apps.service.Role" "id" "01FS033ZNQV6KVZTVE8FGE8QD8" "roleName" "VOCALS"
1641754524.384855 [0 10.4.1.1:33576] "SADD" "micro.apps.service.Role" "01FS033ZNQV6KVZTVE8FGE8QD8"
1641754524.385791 [0 10.4.1.1:33576] "SISMEMBER" "micro.apps.service.Role" "01FS033ZNVX7JS7JJ6Q8HSBG3F"
1641754524.386576 [0 10.4.1.1:33576] "DEL" "micro.apps.service.Role:01FS033ZNVX7JS7JJ6Q8HSBG3F"
1641754524.387179 [0 10.4.1.1:33576] "HMSET" "micro.apps.service.Role:01FS033ZNVX7JS7JJ6Q8HSBG3F" "_class" "micro.apps.service.Role" "id" "01FS033ZNVX7JS7JJ6Q8HSBG3F" "roleName" "GUITAR"
1641754524.387741 [0 10.4.1.1:33576] "SADD" "micro.apps.service.Role" "01FS033ZNVX7JS7JJ6Q8HSBG3F"
1641754524.388445 [0 10.4.1.1:33576] "SISMEMBER" "micro.apps.service.Role" "01FS033ZNYDVCKJ732HNAWW732"
1641754524.389584 [0 10.4.1.1:33576] "DEL" "micro.apps.service.Role:01FS033ZNYDVCKJ732HNAWW732"
1641754524.390202 [0 10.4.1.1:33576] "HMSET" "micro.apps.service.Role:01FS033ZNYDVCKJ732HNAWW732" "_class" "micro.apps.service.Role" "id" "01FS033ZNYDVCKJ732HNAWW732" "roleName" "DRUMS"
1641754524.390869 [0 10.4.1.1:33576] "SADD" "micro.apps.service.Role" "01FS033ZNYDVCKJ732HNAWW732"
1641754524.405800 [0 10.4.1.1:33576] "SISMEMBER" "micro.apps.service.User" "01FS033ZPFJ9CZYVH10J787Y64"
1641754524.411729 [0 10.4.1.1:33576] "DEL" "micro.apps.service.User:01FS033ZPFJ9CZYVH10J787Y64"
1641754524.412614 [0 10.4.1.1:33576] "HMSET" "micro.apps.service.User:01FS033ZPFJ9CZYVH10J787Y64" "_class" "micro.apps.service.User" "email" "zack@ratm.com" "firstName" "Zack" "lastName" "de la Rocha" "role" "micro.apps.service.Role:01FS033ZM1QMHD3C900QFS2M10"
1641754524.413550 [0 10.4.1.1:33576] "SADD" "micro.apps.service.User" "01FS033ZPFJ9CZYVH10J787Y64"
1641754524.415020 [0 10.4.1.1:33576] "SISMEMBER" "micro.apps.service.User" "01FS033ZPREE4FX59D7BWMHNP3"
1641754524.416892 [0 10.4.1.1:33576] "DEL" "micro.apps.service.User:01FS033ZPREE4FX59D7BWMHNP3"
1641754524.417871 [0 10.4.1.1:33576] "HMSET" "micro.apps.service.User:01FS033ZPREE4FX59D7BWMHNP3" "_class" "micro.apps.service.User" "email" "tim@ratm.com" "firstName" "Tim" "lastName" "Commerford" "role" "micro.apps.service.Role:01FS033ZNQV6KVZTVE8FGE8QD8"
1641754524.418800 [0 10.4.1.1:33576] "SADD" "micro.apps.service.User" "01FS033ZPREE4FX59D7BWMHNP3"
1641754524.420122 [0 10.4.1.1:33576] "SISMEMBER" "micro.apps.service.User" "01FS033ZPXW9JSEM2E69QDMV64"
1641754524.421438 [0 10.4.1.1:33576] "DEL" "micro.apps.service.User:01FS033ZPXW9JSEM2E69QDMV64"
1641754524.422207 [0 10.4.1.1:33576] "HMSET" "micro.apps.service.User:01FS033ZPXW9JSEM2E69QDMV64" "_class" "micro.apps.service.User" "email" "tom@ratm.com" "firstName" "Tom" "lastName" "Morello" "role" "micro.apps.service.Role:01FS033ZNVX7JS7JJ6Q8HSBG3F"
1641754524.422908 [0 10.4.1.1:33576] "SADD" "micro.apps.service.User" "01FS033ZPXW9JSEM2E69QDMV64"
1641754524.423675 [0 10.4.1.1:33576] "SISMEMBER" "micro.apps.service.User" "01FS033ZQ1JJ490SDBVMV5V8ZJ"
1641754524.424779 [0 10.4.1.1:33576] "DEL" "micro.apps.service.User:01FS033ZQ1JJ490SDBVMV5V8ZJ"
1641754524.425584 [0 10.4.1.1:33576] "HMSET" "micro.apps.service.User:01FS033ZQ1JJ490SDBVMV5V8ZJ" "_class" "micro.apps.service.User" "email" "brad@ratm.com" "firstName" "Brad" "lastName" "Wilk" "role" "micro.apps.service.Role:01FS033ZNYDVCKJ732HNAWW732"
1641754524.426215 [0 10.4.1.1:33576] "SADD" "micro.apps.service.User" "01FS033ZQ1JJ490SDBVMV5V8ZJ"
1641754524.427612 [0 10.4.1.1:33576] "BF.ADD" "bf_user_email" "zack@ratm.com"
1641754524.428238 [0 10.4.1.1:33576] "BF.ADD" "bf_user_email" "tim@ratm.com"
1641754524.428788 [0 10.4.1.1:33576] "BF.ADD" "bf_user_email" "tom@ratm.com"
1641754524.429361 [0 10.4.1.1:33576] "BF.ADD" "bf_user_email" "brad@ratm.com"
1641754524.443556 [0 10.4.1.1:33576] "SISMEMBER" "micro.apps.service.Person" "01FS033ZQNZKAWMCHC74HNXQ48"
1641754524.463291 [0 10.4.1.1:33576] "JSON.SET" "micro.apps.service.Person:01FS033ZQNZKAWMCHC74HNXQ48" "." "{\"score\":0.0,\"name\":{\"first\":\"kera\",\"last\":\"bani\",\"title\":\"Mr\"},\"addresses\":[{\"suite\":\"222\",\"street\":\"fourt st\",\"city\":\"riverside\",\"state\":\"CA\",\"code\":\"95543\",\"country\":\"USA\",\"location\":\"-122.1245,47.64016\"},{\"suite\":\"111\",\"street\":\"wood\",\"city\":\"riverside\",\"state\":\"CA\",\"code\":\"95543\",\"country\":\"USA\",\"location\":\"-121.1245,46.64016\"}],\"address\":{\"suite\":\"222\",\"street\":\"fourt st\",\"city\":\"riverside\",\"state\":\"CA\",\"code\":\"95543\",\"country\":\"USA\",\"location\":\"-122.1245,47.64016\"},\"gender\":\"MALE\",\"dob\":928047600000,\"email\":\"kera@bani.com\",\"avatar\":\"https://www.gravatar.com/avatarr\"}"
1641754524.465205 [0 10.4.1.1:33576] "SADD" "micro.apps.service.Person" "01FS033ZQNZKAWMCHC74HNXQ48"
1641754524.466481 [0 10.4.1.1:33576] "SISMEMBER" "micro.apps.service.Person" "01FS033ZRCZRQ7A22GBGCZPQ98"
1641754524.468897 [0 10.4.1.1:33576] "JSON.SET" "micro.apps.service.Person:01FS033ZRCZRQ7A22GBGCZPQ98" "." "{\"score\":0.0,\"name\":{\"first\":\"sumo\",\"last\":\"demo\",\"title\":\"Sir\"},\"addresses\":[{\"suite\":\"111\",\"street\":\"wood\",\"city\":\"riverside\",\"state\":\"CA\",\"code\":\"95543\",\"country\":\"USA\",\"location\":\"-121.1245,46.64016\"},{\"suite\":\"333\",\"street\":\"Jambri\",\"city\":\"riverside\",\"state\":\"CA\",\"code\":\"95553\",\"country\":\"USA\",\"location\":\"-111.1245,44.64016\"}],\"address\":{\"suite\":\"333\",\"street\":\"Jambri\",\"city\":\"riverside\",\"state\":\"CA\",\"code\":\"95553\",\"country\":\"USA\",\"location\":\"-111.1245,44.64016\"},\"gender\":\"FEMALE\",\"dob\":610786800000,\"email\":\"sumo@demo.com\",\"avatar\":\"https://www.gravatar.com/avatarr\"}"
1641754524.469909 [0 10.4.1.1:33576] "SADD" "micro.apps.service.Person" "01FS033ZRCZRQ7A22GBGCZPQ98"
1641754524.471124 [0 10.4.1.1:33576] "BF.ADD" "bf_person_email" "kera@bani.com"
1641754524.471887 [0 10.4.1.1:33576] "BF.ADD" "bf_person_email" "sumo@demo.com"
```

## References
- [What’s New in Redis OM Spring?](https://redis.com/blog/redis-om-spring/)
