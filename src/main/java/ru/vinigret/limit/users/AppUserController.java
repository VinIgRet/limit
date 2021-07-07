package ru.vinigret.limit.users;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;
import ru.vinigret.limit.service.PageModel;
import ru.vinigret.limit.urlico.uchetul.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.security.Principal;
import java.util.Objects;

@Controller
public class AppUserController {

    @Autowired
    private AppUserService appUserService;

    @Autowired
    private AppUserRepository appUserRepository;

    @Autowired
    private UchetULRepository uchetULRepository;

    @Autowired
    private AppUserMapper appUserMapper;



//

    @GetMapping(value = { "users/list"})
    public String usersList(@ModelAttribute("listForm") @Valid ListDTO listForm, Model model, Principal principal, HttpServletRequest request, HttpServletResponse response) {
        appUserService.listSetting(listForm, model, principal, request, response);
        return "userslist";
    }

    @GetMapping(value = { "users/list/update"}, headers = "x-requested-with=XMLHttpRequest")
    public String updateList(@ModelAttribute("listForm") @Valid ListDTO listForm, Model model, Principal principal, HttpServletRequest request, HttpServletResponse response) {
        appUserService.listSetting(listForm, model, principal, request, response);
        return "userslist :: fulllistUser";
    }

    @GetMapping("/users/add")
    public String editZap(Model model, Principal principal, HttpServletRequest request, HttpServletResponse response){
        AppUserDTO userForm =  new AppUserDTO();
        userForm.setAction(".reg");
        model.addAttribute("userForm", userForm);
        appUserService.formSetting(userForm, model, principal, request, response);
        userForm.setReturnhttp("/users/list");
        return "profile";
    }

    @GetMapping("/users/edit/{id}")
    public String editZap(@PathVariable(required = true) final Integer id, Model model, Principal principal, HttpServletRequest request, HttpServletResponse response){
        AppUserDTO userForm = (id != 0) ? appUserMapper.toDto(appUserRepository.findById(id).orElse(null)) : new AppUserDTO();
        model.addAttribute("userForm", userForm);
        appUserService.formSetting(userForm, model, principal, request, response);
        userForm.setReturnhttp("/users/list");
        return "profile";
    }

    @GetMapping("/profile")
    public String profileUser(Model model, Principal principal, HttpServletRequest request, HttpServletResponse response) {
        AppUserDTO userForm = appUserMapper.toDto(appUserRepository.getByLoginEquals(principal.getName()).orElse(null));
        model.addAttribute("userForm", userForm);
        appUserService.formSetting(userForm, model, principal, request, response);
        userForm.setReturnhttp("/desktop");
        return "profile";
    }

    @PostMapping("/users/add")
    public String addZap(@ModelAttribute("userForm") @Valid AppUserDTO userForm, Model model, BindingResult bindingResult, Principal principal, HttpServletRequest request, HttpServletResponse response){
        appUserService.saveUser(userForm, bindingResult, model, principal, request, response);
        return "redirect:/users/list";
    }

    @PostMapping("users/edit/{id}")
    public String saveEditZap(@ModelAttribute("userForm") @Valid AppUserDTO userForm, Model model, BindingResult bindingResult, Principal principal, HttpServletRequest request, HttpServletResponse response){
        appUserService.saveUser(userForm, bindingResult, model, principal, request, response);
        return "profile";
    }

    @PostMapping("/profile")
    public String saveProfile(@ModelAttribute("userForm") @Valid AppUserDTO userForm, BindingResult bindingResult, Model model, Principal principal, HttpServletRequest request, HttpServletResponse response) {
        appUserService.saveUser(userForm, bindingResult, model, principal, request, response);
        return "profile";
    }

    @PostMapping(value ={"/users/delete"}, headers = "x-requested-with=XMLHttpRequest")
    public String delZap(@ModelAttribute("listForm") @Valid ListDTO listForm, BindingResult bindingResult, Model model, Principal principal, HttpServletRequest request, HttpServletResponse response){
        if (!bindingResult.hasErrors()) {
            if ("ask".equals(listForm.getA())){
                listForm.setA("delete");
                return "userslist ::listDelAsk";
            } else if ("delete".equals(listForm.getA())) {
                appUserRepository.deleteAllById(listForm.getArray());
                appUserService.listSetting(listForm, model, principal, request, response);
                response.setStatus(HttpServletResponse.SC_ACCEPTED);
                return "userslist :: fulllistUser";
            }
        } else {
            response.setStatus(HttpServletResponse.SC_NOT_MODIFIED);
            return "error";
        }
        return "error";
    }

























/*
    @GetMapping(value = { "/users"})
    public String userList(Model model, HttpServletRequest request) {
        pageModel.initPageAndSize();
        Page<AppUser> pageAppUser = appUserRepository.findAll(PageRequest.of(pageModel.getPAGE(), pageModel.getSIZE()));
        model.addAttribute("zapPage", pageAppUser);
        ArrayList<Integer> listPages = PageModel.getPagList(pageAppUser.getTotalPages(), pageModel.getPAGE());
        model.addAttribute("listPage", listPages);
        model.addAttribute("currentPage", pageModel.getPAGE());
        model.addAttribute("admview", request.isUserInRole("ROLE_ADMIN"));
        return "users";
    }
*/

/*
    @GetMapping("/profile")
    public String profileUser(Model model, Principal principal, HttpServletRequest request) {
        AppUser  appUser = appUserRepository.getByLoginEquals(principal.getName());
        AppUserDTO userForm = new AppUserDTO();
        userForm.setReturnhttp("/desktop");
        model.addAttribute("userForm", userForm);
        model.addAttribute("selfedit", true);
        model.addAttribute("admview", request.isUserInRole("ROLE_ADMIN"));
        return "profile";
    }

    @GetMapping(value ={"/users/{id}"})
    public String profileUser(@PathVariable(name = "id" , required = true) Integer id, Model model, Principal principal, HttpServletRequest request) {
        AppUser  appUser = appUserRepository.getById(id);
        AppUserDTO userForm = new AppUserDTO();
        userForm.setReturnhttp("/users");
        model.addAttribute("userForm", userForm);
        model.addAttribute("selfedit", principal.getName().equals(appUser.getLogin()));
        model.addAttribute("admview", request.isUserInRole("ROLE_ADMIN"));
        model.addAttribute("listUchr", uchetULRepository.findAll());
         return "profile";
    }
*/
    /*
    @PostMapping("/profile")
    public String saveUser(@ModelAttribute("userForm") @Valid AppUserDTO userForm, BindingResult bindingResult, Model model, Principal principal, HttpServletRequest request) {
        AppUser  appUser = (!".reg".equals(userForm.getAction())) ? appUserRepository.getByLoginAndId(userForm.getLogin(),userForm.getId()) : new AppUser();
        model.addAttribute("selfedit", principal.getName().equals(appUser.getLogin()));
        model.addAttribute("admview", request.isUserInRole("ROLE_ADMIN"));
        if (!".reg".equals(userForm.getAction())) {
            if (appUser == null || (!appUser.getLogin().equals(principal.getName()) && !request.isUserInRole("ROLE_ADMIN"))) {
                bindingResult.addError(new ObjectError("globalError", "Вам не доступно изменение данных"));
                return "profile";
            }
        }
        if (".reg".equals(userForm.getAction())) {
            if(!userForm.getLogin().isEmpty()) {
                if (appUserRepository.existsByLoginEquals(userForm.getLogin())) {
                    bindingResult.addError(new FieldError("userForm", "login", "Пользователь с таким логином существует"));
                }
            }
            if(!userForm.getEmail().isEmpty()) {
                if (appUserRepository.existsByEmailEquals(userForm.getEmail())) {
                    bindingResult.addError(new FieldError("userForm", "email", "Пользователь с таким E-mail существует"));
                }
            }
        }
        if (".reg".equals(userForm.getAction()) || ".mailchng".equals(userForm.getAction())) {
            if (!userForm.getEmail().equals(userForm.getEmailConfirm())) {
                bindingResult.addError(new FieldError("userForm","emailConfirm", "E-mail не совпадают"));
            }
        }
        if (".reg".equals(userForm.getAction()) || ".passchng".equals(userForm.getAction())) {
            if (!userForm.getPassword().equals(userForm.getPasswordConfirm())) {
                    bindingResult.addError(new FieldError("userForm","passwordConfirm", "Пароли не совпадают"));
            }
        }

        if (".reg".equals(userForm.getAction())) {
            if (!bindingResult.hasErrors()) {
                //
                appUserRepository.save(appUser);
                return "/login";
            } else {
                return "/registration";
            }
        } else {
            if (".passchng".equals(userForm.getAction())) {
                if (bindingResult.getFieldErrors().stream().filter(fer -> fer.getField().contains("pass")).count() == 0) {
                    appUser.setPassword(new BCryptPasswordEncoder().encode(userForm.getPassword()));
                    appUserRepository.save(appUser);
                }
            } else if (".mailchng".equals(userForm.getAction())) {
                if (bindingResult.getFieldErrors().stream().filter(fer -> fer.getField().contains("email")).count() == 0) {
                    appUser.setEmail(userForm.getEmail());
                    appUserRepository.save(appUser);
                    userForm.setEmailOld(appUser.getEmail());
                }
            } else if (".osn".equals(userForm.getAction())) {
                if (bindingResult.getFieldErrors().stream().filter(fer -> !fer.getField().contains("pass")).filter(fer -> !fer.getField().contains("email")).count() == 0) {
                    appUser.setTitle(userForm.getTitle());
                    ///добавить что еще нужно
                    if (!appUser.getLogin().equals(principal.getName()) && request.isUserInRole("ROLE_ADMIN")) {
                        appUser.setEnabled(userForm.isEnabled());
                        appUser.setAdmin(userForm.isAdmin());
                    }
                    appUserRepository.save(appUser);
                }
            } else {
                return "error";
            }
        }
        return "profile";
    }
*/
/*
    @DeleteMapping("/users/{id}")
    public String deleteUser(@PathVariable(name = "id" , required = true) Integer id, @RequestParam String action, Model model) {
        //AppUser appUser = AppUserRepository.findById(id).orElse(null);
        //if (appUser == null) {
        //    return false;
        //}  else {
            //AppUserRepository.delete(appUser);
        //    return true;
        //}
        return "profile";
    }
*/
}
