package com.itmo.wtsc.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import javax.sql.DataSource;

import static com.itmo.wtsc.utils.enums.UserRole.*;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final DataSource dataSource;
    private final AuthenticationSuccessHandler authenticationSuccessHandler;

    @Bean(name = "wtsc.encoder")
    public PasswordEncoder getPasswordEncoder() {
        return new BCryptPasswordEncoder(8);
    }

    @Autowired
    public SecurityConfig(DataSource dataSource, AuthenticationSuccessHandler authenticationSuccessHandler) {
        this.dataSource = dataSource;
        this.authenticationSuccessHandler = authenticationSuccessHandler;
    }

    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers("/tourist")
                    .hasAnyAuthority(TOURIST.toString())
                .antMatchers("/volunteer")
                    .hasAnyAuthority(VOLUNTEER.toString())
                .antMatchers("/statistic")
                    .hasAnyAuthority(VOLUNTEER.toString())
                .antMatchers("/statistic/requestChanges")
                    .hasAnyAuthority(VOLUNTEER.toString())
                .antMatchers("/user_management")
                    .hasAnyAuthority(ADMIN.toString())
                .antMatchers(HttpMethod.GET, "/requests")
                    .hasAnyAuthority(TOURIST.toString(), VOLUNTEER.toString())
                .antMatchers(HttpMethod.POST, "/requests")
                    .hasAnyAuthority(TOURIST.toString())
                .antMatchers(HttpMethod.PUT, "/requests/*")
                    .hasAnyAuthority(TOURIST.toString(), VOLUNTEER.toString())
                .antMatchers(HttpMethod.DELETE, "/requests/*")
                    .hasAnyAuthority(TOURIST.toString())
                .antMatchers(HttpMethod.PUT, "/users/*")
                    .hasAnyAuthority(ADMIN.toString())
                .antMatchers(HttpMethod.GET, "/users")
                    .hasAnyAuthority(ADMIN.toString())
                .antMatchers("/css/**", "/fonts/**", "/img/**", "/js/**", "/index", "/registration", "/confirm").permitAll()
                .anyRequest().authenticated().
                and()
                    .formLogin()
                    .loginPage("/login")
                    .successHandler(authenticationSuccessHandler)
                    .permitAll()
                .and()
                    .logout()
                    .permitAll()
                .and().httpBasic()
                .and().csrf().disable();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.jdbcAuthentication()
                .dataSource(dataSource)
                .passwordEncoder(getPasswordEncoder())
                .usersByUsernameQuery("select login, password, active from user where login=?")
                .authoritiesByUsernameQuery("select login, role from user where login=?");
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
