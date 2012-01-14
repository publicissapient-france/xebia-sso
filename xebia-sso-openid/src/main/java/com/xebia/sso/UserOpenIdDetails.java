/**
 * Xebia
 */
package com.xebia.sso;

import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

/**
 * @author "Issam EL FATMI"
 */
public class UserOpenIdDetails extends User {

    private String email;

    private String name;

    public UserOpenIdDetails(String username, Collection<GrantedAuthority> authorities) {
        super(username, "unused", authorities);
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
