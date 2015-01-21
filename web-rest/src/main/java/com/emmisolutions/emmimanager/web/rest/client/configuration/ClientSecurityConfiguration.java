package com.emmisolutions.emmimanager.web.rest.client.configuration;

import com.emmisolutions.emmimanager.web.rest.admin.configuration.SecurityConfiguration;
import com.emmisolutions.emmimanager.web.rest.admin.security.AjaxAuthenticationFailureHandler;
import com.emmisolutions.emmimanager.web.rest.admin.security.AjaxAuthenticationSuccessHandler;
import com.emmisolutions.emmimanager.web.rest.admin.security.AjaxLogoutSuccessHandler;
import com.emmisolutions.emmimanager.web.rest.admin.security.PreAuthenticatedAuthenticationEntryPoint;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.access.PermissionEvaluator;
import org.springframework.security.access.expression.SecurityExpressionHandler;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.FilterInvocation;
import org.springframework.security.web.access.expression.DefaultWebSecurityExpressionHandler;
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

    @Resource
    private AjaxAuthenticationSuccessHandler ajaxAuthenticationSuccessHandler;

    @Resource
    private AjaxAuthenticationFailureHandler ajaxAuthenticationFailureHandler;

    @Resource
    private AjaxLogoutSuccessHandler ajaxLogoutSuccessHandler;

    @Resource
    private PreAuthenticatedAuthenticationEntryPoint authenticationEntryPoint;

    @Resource
    private UserDetailsService userDetailsService;

    @Resource(name = "legacyAuthenticationProvider")
    private AuthenticationProvider authenticationProvider;

    @Resource
    PasswordEncoder passwordEncoder;

    @Resource
    PermissionEvaluator permissionEvaluator;

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
     * Hook up our permission evaluator class so we can
     * @return the handler that uses our permission evaluator
     */
    @Bean
    public SecurityExpressionHandler<FilterInvocation> authorizationExpressionHandler(){
        DefaultWebSecurityExpressionHandler ret = new DefaultWebSecurityExpressionHandler();
        ret.setPermissionEvaluator(permissionEvaluator);
        return ret;
    }

    @Override
    @SuppressWarnings("unchecked")
    protected void configure(HttpSecurity http) throws Exception {
        ajaxAuthenticationSuccessHandler.setDefaultTargetUrl("/webapi-client/authenticated");
        ajaxAuthenticationFailureHandler.setDefaultFailureUrl("/login-client.jsp?error");
        http
                .requestMatchers()
                    .antMatchers("/webapi-client/**")
                    .and()
                .exceptionHandling()
                    .defaultAuthenticationEntryPointFor(authenticationEntryPoint,
                            new RequestHeaderRequestMatcher("X-Requested-With", "XMLHttpRequest"))
                    .and()
                .rememberMe()
                    .key(SecurityConfiguration.REMEMBER_ME_KEY)
                    .userDetailsService(userDetailsService)
                    .and()
                .formLogin()
                .loginPage("/login-client.jsp")
                    .loginProcessingUrl("/webapi-client/authenticate")
                    .successHandler(ajaxAuthenticationSuccessHandler)
                    .failureHandler(ajaxAuthenticationFailureHandler)
                    .usernameParameter("j_username")
                    .passwordParameter("j_password")
                    .permitAll()
                    .and()
                .logout()
                .logoutUrl("/webapi-client/logout")
                    .logoutSuccessHandler(ajaxLogoutSuccessHandler)
                    .deleteCookies("JSESSIONID")
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
                    .antMatchers("/api-docs*").permitAll()
                    .antMatchers("/api-docs/**").permitAll()
                    .antMatchers("/webapi-client/**").authenticated();
    }

}
