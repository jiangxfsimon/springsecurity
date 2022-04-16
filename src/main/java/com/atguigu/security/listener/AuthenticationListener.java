package com.atguigu.security.listener;


import org.springframework.context.event.EventListener;
import org.springframework.security.authentication.event.AbstractAuthenticationFailureEvent;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.security.authentication.event.LogoutSuccessEvent;
import org.springframework.stereotype.Component;

/**
 * 认证监听器
 */
@Component
public class AuthenticationListener {
    @EventListener
    public void onSuccess(AuthenticationSuccessEvent success) {
        System.out.println("listen success:"+success);
    }

    @EventListener
    public void onFailure(AbstractAuthenticationFailureEvent failures) {
        System.out.println("listen failure:"+failures);
        System.out.println("exception: "+failures.getException().getMessage());
    }

    @EventListener
    public void onLogoutSuccess(LogoutSuccessEvent successEvent){
        System.out.println(successEvent);
    }
}
