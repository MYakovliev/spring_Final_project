package by.bntu.fitr.springtry.controller;

import by.bntu.fitr.springtry.data.LotData;
import by.bntu.fitr.springtry.entity.Lot;
import by.bntu.fitr.springtry.entity.User;
import by.bntu.fitr.springtry.entity.UserRole;
import by.bntu.fitr.springtry.service.LotService;
import by.bntu.fitr.springtry.util.JspPath;
import by.bntu.fitr.springtry.util.RequestParameter;
import by.bntu.fitr.springtry.util.SessionAttribute;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpSession;
import javax.swing.*;
import java.util.List;

@Controller
public class LotController {
    private static final Logger logger = LogManager.getLogger();
    private static final String DEFAULT_REDIRECT = "redirect:/main";

    @Autowired
    private LotService lotService;

    //todo add paging and search functions and people
    @GetMapping({"/main", "/"})
    public ModelAndView toMain(@RequestParam(value = "page", required = false, defaultValue = "1") int page,
                               @RequestParam(value = "amount", required = false, defaultValue = "20") int pageAmount,
                               ModelAndView modelAndView) {
        modelAndView.setViewName("lots");
        final List<Lot> all = lotService.findAll(page, pageAmount);
        modelAndView.addObject("lots", all);
        return modelAndView;
    }

    //todo add implementation
    @PostMapping("/new_lot")
    public ModelAndView addLot(LotData lotData, HttpSession session) {
        ModelAndView modelAndView = new ModelAndView(DEFAULT_REDIRECT);

        return modelAndView;
    }

    @GetMapping("/lot/{id}")
    public ModelAndView findLot(@PathVariable("id") long id, ModelAndView modelAndView) {
        modelAndView.setViewName(JspPath.LOT);
        final Lot lot = lotService.findLotById(id);
        modelAndView.addObject(RequestParameter.LOT, lot);
        return modelAndView;
    }

    @GetMapping("/lot/edit")
    public ModelAndView toLotEdit(HttpSession session, ModelAndView modelAndView){
        modelAndView.setViewName(DEFAULT_REDIRECT);
        if (session != null){
            User user = (User) session.getAttribute(SessionAttribute.USER);
            if (user != null && user.getUserRole() == UserRole.SELLER){
                modelAndView.setViewName(JspPath.LOT_EDIT);
            }
        }
        return modelAndView;
    }
}
