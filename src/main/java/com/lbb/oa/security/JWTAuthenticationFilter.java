package com.lbb.oa.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lbb.oa.exception.TokenIsExpiredException;
import com.lbb.oa.jwt.JWTUtil;
import com.lbb.oa.pojo.sys.SecuritySysUser;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class JWTAuthenticationFilter extends BasicAuthenticationFilter {

    public JWTAuthenticationFilter(AuthenticationManager authenticationManager) {
        super(authenticationManager);
    }

    /**
     * 验证token的合法性
     *
     * @param request
     * @param response
     * @param filterChain
     * @throws ServletException
     * @throws IOException
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String tokenHeader = request.getHeader(JWTUtil.TOKEN_HEADER);
        if (tokenHeader == null || !tokenHeader.startsWith(JWTUtil.TOKEN_PREFIX)) {
            filterChain.doFilter(request, response);
            return;
        }
        try{
            UsernamePasswordAuthenticationToken authentication = getAuthentication(request, response, tokenHeader);
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }catch (TokenIsExpiredException e){
            //返回json形式的错误信息
            response.setCharacterEncoding("UTF-8");
            response.setContentType("application/json; charset=utf-8");
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            String reason = "请求失败原因：" + e.getMessage();
            response.getWriter().write(new ObjectMapper().writeValueAsString(reason));
            response.getWriter().flush();
            return;
        }
        filterChain.doFilter(request, response);
    }

    private UsernamePasswordAuthenticationToken getAuthentication(HttpServletRequest request, HttpServletResponse response, String tokenHeader) throws TokenIsExpiredException {
        String token = tokenHeader.replace(JWTUtil.TOKEN_PREFIX, "");
        boolean expiration = JWTUtil.isExpiration(token);
        if (expiration) {
            throw new TokenIsExpiredException("token超时了");
        } else {
            String username = JWTUtil.getUsername(token);
            SecuritySysUser securitySysUser = JWTUtil.getSecuritySysUser(token);
            String role = JWTUtil.getUserRole(token);
            String[] result= role.split(",");
            Set<String> permissions = new HashSet<>(Arrays.asList(result));
            securitySysUser.setPermissions(permissions);
            List<GrantedAuthority> auths = AuthorityUtils.createAuthorityList(result);
            if (username != null) {
                return new UsernamePasswordAuthenticationToken(securitySysUser, null, auths);
            }
        }
        return null;
    }
}
