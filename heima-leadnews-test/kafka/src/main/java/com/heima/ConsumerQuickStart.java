package com.heima;

import org.apache.kafka.clients.consumer.*;
import org.apache.kafka.common.TopicPartition;

import java.time.Duration;
import java.util.Collections;
import java.util.Map;
import java.util.Properties;

/**
 * @author 肉豆蔻吖
 * @date 2024/5/12
 */
public class ConsumerQuickStart {
    public static void main(String[] args) {
        Properties properties = new Properties();
        //kafka的连接地址
        properties.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "192.168.1.100:9092");
        //消费者组
        properties.put(ConsumerConfig.GROUP_ID_CONFIG, "group1");

        // 手动提交偏移量
        properties.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, false);

        //消息的反序列化器
        properties.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringDeserializer");
        properties.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringDeserializer");

        Consumer<String, String> consumer = new KafkaConsumer<>(properties);
        consumer.subscribe(Collections.singletonList("itheima-topic"));

        try {
            while (true) {
                ConsumerRecords<String, String> consumerRecords = consumer.poll(Duration.ofMillis(1000));
                for (ConsumerRecord<String, String> consumerRecord : consumerRecords) {
                    System.out.println(consumerRecord.key());
                    System.out.println(consumerRecord.value());
                }

                /*try {
                    // 同步提交偏移量
                    consumer.commitSync();
                } catch (Exception e) {
                    System.out.println("记录提交失败的异常: " + e);
                    throw new RuntimeException(e);
                }*/

                // 异步的方式提交偏移量
                /*consumer.commitAsync(new OffsetCommitCallback() {
                    @Override
                    public void onComplete(Map<TopicPartition, OffsetAndMetadata> map, Exception e) {
                        if (e != null) {
                            System.out.println("记录错的提交偏移量: " + map + ", 异常信息为: " + e);
                        }
                    }
                });*/

                // 异步提交偏移量
                consumer.commitAsync();
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            // 同步
            consumer.commitAsync();
        }


    }
}
