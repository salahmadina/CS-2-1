package com.university.eventmanagement.interceptor;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import com.university.eventmanagement.model.User;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@Component
public class AuthInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response,
                             Object handler) throws Exception {

        String url = request.getRequestURI();

        // Allow public URLs
        if (isPublicUrl(url)) {
            return true;
        }

        // Check session
        HttpSession session = request.getSession(false);
        User user = (session != null) ? (User) session.getAttribute("loggedInUser") : null;

        // Not logged in → redirect to login
        if (user == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return false;
        }

        // Student trying to access admin area → deny
        if (url.startsWith("/admin") && user.getRole() != User.Role.ADMIN) {
            response.sendRedirect(request.getContextPath() + "/student/dashboard");
            return false;
        }

        // Admin trying to access student area → deny
        if (url.startsWith("/student") && user.getRole() != User.Role.STUDENT) {
            response.sendRedirect(request.getContextPath() + "/admin/dashboard");
            return false;
        }

        return true;
    }

    private boolean isPublicUrl(String url) {
        return url.equals("/")
            || url.startsWith("/login")
            || url.startsWith("/register")
            || url.startsWith("/css")
            || url.startsWith("/js")
            || url.startsWith("/images")
            || url.startsWith("/favicon");
    }
}
