package com.atguigu.security.service;

import com.atguigu.security.entity.Users;
import com.atguigu.security.mapper.UsersMapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("userDetailsService")
public class UserDetailService implements UserDetailsService {
    @Autowired
    UsersMapper usersMapper;
    @Autowired
    PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        QueryWrapper<Users> query = new QueryWrapper<>();
        query.eq("username",username);
        Users u = usersMapper.selectOne(query);//从数据库中获取username 和加密过的password
        System.out.println("user:"+u);
        if(u==null){
            throw new UsernameNotFoundException(username+" can not be found!");
        }
        List<GrantedAuthority> auths= AuthorityUtils.commaSeparatedStringToAuthorityList("role,ROLE_admin,ROLE_approver,/update.html");
        return new User(u.getUsername(),passwordEncoder.encode(u.getPassword()),auths);//spring提供的user对象
    }
}
