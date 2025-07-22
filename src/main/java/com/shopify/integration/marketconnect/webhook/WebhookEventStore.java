package com.shopify.integration.marketconnect.webhook;

import org.springframework.stereotype.Component;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

@Component
public class WebhookEventStore {

    private final List<String> events = Collections.synchronizedList(new LinkedList<>());

    public void addEvent(String eventPayload) {
        synchronized (events) {
            events.add(0, eventPayload); // Add at the top
            if (events.size() > 10) {    // Keep only last 10
                events.remove(events.size() - 1);
            }
        }
    }

    public List<String> getRecentEvents() {
        synchronized (events) {
            return new LinkedList<>(events);
        }
    }
}
