package by.bntu.fitr.springtry.filter;

import org.springframework.stereotype.Component;
import javax.servlet.*;
import java.io.IOException;

@Component
public class CharacterSetFilter implements Filter {

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {
        req.setCharacterEncoding("UTF-8");
        res.setContentType("text/html; charset=UTF-8");
        res.setCharacterEncoding("UTF-8");
        chain.doFilter(req,res);
    }
}
