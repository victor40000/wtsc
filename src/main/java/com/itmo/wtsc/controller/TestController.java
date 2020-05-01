package com.itmo.wtsc.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class TestController {

    @RequestMapping(value = "/hello_", method = RequestMethod.GET)
    public String findAll(Model model){
        model.addAttribute("name", "new name");
        return "hello";
    }

    @RequestMapping(value = "/hello", method = RequestMethod.GET)
    @ResponseBody
    public String hello(Model model){
        model.addAttribute("name", "new name");
        return "hello";
    }
}