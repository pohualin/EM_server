package com.emmisolutions.emmimanager.web.rest.admin.configuration;

import com.emmisolutions.emmimanager.model.user.User;
import com.emmisolutions.emmimanager.service.security.UserDetailsConfigurableAuthenticationProvider;
import com.emmisolutions.emmimanager.service.security.UserDetailsService;
import com.emmisolutions.emmimanager.web.rest.admin.security.*;
import org.apache.commons.lang3.StringUtils;
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
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.codec.Hex;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.rememberme.TokenBasedRememberMeServices;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.security.web.csrf.CsrfTokenRepository;
import org.springframework.security.web.csrf.DefaultCsrfToken;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestHeaderRequestMatcher;

import javax.annotation.Resource;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.UUID;

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
    @Resource
    Environment env;
    @Resource
    PasswordEncoder passwordEncoder;
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
    private String XSRF_COOKIE_NAME = "XSRF-TOKEN";
    private String XSRF_PARAMETER_NAME = "X-" + XSRF_COOKIE_NAME;

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
     * The CSRF token repository implementation. This is essentially
     * a double submit solution. This means that both the request
     * parameter and cookie value must match in order for the request
     * to be valid.
     *
     * @return CsrfTokenRepository
     */
    @Bean
    public CsrfTokenRepository csrfTokenRepository() {

        return new CsrfTokenRepository() {

            private String appendHash(String toAppendTo) {
                StringBuilder csrfToken = new StringBuilder(toAppendTo);
                if (SecurityContextHolder.getContext().getAuthentication() != null) {
                    User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
                    if (user != null) {
                        String data = toAppendTo + ":" + user.getUsername() + ":" + user.getPassword();
                        MessageDigest digest;
                        try {
                            digest = MessageDigest.getInstance("MD5");
                        } catch (NoSuchAlgorithmException e) {
                            throw new IllegalStateException("No MD5 algorithm available!");
                        }
                        csrfToken.append("_").append(new String(Hex.encode(digest.digest(data.getBytes()))));
                    }
                }
                return csrfToken.toString();
            }

            @Override
            public CsrfToken generateToken(HttpServletRequest request) {
                return new DefaultCsrfToken(XSRF_PARAMETER_NAME, XSRF_PARAMETER_NAME, appendHash(UUID.randomUUID().toString()));
            }

            @Override
            public void saveToken(CsrfToken token, HttpServletRequest request, HttpServletResponse response) {
                if (token != null) {
                    Cookie csrfCookie = new Cookie(XSRF_COOKIE_NAME, token.getToken());
                    csrfCookie.setPath("/");
                    csrfCookie.setMaxAge(-1);
                    // set secure flag if ssl is in use via a proxy or directly
                    csrfCookie.setSecure(request.isSecure() ||
                            StringUtils.equalsIgnoreCase(request.getHeader("X-Forwarded-Ssl"), "on"));
                    response.addCookie(csrfCookie);
                }
            }

            @Override
            public CsrfToken loadToken(HttpServletRequest request) {
                String csrfFromCookie = null;
                if (request.getCookies() != null) {
                    for (Cookie cookie : request.getCookies()) {
                        if (cookie.getName().equalsIgnoreCase(XSRF_COOKIE_NAME)) {
                            csrfFromCookie = cookie.getValue();
                        }
                    }
                }
                // ensure token is valid, need to move to after the Auth filter though
//                if (csrfFromCookie != null) {
//                    String[] parts = csrfFromCookie.split("_");
//                    if (parts.length == 2) {
//                        System.out.println(appendHash(parts[0]));
//                        System.out.println(csrfFromCookie);
//                        if (!StringUtils.equalsIgnoreCase(csrfFromCookie, appendHash(parts[0]))) {
//                            // hash has been modified by client
//                            System.out.print("modified!!!");
//                            csrfFromCookie = null;
//                        }
//                    }
//                }
                return csrfFromCookie != null ?
                        new DefaultCsrfToken(XSRF_PARAMETER_NAME, XSRF_PARAMETER_NAME, csrfFromCookie) : null;
            }
        };
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
        rootTokenBasedRememberMeServices.setCookieName("EM2_ADMIN_RMC");
        return rootTokenBasedRememberMeServices;
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
                .csrfTokenRepository(csrfTokenRepository()).and()
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
