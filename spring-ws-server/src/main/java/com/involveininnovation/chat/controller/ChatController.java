package com.involveininnovation.chat.controller;

import com.involveininnovation.chat.model.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.socket.messaging.SessionConnectedEvent;

@RestController
public class ChatController {

    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;

    @MessageMapping("/message")
    @SendTo("/chatroom/public")
    public Message receiveMessage(@Payload Message message){
        return message;
    }

    @MessageMapping("/private-message")
    public Message recMessage(@Payload Message message){
        simpMessagingTemplate.convertAndSendToUser(message.getReceiverName(),"/private",message);
        System.out.println(message.toString());
        return message;
    }

    @EventListener
    public void handleSessionConnected(SessionConnectedEvent event){

        StompHeaderAccessor accessor=StompHeaderAccessor.wrap(event.getMessage());
        System.out.println(accessor.getSessionId());
        System.out.println(accessor.getMessage());
        System.out.println(accessor.getLogin());
        System.out.println(accessor.getMessageHeaders());
        System.out.println(accessor.getNativeHeader("X-username"));
        System.out.println(accessor.getFirstNativeHeader("X-username"));
    }
}
