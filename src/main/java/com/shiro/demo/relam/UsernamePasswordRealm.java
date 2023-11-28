package com.shiro.demo.relam;

import com.shiro.demo.bean.MemberInfo;
import com.shiro.demo.constants.JwtTokenConstant;
import com.shiro.demo.jwt.JwtTokenPayload;
import com.shiro.demo.principal.JwtPrincipalMap;
import com.shiro.demo.token.UsernamePasswordBCryptToken;
import com.shiro.demo.util.JwtUtil;
import org.apache.shiro.authc.*;
import org.mindrot.jbcrypt.BCrypt;


/**
 * 用户名密码登录处理Realm
 * Author TianYang
 * Date 2023/11/15 14:44
 */
public class UsernamePasswordRealm extends CustomizeAbstractRealm {

    public UsernamePasswordRealm() {
        //限定支持的Token类型
        this.setAuthenticationTokenClass(UsernamePasswordBCryptToken.class);
    }

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        UsernamePasswordBCryptToken usernamePasswordToken = (UsernamePasswordBCryptToken) authenticationToken;
        String userName = usernamePasswordToken.getUsername();
        //TODO 根据userName从数据库回去对应的凭证
        //生成凭据
        String salt = BCrypt.gensalt(12);
        String password = BCrypt.hashpw("666", salt);
        SimpleAuthenticationInfo simpleAuthenticationInfo = new SimpleAuthenticationInfo();
        //设置凭据
        simpleAuthenticationInfo.setCredentials(password);

        JwtTokenPayload jwtTokenPayload = new JwtTokenPayload();
        String membrerNo = "100001";
        MemberInfo memberInfo = new MemberInfo();
        memberInfo.setMemberNo(membrerNo);
        memberInfo.setName(usernamePasswordToken.getUsername());
        jwtTokenPayload.setSub(userName);
        JwtPrincipalMap jwtPrincipalMap = new JwtPrincipalMap();
        //设置认证用户唯一标识
        jwtPrincipalMap.put(JwtTokenConstant.PRIMARY_PRINCIPAL_KEY, userName);
        //设置Token
        jwtPrincipalMap.put(JwtTokenConstant.JWT_TOKEN, JwtUtil.createJwtToken(jwtTokenPayload));
        //设置JWT失效时间
        jwtPrincipalMap.put(JwtTokenConstant.TOKEN_EXPIRED_TIME, JwtUtil.getExpirationTimeMillis(JwtTokenConstant.TOKEN_EXPIRED_DAYS));

        //设置用户、授权信息
        simpleAuthenticationInfo.setPrincipals(jwtPrincipalMap);
        return simpleAuthenticationInfo;
    }
}
