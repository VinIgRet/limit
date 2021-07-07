package ru.vinigret.limit.users;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import ru.vinigret.limit.users.AppUser;
import ru.vinigret.limit.users.AppUserDTO;
import ru.vinigret.limit.users.AppUserRepository;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@Controller
public class RegistrationController {

    @Autowired
    private AppUserRepository userRepository;

    @Autowired
    private AppUserMapper appUserMapper;

    @GetMapping("/registration")
    public String registration(Model model) {
        AppUserDTO userForm = new AppUserDTO();
        userForm.setReturnhttp("/login");
        model.addAttribute("userForm", userForm);
        model.addAttribute("selfedit", true);
        model.addAttribute("admview", false);
        return "registration";
    }

    @PostMapping("/registration")
    public String addUser(HttpServletRequest request, @ModelAttribute("userForm") @Valid AppUserDTO userForm, BindingResult bindingResult, Model model) {
        if (!userForm.getPassword().equals(userForm.getPasswordConfirm())) {
            bindingResult.addError(new FieldError("userForm","password", "Пароли не совпадают"));
        }
        if(!userForm.getLogin().isEmpty()) {
            System.out.println(userRepository.existsByLoginEquals(userForm.getLogin()));
            if (userRepository.existsByLoginEquals(userForm.getLogin())){
                bindingResult.addError(new FieldError("userForm", "login", "Пользователь с таким логином существует"));
            }
        }
        if(!userForm.getEmail().isEmpty()) {
            System.out.println(userRepository.existsByEmailEquals(userForm.getEmail()));
            if (userRepository.existsByEmailEquals(userForm.getEmail())) {
                bindingResult.addError(new FieldError("userForm", "email", "Пользователь с таким E-mail существует"));
            }
        }
        if (!bindingResult.hasErrors()) {
            AppUser appUser = appUserMapper.toEntity(userForm);
            userRepository.save(appUser);
            return "/login";
        } else {
            return "/registration";
        }

        /*
        if ("XMLHttpRequest".equals(request.getHeader("X-Requested-With"))) {
            // is ajax
        } else {
            //is not ajax
        }

        @RequestMapping(value = "/verify", method = {RequestMethod.GET}, headers = "x-requested-with=XMLHttpRequest")
        * */
    }
}
