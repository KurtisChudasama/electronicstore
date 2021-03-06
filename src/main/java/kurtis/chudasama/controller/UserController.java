package kurtis.chudasama.controller;

import kurtis.chudasama.entity.Cart;
import kurtis.chudasama.entity.User;
import kurtis.chudasama.service.CartService;
import kurtis.chudasama.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;

@Controller
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private CartService cartService;

    @RequestMapping(value = {"/"}, method=RequestMethod.GET)
    public ModelAndView index() {
        ModelAndView model = new ModelAndView();

        model.setViewName("index");
        return model;
    }

    @RequestMapping(value = {"/login"}, method= RequestMethod.GET)
    public ModelAndView login() {
        ModelAndView model = new ModelAndView();

        model.setViewName("login");
        return model;
    }

    @RequestMapping(value = {"/register"}, method=RequestMethod.GET)
    public ModelAndView register() {
        ModelAndView model = new ModelAndView();

        User user = new User();
        model.addObject("user", user);
        model.setViewName("register");

        return model;
    }

    @RequestMapping(value = {"/register"}, method=RequestMethod.POST)
    public ModelAndView createCustomer(@Valid @ModelAttribute("user") User user, BindingResult bindingResult) {
        ModelAndView model = new ModelAndView();
        User userExists = userService.findUserByUsername(user.getUsername());

        if (userExists != null) {
            bindingResult.rejectValue("username", "error.user");
        }
        if (bindingResult.hasErrors()) {
            model.setViewName("register");
            String errorMessage = "";
            model.addObject("errorMessage", errorMessage);
        }
        else {
            userService.saveCustomer(user);

            Cart cart = new Cart();
            cart.setUser(user);
            cartService.saveCart(cart);

            String successMessage = "";
            model.addObject("successMessage", successMessage);
            model.addObject("user", new User());
            model.setViewName("register");
        }

        return model;
    }

    @RequestMapping(value = {"/homepage"}, method=RequestMethod.GET)
    @ResponseBody
    public ModelAndView homepage() {

        ModelAndView model = new ModelAndView();
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findUserByUsername(auth.getName());

        model.addObject("username", user.getUsername());
        model.setViewName("/homepage");
        return model;
    }

    @RequestMapping(value = {"/access_denied"}, method=RequestMethod.GET)
    public ModelAndView accessDenied() {
        ModelAndView model = new ModelAndView();
        model.setViewName("errors/access_denied");
        return model;
    }

    @GetMapping("/homepage/viewusers")
    public ModelAndView listUsers(@RequestParam(defaultValue = "") String name) {
        ModelAndView model = new ModelAndView();

        model.addObject("users", userService.findByUsernameLike(name));
        model.setViewName("user_list");
        return model;
    }

    @GetMapping("/homepage/history/{id}")
    public ModelAndView orderHistory(@PathVariable("id") int id) {
        ModelAndView model = new ModelAndView();
        User user = userService.findUserById(id);

        model.addObject("username", user.getUsername());
        model.addObject("orders", user.getOrders());
        model.setViewName("history");

        return model;
    }
}
