package com.ioc.iotserver.controllers.page;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.ioc.iotserver.controllers.BaseController;

@Controller
@CrossOrigin(origins="*")
@RequestMapping("/page/auth/dashboard")
public class DashboardController extends BaseController {

	@GetMapping("")
    public ModelAndView dashboard() {

		return viewInit("/page/dashboard/index.html");
    }
	
}
