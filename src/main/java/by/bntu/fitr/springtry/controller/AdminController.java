package by.bntu.fitr.springtry.controller;

import by.bntu.fitr.springtry.entity.Lot;
import by.bntu.fitr.springtry.entity.User;
import by.bntu.fitr.springtry.entity.UserRole;
import by.bntu.fitr.springtry.service.AdminService;
import by.bntu.fitr.springtry.service.LotService;
import by.bntu.fitr.springtry.service.UserService;
import by.bntu.fitr.springtry.util.JspPath;
import by.bntu.fitr.springtry.util.RequestParameter;
import by.bntu.fitr.springtry.util.SessionAttribute;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpSession;

@Controller
public class AdminController {
    private static final String DEFAULT_REDIRECT = "redirect:/main";
    private static final String TO_ADMIN_REDIRECT = "redirect:/admin?user_active_page=%d&lot_active_page=%d";
    private static final int DEFAULT_AMOUNT = 20;
    @Autowired
    private AdminService adminService;
    @Autowired
    private LotService lotService;
    @Autowired
    private UserService userService;

    @PostMapping("/ban/{id}")
    public ModelAndView banUser(@RequestParam("user_active_page") int userActivePage,
                                @RequestParam("lot_active_page") int lotActivePage, @PathVariable("id") long userId, HttpSession session) {
        ModelAndView modelAndView = new ModelAndView(DEFAULT_REDIRECT);
        if (session != null) {
            User user = (User) session.getAttribute(SessionAttribute.USER);
            if (user != null && user.getUserRole() == UserRole.ADMIN) {
                adminService.ban(userId);
                modelAndView.setViewName(String.format(TO_ADMIN_REDIRECT
                        , userActivePage, lotActivePage));
            }
        }
        return modelAndView;
    }

    @PostMapping("/unban/{id}")
    public ModelAndView unbanUser(@RequestParam("user_active_page") int userActivePage,
                                  @RequestParam("lot_active_page") int lotActivePage, @PathVariable("id") long userId, HttpSession session) {
        ModelAndView modelAndView = new ModelAndView(DEFAULT_REDIRECT);
        if (session != null) {
            User user = (User) session.getAttribute(SessionAttribute.USER);
            if (user != null && user.getUserRole() == UserRole.ADMIN) {
                adminService.unban(userId);
                modelAndView.setViewName(String.format(TO_ADMIN_REDIRECT
                        , userActivePage, lotActivePage));
            }
        }
        return modelAndView;
    }

    @GetMapping("/admin")
    public ModelAndView toAdmin(@RequestParam(value = "user_active_page", required = false, defaultValue = "1") int userActivePage,
                                @RequestParam(value = "lot_active_page", required = false, defaultValue = "1") int lotActivePage,
                                HttpSession session, ModelAndView modelAndView) {
        modelAndView.setViewName(DEFAULT_REDIRECT);
        if (session != null) {
            User user = (User) session.getAttribute(SessionAttribute.USER);
            if (user != null && user.getUserRole() == UserRole.ADMIN) {
                modelAndView.setViewName(JspPath.ADMIN);
                Page<Lot> lotPage = lotService.findAll(lotActivePage, DEFAULT_AMOUNT);
                Page<User> userPage = userService.findAll(userActivePage, DEFAULT_AMOUNT);
                modelAndView.addObject(RequestParameter.LOT_LIST, lotPage.getContent());
                modelAndView.addObject(RequestParameter.USER_LIST, userPage.getContent());
                modelAndView.addObject(RequestParameter.LOT_PAGE_AMOUNT, lotPage.getTotalPages());
                modelAndView.addObject(RequestParameter.USER_PAGE_AMOUNT, userPage.getTotalPages());
                modelAndView.addObject(RequestParameter.LOT_ACTIVE_PAGE, lotActivePage);
                modelAndView.addObject(RequestParameter.USER_ACTIVE_PAGE, userActivePage);
            }
        }
        return modelAndView;
    }

    @GetMapping("/lot/{id}/submit")
    public ModelAndView submitWinner(@PathVariable("id") long id, HttpSession session, ModelAndView modelAndView) {
        modelAndView.setViewName(DEFAULT_REDIRECT);
        if (session != null) {
            User user = (User) session.getAttribute(SessionAttribute.USER);
            if (user != null && user.getUserRole() == UserRole.ADMIN) {
                Lot lotById = lotService.findLotById(id);
                adminService.submitWinner(lotById);
                modelAndView.setViewName("redirect:/lot/" + id);
            }
        }
        return modelAndView;
    }

}
