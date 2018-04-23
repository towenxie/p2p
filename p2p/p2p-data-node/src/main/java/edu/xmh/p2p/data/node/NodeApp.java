package edu.xmh.p2p.data.node;

import java.util.Scanner;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import edu.xmh.p2p.data.common.util.SpringUtils;
import edu.xmh.p2p.data.node.service.NodeService;

public class NodeApp {

    static {
        // initialize Spring
        new ClassPathXmlApplicationContext("classpath*:spring.xml");
    }

    /**
     * Launch the application.
     */
    public static void main(String[] args) {

        NodeService nodeService = SpringUtils.getBean(NodeService.class);
        nodeService.startUp();

        Scanner scanner = new Scanner(System.in);
        while (!scanner.nextLine().equalsIgnoreCase("stop")) {
            // do nothing
        }
        scanner.close();
        System.exit(0);
    }
}
