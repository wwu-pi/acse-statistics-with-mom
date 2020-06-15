package de.wwu.pi.statisticsweb.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import de.wwu.pi.statisticsweb.service.StatisticsService;

@Controller
@RequestMapping("/")
public class HomeController {

	@Autowired
	private StatisticsService statisticsService;
	
	@GetMapping
	public String index(Model model) {
		model.addAttribute("average", statisticsService.getAverage());
		model.addAttribute("median", statisticsService.getMedian());
		return "home/index";
	}

	@PostMapping
	public String index(Double x) {
		statisticsService.addStatistics(x);
		return "redirect:/";
	}
}
