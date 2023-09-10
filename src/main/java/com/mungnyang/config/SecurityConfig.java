package com.mungnyang.config;

import com.mungnyang.constant.Role;
import com.mungnyang.constant.Url;
import com.mungnyang.service.UserDetailsServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final UserDetailsServiceImpl userDetailsService;
    private final CustomAuthenticationEntryPoint customAuthenticationEntryPoint;
    private final PasswordEncoder passwordEncoder;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.ALWAYS);
        http.formLogin().loginPage(Url.LOGIN).defaultSuccessUrl(Url.MAIN)
                .usernameParameter("email").failureUrl(Url.LOGIN_FAIL);
        http.logout().logoutRequestMatcher(new AntPathRequestMatcher(Url.LOGOUT)).logoutSuccessUrl(Url.MAIN);
        http.authorizeRequests().mvcMatchers(Url.MAIN, "/member/**", "/search/**", "/images/**").permitAll()
                .mvcMatchers("/admin/**").hasRole(Role.ADMIN.name())
                .mvcMatchers("/seller/**").hasAnyRole(Role.ADMIN.name(), Role.SELLER.name())
                .mvcMatchers("/user/**").hasAnyRole(Role.ADMIN.name(), Role.USER.name())
                .anyRequest().authenticated();
        http.exceptionHandling().authenticationEntryPoint(customAuthenticationEntryPoint);

    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers("/css/**", "/image/**", "/js/**");
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder);
    }
}
