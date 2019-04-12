package com.tss.orderService.controller;

import org.springframework.stereotype.Component;

import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArraySet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author ：xiangjun.yang
 * @date ：Created in 2019/4/8 19:38
 * @description：WebSocket 端点
 */
@Component
@ServerEndpoint("/websocket/{shopId}")
public class WebSocket {
    private static Logger logger = LoggerFactory.getLogger(WebSocket.class);

    private Session session;

    private static CopyOnWriteArraySet<WebSocket> webSockets = new CopyOnWriteArraySet<>();
    private static Map<String, Session> sessionPool = new HashMap<>();

    @OnOpen
    public void onOpen(Session session, @PathParam(value = "shopId") String shopId) {
        this.session = session;
        webSockets.add(this);
        sessionPool.put(shopId, session);
        logger.info("有新的连接，总数为:{}", webSockets.size());
    }

    @OnClose
    public void onClose() {
        webSockets.remove(this);
        logger.info("连接断开，总数为:{}", webSockets.size());
    }

    @OnMessage
    public void onMessage(String message) {
        logger.info("收到客户端消息:{}", message);
    }

    // 此为广播消息
    public void sendAllMessage(String message) {
        for (WebSocket webSocket : webSockets) {
            System.out.println("【websocket消息】广播消息:" + message);
            try {
                webSocket.session.getAsyncRemote().sendText(message);
                logger.info("广播消息:{}",message);
            } catch (Exception e) {
                logger.error("推送消息失败", e);
            }
        }
    }

    // 此为单点消息
    public void sendOneMessage(String shopId, String message) {
        Session session = sessionPool.get(shopId);
        if (session != null) {
            try {
                session.getAsyncRemote().sendText(message);
                logger.info("推送消息到窗口{},消息为{}",shopId,message);
            } catch (Exception e) {
                logger.error("推送消息失败", e);
            }
        }
    }


}
