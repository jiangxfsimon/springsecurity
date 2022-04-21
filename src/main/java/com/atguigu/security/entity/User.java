package com.atguigu.security.entity;

import com.alibaba.fastjson.annotation.JSONField;
import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.CollectionUtils;

import java.util.*;



@JsonInclude(value = JsonInclude.Include.NON_NULL)
@ToString
@NoArgsConstructor
@Setter
public class User implements UserDetails {
    private Integer id;
    private String username;
    @JsonIgnore
    private String password;
    private Boolean enable;
    private Boolean accountNotExpired;
    private Boolean accountNotLocked;
    private Boolean credentialsNotExpired;
    @TableField(exist = false)
    private List<Role> roles = null;

    public User(String username, String password) {
        this.username=username;
        this.password=password;

    }

    @Override
    public Collection<SimpleGrantedAuthority> getAuthorities() {
        Set<SimpleGrantedAuthority> authorities = new HashSet<>();
        if(!CollectionUtils.isEmpty(roles)){
            roles.forEach(e-> authorities.add(new SimpleGrantedAuthority(e.getName())));
        }
        return authorities;
    }

    public Integer getId() {
        return id;
    }

    public List<Role> getRoles() {
        return roles;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return accountNotExpired;
    }

    @Override
    public boolean isAccountNonLocked() {
        return accountNotLocked;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return this.credentialsNotExpired;
    }

    @Override
    public boolean isEnabled() {
        return this.enable;
    }


    public Boolean getEnable() {
        return enable;
    }

    public Boolean getAccountNotExpired() {
        return accountNotExpired;
    }

    public Boolean getAccountNotLocked() {
        return accountNotLocked;
    }

    public Boolean getCredentialsNotExpired() {
        return credentialsNotExpired;
    }
}
