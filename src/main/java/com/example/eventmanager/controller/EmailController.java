//package com.example.eventmanager.controller;
//
//import com.example.eventmanager.service.EmailService;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//
//@RestController
//@RequestMapping("/email")
//public class EmailController {
//    private static final Logger log = LoggerFactory.getLogger(EmailController.class);
//
//    @Autowired
//    private EmailService emailService;
//
//    @GetMapping
//    public ResponseEntity<String> sendMessage() {
//        try {
//            emailService.sendMessage("f30j6au6ee@jxpomup.com", "Body text", "Header text");
//        } catch (Exception e) {
//            log.error("Problem: {}", e.getMessage());
//            return new ResponseEntity<>("Not work", HttpStatus.BAD_REQUEST);
//        }
//
//        return new ResponseEntity<>("Work", HttpStatus.OK);
//    }
//}
