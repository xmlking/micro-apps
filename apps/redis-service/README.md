


gradle :apps:redis-service:bootRun

http :8080/api/users/name/Wilk

```log
1639002641.464890 [0 172.18.0.1:64234] "FT.CREATE" "UserIdx" "ON" "HASH" "PREFIX" "1" "com.redis.om.hashes.domain.User:" "SCHEMA" "firstName" "AS" "firstName" "TAG" "middleName" "AS" "middleName" "TAG" "lastName" "AS" "lastName" "TAG" "email" "AS" "email" "TAG"
1639002641.468737 [0 172.18.0.1:64234] "FT.CREATE" "RoleIdx" "ON" "HASH" "PREFIX" "1" "com.redis.om.hashes.domain.Role:" "SCHEMA" "roleName" "AS" "roleName" "TAG"
1639002641.478826 [0 172.18.0.1:64234] "BF.RESERVE" "bf_user_email" "0.001" "100000"
1639002641.522203 [0 172.18.0.1:64234] "SISMEMBER" "com.redis.om.hashes.domain.User" "01FPE2Q52R5AB6AJ90TJZRX59K"
1639002641.535060 [0 172.18.0.1:64234] "DEL" "com.redis.om.hashes.domain.User:01FPE2Q52R5AB6AJ90TJZRX59K"
1639002641.539276 [0 172.18.0.1:64234] "HMSET" "com.redis.om.hashes.domain.User:01FPE2Q52R5AB6AJ90TJZRX59K" "_class" "com.redis.om.hashes.domain.User" "email" "zack@ratm.com" "firstName" "Zack" "id" "01FPE2Q52R5AB6AJ90TJZRX59K" "lastName" "de la Rocha"
1639002641.541930 [0 172.18.0.1:64234] "SADD" "com.redis.om.hashes.domain.User" "01FPE2Q52R5AB6AJ90TJZRX59K"
1639002641.544936 [0 172.18.0.1:64234] "SISMEMBER" "com.redis.om.hashes.domain.User" "01FPE2Q547VD1WADJRH01MCK78"
1639002641.548671 [0 172.18.0.1:64234] "DEL" "com.redis.om.hashes.domain.User:01FPE2Q547VD1WADJRH01MCK78"
1639002641.551090 [0 172.18.0.1:64234] "HMSET" "com.redis.om.hashes.domain.User:01FPE2Q547VD1WADJRH01MCK78" "_class" "com.redis.om.hashes.domain.User" "email" "tim@ratm.com" "firstName" "Tim" "id" "01FPE2Q547VD1WADJRH01MCK78" "lastName" "Commerford"
1639002641.553497 [0 172.18.0.1:64234] "SADD" "com.redis.om.hashes.domain.User" "01FPE2Q547VD1WADJRH01MCK78"
1639002641.555741 [0 172.18.0.1:64234] "SISMEMBER" "com.redis.om.hashes.domain.User" "01FPE2Q54J56YTTKHBMXV23VHA"
1639002641.557856 [0 172.18.0.1:64234] "DEL" "com.redis.om.hashes.domain.User:01FPE2Q54J56YTTKHBMXV23VHA"
1639002641.560215 [0 172.18.0.1:64234] "HMSET" "com.redis.om.hashes.domain.User:01FPE2Q54J56YTTKHBMXV23VHA" "_class" "com.redis.om.hashes.domain.User" "email" "tom@ratm.com" "firstName" "Tom" "id" "01FPE2Q54J56YTTKHBMXV23VHA" "lastName" "Morello"
1639002641.562307 [0 172.18.0.1:64234] "SADD" "com.redis.om.hashes.domain.User" "01FPE2Q54J56YTTKHBMXV23VHA"
1639002641.564413 [0 172.18.0.1:64234] "SISMEMBER" "com.redis.om.hashes.domain.User" "01FPE2Q54WK6KR3ZXWR3PK6NBP"
1639002641.566844 [0 172.18.0.1:64234] "DEL" "com.redis.om.hashes.domain.User:01FPE2Q54WK6KR3ZXWR3PK6NBP"
1639002641.568805 [0 172.18.0.1:64234] "HMSET" "com.redis.om.hashes.domain.User:01FPE2Q54WK6KR3ZXWR3PK6NBP" "_class" "com.redis.om.hashes.domain.User" "email" "brad@ratm.com" "firstName" "Brad" "id" "01FPE2Q54WK6KR3ZXWR3PK6NBP" "lastName" "Wilk"
1639002641.570899 [0 172.18.0.1:64234] "SADD" "com.redis.om.hashes.domain.User" "01FPE2Q54WK6KR3ZXWR3PK6NBP"
1639002641.575167 [0 172.18.0.1:64234] "BF.ADD" "bf_user_email" "zack@ratm.com"
1639002641.577319 [0 172.18.0.1:64234] "BF.ADD" "bf_user_email" "tim@ratm.com"
1639002641.579476 [0 172.18.0.1:64234] "BF.ADD" "bf_user_email" "tom@ratm.com"
1639002641.581328 [0 172.18.0.1:64234] "BF.ADD" "bf_user_email" "brad@ratm.com"
1639002670.812893 [0 172.18.0.1:64234] "PING"
```
