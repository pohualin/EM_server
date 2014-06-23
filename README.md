EmmiManager
=================================

This is a multi-module project maven project that uses:

* Liquibase - to maintain the database (on application startup)
* Spring - as the IOC container
* Spring Data - for easy data repository persistence
* JPA - for standard java object persistence
* Jersey - for the REST server

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

Application Server
=================================
This can run with Tomcat 7.0.42 or higher. Remember to copy whatever JDBC driver file(s) you are
going to use into the $TOMCAT_HOME/lib directory. By default you can use H2 and PostgreSQL.
