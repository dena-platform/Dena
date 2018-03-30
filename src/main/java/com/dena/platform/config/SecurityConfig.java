package com.dena.platform.config;

import com.dena.platform.core.feature.security.JwtAuthenticationEntryPoint;
import com.dena.platform.core.feature.security.JwtAuthenticationFilter;
import com.dena.platform.core.feature.security.JwtAuthenticationProvider;
import com.dena.platform.core.feature.security.JwtSuccessHandler;
import com.dena.platform.restapi.endpoint.v1.API;
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
 * @author Javad Alimohammadi [<bs.alimohammadi@yahoo.com>]
 */

@EnableGlobalMethodSecurity(prePostEnabled = true)
@EnableWebSecurity
@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private JwtAuthenticationEntryPoint entryPoint;
    private JwtAuthenticationProvider authenticationProvider;

    @Autowired
    public SecurityConfig(JwtAuthenticationEntryPoint entryPoint, JwtAuthenticationProvider authenticationProvider) {
        this.entryPoint = entryPoint;
        this.authenticationProvider = authenticationProvider;
    }

    @Bean
    public AuthenticationManager authenticationManager() {
        return new ProviderManager(Collections.singletonList(authenticationProvider));
    }

    @Bean
    public JwtAuthenticationFilter authenticationTokenFilter() {
        JwtAuthenticationFilter filter = new JwtAuthenticationFilter();
        filter.setAuthenticationManager(authenticationManager());
        filter.setAuthenticationSuccessHandler(new JwtSuccessHandler());
        return filter;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
//        http.authorizeRequests().antMatchers("/**").permitAll();

        http.csrf().disable()
                .authorizeRequests().antMatchers("**" + API.API_PATH + "**").authenticated()
                .and()
                .exceptionHandling().authenticationEntryPoint(entryPoint)
                .and()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        http.addFilterBefore(authenticationTokenFilter(), UsernamePasswordAuthenticationFilter.class);

        http.headers().cacheControl();
    }
}
