package org.fabric3.binding.jms.test.handler;

import org.oasisopen.sca.annotation.Scope;

/**
 *
 */
@Scope("COMPOSITE")
public class HandlerServiceImpl implements HandlerService {

    public boolean test(String message) {
        return TestHandler.INBOUND_INVOKED;
    }
}
