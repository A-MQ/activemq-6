/**
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements. See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.activemq.artemis.common.example.hornetq;

import javax.jms.ConnectionFactory;
import javax.jms.Queue;
import javax.jms.Topic;
import javax.naming.Context;
import javax.naming.NamingException;
import javax.naming.spi.InitialContextFactory;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.StringTokenizer;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.activemq.artemis.jndi.ActiveMQInitialContextFactory;
import org.hornetq.api.core.DiscoveryGroupConfiguration;
import org.hornetq.api.core.JGroupsBroadcastGroupConfiguration;
import org.hornetq.api.core.TransportConfiguration;
import org.hornetq.api.core.UDPBroadcastGroupConfiguration;
import org.hornetq.api.core.client.HornetQClient;
import org.hornetq.api.jms.HornetQJMSClient;
import org.hornetq.api.jms.JMSFactoryType;
import org.hornetq.core.client.HornetQClientLogger;
import org.hornetq.core.remoting.impl.netty.NettyConnectorFactory;
import org.hornetq.core.remoting.impl.netty.TransportConstants;
import org.hornetq.jms.client.HornetQConnectionFactory;


/**
 * A factory of the HornetQ InitialContext which contains
 * {@link javax.jms.ConnectionFactory} instances as well as a child context called
 * <i>destinations</i> which contain all of the current active destinations, in
 * child context depending on the QoS such as transient or durable and queue or
 * topic.
 */
public class HornetQInitialContextFactory extends ActiveMQInitialContextFactory
{
   /**
    * Factory method to create new Queue instances
    */
   protected Queue createQueue(String name)
   {
      return HornetQJMSClient.createQueue(name);
   }

   /**
    * Factory method to create new Topic instances
    */
   protected Topic createTopic(String name)
   {
      return HornetQJMSClient.createTopic(name);
   }

   /**
    * Factory method to create a new connection factory from the given environment
    */
   protected ConnectionFactory createConnectionFactory(String uri, String name) throws Exception
   {
      HornetQConnectionFactoryParser parser = new HornetQConnectionFactoryParser();
      return parser.newObject(parser.expandURI(uri), name);
   }
}