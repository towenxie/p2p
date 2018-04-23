/*
 * Copyright (c) 2015 Forte Tradebook, Inc. All rights reserved.
 */
package edu.xmh.p2p.data.platform;

public final class PlatformConstant {

    private PlatformConstant() {}

    public static final String DATA_SOURCE_TYPE_DB = "transaction_db";
    public static final String DATA_SOURCE_TYPE_CACHE = "transaction_cache";
    public static final String DATA_SOURCE_TYPE_FS = "transaction_fs";

    public static final String DATA_SOURCE_DRIVER_PG = "postgresql";
    public static final String DATA_SOURCE_DRIVER_MONGO = "mongodb";
    public static final String DATA_SOURCE_DRIVER_REDIS = "jedis";
    public static final String DATA_SOURCE_DRIVER_CLASS_PG = "org.postgresql.Driver";
    public static final String DATA_SOURCE_DRIVER_CLASS_MONGO = "com.mongodb.MongoClient";
    public static final String DATA_SOURCE_DRIVER_CLASS_REDIS = "redis.clients.jedis.Jedis";
    public static final String DATA_SOURCE_GROUP_DEFAULT = "default";

    public static final String ENCODING_UTF8 = "UTF-8";

    public static final String MAIL_SMTP_AUTH = "mail.smtp.auth";
    public static final String TRUE = "true";
    public static final String FALSE = "false";

    public static final String EMAIL_THREAD_POOL = "emailThreadPool";
    public static final String SMS_THREAD_POOL = "smsThreadPool";

    public static final String SHORT_MESSAGE_SERVICE_URI = "yunpan_sms_uri";
    public static final String API_KEY_VALUE = "yunpan_apikey_value";
    public static final String API_KEY = "apikey";
    public static final String TEXT = "text";
    public static final String MOBILE = "mobile";

    public static final String QUEUE_LISTENER_CONTAINER_NAME_SUFFIX = "ListenerContainer";


    public static final String QUEUE_CONNECTION_FACTORY_NAME_SUFFIX = "_ConnectionFactory";
    public static final String QUEUE_AMQP_TEMPLATE_NAME_SUFFIX = "_AmqpTemplate";
    public static final String QUEUE_AMQP_ADMIN_NAME_SUFFIX = "_AmqpAdmin";
    public static final String QUEUE_EXCHANGE_SUFFIX = "_Exchange";
    public static final String QUEUE_MESSAGE_LISTENER_CONTAINER_NAME_SUFFIX = "_MessageListenerContainer";
    public static final String QUEUE_JSON_CLASS_TYPE = "json_class_type";

    // thrift related
    public static final String THRIFT_SERVERS_CONFIG_KEY = "thrift_server_config";
    public static final String THRIFT_CLIENT_CONFIG_KEY = "thrift_client_config";
    
    public static final int THRIFT_CONNECTION_POOL_RETRY_TIMES = 4;
    public static final int THRIFT_CONNECTION_TIME_OUT = 3600000;
}
