package com.shiro.demo.relam;

import com.shiro.demo.bean.MemberInfo;
import com.shiro.demo.constants.JwtTokenConstant;
import com.shiro.demo.context.AuthContext;
import com.shiro.demo.entity.MemberInfoEntity;
import com.shiro.demo.enums.MemberStatusEnum;
import com.shiro.demo.jwt.JwtTokenPayload;
import com.shiro.demo.principal.JwtPrincipalMap;
import com.shiro.demo.service.BusinessDomainHostService;
import com.shiro.demo.service.MemberInfoService;
import com.shiro.demo.token.UsernamePasswordBCryptToken;
import com.shiro.demo.util.HAMCUtil;
import com.shiro.demo.util.JwtUtil;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.subject.PrincipalCollection;
import org.mindrot.jbcrypt.BCrypt;

import javax.annotation.Resource;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;


/**
 * 用户名密码登录处理Realm
 * Author TianYang
 * Date 2023/11/15 14:44
 */
public class UsernamePasswordRealm extends CustomizeAbstractRealm {

    @Resource
    private BusinessDomainHostService businessDomainHostService;

    @Resource
    private MemberInfoService memberInfoService;

    public UsernamePasswordRealm() {
        //限定支持的Token类型
        this.setAuthenticationTokenClass(UsernamePasswordBCryptToken.class);
    }

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {

        UsernamePasswordBCryptToken usernamePasswordToken = (UsernamePasswordBCryptToken) authenticationToken;
        String userName = usernamePasswordToken.getUsername();
        String clientHost = usernamePasswordToken.getClientHost();
        MemberInfoEntity existsMemberInfo = usernamePasswordToken.getMemberInfo();

        //根据 Host查询对应的业务域编码、租户编码
        Long tenantId = existsMemberInfo.getTenantId();
        String businessDomainCode = usernamePasswordToken.getBusinessDomainCode();
        AuthContext.setBusinessDomainCode(businessDomainCode);


        Long membrerId = existsMemberInfo.getMemberId();
        //生成凭据
        String password = existsMemberInfo.getPassword();
        String realName = existsMemberInfo.getRealName();
        String identityType = existsMemberInfo.getIdentityType();
        int status = existsMemberInfo.getStatus();
        if (MemberStatusEnum.DISABLE.getCode() == status) {
            throw new AuthenticationException("User is disabled");
        }

        SimpleAuthenticationInfo simpleAuthenticationInfo = new SimpleAuthenticationInfo();
        //设置凭据
        simpleAuthenticationInfo.setCredentials(password);


        JwtTokenPayload jwtTokenPayload = new JwtTokenPayload();
        MemberInfo memberInfo = new MemberInfo();
        memberInfo.setMemberId(membrerId);
        memberInfo.setName(realName);
        memberInfo.setTenantId(tenantId);
        memberInfo.setBusinessDomainCode(businessDomainCode);
        memberInfo.setIdentityType(identityType);
        memberInfo.setStatus(status);
        jwtTokenPayload.setSub(String.valueOf(membrerId));
        jwtTokenPayload.setMemberInfo(memberInfo);
        JwtPrincipalMap jwtPrincipalMap = new JwtPrincipalMap();
        //设置认证用户唯一标识
        jwtPrincipalMap.put(JwtTokenConstant.PRIMARY_PRINCIPAL_KEY, membrerId);
        //设置Token
        jwtPrincipalMap.put(JwtTokenConstant.JWT_TOKEN, JwtUtil.createJwtToken(jwtTokenPayload));
        //设置JWT失效时间
        jwtPrincipalMap.put(JwtTokenConstant.TOKEN_EXPIRED_TIME, JwtUtil.getExpirationTimeMillis(JwtTokenConstant.TOKEN_EXPIRED_DAYS));
        jwtPrincipalMap.put(JwtTokenConstant.MEMBER_INFO, memberInfo);
        //设置用户认证信息
        simpleAuthenticationInfo.setPrincipals(jwtPrincipalMap);
        return simpleAuthenticationInfo;
    }

    @Override
    protected void clearCachedAuthenticationInfo(PrincipalCollection principals) {
        super.clearCachedAuthenticationInfo(principals);
    }


    @Override
    protected void clearCachedAuthorizationInfo(PrincipalCollection principals) {
        super.clearCachedAuthorizationInfo(principals);
    }


    public static void main(String[] args) {
        String salt = BCrypt.gensalt(10);
        String password = BCrypt.hashpw("666", salt);
        System.out.println(password);
        try {
            System.out.println(HAMCUtil.signature("123", "456", HAMCUtil.Algorithms.HmacMD5));
            System.out.println(System.currentTimeMillis());
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        } catch (InvalidKeyException e) {
            throw new RuntimeException(e);
        }
    }
}
