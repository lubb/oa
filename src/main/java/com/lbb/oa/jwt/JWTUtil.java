package com.lbb.oa.jwt;

import com.lbb.oa.pojo.sys.SecuritySysUser;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import java.util.Date;
import java.util.HashMap;

public class JWTUtil {

    /**
     * 签名key
     */
    public static final String SECRET_KEY = "spring-security-@Jwt!&Secret^#";

    public static final String TOKEN_HEADER = "Authorization";

    public static final String TOKEN_PREFIX = "Bearer ";
    // 角色的key
    private static final String ROLE_CLAIMS = "ROLE_CLAIMS";

    private static final String SEC_USER_NICKNAME_CLAIMS = "SEC_USER_NICKNAME_CLAIMS";

    private static final String SEC_USER_TYPE_CLAIMS = "SEC_USER_TYPE_CLAIMS";

    private static final String SEC_USER_AVATAR_CLAIMS = "SEC_USER_AVATAR_CLAIMS";

    private static final String SEC_USER_ID_CLAIMS = "SEC_USER_ID_CLAIMS";

    private static final String SEC_USER_PASSWORD_CLAIMS = "SEC_USER_PASSWORD_CLAIMS";

    // 过期时间是3600秒，既是1个小时
    private static final long EXPIRATION = 3600L;

    // 创建token
    public static String createToken(String username, String role, SecuritySysUser securitySysUser) {
        HashMap<String, Object> map = new HashMap<>();
        map.put(ROLE_CLAIMS, role);
        map.put(SEC_USER_NICKNAME_CLAIMS, securitySysUser.getNickname());
        map.put(SEC_USER_TYPE_CLAIMS, securitySysUser.getType().toString());
        map.put(SEC_USER_AVATAR_CLAIMS, securitySysUser.getAvatar());
        map.put(SEC_USER_ID_CLAIMS, securitySysUser.getId().toString());
        map.put(SEC_USER_PASSWORD_CLAIMS, securitySysUser.getPassword());
        return Jwts.builder()
                .signWith(SignatureAlgorithm.HS512, SECRET_KEY)
                .setClaims(map)
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION * 1000))
                .compact();
    }

    // 从token中获取用户名
    public static String getUsername(String token){
        return getTokenBody(token).getSubject();
    }

    // 获取用户角色
    public static String getUserRole(String token){
        return (String) getTokenBody(token).get(ROLE_CLAIMS);
    }

    // 获取登录昵称
    public static String getSecUserNickName(String token){
        return (String) getTokenBody(token).get(SEC_USER_NICKNAME_CLAIMS);
    }

    // 获取登录密码
    public static String getSecUserPassword(String token){
        return (String) getTokenBody(token).get(SEC_USER_PASSWORD_CLAIMS);
    }

    // 获取登录ID
    public static String getSecUserId(String token){
        return (String) getTokenBody(token).get(SEC_USER_ID_CLAIMS);
    }

    // 获取登录头像
    public static String getSecUserAvatar(String token){
        return (String) getTokenBody(token).get(SEC_USER_AVATAR_CLAIMS);
    }

    // 获取登录类型
    public static String getSecUserType(String token){
        return (String) getTokenBody(token).get(SEC_USER_TYPE_CLAIMS);
    }

    // 是否已过期
    public static boolean isExpiration(String token) {
        try {
            return getTokenBody(token).getExpiration().before(new Date());
        } catch (ExpiredJwtException e) {
            return true;
        }
    }

    private static Claims getTokenBody(String token){
        return Jwts.parser()
                .setSigningKey(SECRET_KEY)
                .parseClaimsJws(token)
                .getBody();
    }

    public static SecuritySysUser getSecuritySysUser(String token){
        SecuritySysUser securitySysUser = new SecuritySysUser();
        securitySysUser.setId(Long.parseLong(getSecUserId(token)));
        securitySysUser.setType(Integer.parseInt(getSecUserType(token)));
        securitySysUser.setAvatar(getSecUserAvatar(token));
        securitySysUser.setNickname(getSecUserNickName(token));
        securitySysUser.setPassword(getSecUserPassword(token));
        securitySysUser.setUsername(getUsername(token));
        return securitySysUser;
    }
}
