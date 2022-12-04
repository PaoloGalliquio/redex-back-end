// package com.redexbackend.controllers;

// import com.redexbackend.models.Message;
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.messaging.handler.annotation.MessageMapping;
// import org.springframework.messaging.handler.annotation.Payload;
// import org.springframework.messaging.handler.annotation.SendTo;
// import org.springframework.messaging.simp.SimpMessagingTemplate;
// import org.springframework.scheduling.annotation.EnableScheduling;
// import org.springframework.scheduling.annotation.Scheduled;
// import org.springframework.stereotype.Controller;

// @EnableScheduling
// @Controller
// public class ChatController {

//     @Autowired
//     private SimpMessagingTemplate simpMessagingTemplate;
//     Message mensaje = new Message();

//     @Scheduled(fixedRate = 5000)
//     public void greeting() {
//         System.out.println("scheduled");
//         simpMessagingTemplate.convertAndSend("/greetings", "Hello");
//     }

//     @MessageMapping("/message")
//     @SendTo("/chatroom/public")
//     public Message receiveMessage(@Payload Message message){
//         return message;
//     }

//     @MessageMapping("/private-message")
//     public Message recMessage(@Payload Message message){
//         simpMessagingTemplate.convertAndSendToUser(message.getReceiverName(),"/private",message);
//         System.out.println(message.toString());
//         return message;
//     }
// }