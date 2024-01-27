package org.example.support;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DataGenerator {

    private final SubscriptionGenerator subscriptionGenerator;

    private final PostGenerator postGenerator;

    public void generate() {
//        new DataProcessor().process();
//        subscriptionGenerator.generate();
//        postGenerator.generate();
    }
}
