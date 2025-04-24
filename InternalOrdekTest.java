package internal;

import cn.hutool.core.thread.ThreadUtil;
import com.cloud.common.utils.DateUtils;
import com.cloud.oms.provincial.distribution.service.IProvincialDistributionWarehouseService;
import com.cloud.oms.provincial.internal.service.IInternalTradeOrderDropShippingBizService;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.rocketmq.client.apis.ClientConfiguration;
import org.apache.rocketmq.client.apis.ClientConfigurationBuilder;
import org.apache.rocketmq.client.apis.ClientException;
import org.apache.rocketmq.client.apis.ClientServiceProvider;
import org.apache.rocketmq.client.apis.consumer.*;
import org.apache.rocketmq.client.apis.message.Message;
import org.apache.rocketmq.client.apis.message.MessageBuilder;
import org.apache.rocketmq.client.apis.message.MessageView;
import org.apache.rocketmq.client.apis.producer.Producer;
import org.apache.rocketmq.client.apis.producer.SendReceipt;
import org.apache.rocketmq.client.java.message.MessageBuilderImpl;
import org.junit.Test;

import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.Collections;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * 测试
 *
 * @author dongwk
 * @date 2024-09-12 15:01
 */
@Slf4j
public class InternalOrdekTest {

//    @Autowired
    private IProvincialDistributionWarehouseService warehouseService;

//    @Autowired
    private IInternalTradeOrderDropShippingBizService orderBizService;

//    @Autowired
//    private MQTransactionListener mqTransactionListener;      // 事务消息监听器
    // 消息生产者配置信息
    private String groupName = "rocketmq_demo";                 // 集群名称，这边以应用名称作为集群名称
    private String pNamesrvAddr = "10.10.23.139:9876";          // 生产者nameservice地址
    private Integer maxMessageSize = 4096;                      // 消息最大大小，默认4M
    private Integer sendMsgTimeout = 30000;                     // 消息发送超时时间，默认3秒
    private Integer retryTimesWhenSendFailed = 2;               // 消息发送失败重试次数，默认2次
    private ExecutorService executor = ThreadUtil.newExecutor(32); // 执行任务的线程池

    /**
     * 普通消息生产者.
     */
    @Test
    public void sendMq() throws Exception {
        Properties properties = new Properties();
        properties.put("bootstrap.servers","10.10.23.139:9092");    // 指定 Broker
        properties.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");  // 将 key 的 Java 对象转成字节数组
        properties.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer"); // 将 value 的 Java 对象转成字节数组
        properties.put("acks", "1");       // 消息至少成功发给一个副本后才返回成功
        properties.put("retries", "5");    // 消息重试 5 次
        KafkaProducer producer = new KafkaProducer<String,String>(properties);

        ProducerRecord record = new ProducerRecord<String,String>("quickstart-events", "hello, kafka "+DateUtils.getTime());
        try {
            producer.send(record).get(2000, TimeUnit.MILLISECONDS);
        } catch (Exception ex) {
            log.error(ex.getMessage(), ex);
        }

    }


}
