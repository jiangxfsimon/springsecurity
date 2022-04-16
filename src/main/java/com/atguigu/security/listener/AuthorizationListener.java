package com.atguigu.security.listener;

import org.springframework.context.event.EventListener;
import org.springframework.security.access.event.AbstractAuthorizationEvent;
import org.springframework.security.access.event.AuthorizationFailureEvent;
import org.springframework.security.access.event.AuthorizedEvent;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.stereotype.Component;

@Component
public class AuthorizationListener {
    @EventListener
    public void onSuccess(AuthorizedEvent event) {
        System.out.println(event.getAuthentication());
        System.out.println("AuthorizedEvent onSuccess:"+event);
    }
    @EventListener
    public void onFailure(AuthorizationFailureEvent event) {
        System.out.println(event.getAuthentication());
        System.out.println(event.getAccessDeniedException());
        System.out.println(event.getConfigAttributes());
        System.out.println("AuthorizationFailureEvent onFailure:"+event);
    }
}
