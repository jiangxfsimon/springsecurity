package com.atguigu.security.service;

import com.atguigu.security.entity.Role;
import com.atguigu.security.entity.User;
import com.atguigu.security.mapper.RoleMapper;
import com.atguigu.security.mapper.UserMapper;
import com.atguigu.security.mapper.UsersMapper;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsPasswordService;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("userDetailsService")
public class UserDetailService  implements  UserDetailsService,UserDetailsPasswordService {//升级密码策略, UserDetailsPasswordService(源码在DaoAuthenticationProvider中createSuccessAuthentication)
    @Autowired
    UserMapper userMapper;
    @Autowired
    RoleMapper roleMapper;
//    @Autowired
//    PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
//        QueryWrapper<User> query = new QueryWrapper<>();
//        query.select("id,username,password,enabled,account_non_expired,account_non_locked,credentials_non_expired");
//        query.eq("username",username);
        LambdaQueryWrapper<User> lambdaQueryWrapper = new LambdaQueryWrapper();
        lambdaQueryWrapper.eq(User::getUsername,username);
        User user = userMapper.selectOne(lambdaQueryWrapper);

        System.out.println("user:"+user);

        if(user==null){
            throw new UsernameNotFoundException(username+" can not be found!");
        }
        List<Role> roles = roleMapper.getRolesById(user.getId());
        user.setRoles(roles);
//        List<GrantedAuthority> auths= AuthorityUtils.commaSeparatedStringToAuthorityList("role,ROLE_admin,ROLE_approver,/update.html");
        return user;
    }

    /**
     * 升级密码
     * @param user
     * @param newPassword
     * @return
     */
    @Override
    public UserDetails updatePassword(UserDetails user, String newPassword) {
        User u = (User) user;
        u.setPassword(newPassword);
        int result = userMapper.updatePasswordByUser(u);
        if(result>0){
            return u;
        }
        return null;
    }
}
