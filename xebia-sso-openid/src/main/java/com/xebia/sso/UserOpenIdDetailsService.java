/**
 * Xebia
 */
package com.xebia.sso;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.AuthenticationUserDetailsService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.openid.OpenIDAttribute;
import org.springframework.security.openid.OpenIDAuthenticationToken;

/**
 * @author "Issam EL FATMI"
 */

/**
 * UserOpenIdDetailsService which accepts any OpenID user, "registering" new
 * users in a map so they can be welcomed back to the site on subsequent logins.
 */
public class UserOpenIdDetailsService implements UserDetailsService, AuthenticationUserDetailsService<OpenIDAuthenticationToken> {
    private static final Logger logger = LoggerFactory.getLogger(UserOpenIdDetailsService.class);

    private final Map<String, UserOpenIdDetails> registeredUsers = new HashMap<String, UserOpenIdDetails>();

    private static final List<GrantedAuthority> DEFAULT_AUTHORITIES = AuthorityUtils.createAuthorityList("ROLE_USER");

    /**
     * Implementation of {@code UserDetailsService}. We only need this to
     * satisfy the {@code RememberMeServices} requirements.
     */
    @Override
    public UserDetails loadUserByUsername(String id) throws UsernameNotFoundException {
        UserDetails user = registeredUsers.get(id);
        if (user == null) {
            throw new UsernameNotFoundException(id);
        }
        return user;
    }

    /**
     * Implementation of {@code AuthenticationUserDetailsService} which allows
     * full access to the submitted {@code Authentication} object. Used by the
     * OpenIDAuthenticationProvider.
     */
    @Override
    public UserDetails loadUserDetails(OpenIDAuthenticationToken token) {
        String id = token.getIdentityUrl();
        UserOpenIdDetails user = registeredUsers.get(id);
        if (user != null) {
            return user;
        }
        String email = null;
        String name = null;
        List<OpenIDAttribute> attributes = token.getAttributes();
        for (OpenIDAttribute attribute : attributes) {
            if (attribute.getName().equals("email")) {
                email = attribute.getValues().get(0);
            }
            if (attribute.getName().equals("name")) {
                name = attribute.getValues().get(0);
            }
        }

        if (logger.isDebugEnabled()) {
            logger.debug("OpenID Token: {}", id);
            logger.debug("Name: {}", name);
            logger.debug("Email: {}", email);
        }
        user = new UserOpenIdDetails(id, DEFAULT_AUTHORITIES);
        user.setEmail(email);
        user.setName(name);
        registeredUsers.put(id, user);
        return user;
    }
}
