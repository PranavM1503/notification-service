package com.assignment.notification.filters;

import org.springframework.security.web.authentication.preauth.AbstractPreAuthenticatedProcessingFilter;

import javax.servlet.http.HttpServletRequest;

public class PreAuthFilter extends AbstractPreAuthenticatedProcessingFilter {

    private String requestHeader;

    public PreAuthFilter(String requestHeader) {
        this.requestHeader = requestHeader;
    }

    @Override
    protected Object getPreAuthenticatedPrincipal(HttpServletRequest request) {
        return request.getHeader(requestHeader);
    }

    @Override
    protected Object getPreAuthenticatedCredentials(HttpServletRequest request) {
        return "N/A";
    }
}
