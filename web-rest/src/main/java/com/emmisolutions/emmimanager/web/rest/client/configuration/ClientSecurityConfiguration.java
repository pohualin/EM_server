package com.emmisolutions.emmimanager.web.rest.client.configuration;

import com.emmisolutions.emmimanager.model.user.AnonymousUser;
import com.emmisolutions.emmimanager.service.security.UserDetailsConfigurableAuthenticationProvider;
import com.emmisolutions.emmimanager.service.security.UserDetailsService;
import com.emmisolutions.emmimanager.web.rest.admin.security.DelegateRememberMeServices;
import com.emmisolutions.emmimanager.web.rest.admin.security.PreAuthenticatedAuthenticationEntryPoint;
import com.emmisolutions.emmimanager.web.rest.admin.security.RootTokenBasedRememberMeServices;
import com.emmisolutions.emmimanager.web.rest.admin.security.csrf.CsrfAccessDeniedHandler;
import com.emmisolutions.emmimanager.web.rest.admin.security.csrf.CsrfTokenGeneratorFilter;
import com.emmisolutions.emmimanager.web.rest.admin.security.csrf.CsrfTokenValidationFilter;
import com.emmisolutions.emmimanager.web.rest.admin.security.csrf.DoubleSubmitSignedCsrfTokenRepository;
import com.emmisolutions.emmimanager.web.rest.client.configuration.audit.ProxyAwareWebAuthenticationDetailsSource;
import com.emmisolutions.emmimanager.web.rest.client.configuration.security.AjaxAuthenticationFailureHandler;
import com.emmisolutions.emmimanager.web.rest.client.configuration.security.AjaxAuthenticationSuccessHandler;
import com.emmisolutions.emmimanager.web.rest.client.configuration.security.AjaxLogoutSuccessHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.core.env.Environment;
import org.springframework.security.access.PermissionEvaluator;
import org.springframework.security.access.expression.SecurityExpressionHandler;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.FilterInvocation;
import org.springframework.security.web.access.expression.DefaultWebSecurityExpressionHandler;
import org.springframework.security.web.authentication.AnonymousAuthenticationFilter;
import org.springframework.security.web.authentication.RememberMeServices;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.security.web.csrf.CsrfAuthenticationStrategy;
import org.springframework.security.web.csrf.CsrfFilter;
import org.springframework.security.web.util.matcher.RequestHeaderRequestMatcher;

import javax.annotation.Resource;
import javax.inject.Inject;
import java.util.UUID;

import static com.emmisolutions.emmimanager.config.Constants.SPRING_PROFILE_CAS;
import static com.emmisolutions.emmimanager.config.Constants.SPRING_PROFILE_PRODUCTION;
import static com.emmisolutions.emmimanager.service.audit.AuthenticationAuditService.APPLICATION.CLIENT_FACING;
import static com.emmisolutions.emmimanager.web.rest.client.configuration.ImpersonationConfiguration.IMP_AUTHORIZATION_COOKIE_NAME;

/**
 * Spring Security Setup
 */
@Configuration
@ComponentScan(basePackages = {
        "com.emmisolutions.emmimanager.web.rest.admin.security",
        "com.emmisolutions.emmimanager.web.rest.client.configuration.security",
        "com.emmisolutions.emmimanager.web.rest.client.configuration.audit"
})
@EnableWebSecurity
@Order(200)
@EnableGlobalMethodSecurity(prePostEnabled = true, jsr250Enabled = true)
public class ClientSecurityConfiguration extends WebSecurityConfigurerAdapter {

    static final String AUTHORIZATION_COOKIE_NAME = "EM2_RMC";

    static final String CLIENT_RMC_HASH_KEY_SECRET = "EM2_RMC_SECRET_KEY_999087!";
    /**
     * This is the processing URL for login
     */
    private static final String loginProcessingUrl = "/webapi-client/authenticate";
    /**
     * This is the processing URL for logout
     */
    private static final String logoutProcessingUrl = "/webapi-client/logout";
    @Resource
    Environment env;
    @Resource
    PasswordEncoder passwordEncoder;
    @Resource
    PermissionEvaluator permissionEvaluator;
    @Resource(name = "clientAjaxAuthenticationSuccessHandler")
    private AjaxAuthenticationSuccessHandler ajaxAuthenticationSuccessHandler;
    @Resource(name = "clientAjaxAuthenticationFailureHandler")
    private AjaxAuthenticationFailureHandler ajaxAuthenticationFailureHandler;
    @Resource(name = "clientAjaxLogoutSuccessHandler")
    private AjaxLogoutSuccessHandler ajaxLogoutSuccessHandler;
    @Resource(name ="clientCsrfAccessDeniedHandler")
    private CsrfAccessDeniedHandler csrfAccessDeniedHandler;

    @Resource
    private PreAuthenticatedAuthenticationEntryPoint authenticationEntryPoint;
    @Resource(name = "clientUserDetailsService")
    private UserDetailsService clientUserDetailsService;

    private UserDetailsConfigurableAuthenticationProvider authenticationProvider;

    @Resource(name = "legacyAuthenticationProvider")
    private void setAuthenticationProvider(UserDetailsConfigurableAuthenticationProvider authenticationProvider){
        authenticationProvider.setUserDetailsService(clientUserDetailsService);
        this.authenticationProvider =  authenticationProvider;
    }

    @Bean(name="clientAuthenticationManager")
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    /**
     * Hook up our permission evaluator class so we can
     * @return the handler that uses our permission evaluator
     */
    @Bean
    public SecurityExpressionHandler<FilterInvocation> authorizationExpressionHandler() {
        DefaultWebSecurityExpressionHandler ret = new DefaultWebSecurityExpressionHandler();
        ret.setPermissionEvaluator(permissionEvaluator);
        return ret;
    }

    /**
     * Session storage for security context
     *
     * @return an HttpSessionSecurityContextRepository
     */
    @Bean(name = "clientSecurityContextRepository")
    public SecurityContextRepository securityContextRepository() {
        HttpSessionSecurityContextRepository ret = new HttpSessionSecurityContextRepository();
        ret.setAllowSessionCreation(false);
        ret.setSpringSecurityContextKey("SPRING_SECURITY_CONTEXT_CLIENT");
        return ret;
    }

    /**
     * Sets remember me at the context root. Uses the same secret key so that hashing will
     * work regardless of the implementation used to set the key.
     *
     * @return the TokenBasedRememberMeServices
     */
    @Bean(name = "clientTokenBasedRememberMeServices")
    public RootTokenBasedRememberMeServices tokenBasedRememberMeServices() {
        RootTokenBasedRememberMeServices rootTokenBasedRememberMeServices =
                new RootTokenBasedRememberMeServices(CLIENT_RMC_HASH_KEY_SECRET, clientUserDetailsService);
        rootTokenBasedRememberMeServices.setAlwaysRemember(true);
        rootTokenBasedRememberMeServices.setParameter("remember-me");
        rootTokenBasedRememberMeServices.setCookieName(AUTHORIZATION_COOKIE_NAME);
        rootTokenBasedRememberMeServices.setAuthenticationDetailsSource(authenticationDetailsSource());
        rootTokenBasedRememberMeServices.setApplication(CLIENT_FACING);
        return rootTokenBasedRememberMeServices;
    }

    /**
     * The authentication details source knows how to read the proxied
     * ipv4 address properly.
     *
     * @return the ProxyAwareWebAuthenticationDetailsSource
     */
    @Bean
    public ProxyAwareWebAuthenticationDetailsSource authenticationDetailsSource() {
        return new ProxyAwareWebAuthenticationDetailsSource();
    }

    /**
     * Hook up the anonymous authentication filter to the proxy aware details source
     *
     * @return the filter
     */
    @Bean
    AnonymousAuthenticationFilter anonymousAuthenticationFilter() {
        AnonymousAuthenticationFilter ret = new AnonymousAuthenticationFilter(UUID.randomUUID().toString(),
                new AnonymousUser() {
                }, AuthorityUtils.createAuthorityList("ROLE_ANONYMOUS"));
        ret.setAuthenticationDetailsSource(authenticationDetailsSource());
        return ret;
    }

    /**
     * This service can switch between impersonation and regular client
     * based remember me based upon the cookie names passed.
     * @return the remember me services
     */
    @Bean
    public RememberMeServices delegateRememberMeServices() {
        if (env.acceptsProfiles(SPRING_PROFILE_CAS, SPRING_PROFILE_PRODUCTION)) {
            return new DelegateRememberMeServices();
        } else {
            return tokenBasedRememberMeServices();
        }
    }

    /**
     * Setup the global authentication settings
     *
     * @param auth to setup
     * @throws Exception if there's a problem with the user details service
     */
    @Inject
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(authenticationProvider);
    }

    /**
     * This is the Csrf Token Repository for the client facing applications
     *
     * @return CsrfTokenRepository that processes impersonation and normal client authentication but
     * handles impersonation before normal (this matches the RememberMeServices implementation) and
     * must match
     */
    @Bean(name = "clientCsrfTokenRepository")
    public DoubleSubmitSignedCsrfTokenRepository clientCsrfTokenRepository() {
        return new DoubleSubmitSignedCsrfTokenRepository(
                new DoubleSubmitSignedCsrfTokenRepository.SecurityTokenCookieParameterNameTuple(
                        IMP_AUTHORIZATION_COOKIE_NAME, "XSRF-TOKEN-IMP", "X-XSRF-TOKEN-IMP"
                ),
                new DoubleSubmitSignedCsrfTokenRepository.SecurityTokenCookieParameterNameTuple(
                        AUTHORIZATION_COOKIE_NAME, "XSRF-TOKEN-CLIENT", "X-XSRF-TOKEN-CLIENT"
                ));
    }

    /**
     * Allows us to have access to the change the CSRF token if necessary
     *
     * @return the CsrfAuthenticationStrategy
     */
    @Bean(name = "clientCsrfAuthenticationStrategy")
    public CsrfAuthenticationStrategy clientCsrfServices(){
        return new CsrfAuthenticationStrategy(clientCsrfTokenRepository());
    }

    @Override
    @SuppressWarnings("unchecked")
    protected void configure(HttpSecurity http) throws Exception {
        ajaxAuthenticationSuccessHandler.setDefaultTargetUrl("/webapi-client/authenticated");
        ajaxAuthenticationFailureHandler.setDefaultFailureUrl("/login-client.jsp?error");
        http
                .sessionManagement()
                    .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                    .and()
                .securityContext()
                    .securityContextRepository(securityContextRepository())
                    .and()
                .requestMatchers()
                    .antMatchers("/webapi-client/**")
                .and()
                .anonymous()
                .authenticationFilter(anonymousAuthenticationFilter())
                    .and()
                .exceptionHandling()
                    .defaultAuthenticationEntryPointFor(authenticationEntryPoint,
                            new RequestHeaderRequestMatcher("X-Requested-With", "XMLHttpRequest"))
                    .accessDeniedHandler(csrfAccessDeniedHandler)
                    .and()
                .rememberMe()
                    .key(tokenBasedRememberMeServices().getKey())
                    .rememberMeServices(delegateRememberMeServices())
                .and()
                .formLogin()
                .loginPage("/login-client.jsp")
                    .loginProcessingUrl(loginProcessingUrl)
                    .successHandler(ajaxAuthenticationSuccessHandler)
                    .failureHandler(ajaxAuthenticationFailureHandler)
                .authenticationDetailsSource(authenticationDetailsSource())
                    .usernameParameter("j_username")
                    .passwordParameter("j_password")
                .permitAll()
                .and()
                .logout()
                    .logoutUrl(logoutProcessingUrl)
                    .logoutSuccessHandler(ajaxLogoutSuccessHandler)
                    .permitAll()
                    .and()
                .csrf()
                    .csrfTokenRepository(clientCsrfTokenRepository())
                    .and()
                .addFilterBefore(new CsrfTokenValidationFilter(clientCsrfTokenRepository()), CsrfFilter.class)
                .addFilterAfter(new CsrfTokenGeneratorFilter(clientCsrfTokenRepository()), CsrfFilter.class)
                .headers().frameOptions().disable()
                .authorizeRequests()
                    .expressionHandler(authorizationExpressionHandler())
                    .antMatchers("/webapi-client").permitAll()
                    .antMatchers("/webapi-client.*").permitAll()
                    .antMatchers("/webapi-client/").permitAll()
                    .antMatchers("/webapi-client/messages").permitAll()
                    .antMatchers("/webapi-client/password/expired").permitAll()
                    .antMatchers("/webapi-client/password/reset").permitAll()
                    .antMatchers("/webapi-client/password/forgot").permitAll()
                    .antMatchers("/webapi-client/password/policy/reset").permitAll()
                    .antMatchers("/webapi-client/password/policy/activation").permitAll()
                    .antMatchers("/webapi-client/password/policy/expired/*").permitAll()
                    .antMatchers("/webapi-client/activate").permitAll()
                    .antMatchers("/webapi-client/validate/").permitAll()
                    .antMatchers("/webapi-client/secret_questions/using_reset_token").permitAll()
                    .antMatchers("/webapi-client/secret_questions/is_response_correct").permitAll()
                    .antMatchers("/webapi-client/user_client/lock_out_user/with_reset_token").permitAll()
                .antMatchers("/webapi-client/email/images/**").permitAll()
                    .antMatchers("/api-docs*").permitAll()
                    .antMatchers("/api-docs/**").permitAll()
                    .antMatchers("/webapi-client/**").authenticated()
                    .antMatchers(("/webapi-client/**")).access(
                            "hasPermission(@startsWith, 'PERM_CLIENT')") // make sure user has PERM_CLIENT permission
                    .antMatchers(("/webapi-client/**")).access(
                            "hasPermission(@ipRangeWithinClientConfiguration, 'for the client')"); // ip range checks
    }

}
