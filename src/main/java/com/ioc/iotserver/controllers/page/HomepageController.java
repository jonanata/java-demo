package com.ioc.iotserver.controllers.page;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.ioc.iotserver.controllers.BaseController;
import com.ioc.iotserver.services.AccountService;

@Controller
@CrossOrigin(origins="*")
@RequestMapping("/")
public class HomepageController extends BaseController {
	
	@GetMapping("")
    public ModelAndView index() {

		return viewInit("page/homepage/index.html");
    }

}
