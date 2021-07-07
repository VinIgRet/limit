package ru.vinigret.limit.users;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.ModelAttribute;
import ru.vinigret.limit.service.PageModel;
import ru.vinigret.limit.urlico.uchetul.ListDTO;
import ru.vinigret.limit.urlico.uchetul.UchetUL;
import ru.vinigret.limit.urlico.uchetul.UchetULRepository;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.security.Principal;
import java.util.*;

@Service
public class AppUserService implements UserDetailsService {

    @Autowired
    private AppUserRepository userRepository;

    @Autowired
    private UchetULRepository uchrRepository;

    @Autowired
    private PageModel pageModel;

    @Autowired
    private AppUserMapper userMapper;

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        AppUser user = userRepository.findByLogin(s);
        if (user == null) {
            throw new UsernameNotFoundException("invalid login/password");
        } else {
            Set<SimpleGrantedAuthority> roles = new HashSet<>();
            if (user.isAdmin()) {
                roles.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
            }
            roles.add(new SimpleGrantedAuthority("ROLE_USER"));
            org.springframework.security.core.userdetails.User usr =  new org.springframework.security.core.userdetails.User(
                    user.getLogin(), user.getPassword(), user.isEnabled(),
                    true, true, true, roles
            );
            return usr;
        }
    }

    public void listSetting(ListDTO listForm, Model model, Principal principal, HttpServletRequest request, HttpServletResponse response){
        pageModel.initPageAndSize();
        Page<AppUser> pageUsers = userRepository.findAllByLoginNotEquals(principal.getName(), PageRequest.of(pageModel.getPAGE(), pageModel.getSIZE()/*, Sort.by("title")*/));
        model.addAttribute("zapPage", pageUsers);
        ArrayList<Integer> listPages = PageModel.getPagList(pageUsers.getTotalPages(), pageModel.getPAGE());
        model.addAttribute("listPage", listPages);
        listForm.setP(pageModel.getPAGE() + 1);
        listForm.setN(pageModel.getSIZE());
        model.addAttribute("currentPage", pageModel.getPAGE());
        model.addAttribute("admview", request.isUserInRole("ROLE_ADMIN"));
        model.addAttribute("listForm", listForm);
    }

    public void formSetting(AppUserDTO userForm, Model model, Principal principal, HttpServletRequest request, HttpServletResponse response){
        model.addAttribute("listUchetUL", uchrRepository.findAll());
        model.addAttribute("userForm", userForm);
        model.addAttribute("selfedit", principal.getName().equals(userForm.getLogin()));
        model.addAttribute("admview", request.isUserInRole("ROLE_ADMIN"));

    }

    public void saveUser(AppUserDTO userForm, BindingResult bindingResult, Model model, Principal principal, HttpServletRequest request, HttpServletResponse response) {
        AppUser  appUser =  (!".reg".equals(userForm.getAction())) ? userRepository.getByLoginAndId(userForm.getLogin(),userForm.getId()) : new AppUser();
        if (!request.isUserInRole("ROLE_ADMIN") && !userForm.getLogin().equals(principal.getName())) {
            bindingResult.addError(new ObjectError("globalError", "Вам не доступно изменение данных"));
        } else {
            if (".reg".equals(userForm.getAction())) {
                if (!userForm.getLogin().isEmpty()) {
                    if (userRepository.existsByLoginEquals(userForm.getLogin())) {
                        bindingResult.addError(new FieldError("userForm", "login", "Пользователь с таким логином существует"));
                    }
                }
                if (!userForm.getEmail().isEmpty()) {
                    if (userRepository.existsByEmailEquals(userForm.getEmail())) {
                        bindingResult.addError(new FieldError("userForm", "email", "Пользователь с таким E-mail существует"));
                    }
                }
            }
            if (".reg".equals(userForm.getAction()) || ".mailchng".equals(userForm.getAction())) {
                if (!userForm.getEmail().equals(userForm.getEmailConfirm())) {
                    bindingResult.addError(new FieldError("userForm", "emailConfirm", "E-mail не совпадают"));
                }
            }
            if (".reg".equals(userForm.getAction()) || ".passchng".equals(userForm.getAction())) {
                if (!userForm.getPassword().equals(userForm.getPasswordConfirm())) {
                    bindingResult.addError(new FieldError("userForm", "passwordConfirm", "Пароли не совпадают"));
                }
            }
            if (".reg".equals(userForm.getAction()) && !bindingResult.hasErrors()) {
                appUser = userMapper.toEntity(userForm);
                userRepository.save(appUser);
            } else {
                if (".passchng".equals(userForm.getAction())) {
                    if (bindingResult.getFieldErrors().stream().filter(fer -> fer.getField().contains("pass")).count() == 0) {
                        appUser.setPassword(new BCryptPasswordEncoder().encode(userForm.getPassword()));
                        userRepository.save(appUser);
                    }
                } else if (".mailchng".equals(userForm.getAction())) {
                    if (bindingResult.getFieldErrors().stream().filter(fer -> fer.getField().contains("email")).count() == 0) {
                        appUser.setEmail(userForm.getEmail());
                        userRepository.save(appUser);
                        userForm.setEmailOld(appUser.getEmail());
                    }
                } else if (".osn".equals(userForm.getAction())) {
                    if (bindingResult.getFieldErrors().stream().filter(fer -> !fer.getField().contains("pass")).filter(fer -> !fer.getField().contains("email")).count() == 0) {
                        appUser.setTitle(userForm.getTitle());
                        appUser.setUchetUL(Objects.isNull(userForm.getUchetUL()) ? null : uchrRepository.findById(userForm.getUchetUL()).orElse(null));
                        ///добавить что еще нужно
                        if (!appUser.getLogin().equals(principal.getName()) && request.isUserInRole("ROLE_ADMIN")) {
                            appUser.setEnabled(userForm.isEnabled());
                            appUser.setAdmin(userForm.isAdmin());
                        }
                        userRepository.save(appUser);
                    }
                }
            }
        }
        formSetting(userForm, model, principal, request, response);
    }


}
