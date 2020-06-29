package io.github.kprasad99.kafka.endpoint;

import java.util.function.Supplier;

import org.springframework.context.annotation.Bean;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.EmitterProcessor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@Slf4j
public class MessageRestService {

	private final EmitterProcessor<Message<String>> processor = EmitterProcessor.create();

	@Bean
	public Supplier<Flux<Message<String>>> sender() {
		return () -> processor;
	}

	@PostMapping("/api/notify")
	public Mono<Void> event(@RequestBody io.github.kprasad99.kafka.endpoint.model.Message msg) {
		log.info("Sending message {}", msg.getId());
		Message<String> message = MessageBuilder.withPayload(msg.getText()).setHeader("key", msg.getId()).build();
		processor.onNext(message);
		return Mono.<Void>empty();
	}

}
