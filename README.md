# Reactive Relational Database Connectivity Client (R2DBC)
This project is an exploration of what a Java API for relational database access with [Reactive Streams][rs] might look like.  It uses [Project Reactor][pr].  It uses [Jdbi][jd] as an inspiration.

[jd]: http://jdbi.org
[pr]: https://projectreactor.io
[rs]: http://www.reactive-streams.org

**THIS IS ONLY AN EXPERIEMENT AND NOT SUPPORTED SOFTWARE**

## Examples
A quick example of configuration and execution would look like:

Using postgresql:

```java
PostgresqlConnectionConfiguration configuration = PostgresqlConnectionConfiguration.builder()
    .host("<host>")
    .database("<database>")
    .username("<username>")
    .password("<password>")
    .build();

R2dbc r2dbc = new R2dbc(new PostgresqlConnectionFactory(configuration));

r2dbc.inTransaction(handle ->
    handle.execute("INSERT INTO test VALUES ($1)", 100))

    .thenMany(r2dbc.inTransaction(handle ->
        handle.select("SELECT value FROM test")
            .execute(result -> result.map((row, rowMetadata) -> row.get("value", Integer.class)))))

    .subscribe(System.out::println);
```

Using H2:
```java
//Connect to H2 database
H2ConnectionConfiguration configuration = H2ConnectionConfiguration.builder()
	.url("jdbc:h2:")
	.file("./testdb")
	.username("<username>")
	.password("<password>")
	.build();

R2dbc r2dbc = new R2dbc(new H2ConnectionFactory(configuration));

//Create table and insert sample data
r2dbc.inTransaction(handle -> {
	handle.execute("CREATE TABLE IF NOT EXISTS sampleTable (sampleColumn int)");
	return handle.execute("INSERT INTO sampleTable VALUES ($1)", 100);
}).subscribe();

//Read values
r2dbc.inTransaction(handle -> handle.select("SELECT * FROM sampleTable").mapRow(r -> r.get("sampleColumn", Integer.class)))
	.subscribe(s -> System.out.println("RECEIVED: " + s));
```

## Maven
Both milestone and snapshot artifacts (library, source, and javadoc) can be found in Maven repositories.

```xml
<dependency>
  <groupId>io.r2dbc</groupId>
  <artifactId>r2dbc-client</artifactId>
  <version>1.0.0.M5</version>
</dependency>
```

Artifacts can bound found at the following repositories.

### Repositories
```xml
<repository>
    <id>spring-snapshots</id>
    <name>Spring Snapshots</name>
    <url>https://repo.spring.io/snapshot</url>
    <snapshots>
        <enabled>true</enabled>
    </snapshots>
</repository>
```

```xml
<repository>
    <id>spring-milestones</id>
    <name>Spring Milestones</name>
    <url>https://repo.spring.io/milestone</url>
    <snapshots>
        <enabled>false</enabled>
    </snapshots>
</repository>
```

## License
This project is released under version 2.0 of the [Apache License][l].

[l]: https://www.apache.org/licenses/LICENSE-2.0
