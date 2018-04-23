/*
 * Copyright (c) 2015 Forte Tradebook, Inc. All rights reserved.
 */
package edu.xmh.p2p.data.platform.thrift.server;

import java.util.Set;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Thrift servers configuration, and services/processors that assigned to this server.
 * 
 */
@XmlRootElement(name = "servers")
public class ThriftServerConfig {

    private Set<Server> servers;


    @XmlElement(name = "server")
    public Set<Server> getServers() {
        return servers;
    }

    public void setServers(Set<Server> servers) {
        this.servers = servers;
    }

    /**
     * 
     * Thrift server.
     *
     */
    public static class Server {

        private String name;
        private String host;
        private int port;
        private int selectorThreads;
        private int workerThreads;

        private Set<Service> services;
		private String serverProfile;

        @XmlAttribute(name = "name", required = true)
        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        @XmlAttribute(name = "host", required = false)
        public String getHost() {
            return host;
        }

        public void setHost(String host) {
            this.host = host;
        }

        @XmlAttribute(name = "port", required = true)
        public int getPort() {
            return port;
        }

        public void setPort(int port) {
            this.port = port;
        }

        @XmlAttribute(name = "selectorthreads", required = false)
        public int getSelectorThreads() {
            return selectorThreads;
        }

        public void setSelectorThreads(int selectorThreads) {
            this.selectorThreads = selectorThreads;
        }

        @XmlAttribute(name = "workerthreads", required = false)
        public int getWorkerThreads() {
            return workerThreads;
        }

        public void setWorkerThreads(int workerThreads) {
            this.workerThreads = workerThreads;
        }

        @XmlElement(name = "service")
        public Set<Service> getServices() {
            return services;
        }

        public void setServices(Set<Service> services) {
            this.services = services;
        }

		public String getServerProfile() {
			return this.serverProfile;
		}
		
		public void setServerProfile(String serverProfile) {
			this.serverProfile = serverProfile;
		}
    }

    /**
     * 
     * Thrift service.
     *
     */
    public static class Service {
        private String name;

        private Set<Processor> processors;

        @XmlAttribute(name = "name", required = true)
        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        @XmlElement(name = "processor")
        public Set<Processor> getProcessors() {
            return processors;
        }

        public void setProcessors(Set<Processor> processors) {
            this.processors = processors;
        }

    }

    /**
     * 
     * Thrift processor.
     *
     */
    static class Processor {
        private String contractClass;

        private String handlerClass;

        @XmlAttribute(name = "contractclass", required = true)
        public String getContractClass() {
            return contractClass;
        }

        public void setContractClass(String contractClass) {
            this.contractClass = contractClass;
        }

        @XmlAttribute(name = "handlerclass", required = true)
        public String getHandlerClass() {
            return handlerClass;
        }

        public void setHandlerClass(String handler) {
            this.handlerClass = handler;
        }
    }
}
