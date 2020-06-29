package io.github.kprasad99.kafka.configuration;

import java.util.function.Consumer;

import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.state.KeyValueStore;
import org.apache.kafka.streams.state.StoreBuilder;
import org.apache.kafka.streams.state.Stores;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.github.kprasad99.kafka.streams.DuplicateFilter;
import lombok.extern.slf4j.Slf4j;

@Configuration
@Slf4j
public class StreamsConfigurations {


	@Bean
	public StoreBuilder<KeyValueStore<String, Integer>> myStore() {
		return Stores.keyValueStoreBuilder(
				Stores.persistentKeyValueStore("msg-distinct-store"), Serdes.String(),
				Serdes.Integer());
	}
	
	@Bean
	public Consumer<KStream<String, String>> distinct(){
		return (streams) -> streams.transformValues(() -> new DuplicateFilter(), "msg-distinct-store")
				.filter((k, v) -> v != null).peek((k, v) -> log.info("received message with id {}", k));
	}
	
}
