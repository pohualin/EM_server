JPA Persistence
=======================

The server uses JPA Persistence (implemented by Hibernate) to read/write entities
to the database.

Features
=======================

- implementation of database persistence via [JPA] (http://docs.oracle.com/javaee/7/tutorial/doc/partpersist.htm) annotations.
- automatic debug logging of all method entry/exit for public methods when in development mode (SPRING_PROFILE_DEVELOPMENT)
- [Spring Data] (http://docs.spring.io/spring-data/jpa/docs/1.6.1.RELEASE/reference/html/) integration which provides:
    - automatic audit logging (e.g. updatedBy, createdBy, updatedDate, createdDate) for all entities (extending AbstractAuditingEntity)
    - easy CRUD features for entities (via JpaRepository)
    - easy search features for entities (via JpaSpecificationExecutor repositories)

Database Maintenance
-----------------------

The JPA implementation is not responsible for maintaining the the database, however.
That responsibility is delegated to `liquibase`. All liquibase scripts are maintained
in the database module.

DDL Generation
-----------------------

It is still nice to be able to see automated generation of DDL from the entity classes themselves
via the JPA implementation (Hibernate). This module does create DDL during during the `test` phase of maven builds.

The way this works is that a plugin (`<hbm2ddl/>`) defined in `pom.xml` reads a configuration file
`src/test/resources/generate.ddl.xml`. The configuration file lists the classes that are going to be used for the
database. The DDL will be written out to the console as well as writing it to a file 
`target/generated-ddl/emmi_manager_db.ddl`.

To kick this off run `mvn test` and the DDL will be output to the console.
