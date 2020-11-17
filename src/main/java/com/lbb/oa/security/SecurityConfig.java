package com.lbb.oa.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lbb.oa.util.GlobalConfig;
import com.lbb.oa.util.ResponseBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.session.ConcurrentSessionFilter;
import org.springframework.web.cors.CorsUtils;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private LoginSuccessHandler loginSuccessHandler;

    @Autowired
    private LoginFailureHandler loginFailureHandler;

    @Autowired
    private MyLogoutSuccessHandler myLogoutSuccessHandler;

    @Bean
    SessionRegistryImpl sessionRegistry() {
        return new SessionRegistryImpl();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
                .anyRequest()
                .authenticated()
                .and()
                .formLogin()
                .loginPage("/login")
                .loginProcessingUrl("/doLogin")
                .permitAll()
                .failureHandler(loginFailureHandler)
                .successHandler(loginSuccessHandler)
                .and()
                .logout()
                .logoutSuccessHandler(myLogoutSuccessHandler)
                .permitAll()
                .and()
                .csrf()
                .disable()
                .addFilterAt(new ConcurrentSessionFilter(sessionRegistry(), event -> {
                    HttpServletResponse resp = event.getResponse();
                    resp.setContentType("application/json;charset=utf-8");
                    resp.setStatus(HttpStatus.UNAUTHORIZED.value());
                    PrintWriter out = resp.getWriter();
                    out.write(new ObjectMapper().writeValueAsString(
                            ResponseBean.formatData(GlobalConfig.ResponseCode.ONLINE.getCode(),
                                    GlobalConfig.ResponseCode.ONLINE.getDesc(),
                                    null
                            )));
                    out.flush();
                    out.close();
                }), ConcurrentSessionFilter.class)
                .exceptionHandling().authenticationEntryPoint(new AuthenticationEntryPoint() {
                @Override
                public void commence(HttpServletRequest httpServletRequest, HttpServletResponse resp, AuthenticationException authException) throws IOException, ServletException {
                    resp.setContentType("application/json;charset=utf-8");
                    resp.setStatus(HttpStatus.UNAUTHORIZED.value());
                    PrintWriter out = resp.getWriter();
                    ResponseBean respBean = ResponseBean.errorLogin("访问失败!");
                    if (authException instanceof InsufficientAuthenticationException) {
                        respBean.setMsg("未登录，请重新登录!");
                    }
                    out.write(new ObjectMapper().writeValueAsString(respBean));
                    out.flush();
                    out.close();
                }
        });
    }
}
