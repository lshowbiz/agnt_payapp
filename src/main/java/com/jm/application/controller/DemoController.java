package com.jm.application.controller;

import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.jm.application.entity.TestDEMO;
import com.jm.application.service.TestDemoService;

@Controller
@RequestMapping("/demo")
public class DemoController {

	private static final Logger logger = Logger.getLogger(DemoController.class);

	@Resource(name = "demoService")
	private TestDemoService demoService;

	/**
	 * 访问url: /demo/${id}
	 * 如果是 @RequestMapping("/{id}.do") 
	 *  则访问url: /{id}.do
	 * @param id
	 * @param model
	 * @return
	 */
	@RequestMapping("/{id}")
	public String demo01(@PathVariable("id") Long id, Model model) {
		logger.info("demo01  id==" + id);
		TestDEMO demo = demoService.getById(id);
		model.addAttribute("demo", demo);
		return "/views/demo";
	}

	@RequestMapping("/findall")
	public String handleList(Model model) {
		logger.info("demo02  handleList");
		List<TestDEMO> list = demoService.findAll();
		model.addAttribute("results", list);
		return "/views/testDemoItems";
	}

	@RequestMapping(value = "/xml/write/{id}.xml", produces = { MediaType.APPLICATION_XML_VALUE
			+ ";charset=UTF-8" })
	@ResponseBody
	@ResponseStatus(HttpStatus.OK)
	public TestDEMO handleWriteXML(@PathVariable("id") Long id, Model model) {
		logger.info("handleWriteXML  id==" + id);
		TestDEMO demo = demoService.getById(id);
		return demo;
	}

	@RequestMapping(value = "/json/write/{id}.json", produces = { MediaType.APPLICATION_JSON_VALUE
			+ ";charset=UTF-8" })
	@ResponseBody
	@ResponseStatus(HttpStatus.OK)
	public TestDEMO handleWriteJSON(@PathVariable("id") Long id, Model model) {
		logger.info("handleWriteJSON  id==" + id);
		TestDEMO demo = demoService.getById(id);
		return demo;
	}
	
	@RequestMapping(value = "/save",method = RequestMethod.POST)
	public String handleSave(HttpServletRequest request,Model model,RedirectAttributes redirect){
		logger.info("==========handleSave==============");
		String name = request.getParameter("name");
		String age =  request.getParameter("age");
		TestDEMO demo =  new TestDEMO();
		demo.setAge(Integer.parseInt(age));
		demo.setName(name);
		demoService.save(demo);
		
		model.addAttribute("demo", demo);
		model.addAttribute("id", demo.getId());
		
		return "redirect:/demo/findall";
		
	}
	
	@RequestMapping(value = "/showForm")
	public String showForm(Model model) {
		logger.info("==========showForm==============");
		//demoService.execfunction();
		return "/views/testDemoForm";
		
	}
	
}
