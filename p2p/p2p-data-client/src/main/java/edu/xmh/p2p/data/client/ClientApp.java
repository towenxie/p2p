package edu.xmh.p2p.data.client;

import java.util.Scanner;

import org.springframework.context.support.ClassPathXmlApplicationContext;

import edu.xmh.p2p.data.client.service.ClientService;
import edu.xmh.p2p.data.common.util.SpringUtils;

/**
 * Hello world!
 *
 */
public class ClientApp 
{
    static {
        // initialize Spring
        new ClassPathXmlApplicationContext("classpath*:spring.xml");
    }

    /**
     * Launch the application.
     */
    public static void main(String[] args) {

        ClientService clientService = SpringUtils.getBean(ClientService.class);
        clientService.startUp();

        Scanner scanner = new Scanner(System.in);
        while (!scanner.nextLine().equalsIgnoreCase("stop")) {
            // do nothing
        }
        scanner.close();
        System.exit(0);
    }
}