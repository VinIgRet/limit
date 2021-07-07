package ru.vinigret.limit.urlico.kontrul;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.vinigret.limit.service.PageModel;
import ru.vinigret.limit.urlico.uchetul.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.security.Principal;
import java.util.ArrayList;

@Controller
public class KontrULController {

    @Autowired
    private KontrULRepository kontrRepository;

    @Autowired
    private KontrULMapper kontrULMapper;

    @Autowired
    private PageModel pageModel;

    @GetMapping(value = { "kontr/list"})
    public String userList(Model model, HttpServletRequest request) {
        ListDTO listForm = new ListDTO();
        pageModel.initPageAndSize();
        Page<KontrUL> pageKontrUL = kontrRepository.findAll(PageRequest.of(pageModel.getPAGE(), pageModel.getSIZE()/*, Sort.by("title")*/));
        model.addAttribute("zapPage", pageKontrUL);
        ArrayList<Integer> listPages = PageModel.getPagList(pageKontrUL.getTotalPages(), pageModel.getPAGE());
        model.addAttribute("listPage", listPages);
        listForm.setP(pageModel.getPAGE() + 1);
        listForm.setN(pageModel.getSIZE());
        model.addAttribute("currentPage", pageModel.getPAGE());
        model.addAttribute("admview", request.isUserInRole("ROLE_ADMIN"));
        model.addAttribute("listForm", listForm);
        return "kontrlist";
    }

    @GetMapping(value = { "kontr/list/update"}, headers = "x-requested-with=XMLHttpRequest")
    public String updateList(@ModelAttribute("listForm") @Valid ListDTO listForm, BindingResult bindingResult, Model model, HttpServletRequest request) {
        pageModel.initPageAndSize(listForm);
        Page<KontrUL> pageKontrUL = kontrRepository.findAll(PageRequest.of(pageModel.getPAGE(), pageModel.getSIZE()/*, Sort.by("title")*/));
        model.addAttribute("zapPage", pageKontrUL);
        ArrayList<Integer> listPages = PageModel.getPagList(pageKontrUL.getTotalPages(), pageModel.getPAGE());
        model.addAttribute("listPage", listPages);
        model.addAttribute("admview", request.isUserInRole("ROLE_ADMIN"));
        model.addAttribute("currentPage", pageModel.getPAGE());
        listForm.setP(pageModel.getPAGE() + 1);
        listForm.setN(pageModel.getSIZE());
        return "uchrlist :: fulllistUchr"; //fragments/sections :: listUchr
    }

    @PostMapping(value = { "kontr/list"})
    public String userList(@ModelAttribute("listForm") @Valid ListDTO listForm, BindingResult bindingResult, Model model, HttpServletRequest request) {
        pageModel.initPageAndSize(listForm);
        Page<KontrUL> pageKontrUL = kontrRepository.findAll(PageRequest.of(pageModel.getPAGE(), pageModel.getSIZE()/*, Sort.by("title")*/));
        model.addAttribute("zapPage", pageKontrUL);
        ArrayList<Integer> listPages = PageModel.getPagList(pageKontrUL.getTotalPages(), pageModel.getPAGE());
        model.addAttribute("listPage", listPages);
        model.addAttribute("admview", request.isUserInRole("ROLE_ADMIN"));
        model.addAttribute("currentPage", pageModel.getPAGE());
        listForm.setP(pageModel.getPAGE() + 1);
        listForm.setN(pageModel.getSIZE());
        return "uchrlist :: fulllistUchr";
    }

    @GetMapping(value = {"/kontr/add","kontr/edit/{id}"})
    public String editZap(Model model, HttpServletRequest request, @PathVariable(required = false) final Integer id){
        KontrULDTO userForm = (id != null) ? kontrULMapper.toDto(kontrRepository.findById(id).orElse(null)) : new KontrULDTO();
        userForm.setReturnhttp("/kontr/list");
        model.addAttribute("userForm", userForm);
        //model.addAttribute("listUchredit", kontrRepository.findAllByGrbsIsTrue());
        //model.addAttribute("listParent", kontrRepository.findAllByGroupUchrIsTrue());
        model.addAttribute("admview", request.isUserInRole("ROLE_ADMIN"));
        return "kontrform";
    }


    @PostMapping(value ={"/kontr/save"})
    public String saveZap(@ModelAttribute("userForm") @Valid KontrULDTO userForm, BindingResult bindingResult, Model model, Principal principal, HttpServletRequest request){
        if (!bindingResult.hasErrors()) {
            KontrUL kontr = kontrULMapper.toEntity(userForm);

            kontrRepository.save(kontr);
        }
        model.addAttribute("admview", request.isUserInRole("ROLE_ADMIN"));
        return "kontrform";
    }

    @PostMapping(value ={"/kontr/delete"}, headers = "x-requested-with=XMLHttpRequest")
    public String delZap(@ModelAttribute("listForm") @Valid ListDTO listForm, BindingResult bindingResult, Model model, HttpServletRequest request, HttpServletResponse response){
        if (!bindingResult.hasErrors()) {
            if ("ask".equals(listForm.getA())){
                listForm.setA("delete");
                return "kontrlist ::listDelAsk";
            } else if ("delete".equals(listForm.getA())) {
                kontrRepository.deleteAllById(listForm.getArray());
                pageModel.initPageAndSize(listForm);
                Page<KontrUL> pageKontrUL = kontrRepository.findAll(PageRequest.of(pageModel.getPAGE(), pageModel.getSIZE()/*, Sort.by("title")*/));
                model.addAttribute("zapPage", pageKontrUL);
                ArrayList<Integer> listPages = PageModel.getPagList(pageKontrUL.getTotalPages(), pageModel.getPAGE());
                model.addAttribute("listPage", listPages);
                model.addAttribute("admview", request.isUserInRole("ROLE_ADMIN"));
                model.addAttribute("currentPage", pageModel.getPAGE());
                listForm.setP(pageModel.getPAGE() + 1);
                listForm.setN(pageModel.getSIZE());
                response.setStatus(HttpServletResponse.SC_ACCEPTED);
                return "kontrlist :: fulllistKontr";
            }
        } else {
            response.setStatus(HttpServletResponse.SC_NOT_MODIFIED);
            return "error";
        }
        return "error";
    }
}
