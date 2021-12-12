package by.bntu.fitr.springtry.controller;

import by.bntu.fitr.springtry.data.LotData;
import by.bntu.fitr.springtry.entity.Bid;
import by.bntu.fitr.springtry.entity.Lot;
import by.bntu.fitr.springtry.entity.User;
import by.bntu.fitr.springtry.entity.UserRole;
import by.bntu.fitr.springtry.service.LotService;
import by.bntu.fitr.springtry.service.ServiceException;
import by.bntu.fitr.springtry.util.FileSaver;
import by.bntu.fitr.springtry.util.JspPath;
import by.bntu.fitr.springtry.util.RequestParameter;
import by.bntu.fitr.springtry.util.SessionAttribute;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import java.sql.Timestamp;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Controller
public class LotController {
    private static final Logger logger = LogManager.getLogger();
    private static final String DEFAULT_REDIRECT = "redirect:/main";
    private static final int DEFAULT_AMOUNT = 5;

    @Autowired
    private LotService lotService;
    @Autowired
    private FileSaver fileSaver;

    @GetMapping({"/main", "/"})
    public ModelAndView toMain(@RequestParam(value = "page", required = false, defaultValue = "1") int page,
                               @RequestParam(value = "find_user_lot", required = false, defaultValue = "false") boolean findUserLot,
                               @RequestParam(value = "search", required = false) String search,
                               ModelAndView modelAndView, HttpSession session) {
        modelAndView.setViewName("lots");
        if (search != null && !search.isEmpty()) {
            Page<Lot> all = lotService.findLotByName(search, page, DEFAULT_AMOUNT);
            modelAndView.addObject(RequestParameter.LOT_PAGE_AMOUNT, all.getTotalPages());
            modelAndView.addObject(RequestParameter.LOT_ACTIVE_PAGE, page);
            modelAndView.addObject(RequestParameter.LOT_LIST, all.getContent());
        } else if (findUserLot) {
            if (session != null) {
                User user = (User) session.getAttribute(SessionAttribute.USER);
                if (user != null) {
                    Page<Lot> all = new PageImpl<>(new ArrayList<>());
                    if (user.getUserRole() == UserRole.BUYER) {
                        all = lotService.findLotByBuyerId(user, page, DEFAULT_AMOUNT);
                    } else if (user.getUserRole() == UserRole.SELLER) {
                        all = lotService.findLotBySellerId(user, page, DEFAULT_AMOUNT);
                    }
                    modelAndView.addObject(RequestParameter.LOT_PAGE_AMOUNT, all.getTotalPages());
                    modelAndView.addObject(RequestParameter.LOT_ACTIVE_PAGE, page);
                    modelAndView.addObject(RequestParameter.LOT_LIST, all.getContent());
                }
            }
        } else {
            Page<Lot> all = lotService.findActive(page, DEFAULT_AMOUNT);
            modelAndView.addObject(RequestParameter.LOT_PAGE_AMOUNT, all.getTotalPages());
            modelAndView.addObject(RequestParameter.LOT_ACTIVE_PAGE, page);
            modelAndView.addObject(RequestParameter.LOT_LIST, all.getContent());
        }
        return modelAndView;
    }

    @PostMapping("/new_lot")
    public ModelAndView addLot(LotData lotData, HttpSession session) {
        ModelAndView modelAndView = new ModelAndView(DEFAULT_REDIRECT);
        try {
            if (session != null) {
                User user = (User) session.getAttribute(SessionAttribute.USER);
                if (user != null && user.getUserRole() == UserRole.SELLER) {
                    List<MultipartFile> pictures = lotData.getImages();
                    Timestamp startTime = null;
                    Timestamp finishTime = null;
                    String startTimeString = lotData.getStartTime();
                    String finishTimeString = lotData.getFinishTime();
                    if (!(startTimeString.isEmpty() || startTimeString.isBlank())) {
                        startTime = Timestamp.valueOf(startTimeString.replace("T", " ") + ":00");
                    }
                    if (!(finishTimeString.isEmpty() || finishTimeString.isBlank())) {
                        finishTime = Timestamp.valueOf(finishTimeString.replace("T", " ") + ":00");
                    }
                    List<String> filePaths = uploadLotImages(pictures);
                    lotService.createNewLot(lotData.getName(), lotData.getDescription(), lotData.getStartBid(),
                            startTime, finishTime, user, filePaths);
                }
            }
        } catch (ServiceException e) {
            modelAndView.setViewName(JspPath.LOT_EDIT);
            modelAndView.addObject(RequestParameter.ERROR, e.getMessage());
        }
        return modelAndView;
    }

    private List<String> uploadLotImages(List<MultipartFile> files) {
        List<String> pictures = new ArrayList<>();
        for (MultipartFile file : files) {
            try {
                if (!file.isEmpty()) {
                    String filePath = fileSaver.saveFile(file);
                    pictures.add(filePath);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return pictures;
    }

    @GetMapping("/lot/{id}")
    public ModelAndView findLot(@PathVariable("id") long id, ModelAndView modelAndView) {
        modelAndView.setViewName(JspPath.LOT);
        Lot lot = lotService.findLotById(id);
        logger.info("lot found:{}", lot);
        boolean submitted = lotService.isLotSubmitted(lot.getId());
        logger.info("is lot submitted:{}", submitted);
        modelAndView.addObject(RequestParameter.SUBMITTED, submitted);
        modelAndView.addObject(RequestParameter.USER_LIST, lot.getBidHistory().stream().map(Bid::getBuyer).collect(Collectors.toList()));
        modelAndView.addObject(RequestParameter.LOT, lot);
        return modelAndView;
    }

    @GetMapping("/lot/edit")
    public ModelAndView toLotEdit(HttpSession session, ModelAndView modelAndView) {
        modelAndView.setViewName(DEFAULT_REDIRECT);
        if (session != null) {
            User user = (User) session.getAttribute(SessionAttribute.USER);
            if (user != null && user.getUserRole() == UserRole.SELLER) {
                modelAndView.setViewName(JspPath.LOT_EDIT);
            }
        }
        return modelAndView;
    }
}
