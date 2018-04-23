package edu.xmh.p2p.data.manager;

import org.springframework.context.support.ClassPathXmlApplicationContext;

import edu.xmh.p2p.data.common.util.SpringUtils;
import edu.xmh.p2p.data.manager.service.P2PService;

public class P2PApp {
    static {
        // initialize Spring
        new ClassPathXmlApplicationContext("classpath*:spring.xml");
    }

    /**
     * Launch the application.
     */
    public static void main(String[] args) {
        P2PService p2pService = SpringUtils.getBean(P2PService.class);
        p2pService.startUp();
    }
}
