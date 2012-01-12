/**
 * Xebia
 */
package com.xebia.sso;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.atlassian.crowd.integration.http.CrowdHttpAuthenticator;
import com.atlassian.crowd.integration.http.filter.CrowdSecurityFilter;

/**
 * @author "Issam EL FATMI"
 * @since 2012
 */
public class Login extends HttpServlet {
    private static final Logger logger = LoggerFactory.getLogger(Login.class);

    private String username;

    private String password;

    private Boolean authenticated = null;

    private CrowdHttpAuthenticator crowdHttpAuthenticator;

    /**
     * {@inheritDoc}
     */
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        getServletContext().getRequestDispatcher("/login/login.html").forward(req, resp);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        ApplicationContext applicationContext = WebApplicationContextUtils.getWebApplicationContext(this.getServletContext());
        crowdHttpAuthenticator = (CrowdHttpAuthenticator ) applicationContext.getBean("crowdHttpAuthenticator");
        username = req.getParameter("username");
        password = req.getParameter("password");

        if (username != null && !username.equals("") && password != null) {
            try {
                crowdHttpAuthenticator.authenticate(req, resp, username, password);
            } catch (Exception e) {
                logger.error(e.getMessage(), e);
            }
            HttpSession session = req.getSession(false);
            String requestingPage = (String ) session.getAttribute(CrowdSecurityFilter.ORIGINAL_URL);
            if (logger.isDebugEnabled()) {
                logger.debug("Original Requesting Page {}", requestingPage);
            }
            if (requestingPage != null) {
                resp.sendRedirect(requestingPage);
            }
        } else {
            // didn't supply authentication information, check if already
            // authenticated
            isAuthenticated(req, resp);

        }

    }

    /**
     * Checks if a user is currently authenticated to the Crowd server.
     * 
     * @return <code>true</code> if and only if the user is currently
     *         authenticated, otherwise <code>false</code>.
     */
    private boolean isAuthenticated(HttpServletRequest req, HttpServletResponse resp) {
        if (authenticated == null) {
            try {
                authenticated = crowdHttpAuthenticator.isAuthenticated(req, resp);
            } catch (Exception e) {
                logger.error(e.getMessage(), e);
                authenticated = Boolean.FALSE;
            }
        }

        return authenticated;
    }
}
