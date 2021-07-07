package ru.vinigret.limit.usluga;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import ru.vinigret.limit.service.PageModel;
import ru.vinigret.limit.urlico.uchetul.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.security.Principal;
import java.util.ArrayList;

@Controller
public class UslugaController {

    @Autowired
    private UchetULRepository uchrRepository;

    @Autowired
    private UchetULService uchetULService;

    @Autowired
    private UchetULMapper uchetULMapper;

    @Autowired
    private PageModel pageModel;

    @GetMapping(value = { "uslugi/list"})
    public String uslugiList(Model model, HttpServletRequest request) {
        ListDTO listForm = new ListDTO();
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

    @GetMapping(value = { "uslugi/list/update"}, headers = "x-requested-with=XMLHttpRequest")
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
        return "uchrlist :: fulllistUchr"; //fragments/sections :: listUchr
    }

    @PostMapping(value = { "uslugi/list"})
    public String uslugiList(@ModelAttribute("listForm") @Valid ListDTO listForm, BindingResult bindingResult, Model model, HttpServletRequest request) {
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

    @GetMapping(value = {"/uslugi/add","uslugi/edit/{id}"})
    public String editZap(Model model, HttpServletRequest request, @PathVariable(required = false) final Integer id){
        UchetULDTO userForm = (id != null) ? uchetULMapper.toDto(uchrRepository.findById(id).orElse(null)) : new UchetULDTO();
        userForm.setReturnhttp("/uchr/list");
        model.addAttribute("userForm", userForm);
        model.addAttribute("listUchredit", uchrRepository.findAllByGrbsIsTrue());
        model.addAttribute("listParent", uchrRepository.findAllByGroupUchrIsTrue());
        model.addAttribute("admview", request.isUserInRole("ROLE_ADMIN"));
        return "uchrform";
    }


    @PostMapping(value ={"/uslugi/save"})
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

    @PostMapping(value ={"/uslugi/delete"}, headers = "x-requested-with=XMLHttpRequest")
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
