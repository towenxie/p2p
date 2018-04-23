package edu.xmh.p2p.data.platform.thrift.client;

import org.apache.commons.pool2.PooledObject;
import org.apache.commons.pool2.PooledObjectFactory;
import org.apache.commons.pool2.impl.DefaultPooledObject;
import org.apache.thrift.transport.TFramedTransport;
import org.apache.thrift.transport.TSocket;
import org.apache.thrift.transport.TTransport;

public class ThriftFactory implements PooledObjectFactory<TTransport> {

    private String host;
    private int port;
    private int timeout;

    public ThriftFactory(String host, int port, int timeout) {
        this.host = host;
        this.port = port;
        this.timeout = timeout;
    }

    public TTransport createTTransport() throws Exception {
        TTransport tTransport = new TFramedTransport(new TSocket(this.host, this.port, this.timeout));
        tTransport.open();
        return tTransport;
    }

    @Override
    public PooledObject<TTransport> makeObject() throws Exception {
        TTransport tTransport = new TFramedTransport(new TSocket(this.host, this.port, this.timeout));
        tTransport.open();
        return new DefaultPooledObject<TTransport>(tTransport);
    }

    @Override
    public void destroyObject(PooledObject<TTransport> p) throws Exception {
        TTransport tTransport = p.getObject();
        if (tTransport.isOpen()) {
            tTransport.close();
        }
        p.invalidate();
    }

    @Override
    public boolean validateObject(PooledObject<TTransport> p) {
        try {
            TTransport tTransport = p.getObject();

            return tTransport.isOpen();
        } catch (Exception e) {
            return false;
        }
    }



    @Override
    public void activateObject(PooledObject<TTransport> p) throws Exception {
        // DO NOTHING
    }

    @Override
    public void passivateObject(PooledObject<TTransport> p) throws Exception {
        // DO NOTHING
    }
}
