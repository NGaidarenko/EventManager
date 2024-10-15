//package com.example.eventmanager.controller;
//
//import com.example.eventmanager.domain.MessageRequest;
//import com.example.eventmanager.kafka.KafkaProducer;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestBody;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//
//@RestController
//@RequestMapping("/message")
//public class MessageController {
//    @Autowired
//    private KafkaProducer kafkaProducer;
//
//    @PostMapping
//    public void sendMessage(@RequestBody MessageRequest messageRequest) {
//    }
//}
