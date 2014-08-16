Salesforce API
=============================

This module defines SalesForce services which will be used by the server application.
This project should be the only dependency exposed in the compile classpath, the implementation
project (salesforce-client) should only be exposed as a runtime resource.

Salesforce Model
=============================

The model used by the web services client (WSC) is the 'enterprise' model created from the
SF environment. You will need to re-gen this jar every time we change the SF model in a way
that the application relies on the data (which isn't very often). 

To generate them you need to:

- log into your organization
- click Your Name => Setup => App Setup => Develop => API. 
- Get the enterprise wsdl.