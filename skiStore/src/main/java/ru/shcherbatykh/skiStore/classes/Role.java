package ru.shcherbatykh.skiStore.classes;

import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collections;
import java.util.Set;

public enum Role {
    GUEST, USER, ADMIN;

    public Set<SimpleGrantedAuthority> getAuthorities() {
        return Collections.singleton(new SimpleGrantedAuthority(this.name()));
    }
}