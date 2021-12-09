package by.bntu.fitr.springtry.controller;

import by.bntu.fitr.springtry.data.*;
import by.bntu.fitr.springtry.entity.Lot;
import by.bntu.fitr.springtry.entity.User;
import by.bntu.fitr.springtry.entity.UserRole;
import by.bntu.fitr.springtry.service.LotService;
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

@Controller
public class UserController {
    private static final Logger logger = LogManager.getLogger();
    private static final String DEFAULT_REDIRECT = "redirect:/main";
    private static final String TO_LOT_REDIRECT = "redirect:/lot/%d";
    @Autowired
    private FileSaver fileSaver;
    @Autowired
    private UserService userService;
    @Autowired
    private LotService lotService;

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
    public ModelAndView doLogin(LoginData loginData, ModelAndView modelAndView, HttpSession session) {
        try {
            modelAndView.setViewName(DEFAULT_REDIRECT);
            final User logedin = userService.login(loginData.getLogin(), loginData.getPassword());
            session.setAttribute(SessionAttribute.USER, logedin);
        } catch (ServiceException e) {
            modelAndView.addObject(RequestParameter.ERROR, e.getMessage());
            modelAndView.setViewName(JspPath.LOGIN);
        }
        return modelAndView;
    }

    @PostMapping("/do_signup")
    public ModelAndView doRegister(@ModelAttribute RegisterData registerData, ModelAndView modelAndView, HttpSession session) {
        try {
            modelAndView.setViewName(DEFAULT_REDIRECT);
            UserRole role = UserRole.valueOf(registerData.getRole().toUpperCase());
            User registered = userService.register(registerData.getName(), registerData.getMail(), registerData.getLogin(), registerData.getPassword(), role);
            session.setAttribute(SessionAttribute.USER, registered);
        } catch (ServiceException e) {
            modelAndView.addObject(RequestParameter.ERROR, e.getMessage());
            modelAndView.setViewName(JspPath.REGISTRATION);
        }
        return modelAndView;
    }

    @PostMapping("/do_change_user_data")
    public ModelAndView doChangeUserData(UserChangeData changeData, ModelAndView modelAndView, HttpSession session) throws IOException {
        try {
            modelAndView.setViewName(DEFAULT_REDIRECT);
            if (session != null) {
                User user = (User) session.getAttribute(SessionAttribute.USER);
                if (user != null) {
                    String avatar = user.getAvatar();
                    if (!changeData.getAvatar().isEmpty()) {
                        avatar = fileSaver.saveFile(changeData.getAvatar());
                    }
                    User changeUserData = userService.changeUserData(user.getId(), avatar, changeData.getName(), changeData.getMail());
                    session.setAttribute(SessionAttribute.USER, changeUserData);
                }
            }
        } catch (ServiceException e) {
            modelAndView.addObject(RequestParameter.ERROR, e.getMessage());
            modelAndView.setViewName(JspPath.USER_EDIT);
        }
        return modelAndView;
    }

    @PostMapping("/do_change_user_password")
    public ModelAndView doChangeUserPassword(PasswordChangeData changeData, ModelAndView modelAndView, HttpSession session) {
        try {
            modelAndView.setViewName(DEFAULT_REDIRECT);
            if (session != null) {
                User user = (User) session.getAttribute(SessionAttribute.USER);
                if (user != null) {
                    User changeUserPassword = userService.changeUserPassword(user.getId(), changeData.getOldPassword(), changeData.getNewPassword());
                    session.setAttribute(SessionAttribute.USER, changeUserPassword);
                }
            }
        } catch (ServiceException e) {
            modelAndView.setViewName(JspPath.USER_EDIT);
            modelAndView.addObject(RequestParameter.ERROR, e.getMessage());
        }
        return modelAndView;
    }

    @PostMapping("/do_pay")
    public ModelAndView doPay(PayData payData, ModelAndView modelAndView, HttpSession session) {
        try {
            modelAndView.setViewName(DEFAULT_REDIRECT);
            if (session != null) {
                User user = (User) session.getAttribute(SessionAttribute.USER);
                if (user != null && (user.getUserRole() == UserRole.BUYER || user.getUserRole() == UserRole.SELLER)) {
                    userService.addBalance(user.getId(), payData.getBid());
                }
            }
        } catch (ServiceException e) {
            modelAndView.setViewName(JspPath.PAYMENT);
            modelAndView.addObject(RequestParameter.ERROR, e.getMessage());
        }
        return modelAndView;
    }

    @PostMapping("/make_bid")
    public ModelAndView makeBid(PayData payData, ModelAndView modelAndView, HttpSession session) {
        try {
            modelAndView.setViewName(String.format(TO_LOT_REDIRECT, payData.getLotId()));
            if (session != null){
                User user = (User) session.getAttribute(SessionAttribute.USER);
                if (user != null && user.getUserRole() == UserRole.BUYER){
                    Lot lot = lotService.findLotById(payData.getLotId());
                    userService.makeBid(user, payData.getBid(), lot);
                }
            }
        } catch (ServiceException e) {
            modelAndView.addObject(RequestParameter.ERROR, e.getMessage());
        }
        return modelAndView;
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
            if (user != null && (user.getUserRole() == UserRole.BUYER || user.getUserRole() == UserRole.SELLER)) {
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
    public ModelAndView toUserEdit(HttpSession session, ModelAndView modelAndView) {
        if (session != null) {
            User user = (User) session.getAttribute(SessionAttribute.USER);
            if (user != null) {
                modelAndView.setViewName(JspPath.USER_EDIT);
            } else {
                modelAndView.setViewName(DEFAULT_REDIRECT);
            }
        }
        return modelAndView;
    }
}
