package com.emmisolutions.emmimanager.web.rest.configuration;

import com.emmisolutions.emmimanager.web.rest.security.AjaxAuthenticationFailureHandler;
import com.emmisolutions.emmimanager.web.rest.security.AjaxAuthenticationSuccessHandler;
import com.emmisolutions.emmimanager.web.rest.security.AjaxLogoutSuccessHandler;
import com.emmisolutions.emmimanager.web.rest.security.PreAuthenticatedAuthenticationEntryPoint;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.util.matcher.RequestHeaderRequestMatcher;

import javax.annotation.Resource;
import javax.inject.Inject;

/**
 * Spring Security Setup
 */
@Configuration
@ComponentScan(basePackages = {
        "com.emmisolutions.emmimanager.web.rest.security"
})
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true, jsr250Enabled = true)
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

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

    @Resource
    PasswordEncoder passwordEncoder;

    private static final String REMEMBER_ME_KEY = "emSrm";

    /**
     * Setup the global authentication settings
     *
     * @param auth to setup
     * @throws Exception if there's a problem with the user details service
     */
    @Inject
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth
                .userDetailsService(userDetailsService)
                .passwordEncoder(passwordEncoder);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        ajaxAuthenticationSuccessHandler.setDefaultTargetUrl("/webapi/authenticated");
        http
                .exceptionHandling()
                .defaultAuthenticationEntryPointFor(authenticationEntryPoint,
                        new RequestHeaderRequestMatcher("X-Requested-With", "XMLHttpRequest"))
                .and()
                    .rememberMe()
                        .key(REMEMBER_ME_KEY)
                .and()
                    .formLogin()
                        .loginProcessingUrl("/webapi/authenticate")
                        .successHandler(ajaxAuthenticationSuccessHandler)
                        .failureHandler(ajaxAuthenticationFailureHandler)
                        .usernameParameter("j_username")
                        .passwordParameter("j_password")
                        .permitAll()
                .and()
                    .logout()
                        .logoutUrl("/webapi/logout")
                        .logoutSuccessHandler(ajaxLogoutSuccessHandler)
                        .deleteCookies("JSESSIONID")
                        .permitAll()
                .and()
                    .csrf()
                        .disable()
                .headers()
                    .frameOptions().disable()
                    .authorizeRequests()
                .antMatchers("/webapi").permitAll()
                .antMatchers("/webapi.*").permitAll()
                .antMatchers("/webapi/").permitAll()
                .antMatchers("/webapi/messages").permitAll()
                .antMatchers("/api-docs*").permitAll()
                .antMatchers("/**").authenticated();
    }

}
