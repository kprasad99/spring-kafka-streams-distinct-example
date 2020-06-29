package io.github.kprasad99.kafka.streams;

import org.apache.kafka.streams.kstream.ValueTransformerWithKey;
import org.apache.kafka.streams.processor.ProcessorContext;
import org.apache.kafka.streams.state.KeyValueStore;

public class DuplicateFilter implements ValueTransformerWithKey<String, String, String> {

	private KeyValueStore<String, Integer> store;
	
	@SuppressWarnings("unchecked")
	@Override
	public void init(ProcessorContext context) {
		store = (KeyValueStore<String, Integer>) context.getStateStore("msg-distinct-store");
	}

	@Override
	public String transform(String readOnlyKey, String value) {
		if(readOnlyKey == null) {
			return value;
		}
		if(store.get(readOnlyKey) == null) {
			store.put(readOnlyKey, value.hashCode());
			return value;
		}
		return null;
	}

	@Override
	public void close() {
		
	}

}
