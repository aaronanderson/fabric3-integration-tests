/*
 * Fabric3
 * Copyright (c) 2009-2013 Metaform Systems
 *
 * Fabric3 is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 3 of
 * the License, or (at your option) any later version, with the
 * following exception:
 *
 * Linking this software statically or dynamically with other
 * modules is making a combined work based on this software.
 * Thus, the terms and conditions of the GNU General Public
 * License cover the whole combination.
 *
 * As a special exception, the copyright holders of this software
 * give you permission to link this software with independent
 * modules to produce an executable, regardless of the license
 * terms of these independent modules, and to copy and distribute
 * the resulting executable under terms of your choice, provided
 * that you also meet, for each linked independent module, the
 * terms and conditions of the license of that module. An
 * independent module is a module which is not derived from or
 * based on this software. If you modify this software, you may
 * extend this exception to your version of the software, but
 * you are not obligated to do so. If you do not wish to do so,
 * delete this exception statement from your version.
 *
 * Fabric3 is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty
 * of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU General Public License for more details.
 *
 * You should have received a copy of the
 * GNU General Public License along with Fabric3.
 * If not, see <http://www.gnu.org/licenses/>.
*/
package org.fabric3.test.node;

import java.net.URL;

import junit.framework.TestCase;
import org.fabric3.api.node.Bootstrap;
import org.fabric3.api.node.Domain;
import org.fabric3.api.node.Fabric;

/**
 *
 */
public class FabricClusterTestCase extends TestCase {

    public void testBasicCluster() throws Exception {
        Fabric fabric1 = Bootstrap.initialize(getClass().getResource("/systemConfigZone1.xml"));
        fabric1.start();

        Fabric fabric2 = Bootstrap.initialize(getClass().getResource("/systemConfigZone2.xml"));
        fabric2.start();

        Domain domain1 = fabric1.getDomain();
        URL serviceComposite = getClass().getClassLoader().getResource("test.composite");
        domain1.deploy(serviceComposite);

        // wait for the runtimes to converge before deploying client composite
        Thread.sleep(2000);

        Domain domain2 = fabric2.getDomain();
        URL clientComposite = getClass().getClassLoader().getResource("client.composite");
        domain2.deploy(clientComposite);

        // invoke local service
        TestService service = domain1.getService(TestService.class);
        assertEquals("test", service.message("test"));

        TestClient client = domain2.getService(TestClient.class);

        // invoke local client connected to the remote service in runtime 1
        assertEquals("test", client.invoke("test"));

        // test one-way callbacks
        assertTrue(client.invokeOneWay("test"));

        // invoke remote service
        TestService remoteService = domain2.getService(TestService.class);
        assertEquals("test", remoteService.message("test"));

        domain2.undeploy(clientComposite);
        fabric2.stop();

        domain1.undeploy(serviceComposite);
        fabric1.stop();

    }

}
