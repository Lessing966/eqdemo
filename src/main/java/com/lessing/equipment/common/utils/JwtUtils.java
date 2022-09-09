package com.lessing.equipment.common.utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.Claim;
import com.lessing.equipment.modules.sys.dto.UserListDTO;
import com.lessing.equipment.modules.sys.dto.UserRoleDTO;
import com.lessing.equipment.modules.sys.entity.UserEntity;
import com.lessing.equipment.modules.sys.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
@Component
public class JwtUtils {

    @Autowired
    private UserService userService;

    public static String createToken(String userId,String phone) {
        return JWT.create().withAudience(userId)   //签发对象
                .withClaim("userid", userId)
                .withClaim("phone", phone)
                .sign(Algorithm.HMAC256(userId+"HelloLessing"));   //加密
    }

    /**
     * 通过载荷名字获取载荷的值
     */
    public static Claim getClaimByName(String token, String name){
        return JWT.decode(token).getClaim(name);
    }

    public UserRoleDTO getUid(HttpServletRequest request){
        UserRoleDTO userRole =new UserRoleDTO();
        String token = request.getHeader("token");
        String userid = JwtUtils.getClaimByName(token, "userid").asString();
        UserEntity userEntity = userService.selectUserOneByUid(userid);
        if(null !=userEntity){
            userRole.setUid(userid);
            userRole.setRole(userEntity.getRole());
        }
        return userRole;
    }


}