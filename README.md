Emmi Manager Server 
=================================

This is a multi-module project maven application that uses:

* [Liquibase] (http://www.liquibase.org/documentation/changes/index.html) - to maintain the database (on application startup)
* [Spring] (http://docs.spring.io/spring/docs/4.0.6.RELEASE/spring-framework-reference/htmlsingle/) - as the IOC container
* [Spring Data] (http://docs.spring.io/spring-data/jpa/docs/1.6.1.RELEASE/reference/html/) - for easy data repository persistence
* [JPA] (http://docs.oracle.com/javaee/7/tutorial/doc/partpersist.htm) - for standard java object persistence
* [Hibernate ORM] (http://docs.jboss.org/hibernate/orm/4.3/manual/en-US/html_single/) - the JPA implementation
* [Spring Web] (http://docs.spring.io/spring/docs/4.0.6.RELEASE/spring-framework-reference/htmlsingle/#spring-web) - 
    for the REST server
* [Spring Security] (http://docs.spring.io/spring-security/site/docs/3.2.4.RELEASE/reference/htmlsingle/) - 
    as the authentication and authorization platform.
* [Spring HATEAOAS] (https://github.com/spring-projects/spring-hateoas/blob/master/readme.md) - 
    for the [HATEAOS] (http://restcookbook.com/Basics/hateoas/) implementation.

JavaVM
=================================
This runs under a Java 1.7+ virtual machine.

Spring Profiles
=================================
This project uses Spring profiles in runtime to wire up resources.
To specify a runtime profile use -Dspring.profiles.active=xxx where xxx is:

* jndi - to specify that the data source and hibernate properties are going to be specified via JNDI. The 
  web-rest/src/main/webapp/META-INF/context.xml will setup a local PostgreSQL data source.
* test - to specify that the data source and hibernate properties will use an in memory H2 database.
* h2 - to use the H2 database
* prod - the production profile, uses jndi data source

__Note:__ If you do not specify a profile (for runtime) at all, the: dev,h2 will be used

Application Server
=================================
This can run with Tomcat 7.0.42 or higher. Remember to copy whatever JDBC driver file(s) you are
going to use into the $TOMCAT_HOME/lib directory. By default you can use H2 and PostgreSQL.

Project Organization
=================================
The application is divided into two distinct root projects: the client and the server. The idea is that the
client and server are un-coupled. There are no shared dependencies between the two. The only link between
the two projects is the message API.

The server side stack is organized in a kind of OSGI style, where the individual projects are in the form of
xxx-api and xxx-impl. The projects are organized by system function (e.g. services, persistence, web, etc) but can 
also be used to encompass specific 'large' functionality (e.g. third party integration, shared utility, etc).

The key thing to note is that two distinct projects should only ever share the xxx-api project for compilation.
The xxx-impl projects should only be runtime dependencies.

Application Configuration
=================================
The source trees are set up using standard Maven conventions. (e.g. src/main/java, src/main/test for the classes, etc) 
We are using a 'no-xml' style of configuration. As such, all configuration is done via classes and annotations.
Spring is used to configure the dependencies (both compile and runtime). The configuration is in the classes at
the _com.emmisolutions.emmimanager.xxx.configuration_ package where xxx is the proejct purpose (e.g. service, persistence, etc)

Request Flow
=================================
From the outside-in the flow looks like this (top is outermost):

* _web-rest_ - responsible for marshalling web requests to java objects and methods
* _service-api_ (service-spring) - responsible for transaction demarcation
* _persistence-api_ (persistence-jpa) - responsible for data persistence and retrieval from the database
* _database_ - maintains the structure of the database.
* _domain_ - the set of objects that travels between the layers.

Collaboration-ish Diagram
=================================
    (client-angular)                 (server)
    angular ui (javascript) --> 
            XHR (json request) --> 
                             spring-security (security) --> 
                                jackson (serialization framework) --> 
                                    spring-web (rest end-points) --> 
                                        service (business logic) --> 
                                            jpa (persistence logic) --> 
                                                spring-data (persistence implementation) --> 
                                                    h2/postgres/SQL Server 
                                                <-- liquibase (db maintainance)
