package com.acledabank.vicheak.api.core.socket;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.WebSocketMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

//socket connection configuration class
@Configuration
@Slf4j
public class SocketConnectionHandler extends TextWebSocketHandler {

    //store all the connections and use it to broadcast message
    List<WebSocketSession> webSocketSessions = Collections.synchronizedList(new ArrayList<>());

    //executes when clients try to connect
    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        super.afterConnectionEstablished(session);

        log.info("Socket connection : {}", session.getId());

        //add session to the list
        webSocketSessions.add(session);
    }

    //executes when clients disconnect from websocket
    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        super.afterConnectionClosed(session, status);

        log.info("Socket disconnection : {}", session.getId());

        //remove the connection from the list
        webSocketSessions.remove(session);
    }

    //handle exchanging of messages in the network
    //get session info who is sending the message and also the message object
    @Override
    public void handleMessage(WebSocketSession session, WebSocketMessage<?> message) throws Exception {
        super.handleMessage(session, message);

        //ignore the session in the list which wants to send the message
        for(WebSocketSession webSocketSession : webSocketSessions){
            if(webSocketSession == session) continue;

            webSocketSession.sendMessage(message);
        }
    }

}
