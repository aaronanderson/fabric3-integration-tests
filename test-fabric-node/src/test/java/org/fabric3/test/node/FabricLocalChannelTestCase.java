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
import org.fabric3.api.model.type.builder.ChannelDefinitionBuilder;
import org.fabric3.api.model.type.component.ChannelDefinition;
import org.fabric3.api.node.Bootstrap;
import org.fabric3.api.node.Domain;
import org.fabric3.api.node.Fabric;

/**
 *
 */
public class FabricLocalChannelTestCase extends TestCase {
    public void testBlank() throws Exception {

    }

    public void testGetLocalChannel() throws Exception {
        Fabric fabric = Bootstrap.initialize();
        fabric.start();

        Domain domain = fabric.getDomain();
        URL channelComposite = getClass().getClassLoader().getResource("channel.composite");
        domain.deploy(channelComposite);

        TestChannel channel = domain.getChannel(TestChannel.class, "TestChannel");
        channel.send("test");

        fabric.stop();
    }

    public void testProducerConsumer() throws Exception {
        Fabric fabric = Bootstrap.initialize();
        fabric.start();

        Domain domain = fabric.getDomain();

        URL channelComposite = getClass().getClassLoader().getResource("channel.composite");
        domain.deploy(channelComposite);

        URL producerComposite = getClass().getClassLoader().getResource("producer.composite");
        domain.deploy(producerComposite);

        URL consumerComposite = getClass().getClassLoader().getResource("consumer.composite");
        domain.deploy(consumerComposite);

        TestProducer producer = domain.getService(TestProducer.class);
        producer.send();

        LatchService latchService = domain.getService(LatchService.class);
        latchService.await();

        fabric.stop();
    }

    public void testDeployChannelDefinition() throws Exception {
        Fabric fabric = Bootstrap.initialize();
        fabric.start();

        Domain domain = fabric.getDomain();

        ChannelDefinition definition = ChannelDefinitionBuilder.newBuilder("TestChannel").build();
        domain.deploy(definition);

        TestChannel channel = domain.getChannel(TestChannel.class, "TestChannel");
        channel.send("test");

        fabric.stop();
    }

    public void testDeployConsumer() throws Exception {
        Fabric fabric = Bootstrap.initialize();
        fabric.start();

        Domain domain = fabric.getDomain();

        ChannelDefinition definition = ChannelDefinitionBuilder.newBuilder("TestChannel").build();
        domain.deploy(definition);

        domain.deploy("LatchService", new LatchServiceImpl());
        domain.deploy("TestConsumer", new TestConsumer());

        fabric.stop();
    }
}
