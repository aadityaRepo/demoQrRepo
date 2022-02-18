package com.demo.qr.demo;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;


@RestController
public class QRCodeController {
	
	private static final String QR_CODE_IMAGE_PATH = "./src/main/resources/QRCode.png";
	private final int WIDTH = 300;
	private final int HEIGHT = 300;
	private final String QR_TEXT = "Spring Boot REST API to generate QR Code - Websparrow.org";



	@GetMapping("/greeting-from-dhruv")
	public ModelAndView greeting(@RequestParam(name="name", required=false, defaultValue="World") String name, Model model) {
		ModelAndView mav = new ModelAndView("developer");
		Developer dev = new Developer();
		mav.addObject("developer", dev);
		return mav;
	}

	@PostMapping("/genrateQRCode")
	public ModelAndView QRCode() {
		ModelAndView mav = new ModelAndView("greeting");
		return mav;
	}

}
