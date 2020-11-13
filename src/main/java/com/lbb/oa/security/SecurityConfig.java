package com.lbb.oa.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.ObjectPostProcessor;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.access.intercept.FilterSecurityInterceptor;

@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private LoginSuccessHandler loginSuccessHandler;

    @Autowired
    private LoginFailureHandler loginFailureHandler;

    @Autowired
    private MyLogoutSuccessHandler myLogoutSuccessHandler;

    @Autowired
    CustomFilterInvocationSecurityMetadataSource customFilterInvocationSecurityMetadataSource;

    @Autowired
    CustomAccessDecisionManager customAccessDecisionManager;

    @Autowired
    private MyAccessDeniedHandler deniedHandler;//自定义错误(403)返回数据

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
                .withObjectPostProcessor(new ObjectPostProcessor<FilterSecurityInterceptor>() {
                    @Override
                    public <O extends FilterSecurityInterceptor> O postProcess(O object) {
                        object.setSecurityMetadataSource(customFilterInvocationSecurityMetadataSource);
                        object.setAccessDecisionManager(customAccessDecisionManager);
                        return object;
                    }
                })
                .and()
                .formLogin()
                .loginPage("/login")
                .loginProcessingUrl("/doLogin")
                .failureHandler(loginFailureHandler)
                .successHandler(loginSuccessHandler)
                .permitAll()
                .and()
                .logout()
                .logoutSuccessHandler(myLogoutSuccessHandler)
                .permitAll()
                .and()
                .csrf()
                .disable()
                .exceptionHandling().accessDeniedHandler(deniedHandler);

    }
}
