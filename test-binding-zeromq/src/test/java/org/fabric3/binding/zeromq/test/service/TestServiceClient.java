package org.fabric3.binding.zeromq.test.service;

import junit.framework.TestCase;
import org.oasisopen.sca.annotation.Reference;

/**
 *
 */
public class TestServiceClient extends TestCase {

    @Reference
    protected TestService testService;

    public void testInvoke() throws Exception {
        assertEquals("test", testService.invoke("test"));
    }
}