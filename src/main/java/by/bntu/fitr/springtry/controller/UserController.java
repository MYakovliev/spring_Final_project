package by.bntu.fitr.springtry.controller;

import by.bntu.fitr.springtry.data.*;
import by.bntu.fitr.springtry.entity.User;
import by.bntu.fitr.springtry.service.ServiceException;
import by.bntu.fitr.springtry.service.UserService;
import by.bntu.fitr.springtry.util.*;
import by.bntu.fitr.springtry.util.SessionAttribute;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Locale;

@Controller
public class UserController {
    private static final Logger logger = LogManager.getLogger();
    private static final String DEFAULT_REDIRECT = "redirect:/main";
    @Autowired
    private FileSaver fileSaver;
    @Autowired
    private UserService userService;

    @GetMapping("/login")
    public ModelAndView toLogin(ModelAndView modelAndView) {
        modelAndView.setViewName(JspPath.LOGIN);
        return modelAndView;
    }

    @GetMapping("/signup")
    public ModelAndView toRegistration(ModelAndView modelAndView) {
        modelAndView.setViewName(JspPath.REGISTRATION);
        return modelAndView;
    }

    @PostMapping("/do_signin")
    public String doLogin(LoginData loginData, HttpSession session) {
        final User logedin = userService.login(loginData.getLogin(), loginData.getPassword());
        session.setAttribute(SessionAttribute.USER, logedin);
        return "redirect:/main";
    }

    //todo add implementation
    @PostMapping("/do_signup")
    public String doRegister(RegisterData registerData, HttpSession session) {

        return "redirect:/main";
    }

    //todo add implementation
    @PostMapping("/do_change_user_data")
    public String doChangeUserData(UserChangeData changeData, HttpSession session) throws IOException {
        logger.info("part:{}", changeData.getAvatar().getOriginalFilename());
        final String s = fileSaver.saveFile(changeData.getAvatar());
        logger.info(s);
        return "redirect:/main";
    }

    //todo add implementation
    @PostMapping("/do_change_user_password")
    public String doChangeUserPassword(PasswordChangeData changeData, HttpSession session) {

        return "redirect:/main";
    }

    //todo add implementation
    @PostMapping("/do_pay")
    public String doPay(PayData payData, HttpSession session) {

        return "redirect:/main";
    }

    //todo add implementation
    @PostMapping("/make_bid")
    public String makeBid(PayData payData, HttpSession session) {

        return "redirect:/main";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        if (session != null) {
            User user = (User) session.getAttribute(SessionAttribute.USER);
            if (user != null) {
                session.invalidate();
            }
        }
        return "redirect:/main";
    }

    @GetMapping("/change_language")
    public String changeLanguage(@RequestParam("lang") String language, HttpSession session) {
        if (Language.contains(language)) {
            session.setAttribute(SessionAttribute.LOCAL_LANG, language.toLowerCase());
        }
        return "redirect:" + session.getAttribute(SessionAttribute.CURRENT_PAGE);
    }

    @GetMapping("/to_ban")
    public String toBan(HttpSession session) {
        String result = "redirect:/main";
        if (session != null) {
            User user = (User) session.getAttribute(SessionAttribute.USER);
            if (user != null && user.isBanned()) {
                result = JspPath.BAN;
            }
        }
        return result;
    }

    @GetMapping("/pay")
    public String toPayment(HttpSession session) {
        if (session != null) {
            User user = (User) session.getAttribute(SessionAttribute.USER);
            if (user != null) {
                return JspPath.PAYMENT;
            }
        }
        return "redirect:/main";
    }

    @GetMapping("/user/{id}")
    public ModelAndView toProfile(@PathVariable("id") long id, HttpSession session, ModelAndView modelAndView) {
        try {
            final User user = userService.findUserById(id);
            modelAndView.addObject(RequestParameter.USER, user);
            modelAndView.setViewName(JspPath.PROFILE);
        } catch (ServiceException e) {
            logger.error(e);
            modelAndView.setViewName(DEFAULT_REDIRECT);
        }
        return modelAndView;
    }

    @GetMapping("/user/edit")
    public ModelAndView toUserEdit(HttpSession session, ModelAndView modelAndView){
        if (session != null){
            User user = (User) session.getAttribute(SessionAttribute.USER);
            if (user != null){
                modelAndView.setViewName(JspPath.USER_EDIT);
            } else {
                modelAndView.setViewName(DEFAULT_REDIRECT);
            }
        }
        return modelAndView;
    }
}
