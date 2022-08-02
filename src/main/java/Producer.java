import org.apache.kafka.clients.producer.*;

import java.util.Date;
import java.util.Properties;

public class Producer {
    public static void main(String[] args) {

        String clientID = "my-producer";
        Properties props = new Properties();
        props.put("bootstrap.servers", "localhost:9092, localhost:9093, localhost:9094");
        props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        props.put("acks", "all");
        props.put("client.id", clientID);

        KafkaProducer<String, String> producer = new KafkaProducer<String, String>(props);

        int numRec = 100;
        String topic = "strings";

        try {
            for (int i = 0; i < numRec; i++) {
                String message = String.format("Producer %s has sent message %s at %s", clientID, i, new Date());
                System.out.println(message);
                producer.send(new ProducerRecord<>(topic, Integer.toString(i), message));
                Thread.sleep(50);
            }
        } catch (Exception err) {
            err.printStackTrace();
        } finally {
            producer.close();
        }

    }
}
