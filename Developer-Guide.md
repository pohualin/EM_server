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
 

 


 