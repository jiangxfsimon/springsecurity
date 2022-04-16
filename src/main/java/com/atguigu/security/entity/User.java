package com.atguigu.security.entity;

import com.alibaba.fastjson.annotation.JSONField;
import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
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
    private Boolean enabled;
    private Boolean accountNonExpired;
    private Boolean accountNonLocked;
    private Boolean credentialsNonExpired;
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
        return accountNonExpired;
    }

    @Override
    public boolean isAccountNonLocked() {
        return accountNonLocked;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return this.credentialsNonExpired;
    }

    @Override
    public boolean isEnabled() {
        return this.enabled;
    }


//    public Boolean getEnabled() {
//        return this.enabled;
//    }
//
//    public Boolean getAccountNonExpired() {
//        return this.accountNonExpired;
//    }
//
//    public Boolean getAccountNonLocked() {
//        return this.accountNonLocked;
//    }
//
//    public Boolean getCredentialsNonExpired() {
//        return this.credentialsNonExpired;
//    }
}
