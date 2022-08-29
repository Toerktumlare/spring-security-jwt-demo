package com.example.jwt;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;

import java.util.Collection;

public record Token(Jwt token, Collection<GrantedAuthority> authorities){}
