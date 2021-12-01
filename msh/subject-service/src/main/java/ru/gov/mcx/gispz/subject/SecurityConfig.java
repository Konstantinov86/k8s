package ru.gov.mcx.gispz.subject;

import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import ru.gov.mcx.gispz.auth.AuthTokenFilter;
import ru.gov.mcx.gispz.auth.AuthenticationBean;
import ru.gov.mcx.gispz.auth.JwtTokenService;


@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    @Bean
    public AuthTokenFilter authenticationJwtTokenFilter() {
        return new AuthTokenFilter();
    }


    @Bean
    public JwtTokenService tokenService() {
        return new JwtTokenService();
    }


    @Bean
    public AuthenticationBean authBean() {
        return new AuthenticationBean();
    }


    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
            .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            .and().cors()
            .and().csrf().disable()
            .authorizeRequests((authorize) -> authorize
//                    .anyRequest().authenticated()
                    .antMatchers("/api/**").permitAll()
//                    .antMatchers("/api/**").hasRole("SUBJECT_USER")
//                    .antMatchers("/api/**").hasRole("ROLE_ADMIN")
//                    .antMatchers("/api/**").hasAuthority("ADMIN")
//                .antMatchers("/api/**").hasAnyRole("ROLE_ADMIN", "ROLE_ADMIN")
//                .antMatchers("/api/**").hasRole("ROLE_MCX_USER")
//                .antMatchers("/api/**").hasRole("ROLE_GOVERMENT_USER")
            )
            .addFilterBefore(authenticationJwtTokenFilter(), UsernamePasswordAuthenticationFilter.class)
        ;
    }
}


