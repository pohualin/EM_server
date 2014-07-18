Domain Model
=====================
This model is used throughout the entire application stack. We do not want to create any Data Transfer Objects (DTO), 
if possible. If DTOs were necessary, the only place they would be appropriate would be the _web-rest_ project.

All other layers of the application (service, persistence, etc) should use these objects in method signatures.

Usage
=====================
This model serves in three primary capacities:

- The internal model used as the interface between internal application layers
- The persistence model used to marshal data to/from the database
- The external model used to marshal data to/from the client
 
The above capabilities are supported through annotations:

- For the external model: we should only be using _javax.xml.bind.annotation.*_ classes. We should refrain from using
    other types of annotations (e.g. @JSON annotations) as those annotations would not be able to be converted
    to/from both JSON and XML.
- For the persistence model: we use _JPA_, _Hibernate_ and _Spring Data_ annotations. The annotation serve different
    purposes and are all used freely throughout.