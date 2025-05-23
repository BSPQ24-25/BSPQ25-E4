package com.carrental.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.server.ResponseStatusException;

import com.carrental.models.Insurance;
import com.carrental.service.InsuranceService;

import jakarta.validation.Valid;

@Controller
public class InsuranceController {
	@Autowired
	private InsuranceService insuranceService;

    @GetMapping("/admin/insurances")
    public String listAdminInsurances(Model model) {
        model.addAttribute("insurances", insuranceService.getAllInsurances());
        return "admin/insurance-management"; 
    }
    
    @GetMapping("/admin/insurances/edit/{id}")
    public String editInsurance(@PathVariable Long id, Model model) {
        Insurance insurance = insuranceService.getInsuranceById(id);
        if (insurance == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Insurance not found");
        }
        model.addAttribute("insurance", insurance);
        return "admin/insurance-form";
    }

    @PostMapping("/admin/insurances/delete/{id}")
    public String deleteInsurance(@PathVariable Long id) {
        try {
            insuranceService.deleteInsurance(id);
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Insurance not found", e);
        }
        return "redirect:/admin/insurances";
    }

    @GetMapping("/admin/insurances/new")
    public String createInsuranceForm(Model model) {
        model.addAttribute("insurance", new Insurance());
        return "admin/insurance-form"; 
    }

    @PostMapping("/admin/insurances/save")
    public String saveInsurance(@ModelAttribute("insurance") @Valid Insurance insurance, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "admin/insurance-form";
        }
        insuranceService.saveInsurance(insurance);
        return "redirect:/admin/insurances";
    }

    @GetMapping("/user/insurances")
    public String listUserInsurances(Model model) {
        model.addAttribute("insurances", insuranceService.getAllInsurances());
        return "user/insurance-view"; 
    }
}
