package by.bntu.fitr.springtry.tag;

import by.bntu.fitr.springtry.entity.User;
import by.bntu.fitr.springtry.entity.UserRole;
import by.bntu.fitr.springtry.util.SessionAttribute;

import javax.servlet.http.HttpSession;
import javax.servlet.jsp.tagext.TagSupport;

public class RoleAccessTag extends TagSupport {
    private String role;

    public void setRole(String role) {
        this.role = role;
    }

    @Override
    public int doStartTag() {
        HttpSession session = pageContext.getSession();
        User user = (User) session.getAttribute(SessionAttribute.USER);
        UserRole userRole;
        if (user != null) {
            userRole = user.getUserRole();
        } else {
            userRole = UserRole.GUEST;
        }
        if ((UserRole.valueOf(role.toUpperCase())) == userRole) {
            return EVAL_BODY_INCLUDE;
        }
        return SKIP_BODY;
    }
}
