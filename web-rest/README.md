Web REST
========================
This project is the main gateway to the server side resources. 
The responsibilities of this project are:

- Create/Manage URI endpoints.
- Serialize and De-serialize domain objects to both JSON and XML.
- Create and publish all links to the endpoints. This is the [HATEOAS] (https://github.com/spring-projects/spring-hateoas/blob/master/readme.md) 
implementation. Specific challenges:
    - The pagination of lists of objects
    - The ability to sort and filter via [RFC6570] (http://tools.ietf.org/html/rfc6570) - URI Templates.
    
Philosophy
    
