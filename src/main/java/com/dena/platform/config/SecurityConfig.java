package com.dena.platform.config;

import com.dena.platform.common.config.DenaConfigReader;
import com.dena.platform.core.feature.security.JWTAuthenticationEntryPoint;
import com.dena.platform.core.feature.security.JWTAuthenticationProvider;
import com.dena.platform.core.feature.security.JWTSuccessHandler;
import com.dena.platform.core.feature.security.authentication.DenaUserPassAuthenticationFilter;
import com.dena.platform.restapi.endpoint.v1.API;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.util.Collections;

/**
 * @author Javad Alimohammadi [<bs.alimohammadi@gmail.com>]
 */

@EnableGlobalMethodSecurity(prePostEnabled = true)
@EnableWebSecurity
@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    private final static Logger log = LoggerFactory.getLogger(SecurityConfig.class);


    private JWTAuthenticationEntryPoint entryPoint;

    private JWTAuthenticationProvider authenticationProvider;


    @Autowired
    public SecurityConfig(JWTAuthenticationEntryPoint entryPoint, JWTAuthenticationProvider authenticationProvider) {
        this.entryPoint = entryPoint;
        this.authenticationProvider = authenticationProvider;
    }

    @Bean
    public AuthenticationManager authenticationManager() {
        return new ProviderManager(Collections.singletonList(authenticationProvider));
    }


    @Override
    protected void configure(HttpSecurity http) throws Exception {
        boolean isDenaSecurityModuleEnabled = DenaConfigReader.readBooleanProperty("dena.api.security.enabled", true);

        if (isDenaSecurityModuleEnabled) {
            http.csrf().disable()
                    .authorizeRequests().antMatchers("**" + API.API_PATH + "**").authenticated()
                    .and()
                    .exceptionHandling().authenticationEntryPoint(entryPoint)
                    .and()
                    .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

            http.headers().cacheControl();
            registerAuthenticationFilter(http);

        } else {
            log.warn("Security is disabled. this property should be used in development mode only");
            http.csrf().disable();
            http.authorizeRequests()
                    .antMatchers("/**")
                    .permitAll();
        }

    }


    private void registerAuthenticationFilter(HttpSecurity http) {
        DenaUserPassAuthenticationFilter filter = new DenaUserPassAuthenticationFilter("/*/users/login");
        filter.setAuthenticationManager(authenticationManager());
        filter.setAuthenticationSuccessHandler(new JWTSuccessHandler());

        http.addFilterBefore(filter, UsernamePasswordAuthenticationFilter.class);

    }
}
