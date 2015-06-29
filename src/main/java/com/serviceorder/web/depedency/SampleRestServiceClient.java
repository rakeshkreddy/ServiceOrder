package com.serviceorder.web.depedency;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by rakesh on 6/26/15.
 */
@Component
public class SampleRestServiceClient {
    private final AtomicInteger counter = new AtomicInteger();

    private final Logger log = LoggerFactory.getLogger(SampleRestServiceClient.class);

    @HystrixCommand(groupKey = "SampleService", fallbackMethod = "fallback",
            commandProperties = {
                    @HystrixProperty(name="execution.isolation.thread.timeoutInMilliseconds", value="1000")
            }
    )
    public Object sampleHystrixCommand(Map<String, Object> data) {
        int currentCount = counter.getAndIncrement();
        if(currentCount%2 == 0){
            try {
                log.info("## sleeping");
                //simulate a delay in response`
                Thread.currentThread().sleep(2000);
                log.info("## sleeping awake");
            } catch (InterruptedException e) {
            }
        }
        log.info("*** Service executed {}",currentCount);
        return "dynamic-result";
    }

    public Object fallback(Map<String, Object> data) {
        log.info("*** Fallback executed");
        return "cache-result";
    }

}
