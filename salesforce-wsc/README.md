Salesforce Client Implementation
========================================

This is the implementation of the SalesForce API. It is the project which will be responsible
for making remote API calls to SalesForce itself.

Generate WSC Code
========================================

Most of the code we use is generated via the Salesforce model. See the salesforce-api/README.md for
info on how to get the wsdl. You may not need to generate the model every time as we have a 
version 31.0.0 in our nexus repository now (as of this writing). Next check out this [tutorial
on using the client] (https://developer.salesforce.com/page/Introduction_to_the_Force.com_Web_Services_Connector)

Salesforce Queries
========================================

We use both the query language (SOQL) and the search language (SOSL) to find objects within
SF. For more information check out [the developer documentation.] (http://www.salesforce.com/us/developer/docs/soql_sosl/index.htm)