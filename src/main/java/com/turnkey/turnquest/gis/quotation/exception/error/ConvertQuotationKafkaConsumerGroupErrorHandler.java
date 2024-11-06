package com.turnkey.turnquest.gis.quotation.exception.error;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.Consumer;
import org.jetbrains.annotations.NotNull;
import org.springframework.kafka.listener.KafkaListenerErrorHandler;
import org.springframework.kafka.listener.ListenerExecutionFailedException;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;

@Slf4j
@Component("kafkaQuotationsErrorHandler")
public class ConvertQuotationKafkaConsumerGroupErrorHandler implements KafkaListenerErrorHandler {

    @NotNull
    @Override
    public Object handleError(Message<?> message, ListenerExecutionFailedException exception) {
        log.error("Group: {},Message: {}",exception.getGroupId(),exception.getMessage());
        return new RuntimeException(exception.getMessage());
    }

    @NotNull
    @Override
    public Object handleError(Message<?> message, ListenerExecutionFailedException exception, Consumer<?, ?> consumer) {
        return KafkaListenerErrorHandler.super.handleError(message, exception, consumer);
    }
}
