package tech.blend.service;

import com.google.common.base.Enums;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configurers.provisioning.InMemoryUserDetailsManagerConfigurer;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.stereotype.Service;
import tech.blend.enums.AccessControlType;

import javax.sql.DataSource;

/**
 * This class help us to set the authentication type from external parameters.
 * So using it no code change required and
 * we can use it according to requirements based on applications.
 */

@Slf4j
@Service
public class AuthenticationManagerProvider {

    //CSV absolute path which contain the user authentication information.
    @Value("${access.control.type:ROLE_BASED}")
    private String authType;

    @Autowired
    private DataSource dataSource;
    @Autowired
    private InMemoryUserAuthenticationCsvReader inMemoryUserAuthenticationCsvReader;

    /**
     * Set the authentication type based on choice. Currently its implemented for three types:
     * 1. IN_MEMORY
     * 2. GROUP_BASED
     * 3. ROLE_BASED
     *
     * The default is role based authentication.
     * @param auth
     * @throws Exception
     */
    public void setAuthenticationManager(AuthenticationManagerBuilder auth) throws Exception{
        AccessControlType accessControlType = getAccessControlType();
        log.info("Setting authentication type : {}", accessControlType);

        switch (accessControlType){
            case IN_MEMORY: setInMemoryBasedAccessControl(auth); break;
            case GROUP_BASED: setGroupBasedAccessControl(auth); break;
            case ROLE_BASED: setRoleBasedAccessControl(auth); break;
        }
    }

    /**
     * Role based authentication.
     * @param auth
     * @throws Exception
     */
    private void setRoleBasedAccessControl(AuthenticationManagerBuilder auth) throws Exception {
        auth
                .jdbcAuthentication()
                .dataSource(dataSource);
    }

    /**
     * Group based Authentication.
     * @param auth
     * @throws Exception
     */
    private void setGroupBasedAccessControl(AuthenticationManagerBuilder auth) throws Exception {
        JdbcUserDetailsManager userDetailsService = auth
                .jdbcAuthentication()
                .dataSource(dataSource)
                .getUserDetailsService();

        userDetailsService.setEnableGroups(true);
        userDetailsService.setEnableAuthorities(false);
    }

    /**
     * In Memory Authentication
     * @param auth
     * @throws Exception
     */
    private void setInMemoryBasedAccessControl(AuthenticationManagerBuilder auth) throws Exception {

        InMemoryUserDetailsManagerConfigurer<AuthenticationManagerBuilder> managerConfigurer = auth
                .inMemoryAuthentication();

        inMemoryUserAuthenticationCsvReader.getInMemoryUserAuthenticationDetails().stream()
                .forEach(
                        inMemoryUserAuthenticationCsv ->
                                managerConfigurer
                                        .withUser(inMemoryUserAuthenticationCsv.getUsername())
                                        .password(inMemoryUserAuthenticationCsv.getPassword())
                                        .roles(inMemoryUserAuthenticationCsv.getRole())
                );
    }

    /**
     * Return the enum value of AccessControlType which mention in properties file.
     * If no value passed then, it will return ROLE_BASED as default value.
     * @return
     */
    private AccessControlType getAccessControlType() {
        return Enums.getIfPresent(AccessControlType.class, authType)
                .or(AccessControlType.ROLE_BASED);
    }

}
