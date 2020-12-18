package com.kdimitrov.edentist.common.api.services;

import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.concurrent.atomic.AtomicInteger;

@Service
public class StatisticsService {

    private AtomicInteger requestCount = new AtomicInteger(0);
    private Instant startedOn = Instant.now();

    public void incRequestCount() {
        requestCount.incrementAndGet();
    }

    public int getRequestCount() {
        return requestCount.intValue();
    }

    public Instant getStartedOn() {
        return startedOn;
    }

}
