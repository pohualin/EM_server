package com.emmisolutions.emmimanager.web.rest.client.configuration;

import com.emmisolutions.emmimanager.service.security.UserDetailsConfigurableAuthenticationProvider;
import com.emmisolutions.emmimanager.service.security.UserDetailsService;
import com.emmisolutions.emmimanager.web.rest.admin.security.PreAuthenticatedAuthenticationEntryPoint;
import com.emmisolutions.emmimanager.web.rest.admin.security.RootTokenBasedRememberMeServices;
import com.emmisolutions.emmimanager.web.rest.client.configuration.security.AjaxAuthenticationFailureHandler;
import com.emmisolutions.emmimanager.web.rest.client.configuration.security.AjaxAuthenticationSuccessHandler;
import com.emmisolutions.emmimanager.web.rest.client.configuration.security.AjaxLogoutSuccessHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.access.PermissionEvaluator;
import org.springframework.security.access.expression.SecurityExpressionHandler;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.FilterInvocation;
import org.springframework.security.web.access.expression.DefaultWebSecurityExpressionHandler;
import org.springframework.security.web.authentication.rememberme.TokenBasedRememberMeServices;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.security.web.util.matcher.RequestHeaderRequestMatcher;

import javax.annotation.Resource;
import javax.inject.Inject;

/**
 * Spring Security Setup
 */
@Configuration
@ComponentScan(basePackages = {
        "com.emmisolutions.emmimanager.web.rest.admin.security",
        "com.emmisolutions.emmimanager.web.rest.client.configuration.security"
})
@EnableWebSecurity
@Order(200)
@EnableGlobalMethodSecurity(prePostEnabled = true, jsr250Enabled = true)
public class ClientSecurityConfiguration extends WebSecurityConfigurerAdapter {

    @Resource(name = "clientAjaxAuthenticationSuccessHandler")
    private AjaxAuthenticationSuccessHandler ajaxAuthenticationSuccessHandler;

    @Resource(name = "clientAjaxAuthenticationFailureHandler")
    private AjaxAuthenticationFailureHandler ajaxAuthenticationFailureHandler;

    @Resource(name = "clientAjaxLogoutSuccessHandler")
    private AjaxLogoutSuccessHandler ajaxLogoutSuccessHandler;

    @Resource
    private PreAuthenticatedAuthenticationEntryPoint authenticationEntryPoint;

    @Resource(name = "clientUserDetailsService")
    private UserDetailsService clientUserDetailsService;

    private UserDetailsConfigurableAuthenticationProvider authenticationProvider;

    @Resource
    PasswordEncoder passwordEncoder;

    @Resource
    PermissionEvaluator permissionEvaluator;

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
    public SecurityExpressionHandler<FilterInvocation> authorizationExpressionHandler(){
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
    public SecurityContextRepository securityContextRepository(){
        HttpSessionSecurityContextRepository ret = new HttpSessionSecurityContextRepository();
        ret.setSpringSecurityContextKey("SPRING_SECURITY_CONTEXT_CLIENT");
        return ret;
    }

    /**
     * Sets remember me at the context root
     *
     * @return the TokenBasedRememberMeServices
     */
    @Bean(name = "clientTokenBasedRememberMeServices")
    public TokenBasedRememberMeServices tokenBasedRememberMeServices(){
        RootTokenBasedRememberMeServices rootTokenBasedRememberMeServices =
                new RootTokenBasedRememberMeServices("EM2_RMC_KEY_999087!", clientUserDetailsService);
        rootTokenBasedRememberMeServices.setUseSecureCookie(false);
        rootTokenBasedRememberMeServices.setParameter("remember-me");
        rootTokenBasedRememberMeServices.setCookieName("EM2_RMC");
        return rootTokenBasedRememberMeServices;
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
     * This is the processing URL for login
     */
    private static final String loginProcessingUrl = "/webapi-client/authenticate";

    /**
     * This is the processing URL for logout
     */
    private static final String logoutProcessingUrl = "/webapi-client/logout";

    @Override
    @SuppressWarnings("unchecked")
    protected void configure(HttpSecurity http) throws Exception {
        ajaxAuthenticationSuccessHandler.setDefaultTargetUrl("/webapi-client/authenticated");
        ajaxAuthenticationFailureHandler.setDefaultFailureUrl("/login-client.jsp?error");
        http
                .securityContext()
                    .securityContextRepository(securityContextRepository())
                    .and()
                .requestMatchers()
                    .antMatchers("/webapi-client/**")
                .and()
                    .exceptionHandling()
                        .defaultAuthenticationEntryPointFor(authenticationEntryPoint,
                                new RequestHeaderRequestMatcher("X-Requested-With", "XMLHttpRequest"))
                    .and()
                .rememberMe()
                    .key(tokenBasedRememberMeServices().getKey())
                    .rememberMeServices(tokenBasedRememberMeServices())
                .and()
                .formLogin()
                .loginPage("/login-client.jsp")
                    .loginProcessingUrl(loginProcessingUrl)
                    .successHandler(ajaxAuthenticationSuccessHandler)
                    .failureHandler(ajaxAuthenticationFailureHandler)
                    .usernameParameter("j_username")
                    .passwordParameter("j_password")
                .permitAll()
                .and()
                .logout()
                    .logoutUrl(logoutProcessingUrl)
                    .logoutSuccessHandler(ajaxLogoutSuccessHandler)
                    .permitAll()
                    .and()
                .csrf().disable()
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
                    .antMatchers("/api-docs*").permitAll()
                    .antMatchers("/api-docs/**").permitAll()
                    .antMatchers("/webapi-client/**").authenticated();
    }

}
