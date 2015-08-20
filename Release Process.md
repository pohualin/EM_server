Emmi Manager Source Hierarchy
===================================

We use an 'open source' style of source management. In addition, we maintain a separate source repository
for each phase of the application lifecycle (production, release candidate, qa, development). Each lifecycle
repository is a fork of the 'upstream' lifecycle. 

Here is the 'fork chain' across the repository groups:

Emmi Manager Production  --> 
    Emmi Manager Release Candidate --> 
        Emmi Manager QA --> 
            Emmi Manager Development --> 
                Individual Developers


To move software up the fork chain, we use Merge Requests within [gitlab] (https://git.emmisolutions.com).

The process below details how to convert a SNAPSHOT version of the software into a RELEASE version of
the software.

Create New Release Candidate(s)
----------------------------------

The Jenkins build process will create the proper artifacts and upload to nexus when necessary. The key is
to start at the top of the fork chain and work down. In this case, the top of the fork chain
is the Emmi Manager Release Candidate group. 

Here is the high level overview:

1. Submit a merge request from the qa group to the release candidate group in gitlab for both
    client-angular and server repositories.
2. Accept the merge requests for each repository.     
3. Upgrade the version in each Release Candidate repository to non SNAPSHOT.
4. Update repositories in QA group from Release Candidate group.
5. Update versions to $NEW_VERSION-SNAPSHOT in QA Group.
6. Update repositories in Development group from QA group.

Note: See the `Release Candidate Creation.md` document inside each repository root for detailed instructions on
how to deal with versions in each repository, the server and angular-client as they are slightly different.


Move to Production
--------------------------

Here we need to move the software up the fork chain and tag the source. The idea is that our production repository
group is pristine. The head should also represent what is currently in production. 

To do that we need to:

1. Submit a merge request from the client-angular and server repositories inside the Emmi Release Candidate group.
2. Accept merge request(s) within the Emmi Manager Production group.
3. Tag the source with the version number.
