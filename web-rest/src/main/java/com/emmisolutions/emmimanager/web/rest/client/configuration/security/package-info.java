/**
 * The classes in here are really related to spring's version of expression based access control.
 *
 * For more information on the possible expressions visit:
 *          http://docs.spring.io/spring-security/site/docs/3.0.x/reference/el-access.html
 *
 * The idea of this is to be able to perform authorization tasks in declared
 * at compile time and evaluated at runtime.
 *
 * Example of using both client and team level permissions along with administrative
 * level permissions on a REST GET method 'authorizeMe' could look like:
 *
 * @RequestMapping(value = "/authorizeMe/{clientId}/{teamId}", method = RequestMethod.GET)
 * @PreAuthorize("hasAnyRole('PERM_GOD', 'PERM_ADMIN_SUPER_USER', 'PERM_ADMIN_USER') or " +
 *      "hasPermission(@client._new(#clientId), 'PERM_CLIENT_USER') or " +
 *      "hasPermission(@client._new(#clientId), 'PERM_CLIENT_SUPER_USER') or " +
 *      "hasPermission(@team._new(#teamId), 'PERM_CLIENT_TEAM_MODIFY_USER_METADATA')")
 *  public ResponseEntity<String> authorizeMe(@PathVariable Long clientId, @PathVariable Long teamId) {...}
 *
 * The first hasAnyRole() just looks for any permissions that exactly match that role name.
 * The @client based hasPermission() blocks will create a ClientAuthorizationRequest for the client id (#clientId)
 * which is on the method signature. The @team hasPermission() blocks create a TeamAuthorizationRequest for
 * the team id (#teamId) which is on the method signature.
 *
 */
package com.emmisolutions.emmimanager.web.rest.client.configuration.security;