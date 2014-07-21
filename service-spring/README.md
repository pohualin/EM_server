Service Implementation (Spring)
===================================

This is an implementation of the service-api project.

Features
===================================

The main features of this project are:

- implementation of business logic
- automatic debug logging of all method entry/exit for public methods when in development mode (SPRING_PROFILE_DEVELOPMENT)
- transaction management: via Spring @Transactional annotations
- audit (entity revisions): via [Hibernate Envers](http://docs.jboss.org/envers/docs/index.html)
