package com.emmisolutions.emmimanager.web.rest.admin.configuration;

import com.emmisolutions.emmimanager.service.security.UserDetailsConfigurableAuthenticationProvider;
import com.emmisolutions.emmimanager.service.security.UserDetailsService;
import com.emmisolutions.emmimanager.web.rest.admin.security.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.core.env.Environment;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.rememberme.TokenBasedRememberMeServices;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.security.web.context.SecurityContextRepository;
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

    @Resource
    private AjaxAuthenticationSuccessHandler ajaxAuthenticationSuccessHandler;

    @Resource
    private AjaxAuthenticationFailureHandler ajaxAuthenticationFailureHandler;

    @Resource
    private AjaxLogoutSuccessHandler ajaxLogoutSuccessHandler;

    @Resource
    private PreAuthenticatedAuthenticationEntryPoint authenticationEntryPoint;

    @Resource(name = "adminUserDetailsService")
    private UserDetailsService adminUserDetailsService;

    private UserDetailsConfigurableAuthenticationProvider authenticationProvider;

    @Resource
    Environment env;

    @Resource
    PasswordEncoder passwordEncoder;

    private static final String REMEMBER_ME_KEY = "EM2_ADMIN_RMT";

    @Resource(name = "legacyAuthenticationProvider")
    private void setAuthenticationProvider(UserDetailsConfigurableAuthenticationProvider authenticationProvider){
        authenticationProvider.setUserDetailsService(adminUserDetailsService);
        this.authenticationProvider =  authenticationProvider;
    }

    @Bean(name = "adminSecurityContextRepository")
    public SecurityContextRepository securityContextRepository(){
        HttpSessionSecurityContextRepository ret = new HttpSessionSecurityContextRepository();
        ret.setSpringSecurityContextKey("SPRING_SECURITY_CONTEXT_ADMIN");
        return ret;
    }

    /**
     * Sets remember me at the context root
     *
     * @return the TokenBasedRememberMeServices
     */
    @Bean(name = "adminTokenBasedRememberMeServices")
    public TokenBasedRememberMeServices tokenBasedRememberMeServices(){
        RootTokenBasedRememberMeServices rootTokenBasedRememberMeServices =
                new RootTokenBasedRememberMeServices("EM2_ADMIN_RMC_KEY_1223452!", adminUserDetailsService);
        rootTokenBasedRememberMeServices.setUseSecureCookie(false);
        rootTokenBasedRememberMeServices.setParameter("remember-me");
        rootTokenBasedRememberMeServices.setCookieName("EM2_ADMIN_RMC");
        return rootTokenBasedRememberMeServices;
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
