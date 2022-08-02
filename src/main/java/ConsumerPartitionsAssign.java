import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.TopicPartition;

import java.time.Duration;
import java.util.Arrays;
import java.util.Properties;

public class ConsumerPartitionsAssign {
    public static void main(String[] args) {
        Properties props = new Properties();
        props.put("bootstrap.servers", "localhost:9092, localhost:9093, localhost:9094");
        props.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        props.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        props.put("enable.auto.commit", "true");
        props.put("auto.commit.interval.ms", "1000");
        props.put("group.id", "yet-another-group");

        String topic = "strings";

        KafkaConsumer<String, String> consumer = new KafkaConsumer<>(props);
        consumer.assign(Arrays.asList(
                new TopicPartition(topic,1),
                new TopicPartition(topic, 3)
        ));

        try {
            while (true) {
                ConsumerRecords<String, String> records = consumer.poll(Duration.ofMillis(100));
                for (ConsumerRecord<String, String> record : records){
                    String message = String.format("offset = %d, key = %s, value = %s, partition = %s%n",
                            record.offset(), record.key(), record.value(), record.partition());
                    System.out.printf(message);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            consumer.close();
        }

    }
}
