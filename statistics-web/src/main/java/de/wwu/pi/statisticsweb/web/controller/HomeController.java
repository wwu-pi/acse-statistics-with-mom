package de.wwu.pi.statisticsweb.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/")
public class HomeController {

	@GetMapping
	public String index(Model model) {
		model.addAttribute("average", 12.0);
		model.addAttribute("mean", 11.0);
		return "home/index";
	}

	@PostMapping
	public String index(Double x) {
		return "redirect:/";
	}
}
