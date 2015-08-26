Create Release Candidate
-----------------------------

Here are the steps required to update the versions across the groups for the server application.

1. Create a release directory. (E.g. `mkdir release ; cd release`)
2. Clone client project: `git clone git@git.emmisolutions.com:emmi-manager-release-candidate/server.git`
3. Change into the source directory: `cd server`
4. Update the version on the server via maven: `mvn versions:set -DnewVersion=2.0.0 versions:commit`
5. commit the version change: `git commit -m "created release version" -a`
6. push to the remote repo: `git push`
7. Create a qa directory same level as the release directory. 
    (e.g. if you're still in the server directory: `cd ../.. ; mkdir qa ; cd qa`)
8. Clone client project: `git clone git@git.emmisolutions.com:emmi-manager-qa/server.git`
9. Change into the source directory: `cd server`
9. Add upstream project: `git remote add upstream git@git.emmisolutions.com:emmi-manager-release-candidate/server.git`
10. Fetch upstream changes: `git fetch upstream`
11. Merge upstream changes into master: `git merge -m "merging upstream" upstream/master`
12. Update the version on the server to $NEW_VERSION-SNAPSHOT via maven: 
    `mvn versions:set -DnewVersion=2.0.1-SNAPSHOT versions:commit`
13. Commit and push: `git commit -m "new version of application" -a ; git push`
14. Create a dev directory same level as the release directory. 
    (e.g. if you're still in the server directory: `cd ../.. ; mkdir dev ; cd dev`)
15. Clone client project: `git clone git@git.emmisolutions.com:emmi-manager-development/server.git`
16. Change into the source directory: `cd server`
17. Add upstream project: `git remote add upstream git@git.emmisolutions.com:emmi-manager-qa/server.git`
18. Fetch upstream changes: `git fetch upstream`
19. Merge upstream changes into master: `git merge -m "merging upstream" upstream/master`
20. Commit and push: `git commit -m "new version of application" -a ; git push`


        
