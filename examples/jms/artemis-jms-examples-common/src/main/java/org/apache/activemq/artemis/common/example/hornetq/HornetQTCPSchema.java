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

import org.apache.activemq.artemis.uri.JMSConnectionOptions;
import org.apache.activemq.artemis.utils.uri.SchemaConstants;
import org.apache.activemq.artemis.utils.uri.URISchema;
import org.hornetq.api.core.TransportConfiguration;
import org.hornetq.api.jms.HornetQJMSClient;
import org.hornetq.api.jms.JMSFactoryType;
import org.hornetq.core.remoting.impl.netty.NettyConnectorFactory;
import org.hornetq.core.remoting.impl.netty.TransportConstants;
import org.hornetq.jms.client.HornetQConnectionFactory;

import java.net.URI;
import java.util.List;
import java.util.Map;

/**
 * @author <a href="mailto:andy.taylor@jboss.org">Andy Taylor</a>
 */
public class HornetQTCPSchema extends AbstractHornetQCFSchema
{
   @Override
   public String getSchemaName()
   {
      return SchemaConstants.TCP;
   }

   @Override
   protected HornetQConnectionFactory internalNewObject(URI uri, Map<String, String> query, String name) throws Exception
   {
      JMSConnectionOptions options = newConectionOptions(uri, query);

      List<TransportConfiguration> configurations =
            HornetQTCPTransportConfigurationSchema.getTransportConfigurations(uri, query, TransportConstants.ALLOWABLE_CONNECTOR_KEYS, name, NettyConnectorFactory.class.getName());

      TransportConfiguration[] tcs = new TransportConfiguration[configurations.size()];

      configurations.toArray(tcs);

      HornetQConnectionFactory factory;

      if (options.isHa())
      {
         factory = HornetQJMSClient.createConnectionFactoryWithHA(JMSFactoryType.valueOf(options.getFactoryTypeEnum().intValue()), tcs);
      }
      else
      {
         factory =  HornetQJMSClient.createConnectionFactoryWithoutHA(JMSFactoryType.valueOf(options.getFactoryTypeEnum().intValue()), tcs);
      }

      return URISchema.setData(uri, factory, query);
   }

   @Override
   protected URI internalNewURI(HornetQConnectionFactory bean) throws Exception
   {
      return null;
   }
}