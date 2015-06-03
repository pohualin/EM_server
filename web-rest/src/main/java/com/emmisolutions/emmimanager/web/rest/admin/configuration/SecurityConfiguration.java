package com.emmisolutions.emmimanager.web.rest.admin.configuration;

import com.emmisolutions.emmimanager.service.security.UserDetailsConfigurableAuthenticationProvider;
import com.emmisolutions.emmimanager.service.security.UserDetailsService;
import com.emmisolutions.emmimanager.web.rest.admin.security.*;
import com.emmisolutions.emmimanager.web.rest.admin.security.csrf.CsrfAccessDeniedHandler;
import com.emmisolutions.emmimanager.web.rest.admin.security.csrf.CsrfTokenGeneratorFilter;
import com.emmisolutions.emmimanager.web.rest.admin.security.csrf.CsrfTokenValidationFilter;
import com.emmisolutions.emmimanager.web.rest.admin.security.csrf.DoubleSubmitSignedCsrfTokenRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.core.env.Environment;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.rememberme.TokenBasedRememberMeServices;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.security.web.csrf.CsrfFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestHeaderRequestMatcher;

import javax.annotation.Resource;

import static com.emmisolutions.emmimanager.config.Constants.SPRING_PROFILE_CAS;
import static com.emmisolutions.emmimanager.config.Constants.SPRING_PROFILE_PRODUCTION;

/**
 * Spring Security Setup
 */
@Configuration
@ComponentScan(basePackages = {
        "com.emmisolutions.emmimanager.web.rest.admin.security"
})
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true, jsr250Enabled = true)
@Order(210)
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    /**
     * This is the processing URL for login
     */
    public static final String loginProcessingUrl = "/webapi/authenticate";
    /**
     * This is the processing URL for logout
     */
    public static final String logoutProcessingUrl = "/webapi/logout";

    /**
     * This is the authentication token cookie name
     */
    public static String AUTHENTICATION_TOKEN_NAME = "EM2_ADMIN_RMC";

    @Resource
    Environment env;
    @Resource
    PasswordEncoder passwordEncoder;
    @Resource
    private AjaxAuthenticationSuccessHandler ajaxAuthenticationSuccessHandler;
    @Resource
    private AjaxAuthenticationFailureHandler ajaxAuthenticationFailureHandler;
    @Resource(name ="clientCsrfAccessDeniedHandler")
    private CsrfAccessDeniedHandler csrfAccessDeniedHandler;
    @Resource
    private AjaxLogoutSuccessHandler ajaxLogoutSuccessHandler;
    @Resource
    private PreAuthenticatedAuthenticationEntryPoint authenticationEntryPoint;
    @Resource(name = "adminUserDetailsService")
    private UserDetailsService adminUserDetailsService;
    private UserDetailsConfigurableAuthenticationProvider authenticationProvider;

    @Resource(name = "legacyAuthenticationProvider")
    private void setAuthenticationProvider(UserDetailsConfigurableAuthenticationProvider authenticationProvider) {
        authenticationProvider.setUserDetailsService(adminUserDetailsService);
        this.authenticationProvider = authenticationProvider;
    }

    /**
     * This is the context repository but we shouldn't really ever need this one since we are
     * not using HttpSession objects to store our authenticated users.
     *
     * @return the security repo
     */
    @Bean(name = "adminSecurityContextRepository")
    public SecurityContextRepository securityContextRepository() {
        HttpSessionSecurityContextRepository ret = new HttpSessionSecurityContextRepository();
        ret.setAllowSessionCreation(false);
        ret.setSpringSecurityContextKey("SPRING_SECURITY_CONTEXT_ADMIN");
        return ret;
    }

    /**
     * Sets remember me at the context root
     *
     * @return the TokenBasedRememberMeServices
     */
    @Bean(name = "adminTokenBasedRememberMeServices")
    public TokenBasedRememberMeServices tokenBasedRememberMeServices() {
        RootTokenBasedRememberMeServices rootTokenBasedRememberMeServices =
                new RootTokenBasedRememberMeServices("EM2_ADMIN_RMC_KEY_1223452!", adminUserDetailsService);
        rootTokenBasedRememberMeServices.setUseSessionCookieOnly(true);
        rootTokenBasedRememberMeServices.setAlwaysRemember(true);
        rootTokenBasedRememberMeServices.setParameter("remember-me");
        rootTokenBasedRememberMeServices.setCookieName(AUTHENTICATION_TOKEN_NAME);
        return rootTokenBasedRememberMeServices;
    }

    /**
     * This is the Csrf Token Repository for the client facing applications
     *
     * @return CsrfTokenRepository that processes impersonation and normal client authentication but
     * handles impersonation before normal (this matches the RememberMeServices implementation) and
     * must match
     */
    @Bean(name = "adminCsrfTokenRepository")
    public DoubleSubmitSignedCsrfTokenRepository adminCsrfTokenRepository() {
        // make sure the csrf repo handles impersonation as well as normal client login
        return new DoubleSubmitSignedCsrfTokenRepository(
                new DoubleSubmitSignedCsrfTokenRepository.SecurityTokenCookieParameterNameTuple(
                AUTHENTICATION_TOKEN_NAME, "XSRF-TOKEN", "X-XSRF-TOKEN"
        ));
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        ajaxAuthenticationSuccessHandler.setDefaultTargetUrl("/webapi/authenticated");
        ajaxAuthenticationFailureHandler.setDefaultFailureUrl("/login-admin.jsp?error");
        if (env.acceptsProfiles(SPRING_PROFILE_CAS, SPRING_PROFILE_PRODUCTION)) {
            // only register the login and logout URLs for processing when CAS is enabled
            http
                    .requestMatchers()
                        .antMatchers(loginProcessingUrl, logoutProcessingUrl)
                        .and()
                    .exceptionHandling()
                        .defaultAuthenticationEntryPointFor(authenticationEntryPoint,
                                new AntPathRequestMatcher(loginProcessingUrl))
                        .accessDeniedHandler(csrfAccessDeniedHandler);
        } else {
            // cas isn't enabled register as normal
            http
                    .requestMatchers()
                        .antMatchers("/webapi/**")
                        .and()
                    .exceptionHandling()
                        .defaultAuthenticationEntryPointFor(authenticationEntryPoint,
                                new RequestHeaderRequestMatcher("X-Requested-With", "XMLHttpRequest"))
                        .accessDeniedHandler(csrfAccessDeniedHandler);
        }
        http
                .sessionManagement()
                    .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                    .and()
                .securityContext()
                    .securityContextRepository(securityContextRepository())
                    .and()
                .authenticationProvider(authenticationProvider)
                .formLogin()
                    .loginPage("/login-admin.jsp")
                    .loginProcessingUrl(loginProcessingUrl)
                    .successHandler(ajaxAuthenticationSuccessHandler)
                    .failureHandler(ajaxAuthenticationFailureHandler)
                    .usernameParameter("j_username")
                    .passwordParameter("j_password")
                    .permitAll()
                    .and()
                .rememberMe()
                    .key(tokenBasedRememberMeServices().getKey())
                    .rememberMeServices(tokenBasedRememberMeServices())
                    .and()
                .logout()
                    .logoutUrl(logoutProcessingUrl)
                    .logoutSuccessHandler(ajaxLogoutSuccessHandler)
                    .permitAll()
                    .and()
                .csrf()
                    .csrfTokenRepository(adminCsrfTokenRepository())
                    .and()
                .addFilterBefore(new CsrfTokenValidationFilter(adminCsrfTokenRepository()), CsrfFilter.class)
                .addFilterAfter(new CsrfTokenGeneratorFilter(adminCsrfTokenRepository()), CsrfFilter.class)
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

}
