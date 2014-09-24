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

Postgres setup
==============
* Download a latest version of Postgres database (I have Version 1.18.1)
* Create a blank database called emmimanager
* Create a new user called postgres with no password
* If you create the user with a password, you will need to edit: server\web-rest\src\main\webapp\META-INF\context.xml to specify the password.
* If you create a user with a different name or point to a different port, the JNDI datasource in context.xml (EmmiManagerDS) will need to be edited to match your database configuration.


 