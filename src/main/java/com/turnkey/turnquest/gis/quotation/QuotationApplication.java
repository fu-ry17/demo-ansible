package com.turnkey.turnquest.gis.quotation;
/**
 *
 */

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.kafka.annotation.EnableKafkaRetryTopic;

/**
 * @author Paul Gichure
 * @version 1.0.0
 */
@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients
@EnableJpaRepositories
@EnableKafkaRetryTopic
public class QuotationApplication extends SpringBootServletInitializer {

    public static void main(String[] args) {
        SpringApplication.run(QuotationApplication.class, args);
    }
}
