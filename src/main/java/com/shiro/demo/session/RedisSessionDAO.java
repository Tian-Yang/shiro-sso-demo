package com.shiro.demo.session;

import org.apache.shiro.session.Session;
import org.apache.shiro.session.UnknownSessionException;
import org.apache.shiro.session.mgt.eis.AbstractSessionDAO;

import java.io.Serializable;
import java.util.Collection;

public class RedisSessionDAO extends AbstractSessionDAO {
    @Override
    protected Serializable doCreate(Session session) {
        Serializable sessionId = generateSessionId(session);
        assignSessionId(session, sessionId);
        //store session
        storeSession(sessionId,session);
        return null;
    }


    protected Session storeSession(Serializable id, Session session) {
        if (id == null) {
            throw new NullPointerException("id argument cannot be null.");
        }
        return session;
    }

    @Override
    protected Session doReadSession(Serializable sessionId) {
        return null;
    }

    @Override
    public void update(Session session) throws UnknownSessionException {

    }

    @Override
    public void delete(Session session) {

    }

    @Override
    public Collection<Session> getActiveSessions() {
        return null;
    }
}
