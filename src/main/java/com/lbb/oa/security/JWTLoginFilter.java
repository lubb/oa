package com.lbb.oa.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lbb.oa.enums.GlobalConfigEnum;
import com.lbb.oa.jwt.JWTUtil;
import com.lbb.oa.pojo.sys.SecuritySysUser;
import com.lbb.oa.util.ResponseBean;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 登录处理器
 */
public class JWTLoginFilter extends UsernamePasswordAuthenticationFilter {

    private AuthenticationManager authenticationManager;

    public JWTLoginFilter(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
        super.setFilterProcessesUrl("/doLogin");
    }

    /**
     * 尝试身份认证(接收并解析用户凭证)
     * @param request
     * @param response
     * @return
     * @throws AuthenticationException
     */
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        try {
            SecuritySysUser securitySysUser = new ObjectMapper().readValue(request.getInputStream(), SecuritySysUser.class);
            return authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(securitySysUser.getUsername(), securitySysUser.getPassword())
            );
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 登录成功后的处理
     * @param request
     * @param response
     * @param chain
     * @param authResult
     * @throws IOException
     * @throws ServletException
     */
    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
        String token = null;
        try {
            SecuritySysUser securitySysUser = (SecuritySysUser)authResult.getPrincipal();
            token = JWTUtil.createToken(securitySysUser.getUsername(), String.join(",",securitySysUser.getPermissions()), securitySysUser);
            // 登录成功后，返回token到header里面
            response.addHeader("token", JWTUtil.TOKEN_PREFIX + token);
            response.setContentType("application/json;charset=UTF-8");
            response.getWriter().write(new ObjectMapper().writeValueAsString(
                    ResponseBean.formatData(GlobalConfigEnum.ResponseCode.SUCCESS.getCode(),
                            "登录"+ GlobalConfigEnum.ResponseCode.SUCCESS.getDesc(),
                            securitySysUser
                    )));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 登录失败后的处理
     * @param request
     * @param httpServletResponse
     * @param exception
     * @throws IOException
     * @throws ServletException
     */
    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse httpServletResponse, AuthenticationException exception) throws IOException, ServletException {
        httpServletResponse.setStatus(HttpStatus.BAD_REQUEST.value());
        httpServletResponse.setContentType("application/json;charset=UTF-8");
        String msg = "";
        if (exception instanceof LockedException) {
            msg = "账户被锁定，请联系管理员!";
        } else if (exception instanceof CredentialsExpiredException) {
            msg ="密码过期，请联系管理员!";
        } else if (exception instanceof AccountExpiredException) {
            msg ="账户过期，请联系管理员!";
        } else if (exception instanceof DisabledException) {
            msg ="账户被禁用，请联系管理员!";
        } else if (exception instanceof BadCredentialsException) {
            msg ="用户名或者密码输入错误，请重新输入!";
        }
        httpServletResponse.getWriter().write(new ObjectMapper().writeValueAsString(
                ResponseBean.formatData(GlobalConfigEnum.ResponseCode.ERROR.getCode(),
                        msg,
                        "登录失败:"+ msg
                )));
    }
}
