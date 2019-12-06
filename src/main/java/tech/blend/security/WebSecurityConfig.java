package tech.blend.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import tech.blend.service.AuthenticationManagerProvider;

import java.security.SecureRandom;

/**
 * It ensures that any request to our application requires the user to be authenticated.
 * Allows users to authenticate with form based login.
 */

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private AuthenticationManagerProvider authenticationManagerProvider;

    /**
     * The method that contains the configuration for request authentication.
     * @param httpSecurity
     * @throws Exception
     */
    protected void configure(HttpSecurity httpSecurity) throws Exception {

        //Specify that URLs are allowed by anyone.
        httpSecurity
                .authorizeRequests()
                .antMatchers( "/login")
                .permitAll();

        /*
        * Role Based Authentication:
        * Specify that URLs are allowed by any authenticated user.
        * */
        httpSecurity
                .authorizeRequests()
                .antMatchers("/home", "/").hasRole("ADMIN")
                .anyRequest()
                .authenticated();

        /*
        * Specifies to support form based authentication.
        * Also mention login page to redirect to if authentication is required (/signin or /login)
        * */
        httpSecurity
                .formLogin()
                .loginPage("/login")
                .permitAll();

        /*
        * Provides logout support.
        * Accessing the URL "/logout" will log the user out by invalidating the HTTP Session
        * */
        httpSecurity
                .logout()
                .logoutUrl("/logout")
                .logoutSuccessUrl("/login?logout");

        //Handle exception.
        httpSecurity
                .exceptionHandling()
                .accessDeniedPage("/accessDenied");

        //Disable "Cross-Site Request Forgery".
        httpSecurity
                .csrf()
                .disable();

        //Manage number of session and timeout page.
        httpSecurity
                .sessionManagement()
                .maximumSessions(1)
                .expiredUrl("/sessiontimedout");
    }

    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        authenticationManagerProvider.setAuthenticationManager(auth);
    }

    /**
     * BCryptPasswordEncoder: Using BCrypt algorithm for Hashing the password.
     * Strength: Hash iteration count. It can be increased to make it more slower,
     * so it remains resistant to brute-force search attacks even with increasing computation power.
     * Secure Random: Use to get random salt for BCrypt.
     * @return
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(15, new SecureRandom());
    }
}
