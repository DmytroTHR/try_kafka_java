import org.apache.kafka.clients.consumer.*;

import java.io.FileWriter;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

public class ConsumerManualcommit {
    public static void main(String[] args) {
        Properties props = new Properties();
        props.put("bootstrap.servers", "localhost:9092, localhost:9093, localhost:9094");
        props.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        props.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        props.put("group.id", "manual-group");

        String topics[] = {"strings"};

        final int minBatchSize = 50;
        List<ConsumerRecord<String, String>> buffer = new ArrayList<>();

        try (KafkaConsumer<String, String> consumer = new KafkaConsumer<>(props);
             FileWriter fileWriter = new FileWriter("./test.txt", true);) {

            consumer.subscribe(Arrays.asList(topics));

            while (true) {
                ConsumerRecords<String, String> records = consumer.poll(Duration.ofMillis(100));
                for (ConsumerRecord<String, String> record : records) {
                    buffer.add(record);
                }
                if (buffer.size() >= minBatchSize) {
                    fileWriter.append(buffer.toString());
                    consumer.commitSync();
                    buffer.clear();
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
