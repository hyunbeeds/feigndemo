package com.polarbear.feigndemo.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.authentication.Http403ForbiddenEntryPoint;
import org.springframework.security.web.firewall.HttpFirewall;
import org.springframework.security.web.firewall.StrictHttpFirewall;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.regex.Pattern;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
                .headers().frameOptions().disable().and()
                .authorizeRequests().antMatchers("/h2-console/**", "/health-check", "/*").permitAll().and()
                .exceptionHandling().authenticationEntryPoint(http403ForbiddenEntryPoint());
    }

    @Bean
    public AuthenticationEntryPoint http403ForbiddenEntryPoint() {
        return new Http403ForbiddenEntryPoint();
    }

    @Bean
    public HttpFirewall allowAllowedHeaderValuesHttpFirewall() {
        StrictHttpFirewall firewall = new StrictHttpFirewall();
        firewall.setAllowedHeaderValues(value -> {
            String encodedValue;
            Pattern pattern = Pattern.compile("[\\p{IsAssigned}&&[^\\p{IsControl}]]*");
            boolean result = true;
            try {
                encodedValue = new String(value.getBytes(StandardCharsets.ISO_8859_1), "ksc5601");
                result = pattern.matcher(encodedValue).matches();

                if (!result) {
                    encodedValue = new String(value.getBytes(StandardCharsets.ISO_8859_1), StandardCharsets.UTF_8);
                    result = pattern.matcher(encodedValue).matches();
                }
            } catch (UnsupportedEncodingException e) { // NOSONAR
                // do nothing
            }

            return result;
        });
        return firewall;
    }
}