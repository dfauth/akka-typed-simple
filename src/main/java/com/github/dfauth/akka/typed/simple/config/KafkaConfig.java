package com.github.dfauth.akka.typed.simple.config;

import com.github.dfauth.actor.kafka.EnvelopeHandler;
import com.github.dfauth.actor.kafka.confluent.AvroDeserializer;
import com.github.dfauth.actor.kafka.confluent.AvroSerializer;
import com.typesafe.config.Config;
import io.confluent.kafka.schemaregistry.client.CachedSchemaRegistryClient;
import io.confluent.kafka.schemaregistry.client.MockSchemaRegistryClient;
import io.confluent.kafka.schemaregistry.client.SchemaRegistryClient;
import org.apache.avro.specific.SpecificRecordBase;
import org.apache.kafka.common.serialization.Serdes;

import java.util.List;
import java.util.Map;

import static com.github.dfauth.utils.ConfigUtils.wrap;

public class KafkaConfig {

    private Config config;
    private String stem = "akka.kafka";

    public KafkaConfig(Config config) {
        this.config = config;
    }

    public List<String> topics() {
        return config.getStringList(stem("topics"));
    }

    private String stem(String leaf) {
        return String.format("%s.%s",stem,leaf);
    }

    public String groupId() {
        return wrap(config).getString(stem("groupId")).orElseThrow();
    }

    public Map<String, Object> properties() {
        return wrap(config).getMap(stem);
    }

    public <T extends SpecificRecordBase> EnvelopeHandler<T> envelopeHandler() {
        return EnvelopeHandler.of(Serdes.serdeFrom(
                avroSerializer(),
                avroDeserializer()
        ));
    }

    public <T extends SpecificRecordBase> AvroSerializer<T> avroSerializer() {
        return (AvroSerializer<T>) AvroSerializer.builder()
                .withAutoRegisterSchema(isAutoRegisterSchema())
                .withSchemaRegistryClient(schemaRegistryClient())
                .withUrl(schemaRegistryUrl())
                .build();
    }

    public <T extends SpecificRecordBase> AvroDeserializer<T> avroDeserializer() {
        return (AvroDeserializer<T>) AvroDeserializer.builder()
                .withAutoRegisterSchema(isAutoRegisterSchema())
                .withSchemaRegistryClient(schemaRegistryClient())
                .withUrl(schemaRegistryUrl())
                .build();
    }

    public SchemaRegistryClient schemaRegistryClient() {
        return wrap(config).getMap("schema.registry").isEmpty() ?
                new MockSchemaRegistryClient() :
                new CachedSchemaRegistryClient(schemaRegistryUrl(), schemaRegistryCapacity());
    }

    public String schemaRegistryUrl() {
        return wrap(config).getString("schema.registry.url").orElse("none");
    }

    public int schemaRegistryCapacity() {
        return wrap(config).getInt("schema.registry.capacity").orElse(1024);
    }

    public boolean isAutoRegisterSchema() {
        return wrap(config).getBoolean("autoRegisterSchema").orElse(true);
    }

}
