package com.emmisolutions.emmimanager.web.rest.admin.configuration;

import com.emmisolutions.emmimanager.web.rest.admin.security.cas.*;
import com.emmisolutions.emmimanager.web.rest.admin.security.csrf.CsrfAccessDeniedHandler;
import com.emmisolutions.emmimanager.web.rest.admin.security.csrf.CsrfEnsureCookiesUniqueFilter;
import com.emmisolutions.emmimanager.web.rest.admin.security.csrf.CsrfTokenGeneratorFilter;
import com.emmisolutions.emmimanager.web.rest.admin.security.csrf.DoubleSubmitSignedCsrfTokenRepository;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.StringUtils;
import org.jasig.cas.client.util.CommonUtils;
import org.jasig.cas.client.validation.Cas20ServiceTicketValidator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.Scope;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationDetailsSource;
import org.springframework.security.cas.ServiceProperties;
import org.springframework.security.cas.authentication.CasAssertionAuthenticationToken;
import org.springframework.security.cas.authentication.CasAuthenticationProvider;
import org.springframework.security.cas.web.CasAuthenticationEntryPoint;
import org.springframework.security.cas.web.CasAuthenticationFilter;
import org.springframework.security.cas.web.authentication.ServiceAuthenticationDetails;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.AuthenticationUserDetailsService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.rememberme.TokenBasedRememberMeServices;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.security.web.csrf.CsrfFilter;
import org.springframework.security.web.util.matcher.AndRequestMatcher;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.NegatedRequestMatcher;
import org.springframework.web.util.UriComponentsBuilder;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static com.emmisolutions.emmimanager.web.rest.admin.security.cas.DynamicAuthenticationDetailsSource.makeDynamicUrlFromRequest;

/**
 * CAS Setup
 */
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true, jsr250Enabled = true)
@Order(100)
@Profile(value = {"cas", "prod"})
public class CasSecurityConfiguration extends WebSecurityConfigurerAdapter {

    public static final String SERVICE_REQUEST_URL_PARAMETER = "redirect-url";
    @Resource
    DynamicAuthenticationDetailsSource dynamicAuthenticationDetailsSource;
    @Resource(name = "adminSecurityContextRepository")
    SecurityContextRepository adminSecurityContextRepository;
    @Resource(name = "adminTokenBasedRememberMeServices")
    TokenBasedRememberMeServices adminTokenBasedRememberMeServices;
    @Resource
    private CasAuthenticationSuccessHandler casAuthenticationSuccessHandler;
    @Resource
    private CasAuthenticationFailureHandler casAuthenticationFailureHandler;
    @Resource(name ="clientCsrfAccessDeniedHandler")
    private CsrfAccessDeniedHandler csrfAccessDeniedHandler;
    @Resource(name = "adminUserDetailsService")
    private UserDetailsService userDetailsService;
    @Resource(name = "adminCsrfTokenRepository")
    private DoubleSubmitSignedCsrfTokenRepository adminCsrfTokenRepository;
    @Value("${cas.server.url:https://devcas1.emmisolutions.com/cas}")
    private String casServerUrl;
    @Value("${cas.server.login.url:https://devcas1.emmisolutions.com/cas/login}")
    private String casServerLoginUrl;
    @Value("${cas.service.validation.uri:/webapi/j_spring_cas_security_check}")
    private String casValidationUri;
    @Value("${cas.provider.key:12234245632699}")
    private String casProviderKey;
    @Value("${cas.username.suffix:@emmisolutions.com}")
    private String userNameSuffix;
    private Base64 urlSafeBase64;

    /**
     * This defines the service that is going to be validated. In
     * this case the service is the webapi application
     *
     * @return the ServiceProperties
     */
    @Bean
    public ServiceProperties serviceProperties() {
        ServiceProperties serviceProperties = new ServiceProperties();
        serviceProperties.setService(casValidationUri);
        serviceProperties.setSendRenew(false);
        serviceProperties.setAuthenticateAllArtifacts(true);
        return serviceProperties;
    }

    /**
     * This is the authentication provider that is responsible for
     * authenticating CAS requests.
     *
     * @return the authentication provider
     */
    @Bean
    public CasAuthenticationProvider casAuthenticationProvider() {
        CasAuthenticationProvider casAuthenticationProvider = new CasAuthenticationProvider();
        casAuthenticationProvider.setAuthenticationUserDetailsService(authenticationUserDetailsService());
        casAuthenticationProvider.setServiceProperties(serviceProperties());
        casAuthenticationProvider.setTicketValidator(cas20ServiceTicketValidator());
        casAuthenticationProvider.setKey(casProviderKey);
        return casAuthenticationProvider;
    }

    /**
     * This is how an authenticated user is loaded from our system. We take
     * the token name and append the userNameSuffix field. E.g. mfleming is the
     * token name, and @emmisolutions.com is the suffix, so we would try
     * to load mfleming@emmisolutions.com from the database.
     *
     * @return the authenticated user
     */
    @Bean
    public AuthenticationUserDetailsService<CasAssertionAuthenticationToken> authenticationUserDetailsService() {
        return new AuthenticationUserDetailsService<CasAssertionAuthenticationToken>() {
            @Override
            public UserDetails loadUserDetails(CasAssertionAuthenticationToken token) throws UsernameNotFoundException {
                String username = (token.getPrincipal() == null) ? "NONE_PROVIDED" : token.getName();
                return userDetailsService.loadUserByUsername(username + userNameSuffix);
            }
        };
    }

    /**
     * Knows how to validate a service ticket with the CAS server.
     *
     * @return the validator
     */
    @Bean
    public Cas20ServiceTicketValidator cas20ServiceTicketValidator() {
        return new Cas20ServiceTicketValidator(casServerUrl);
    }

    /**
     * The main authentication filter that pulls everything together. It
     * validates CAS service tickets (with dynamic urls) then hooks to
     * our authentication success and failure handlers.
     *
     * @return the authentication filter
     * @throws Exception if the authenticationManager() call freaks out
     */
    @Bean
    public CasAuthenticationFilter casAuthenticationFilter() throws Exception {
        CasAuthenticationFilter casAuthenticationFilter = new AllowSuccessHandlerCasAuthenticationFilter();
        casAuthenticationFilter.setAuthenticationManager(authenticationManager());
        casAuthenticationFilter.setRequiresAuthenticationRequestMatcher(new AntPathRequestMatcher("/webapi/j_spring_cas_security_check"));
        casAuthenticationFilter.setAuthenticationSuccessHandler(casAuthenticationSuccessHandler);
        casAuthenticationFilter.setServiceProperties(serviceProperties());
        casAuthenticationFilter.setAuthenticationDetailsSource(dynamicServiceResolver());
        casAuthenticationFilter.setProxyAuthenticationFailureHandler(casAuthenticationFailureHandler);
        casAuthenticationFilter.setAuthenticationFailureHandler(casAuthenticationFailureHandler);
        return casAuthenticationFilter;
    }

    /**
     * This dynamically resolves the service url from the current request
     *
     * @return the url or AccessDeniedException if the url is not configured
     * via the cas.valid.server.suffixes environment variable
     */
    @Bean
    AuthenticationDetailsSource<HttpServletRequest,
            ServiceAuthenticationDetails> dynamicServiceResolver() {
        dynamicAuthenticationDetailsSource.setServiceProperties(serviceProperties());
        return dynamicAuthenticationDetailsSource;
    }

    /**
     * This is the entry point that redirects to CAS when the current request
     * is not authenticated.
     *
     * @return the entry point
     */
    @Bean
    @Scope(value = "prototype")
    public CasAuthenticationEntryPoint casAuthenticationEntryPoint() {
        CasAuthenticationEntryPoint casAuthenticationEntryPoint = new CasAuthenticationEntryPoint() {

            /**
             * We need to append the original URL requested by the client side application
             * to the service request. We are going to Base64 encode this url in a way that
             * makes it safe to use in a query string and use SERVICE_REQUEST_URL_PARAMETER
             * as the name of the query parameter
             *
             * @param request the request
             * @param response the response
             * @return the service url with our SERVICE_REQUEST_URL_PARAMETER appended
             */
            @Override
            protected String createServiceUrl(final HttpServletRequest request, final HttpServletResponse response) {
                String service = makeDynamicUrlFromRequest(request, getServiceProperties());
                String requestedUrl = request.getHeader("X-Requested-Url");
                if (StringUtils.isNotBlank(requestedUrl)) {
                    // add requested URL as a query parameter
                    service = UriComponentsBuilder.fromHttpUrl(service)
                            .queryParam(SERVICE_REQUEST_URL_PARAMETER,
                                    StringUtils.trim(urlSafeBase64.encodeToString(requestedUrl.getBytes())))
                            .build(false)
                            .toString();
                }
                return CommonUtils.constructServiceUrl(null, response, service,
                        null, serviceProperties().getArtifactParameter(), false);
            }

            /**
             * Normally CAS issues a redirect when authentication is needed. When
             * XHR requests are happening this will either a) not work due to cross domain
             * issues or b) will result in the XHR response having the CAS login page as the
             * response. Neither of these are really desirable so instead we will throw a
             * special exception that has the redirect URL embedded in it as an object. Then
             * the client side will use the embedded object to do a full redirect. The
             * xhr-redirect.jsp is the file that creates the embedded object.
             *
             * @param request the request
             * @param response the response
             */
            @Override
            protected void preCommence(HttpServletRequest request, HttpServletResponse response) {

                if ("XMLHttpRequest".equals(request.getHeader("X-Requested-With"))) {
                    // don't redirect when XHR, throw exception instead
                    final String urlEncodedService = createServiceUrl(request, response);
                    String redirectUrl = createRedirectUrl(urlEncodedService);
                    throw new XhrNeedsRedirectException(redirectUrl);
                }

            }
        };
        casAuthenticationEntryPoint.setLoginUrl(casServerLoginUrl);
        casAuthenticationEntryPoint.setServiceProperties(serviceProperties());
        return casAuthenticationEntryPoint;
    }

    /**
     * Adds the CAS filters to spring security when CAS is enabled.
     *
     * @param http security configuration
     * @throws Exception on error
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .sessionManagement()
                    .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                    .and()
                .addFilter(casAuthenticationFilter())
                .securityContext()
                    .securityContextRepository(adminSecurityContextRepository)
                    .and()
                .requestMatchers()
                    // ignore the form login and logout pages
                    .requestMatchers(
                            new AndRequestMatcher(
                                    new NegatedRequestMatcher(
                                            new AntPathRequestMatcher(SecurityConfiguration.loginProcessingUrl)),
                                    new NegatedRequestMatcher(
                                            new AntPathRequestMatcher(SecurityConfiguration.logoutProcessingUrl)),
                                    new NegatedRequestMatcher(
                                            new AntPathRequestMatcher("*.jsp")),
                                    new NegatedRequestMatcher(
                                            new AntPathRequestMatcher("/webapi/messages")),
                                    new AntPathRequestMatcher("/webapi/**"))
                    )
                    .and()
                .exceptionHandling()
                    .defaultAuthenticationEntryPointFor(casAuthenticationEntryPoint(),
                            new AntPathRequestMatcher("/webapi/**"))
                    .accessDeniedHandler(csrfAccessDeniedHandler)
                    .and()
                .rememberMe()
                    .key(adminTokenBasedRememberMeServices.getKey())
                    .rememberMeServices(adminTokenBasedRememberMeServices)
                    .and()
                .csrf()
                    .csrfTokenRepository(adminCsrfTokenRepository)
                    .and()
                .addFilterBefore(new CsrfEnsureCookiesUniqueFilter(adminCsrfTokenRepository), CsrfFilter.class)
                .addFilterAfter(new CsrfTokenGeneratorFilter(adminCsrfTokenRepository), CsrfFilter.class)
                .headers().frameOptions().disable()
                .authorizeRequests()
                    .antMatchers("/webapi").permitAll()
                    .antMatchers("/webapi.*").permitAll()
                    .antMatchers("/webapi/").permitAll()
                    .antMatchers("/webapi/messages").permitAll()
                    .antMatchers("/api-docs*").permitAll()
                    .antMatchers("/api-docs/**").permitAll()
                    .antMatchers("/webapi/**").authenticated();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(casAuthenticationProvider());
    }

    @PostConstruct
    private void init() {
        // make sure the Base64 encoding doesn't chunk the output
        urlSafeBase64 = new Base64(-1, null, true);
    }

}

