package org.fabric3.tests.spring;

import junit.framework.TestCase;
import org.oasisopen.sca.ServiceRuntimeException;
import org.oasisopen.sca.annotation.Reference;

/**
 *
 */
public class SecurityTest extends TestCase {

    @Reference
    protected SecureTestService service;

    public void testInvoke() throws Exception {
        service.allowed();
        try {
            service.notAllowed();
            fail("Authorization not enforced");
        } catch (ServiceRuntimeException e) {
            // expected
        }
    }
}