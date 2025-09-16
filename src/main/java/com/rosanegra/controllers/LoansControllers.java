package com.rosanegra.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.Map;

@RestController
@RequestMapping("/loans")
public class LoansControllers {

    @GetMapping
    public Map<String,String> loans(){
        return Collections.singletonMap("msj","loans");
    }
}
