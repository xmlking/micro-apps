# Database



## H2
Starting the server doesn't open a database. Databases are opened as soon as a client connects.

### Dialect and Drivers
H2 server has its own jdbc driver. It will not work with others.

But H2 server is compatible with different dialects. But the database connection should initialize with spesific mode. example jdbc url compatible with MSSQLServer:

```
jdbc:h2:~/test;MODE=MSSQLServer
```

or it can be change anytime using simple sql:

```sql
SET MODE MSSQLServer
```

H2 itself has its own SQL dialect. It is enabled as default.


## Flyway

- Using the basic `schema.sql` and `data.sql` scripts (a.k.a `spring.sql.init.mode`) alongside **Flyway** or **Liquibase** is not recommended and support will be removed in a future release.
