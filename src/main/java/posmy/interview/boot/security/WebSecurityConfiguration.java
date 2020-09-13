package posmy.interview.boot.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

import posmy.interview.boot.enums.role.RoleEnum;

/**
 * @author Rashidi Zin
 */
@Configuration
public class WebSecurityConfiguration extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
        .httpBasic()
        .and()
        .csrf().disable().authorizeRequests()
        .antMatchers(HttpMethod.GET, "/").permitAll()
        .antMatchers(HttpMethod.GET, "/users/read").hasAnyRole(RoleEnum.LIBRARIAN.getRole(), RoleEnum.MEMBER.getRole())
        .antMatchers(HttpMethod.DELETE, "/users/delete").hasAnyRole(RoleEnum.LIBRARIAN.getRole(), RoleEnum.MEMBER.getRole())
        .antMatchers(HttpMethod.GET, "/book/read").hasAnyRole(RoleEnum.LIBRARIAN.getRole(), RoleEnum.MEMBER.getRole())
        .antMatchers(HttpMethod.POST, "/book/create").hasRole(RoleEnum.LIBRARIAN.getRole())
        .antMatchers(HttpMethod.PUT, "/book/update").hasRole(RoleEnum.LIBRARIAN.getRole())
        .antMatchers(HttpMethod.DELETE, "/book/delete").hasRole(RoleEnum.LIBRARIAN.getRole())
        .antMatchers(HttpMethod.PUT, "/book/borrow").hasRole(RoleEnum.MEMBER.getRole())
        .antMatchers(HttpMethod.PUT, "/book/return").hasRole(RoleEnum.MEMBER.getRole())
        ;
    }

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth.inMemoryAuthentication()
            .withUser("member")
            .password("{noop}password")
            .roles(RoleEnum.MEMBER.getRole())
            .and()
            .withUser("librarian")
            .password("{noop}password")
            .roles(RoleEnum.LIBRARIAN.getRole());
    }
    
}
