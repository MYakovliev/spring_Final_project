package by.bntu.fitr.springtry.filter;

import by.bntu.fitr.springtry.entity.User;
import by.bntu.fitr.springtry.service.UserService;
import by.bntu.fitr.springtry.util.SessionAttribute;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@Component
public class BanFilter implements Filter {
    @Autowired
    private UserService userService;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        Filter.super.init(filterConfig);
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest servletRequest = (HttpServletRequest) request;
        final HttpSession session = servletRequest.getSession(false);
        if (session != null) {
            User userFromSession = (User) session.getAttribute(SessionAttribute.USER);
            if (userFromSession != null) {
                User user = userService.findUserById(userFromSession.getId());
                session.setAttribute(SessionAttribute.USER, user);
                boolean banned = user.isBanned();
                if (banned) {
                    servletRequest.getRequestDispatcher("/to_ban").forward(request, response);
                    return;
                }
            }
        }
        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {
        Filter.super.destroy();
    }
}
