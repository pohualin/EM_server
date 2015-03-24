Developer Guide
=================================
This guide describes the steps needed to setup the server project in eclipse.

Importing projects from Eclipse
=================================

* Use the Import -> Git -> Projects from Git. 
* Once you all the client and server repos to the list, you can select each one of them and pull down the client and server projects.

Enabling Annotation Precompiler
=================================

* Open up Project properties on the domain project.
* Open up Java Compiler option.
* Enable 'Enable project specific settings', 'Enable annotation processing' and 'Enable processing in editor'
* Click on Factory Path under Annotation Processing and ‘Add External jars’ and select the jar for jpa model gen (The jar is 
generally located under C:\Users\<your id>\.m2\repositories\org\hibernate\hibernate-jpamodelgen\4.3.6.Final\hibernate-jpamodelgen-4.3.6.Final.jar

Deploying server side code to root 
====================================

By default tomcat wants to deploy the war to web-rest path. To deploy it at the root, follow the steps below

* Double click the Tomcat server
* Go to the modules tab
* Set the Path variable to /
* Leave document base as web-rest
 
Running the application 
=======================
The application can run with an in memory database or with a real database. We use Postgres for local development. The liquibase scripts create the database objects on startup. 
The server uses Spring profiles to determine which mode to run in. Here is how we set the spring profile.

* Double click the Tomcat server
* Click "Open Launch Configuration"
* Click the arguments tab
* Here is the argument to run the server pointing to an in memory database: -Dspring.profiles.active=dev,H2,test
* Here is the argument to run the server pointing to a local postgres installation: -Dspring.profiles.active=prod

Single-Sign-On (SSO)
=================================
If you want to run like we do in production and use the SSO server (CAS), you'll probably
need to do a couple of things.

1. You'll need to activate the 'cas' spring profile. See below if you don't know how to do it.
2. If you are running on a different server than localhost you'll need to add it to the list of
acceptable CAS service prefixes via:

        -Dcas.valid.server.suffixes=your_local_hostname,localhost

3. You may need to import the wildcard emmisolutions.com SSL certificate into your java VM truststore.

    - Go to https://devcas1.emmisolutions.com and download the certificate.
    - Open up the command line and:

        cd %JAVA_JDK_HOME%/jre/lib/security (e.g. cd /Library/Java/JavaVirtualMachines/jdk1.8.0_31.jdk/Contents/Home/jre/lib/security)
        sudo keytool -importcert -alias emmidevcas -keystore cacerts -storepass changeit -file /where/you/put/the/ssl/cert

__Note:__ You'll know if you need to import the certificate if you get a SSL PKIX kind of exception
after you authenticate successfully to CAS

Postgres setup
==============
* Download a latest version of Postgres database (I have Version 1.18.1)
* Create a blank database called emmimanager
* Create a new user called postgres with no password
* Give the postgres user rights to create: `GRANT CREATE ON DATABASE emmimanager to postgres`
* If you create the user with a password, you will need to edit: server\web-rest\src\main\webapp\META-INF\context.xml to specify the password.
* If you create a user with a different name or point to a different port, the JNDI datasource in context.xml (EmmiManagerDS) will need to be edited to match your database configuration.


 