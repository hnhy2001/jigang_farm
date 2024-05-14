package com.example.jingangfarmmanagement.service;


import org.springframework.security.core.Authentication;

public interface AuthorizerService {
    boolean authorize(Authentication authentication, String action, Object callerObj) throws Exception;
}
