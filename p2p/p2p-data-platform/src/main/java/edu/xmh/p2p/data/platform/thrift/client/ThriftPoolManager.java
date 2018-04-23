package edu.xmh.p2p.data.platform.thrift.client;

import org.apache.commons.pool2.impl.GenericObjectPool;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.thrift.transport.TTransport;

import edu.xmh.p2p.data.platform.PlatformConstant;

public class ThriftPoolManager {

    private static final Logger logger = LogManager.getLogger(ThriftPoolManager.class.getName());

    private GenericObjectPool<TTransport> pool;
    private ThriftFactory factory;

    public ThriftPoolManager(String host, int port, int timeout, ThriftPoolConfig config) {
        this.factory = new ThriftFactory(host, port, timeout);
        this.pool = new GenericObjectPool<TTransport>(factory, config);
    }

    public TTransport getSocket() {
        try {
            TTransport tTransport = pool.borrowObject();

            if (!tTransport.peek()) {
                tTransport.close();
                pool.invalidateObject(tTransport);

                boolean connected = false;
                for (int i = 1; i < PlatformConstant.THRIFT_CONNECTION_POOL_RETRY_TIMES; i++) {
                    tTransport = pool.borrowObject();
                    if (!tTransport.peek()) {
                        tTransport.close();
                        pool.invalidateObject(tTransport);
                    } else {
                        connected = true;
                        break;
                    }
                }

                if (!connected) {
                    tTransport = factory.createTTransport();
                }
            }

            return tTransport;
        } catch (Exception e) {
            throw new RuntimeException("Get thrift resource error", e);
        }
    }

    public void returnSocket(TTransport tTransport) {
        try {
            int remainingBuffer = tTransport.getBytesRemainingInBuffer();
            if (remainingBuffer > 0) {
                // tTransport interrupted exceptionally
                tTransport.close();
                logger.warn("Detect a TTransport was interrupted exceptionally, and close it");
            } else {
                pool.returnObject(tTransport);
            }
        } catch (Exception e) {
            throw new RuntimeException("return thrift resource error", e);
        }
    }

}
