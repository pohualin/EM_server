package com.emmisolutions.emmimanager.web.rest.admin.configuration;

import com.emmisolutions.emmimanager.service.security.UserDetailsConfigurableAuthenticationProvider;
import com.emmisolutions.emmimanager.service.security.UserDetailsService;
import com.emmisolutions.emmimanager.web.rest.admin.security.AjaxAuthenticationFailureHandler;
import com.emmisolutions.emmimanager.web.rest.admin.security.AjaxAuthenticationSuccessHandler;
import com.emmisolutions.emmimanager.web.rest.admin.security.AjaxLogoutSuccessHandler;
import com.emmisolutions.emmimanager.web.rest.admin.security.PreAuthenticatedAuthenticationEntryPoint;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.core.env.Environment;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestHeaderRequestMatcher;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.inject.Inject;

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

    @Resource
    private AjaxAuthenticationSuccessHandler ajaxAuthenticationSuccessHandler;

    @Resource
    private AjaxAuthenticationFailureHandler ajaxAuthenticationFailureHandler;

    @Resource
    private AjaxLogoutSuccessHandler ajaxLogoutSuccessHandler;

    @Resource
    private PreAuthenticatedAuthenticationEntryPoint authenticationEntryPoint;

    @Resource(name = "adminUserDetailsService")
    private UserDetailsService userDetailsService;

    @Resource(name = "legacyAuthenticationProvider")
    private UserDetailsConfigurableAuthenticationProvider authenticationProvider;

    @Resource
    Environment env;

    @Resource
    PasswordEncoder passwordEncoder;

    public static final String REMEMBER_ME_KEY = "emSrm";


    @PostConstruct
    private void init(){
        authenticationProvider.setUserDetailsService(userDetailsService);
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
    public static final String loginProcessingUrl = "/webapi/authenticate";

    /**
     * This is the processing URL for logout
     */
    public static final String logoutProcessingUrl = "/webapi/logout";

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        ajaxAuthenticationSuccessHandler.setDefaultTargetUrl("/webapi/authenticated");
        ajaxAuthenticationFailureHandler.setDefaultFailureUrl("/login-admin.jsp?error");
        if (env.acceptsProfiles(SPRING_PROFILE_CAS, SPRING_PROFILE_PRODUCTION)){
            // only register the login and logout URLs for processing when CAS is enabled
            http
                    .requestMatchers()
                        .antMatchers(loginProcessingUrl, logoutProcessingUrl)
                    .and()
                    .exceptionHandling()
                    .defaultAuthenticationEntryPointFor(authenticationEntryPoint,
                            new AntPathRequestMatcher(loginProcessingUrl));
        } else {
            // cas isn't enabled register as normal
            http
                    .requestMatchers()
                        .antMatchers("/webapi/**")
                        .and()
                    .exceptionHandling()
                        .defaultAuthenticationEntryPointFor(authenticationEntryPoint,
                                new RequestHeaderRequestMatcher("X-Requested-With", "XMLHttpRequest"));
        }
        http
                .rememberMe()
                    .key(REMEMBER_ME_KEY)
                    .userDetailsService(userDetailsService)
                    .and()
                .formLogin()
                    .loginPage("/login-admin.jsp")
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
                    .deleteCookies("JSESSIONID")
                    .permitAll()
                    .and()
                .csrf().disable()
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
