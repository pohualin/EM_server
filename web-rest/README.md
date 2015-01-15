Web REST
========================
This project is the main gateway to the server side resources accessed from remote clients over http(s).
 
Features
========================

The responsibilities of this project are:

- Create/Manage URI endpoints.
- Security: Execution permissions are evaluated here
- Serialize and De-serialize domain objects to both JSON and XML.
- Create and publish all links to the endpoints. This is the [HATEOAS] (https://github.com/spring-projects/spring-hateoas/blob/master/readme.md) 
implementation. Specific challenges:
    - The pagination of lists of objects
    - The ability to sort and filter via [RFC6570] (http://tools.ietf.org/html/rfc6570) - URI Templates.

Installing the Server
========================
You'll need to get some stuff.

On Mac OSX:

- Install [homebrew] (http://brew.sh): a package manager for OSX
- Install [Java JDK 1.7+] (http://www.oracle.com/technetwork/java/javase/downloads/index.html)
- Download the [Emmi Solutions SSL Certificate] (https://build1.emmisolutions.com).
- Install SSL certificate into the Java VM trust store (e.g. keytool -import -file downloaded_certificate.cer -keystore $JDK_HOME/jre/lib/security/cacerts).
- Install tomcat via `brew install tomcat`. Optionally, install tomcat native via `brew install tomcat-native`.
- Install maven via `brew install maven`.

After this, you should be able run a maven build and/or start tomcat via your IDE.
    
