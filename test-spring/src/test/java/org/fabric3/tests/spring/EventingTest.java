package org.fabric3.tests.spring;

import junit.framework.TestCase;
import org.oasisopen.sca.annotation.Reference;

import org.fabric3.tests.binding.harness.eventing.TestConsumer;
import org.fabric3.tests.binding.harness.eventing.TestProducer;

/**
 *
 */
public class EventingTest extends TestCase {

    @Reference
    protected TestProducer producer;

    @Reference
    protected TestConsumer consumer;

    public void testProduce() throws Exception {
        consumer.setWaitCount(2);
        producer.produce("message");
        producer.produce("message2");
        consumer.waitOnEvents();
    }

}