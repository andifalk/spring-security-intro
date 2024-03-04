package org.example.opaquetokenresourceserver.introspect;

@SuppressWarnings("unused")
public class IntrospectionResult {
    private boolean active;
    private String client_id;
    private String username;
    private String scope;
    private String sub;
    private String aud;
    private String iss;
    private long exp;
    private long iat;

    public IntrospectionResult() {
    }

    public IntrospectionResult(boolean active, String client_id, String username, String scope, String sub, String aud, String iss, long exp, long iat) {
        this.active = active;
        this.client_id = client_id;
        this.username = username;
        this.scope = scope;
        this.sub = sub;
        this.aud = aud;
        this.iss = iss;
        this.exp = exp;
        this.iat = iat;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public String getClient_id() {
        return client_id;
    }

    public void setClient_id(String client_id) {
        this.client_id = client_id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getScope() {
        return scope;
    }

    public void setScope(String scope) {
        this.scope = scope;
    }

    public String getSub() {
        return sub;
    }

    public void setSub(String sub) {
        this.sub = sub;
    }

    public String getAud() {
        return aud;
    }

    public void setAud(String aud) {
        this.aud = aud;
    }

    public String getIss() {
        return iss;
    }

    public void setIss(String iss) {
        this.iss = iss;
    }

    public long getExp() {
        return exp;
    }

    public void setExp(long exp) {
        this.exp = exp;
    }

    public long getIat() {
        return iat;
    }

    public void setIat(long iat) {
        this.iat = iat;
    }
}
