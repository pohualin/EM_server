package com.emmisolutions.emmimanager.web.rest.client.configuration;

import com.emmisolutions.emmimanager.model.user.User;
import com.emmisolutions.emmimanager.model.user.client.UserClient;
import com.emmisolutions.emmimanager.service.security.UserDetailsService;
import com.emmisolutions.emmimanager.web.rest.admin.security.RootTokenBasedRememberMeServices;
import com.emmisolutions.emmimanager.web.rest.admin.security.cas.*;
import org.apache.commons.lang3.StringUtils;
import org.jasig.cas.client.validation.Cas20ServiceTicketValidator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationDetailsSource;
import org.springframework.security.cas.ServiceProperties;
import org.springframework.security.cas.authentication.CasAssertionAuthenticationToken;
import org.springframework.security.cas.authentication.CasAuthenticationProvider;
import org.springframework.security.cas.web.CasAuthenticationEntryPoint;
import org.springframework.security.cas.web.CasAuthenticationFilter;
import org.springframework.security.cas.web.authentication.ServiceAuthenticationDetails;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.AuthenticationUserDetailsService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.context.SecurityContextPersistenceFilter;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.security.web.csrf.CsrfTokenRepository;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.OrRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import static com.emmisolutions.emmimanager.web.rest.client.configuration.ClientSecurityConfiguration.CLIENT_RMC_HASH_KEY_SECRET;

/**
 * Impersonation in the client application or administrative users logging into the client side
 * application is dealt by using CAS. It does this by looking for the TRIGGER_VALUE as a query
 * parameter or header.
 * <p/>
 * This class uses many of the components configured within the CasSecurityConfiguration class
 * and then tweaks them to come back to the client facing application.
 *
 * @see com.emmisolutions.emmimanager.web.rest.admin.configuration.CasSecurityConfiguration
 */
@Configuration
@Profile(value = {"cas", "prod"})
@Order(110)
public class ImpersonationConfiguration extends WebSecurityConfigurerAdapter {

    public static final String TRIGGER_VALUE = "impersonate-client";
    static final String IMP_AUTHORIZATION_COOKIE_NAME = "EM2_IMP_RMC";
    @Resource(name = "clientSecurityContextRepository")
    SecurityContextRepository clientSecurityContextRepository;
    @Resource
    CasAuthenticationEntryPoint casAuthenticationEntryPoint;
    @Resource
    DynamicAuthenticationDetailsSource dynamicAuthenticationDetailsSource;
    @Resource
    Cas20ServiceTicketValidator cas20ServiceTicketValidator;
    @Resource
    CasImpersonationAuthenticationSuccessHandler casImpersonationAuthenticationSuccessHandler;
    @Resource
    CasVariables casVariablesResource;
    @Resource(name = "impersonationUserDetailsService")
    UserDetailsService userDetailsService;
    @Resource(name = "clientCsrfTokenRepository")
    CsrfTokenRepository clientCsrfTokenRepository;
    @Resource
    private CasAuthenticationFailureHandler casAuthenticationFailureHandler;

    /**
     * Determines if the request is for a resource that should be impersonated
     *
     * @return the request matcher
     */
    @Bean
    public RequestMatcher impersonationRequestMatcher() {
        return new RequestMatcher() {
            @Override
            public boolean matches(HttpServletRequest request) {
                return StringUtils.isNotBlank(request.getParameter(TRIGGER_VALUE)) ||
                        StringUtils.isNotBlank(request.getHeader(TRIGGER_VALUE));
            }
        };
    }

    /**
     * Sets remember me at the context root
     *
     * @return the TokenBasedRememberMeServices
     */
    @Bean(name = "impersonationTokenBasedRememberMeServices")
    public RootTokenBasedRememberMeServices impersonationTokenBasedRememberMeServices(CasVariables casVariables) {
        RootTokenBasedRememberMeServices rootTokenBasedRememberMeServices =
                new RootTokenBasedRememberMeServices(CLIENT_RMC_HASH_KEY_SECRET, new UserDetailsService() {
                    @Override
                    public User getLoggedInUser() {
                        return userDetailsService.getLoggedInUser();
                    }

                    @Override
                    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
                        return userDetailsService.loadUserByUsername(username);
                    }

                    @Override
                    public UserClient get(UserClient userClient) {
                        return userDetailsService.get(userClient);
                    }
                });
        rootTokenBasedRememberMeServices.setAlwaysRemember(true);
        rootTokenBasedRememberMeServices.setUseSessionCookieOnly(true);
        rootTokenBasedRememberMeServices.setCookieName(IMP_AUTHORIZATION_COOKIE_NAME);
        rootTokenBasedRememberMeServices.setAuthenticationDetailsSource(impersonationAuthenticationDetailsSource(casVariables));
        return rootTokenBasedRememberMeServices;
    }

    /**
     * This filter is responsible for dealing with impersonation concerns at
     * the spring security framework level
     *
     * @return the ImpersonationFilter
     */
    @Bean
    public ImpersonationFilter impersonationFilter() {
        return new ImpersonationFilter();
    }

    @Bean
    public ServiceProperties impersonationServiceProperties(CasVariables casVariables) {
        ServiceProperties serviceProperties = new ServiceProperties();
        serviceProperties.setService(casVariables.getCasClientValidationUri());
        serviceProperties.setSendRenew(false);
        serviceProperties.setAuthenticateAllArtifacts(true);
        return serviceProperties;
    }

    /**
     * This is the authentication provider that is responsible for
     * authenticating CAS requests that creates impersonation users.
     *
     * @return the authentication provider
     */
    @Bean
    public CasAuthenticationProvider impersonationAuthenticationProvider(CasVariables casVariables) {
        CasAuthenticationProvider casAuthenticationProvider = new CasAuthenticationProvider();
        casAuthenticationProvider.setAuthenticationUserDetailsService(impersonationAuthenticationUserDetailsService(casVariables));
        casAuthenticationProvider.setServiceProperties(impersonationServiceProperties(casVariables));
        casAuthenticationProvider.setTicketValidator(cas20ServiceTicketValidator);
        casAuthenticationProvider.setKey(casVariables.getCasProviderKey());
        return casAuthenticationProvider;
    }

    @Bean
    AuthenticationDetailsSource<HttpServletRequest, ServiceAuthenticationDetails>
    impersonationAuthenticationDetailsSource(CasVariables casVariables) {
        dynamicAuthenticationDetailsSource.setServiceProperties(impersonationServiceProperties(casVariables));
        return dynamicAuthenticationDetailsSource;
    }

    @Bean
    public CasAuthenticationFilter impersonationAuthenticationFilter(CasVariables casVariables) throws Exception {
        CasAuthenticationFilter casAuthenticationFilter = new AllowSuccessHandlerCasAuthenticationFilter();
        casAuthenticationFilter.setAuthenticationManager(authenticationManager());
        casAuthenticationFilter.setRequiresAuthenticationRequestMatcher(new AntPathRequestMatcher("/webapi-client/j_spring_cas_security_check"));
        casAuthenticationFilter.setAuthenticationSuccessHandler(casImpersonationAuthenticationSuccessHandler);
        casAuthenticationFilter.setServiceProperties(impersonationServiceProperties(casVariables));
        casAuthenticationFilter.setAuthenticationDetailsSource(impersonationAuthenticationDetailsSource(casVariables));
        casAuthenticationFilter.setProxyAuthenticationFailureHandler(casAuthenticationFailureHandler);
        casAuthenticationFilter.setAuthenticationFailureHandler(casAuthenticationFailureHandler);
        return casAuthenticationFilter;
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(impersonationAuthenticationProvider(casVariablesResource));
    }

    @Bean
    public AuthenticationUserDetailsService<CasAssertionAuthenticationToken>
    impersonationAuthenticationUserDetailsService(final CasVariables casVariables) {
        return new AuthenticationUserDetailsService<CasAssertionAuthenticationToken>() {
            @Override
            public UserDetails loadUserDetails(CasAssertionAuthenticationToken token) throws UsernameNotFoundException {
                String username = (token.getPrincipal() == null) ? "NONE_PROVIDED" : token.getName();
                return userDetailsService.loadUserByUsername(username + casVariables.getUserNameSuffix());
            }
        };
    }

    /**
     * Adds the CAS filters for administrators to impersonate client level admins
     * to spring security when CAS is enabled.
     *
     * @param http security configuration
     * @throws Exception on error
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        casAuthenticationEntryPoint.setServiceProperties(impersonationServiceProperties(casVariablesResource));
        http
                .sessionManagement()
                    .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                    .and()
                .addFilterAfter(impersonationFilter(),
                        SecurityContextPersistenceFilter.class)
                .addFilter(impersonationAuthenticationFilter(casVariablesResource))
                .securityContext()
                    .securityContextRepository(clientSecurityContextRepository)
                    .and()
                .requestMatchers()
                        // only authenticate impersonation requests
                    .requestMatchers(
                            new OrRequestMatcher(new AntPathRequestMatcher("/webapi-client/j_spring_cas_security_check"),
                                    impersonationRequestMatcher()))
                    .and()
                .exceptionHandling()
                    .defaultAuthenticationEntryPointFor(casAuthenticationEntryPoint,
                            impersonationRequestMatcher())
                    .and()
                .rememberMe()
                .key(impersonationTokenBasedRememberMeServices(casVariablesResource).getKey())
                .rememberMeServices(impersonationTokenBasedRememberMeServices(casVariablesResource))
                .and()
                .csrf().disable()
                .headers().frameOptions().disable()
                .authorizeRequests()
                    .requestMatchers(new OrRequestMatcher(new AntPathRequestMatcher("/webapi-client/j_spring_cas_security_check"),
                            impersonationRequestMatcher()))
                    .authenticated();
    }

}

