package com.shiro.demo.principal;

import org.apache.shiro.subject.PrincipalCollection;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

public class ForjPrincipal implements PrincipalCollection {
    @Override
    public Object getPrimaryPrincipal() {
        return null;
    }

    @Override
    public <T> T oneByType(Class<T> type) {
        return null;
    }

    @Override
    public <T> Collection<T> byType(Class<T> type) {
        return null;
    }

    @Override
    public List asList() {
        return null;
    }

    @Override
    public Set asSet() {
        return null;
    }

    @Override
    public Collection fromRealm(String realmName) {
        return null;
    }

    @Override
    public Set<String> getRealmNames() {
        return null;
    }

    @Override
    public boolean isEmpty() {
        return false;
    }

    @Override
    public Iterator iterator() {
        return null;
    }
}
