Coding Guidelines
=======================

These aren't hard and fast rules just a set of practices that we have used to build the application.

REST Server
=================

The key thing to remember for this layer is that the server application is completely stand-alone. This
means that we need to make sure that every single method works as intended all by itself. The main
area where this becomes important is for _method level security_.

Guidelines
-------------

-   There should never be a server side method that takes a Collection (List, Set, etc) either naturally or 
    contained within a wrapper class). _Note:_ I know we’ve done this in the past, but it’s the wrong pattern 
    and we should not ever do it again. 
    -   On the read side, all responses should be Paginated.
            
            // correct resource-oriented approach, takes a Pageable, returns a PagedResource
            public ResponseEntity<? extends PagedResource<TeamLocationResource>> locations(
                       @PageableDefault(size = 20, sort = "location.name") Pageable pageable);
                       
            // incorrect way, exposes us to unbounded collections
            public ResponseEntity<List<TeamLocationResource>> locations();
            
    -   On the write side, there should also be no Collection based resources either explicitly or wrapped
        in a different RequestXxx kind of object. So how can a do stuff to a collection then? Well you don't.
        Instead the client side code will call the server for each row. Let's say you had a bunch of _Thing_
        objects to save. The *service-oriented* approach (not what we use) would be to send a 
        List<Thing> objects across and return the same List of 'saved' Thing objects. We, however, are following
        a *resource-oriented* approach and as such the caller would send one request per  _Thing_ to be saved.
        For example:
        
            // what *not* to do SOA
            public List<ThingResource> save(List<Thing> thingsToSave);
            
            // correct resource-oriented way
            public ThingResource save(Thing toSave);
 
-   The documentation on the web api resources layer is really important. Mainly we just need to do javdoc 
    the correct way.. no empty descriptions for variables! For return types document like this:
    STATUS_CODE (#): type of object in return. Yes you need to document all of the return codes. Also you
    should be sure to include all @ApiImplicitParams so that our automatic api documentation works with
    pagination parameters
    
    Example of fully documented return codes plus api documentation
    
        /**
         * GET for searching for patients
         *
         * @param clientId  for security, ensures logged in user has rights to search the client
         * @param page  the page specifcation
         * @param assembler to create PatientResource objects
         * @param name of the patient to search for
         * @return OK (200): containing a PatientResourcePage
         * <p/>
         * NO_CONTENT (204): when there are no matches
         */
        @RequestMapping(value = "/clients/{clientId}/patients", method = RequestMethod.GET)
        @PreAuthorize("hasPermission(@client.id(#clientId), 'PERM_CLIENT_SUPER_USER')")
        @ApiImplicitParams(value = {
                @ApiImplicitParam(name = "size", defaultValue = "10", 
                    value = "number of items on a page", dataType = "integer", paramType = "query"),
                @ApiImplicitParam(name = "page", defaultValue = "0", 
                    value = "page to request (zero index)", dataType = "integer", paramType = "query"),
                @ApiImplicitParam(name = "sort", defaultValue = "lastName,asc", 
                    value = "sort to apply format: property,asc or desc", dataType = "string", paramType = "query")
        })
        public ResponseEntity<PatientResourcePage> list(@PathVariable("clientId") Long clientId,
                                                        @PageableDefault(size = 10, sort = "lastName") Pageable page,
                                                        PagedResourcesAssembler<Patient> assembler,
                                                        @RequestParam(value = "name", required = false) String name)
                
-   The actual URL values for resources should not look like java method names. So 
    '/user_client/{userId}/doSomethingOnTheServer' should be '/user_client/{userId}/do_something_on_the_server'

-   Don’t add the ‘verb’ to the url: Instead of /user_client/{id} (the GET) and /user_client/{id}/save (POST) use 
    /user_client/{id} only the verb should be the GET or PUT or DELETE.
-   All @Resource attributes of the classes should only point to interfaces, not implementation classes. 
    The only real offender of this is in the web-rest layer in the XxxResources classes. For example: 

        // not this
        @Resource
        UserClientSecretQuestionResponseResourceAssembler assembler;
        
        // use this instead (the bean name indicates the implementation not the data type):
        @Resource(name = “userClientSecretQuestionResponseResourceAssembler”)
        ResourceAssembler<UserClientSecretQuestionResponse, UserClientSecretQuestionResponseResource> assembler;
        
Resource (ROA) vs Service Oriented (SOA)
-------------------------------------

These terms are pretty overloaded in the development space but this server follows *ROA*.

*ROA* is based on four concepts:
                                                                                                           
1.  __Resources__. e.g. the article about [REST] (http://www.wikipedia.org/wiki/Representational_State_Transfer).
2.  Their name, a __URI__. The URI is the name and address of a resource.
3.  Their __representations__. A resource is a source of representations.
4.  The __links__ between them. Normally a hypermedia representation of a resource contains links to others resources.

and four properties:

1.  __Addressability__. Addressable applications expose a URI for every piece of information they might conceivably serve.
2.  __Statelessness__. Statelessness means that every HTTP request happens in complete isolation. 
    The server never relies on information from previous requests.
3.  __Connectedness__. A Web service is connected to the extent that you can put the service in different states 
    just by following links and filling out forms.
4.  A __uniform interface__. In ROAs, HTTP is the uniform interface. GET method to retrieve a representation of a 
    resource, PUT method to a new URI or POST method to an existing URI to create a new resource, 
    PUT method to an existing URI to modify a resource and DELETE method to remove an existing resource. 
    Probably HTTP methods are not a perfect interface but what is important is the uniformity. 
    The point is not that GET is the best name for a read operation, but that GET means “read” across the Web. 
    Given a URI of a resource, everybody knows that to retrieve the resource s/he has to send a GET request to that URI.
    
*SOA* is based on collections of discrete software modules, known as __services__. A Service is usually much more
__course-grained__ than Resource. I tend to think of them as using multiple Resources at a time rather than a single
Resource. 

Example ROA vs SOA
-------------------------

There isn't a super clean line between the two but here's an example. Let's say we want to allow an application the
ability to subscribe to many advertising lists. 

In *ROA*, we would have a single Resource called a Subscription that the application would 'create' by doing something 
like a POST /subscription that would return a Subscription resource JSON or XML encoded. If multiple Subscription 
resources were required the client would do multiple POST requests. If we wanted to remove a subscription we would make a
DELETE /subscription call and an update would be to PUT /subscription. In all cases we would use the same Subscription
resource/object for all of the requests as a return type or input. The *Subscription* object could look something
like this:

    Subscription
    Email address;
 
Each of the verbs would return whatever makes sense for that verb. For instance POST would return a new Subscription,
PUT the updated one, GET the current, and DELETE probably just void.

In *SOA*, in addition to the Subscription object, we would create a service called a SubscriptionManagement service. 
The service would probably take a single object (e.g. SubscriptionManagementRequest) that would have wrappers for the 
subscriptions we wanted to maintain. The attribute wrappers are really the 'verbs' for the service.
 
    SubscriptionManagementRequest
    List<Subscription> toDelete;
    List<Subscription> toAdd;
    List<Subscription> toUpdate;
    
The SubscriptionManagement would also probably just return a single OK or ERROR for the methods on the service.    
   
