package ru.vinigret.limit.urlico.uchetul;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.vinigret.limit.service.PageModel;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.security.Principal;
import java.util.ArrayList;

@Controller
public class UchetULController {

    @Autowired
    private UchetULRepository uchrRepository;

    @Autowired
    private UchetULService uchetULService;

    @Autowired
    private UchetULMapper uchetULMapper;

    @Autowired
    private PageModel pageModel;


/*
    @PostMapping(value = { "uchr/list"})
    public String userList(@ModelAttribute("listForm") @Valid ListDTO listForm, BindingResult bindingResult, Model model, HttpServletRequest request) {
        pageModel.initPageAndSize(listForm);
        Page<UchetUL> pageUchetUL = uchrRepository.findAll(PageRequest.of(pageModel.getPAGE(), pageModel.getSIZE()));
        model.addAttribute("zapPage", pageUchetUL);
        ArrayList<Integer> listPages = PageModel.getPagList(pageUchetUL.getTotalPages(), pageModel.getPAGE());
        model.addAttribute("listPage", listPages);
        model.addAttribute("admview", request.isUserInRole("ROLE_ADMIN"));
        model.addAttribute("currentPage", pageModel.getPAGE());
        listForm.setP(pageModel.getPAGE() + 1);
        listForm.setN(pageModel.getSIZE());
        return "uchrlist :: fulllistUchr";
    }

 */

    @GetMapping(value = { "uchr/list"})
    public String userList(Model model, HttpServletRequest request, @ModelAttribute("listForm") @Valid ListDTO listForm) {
        pageModel.initPageAndSize();
        Page<UchetUL> pageUchetUL = uchrRepository.findAll(PageRequest.of(pageModel.getPAGE(), pageModel.getSIZE()/*, Sort.by("title")*/));
        model.addAttribute("zapPage", pageUchetUL);
        ArrayList<Integer> listPages = PageModel.getPagList(pageUchetUL.getTotalPages(), pageModel.getPAGE());
        model.addAttribute("listPage", listPages);
        listForm.setP(pageModel.getPAGE() + 1);
        listForm.setN(pageModel.getSIZE());
        model.addAttribute("currentPage", pageModel.getPAGE());
        model.addAttribute("admview", request.isUserInRole("ROLE_ADMIN"));
        model.addAttribute("listForm", listForm);
        return "uchrlist";
    }

    @GetMapping(value = { "uchr/list/update"}, headers = "x-requested-with=XMLHttpRequest")
    public String updateList(@ModelAttribute("listForm") @Valid ListDTO listForm, BindingResult bindingResult, Model model, HttpServletRequest request) {
        pageModel.initPageAndSize(listForm);
        Page<UchetUL> pageUchetUL = uchrRepository.findAll(PageRequest.of(pageModel.getPAGE(), pageModel.getSIZE()/*, Sort.by("title")*/));
        model.addAttribute("zapPage", pageUchetUL);
        ArrayList<Integer> listPages = PageModel.getPagList(pageUchetUL.getTotalPages(), pageModel.getPAGE());
        model.addAttribute("listPage", listPages);
        model.addAttribute("admview", request.isUserInRole("ROLE_ADMIN"));
        model.addAttribute("currentPage", pageModel.getPAGE());
        listForm.setP(pageModel.getPAGE() + 1);
        listForm.setN(pageModel.getSIZE());
        return "uchrlist :: fulllistUchr";
    }

    @GetMapping(value = {"/uchr/add","uchr/edit/{id}"})
    public String editZap(Model model, HttpServletRequest request, @PathVariable(required = false) final Integer id){
        UchetULDTO userForm = (id != null) ? uchetULMapper.toDto(uchrRepository.findById(id).orElse(null)) : new UchetULDTO();
        userForm.setReturnhttp("/uchr/list");
        model.addAttribute("userForm", userForm);
        model.addAttribute("listUchredit", uchrRepository.findAllByGrbsIsTrue());
        model.addAttribute("listParent", uchrRepository.findAllByGroupUchrIsTrue());
        model.addAttribute("admview", request.isUserInRole("ROLE_ADMIN"));
        return "uchrform";
    }


    @PostMapping(value ={"/uchr/save"})
    public String saveZap(@ModelAttribute("userForm") @Valid UchetULDTO userForm, BindingResult bindingResult, Model model, Principal principal, HttpServletRequest request){
        if (!bindingResult.hasErrors()) {
            UchetUL uchr = uchetULMapper.toEntity(userForm);
            if (uchr.getGrbs()){

            }
            uchrRepository.save(uchr);
        }
        model.addAttribute("admview", request.isUserInRole("ROLE_ADMIN"));
        return "uchrform";
    }

    @PostMapping(value ={"/uchr/delete"}, headers = "x-requested-with=XMLHttpRequest")
    public String delZap(@ModelAttribute("listForm") @Valid ListDTO listForm, BindingResult bindingResult, Model model, HttpServletRequest request, HttpServletResponse response){
        if (!bindingResult.hasErrors()) {
            if ("ask".equals(listForm.getA())){
                listForm.setA("delete");
                return "uchrlist ::listDelAsk";
            } else if ("delete".equals(listForm.getA())) {
                uchrRepository.deleteAllById(listForm.getArray());
                pageModel.initPageAndSize(listForm);
                Page<UchetUL> pageUchetUL = uchrRepository.findAll(PageRequest.of(pageModel.getPAGE(), pageModel.getSIZE()/*, Sort.by("title")*/));
                model.addAttribute("zapPage", pageUchetUL);
                ArrayList<Integer> listPages = PageModel.getPagList(pageUchetUL.getTotalPages(), pageModel.getPAGE());
                model.addAttribute("listPage", listPages);
                model.addAttribute("admview", request.isUserInRole("ROLE_ADMIN"));
                model.addAttribute("currentPage", pageModel.getPAGE());
                listForm.setP(pageModel.getPAGE() + 1);
                listForm.setN(pageModel.getSIZE());
                response.setStatus(HttpServletResponse.SC_ACCEPTED);
                return "uchrlist :: fulllistUchr";
            }
        } else {
            response.setStatus(HttpServletResponse.SC_NOT_MODIFIED);
            return "error";
        }
        return "error";
    }

}
