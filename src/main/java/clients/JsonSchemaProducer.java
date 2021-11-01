package clients;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;

import java.util.Date;
import java.util.List;
import java.util.Properties;

import io.confluent.kafka.serializers.AbstractKafkaSchemaSerDeConfig;
import io.confluent.kafka.serializers.json.KafkaJsonSchemaSerializer;
import io.confluent.kafka.serializers.json.KafkaJsonSchemaSerializerConfig;
import io.confluent.kafka.serializers.subject.TopicNameStrategy;
import io.confluent.kafka.serializers.subject.TopicRecordNameStrategy;

public class JsonSchemaProducer {

  public static void main(String[] args) throws Exception {
    System.out.println("*** Starting JSON Schema Producer ***");
    Properties settings = new Properties();
    settings.put(ProducerConfig.CLIENT_ID_CONFIG, "json-schema-producer-v0.1.0");
    settings.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
    settings.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
    settings.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, KafkaJsonSchemaSerializer.class);
    settings.put(KafkaJsonSchemaSerializerConfig.ONEOF_FOR_NULLABLES, false);
    settings.put(KafkaJsonSchemaSerializerConfig.FAIL_INVALID_SCHEMA, true);
    settings.put(AbstractKafkaSchemaSerDeConfig.AUTO_REGISTER_SCHEMAS, false);
    settings.put(AbstractKafkaSchemaSerDeConfig.USE_LATEST_VERSION, true);
    settings.put(AbstractKafkaSchemaSerDeConfig.SCHEMA_REGISTRY_URL_CONFIG, "http://localhost:8081");

    final KafkaProducer<String, PersonRequest> producer = new KafkaProducer<>(settings);

    Runtime.getRuntime().addShutdownHook(new Thread(() -> {
      System.out.println("### Stopping Json Schema Producer ###");
      producer.close();
    }));

    final String topic = "js";

    PersonRequest person = new PersonRequest();
    person.setName("Firstname");
    person.setSurname("Lastname");
    person.setEmail("a@b.com");

    ProducerRecord<String, PersonRequest>
        record =
        new ProducerRecord<>(topic, null, person);

    producer.send(record);
  }
}