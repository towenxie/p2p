package edu.xmh.p2p.data.manager.service;

import javax.annotation.Resource;
import org.springframework.stereotype.Service;

import edu.xmh.p2p.data.business.conf.P2PAppConfig;
import edu.xmh.p2p.data.business.constant.P2PConstants;

@Service
public class P2PService {
    @Resource
    private P2PAppConfig appConfig;

    public void startUp() {
        P2PConstants.appConfig = appConfig;

    }

}
