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

import org.apache.activemq.artemis.api.core.BroadcastGroupConfiguration;
import org.apache.activemq.artemis.api.core.UDPBroadcastEndpointFactory;
import org.apache.activemq.artemis.uri.JMSConnectionOptions;
import org.apache.activemq.artemis.uri.UDPServerLocatorSchema;
import org.apache.activemq.artemis.utils.uri.SchemaConstants;
import org.apache.activemq.artemis.utils.uri.URISchema;
import org.hornetq.api.core.DiscoveryGroupConfiguration;
import org.hornetq.api.core.UDPBroadcastGroupConfiguration;
import org.hornetq.api.jms.HornetQJMSClient;
import org.hornetq.api.jms.JMSFactoryType;
import org.hornetq.jms.client.HornetQConnectionFactory;

import java.net.URI;
import java.util.Map;

/**
 * @author clebertsuconic
 */

public class HornetQUDPSchema extends AbstractHornetQCFSchema
{
   @Override
   public String getSchemaName()
   {
      return SchemaConstants.UDP;
   }

   @Override
   public HornetQConnectionFactory internalNewObject(URI uri, Map<String, String> query, String name) throws Exception
   {
      JMSConnectionOptions options = newConectionOptions(uri, query);

      DiscoveryGroupConfiguration dgc = getDiscoveryGroupConfiguration(uri, query, getHost(uri), getPort(uri), name);

      HornetQConnectionFactory factory;
      if (options.isHa())
      {
         factory = HornetQJMSClient.createConnectionFactoryWithHA(dgc, JMSFactoryType.valueOf(options.getFactoryTypeEnum().intValue()));
      }
      else
      {
         factory =  HornetQJMSClient.createConnectionFactoryWithoutHA(dgc, JMSFactoryType.valueOf(options.getFactoryTypeEnum().intValue()));
      }
      return URISchema.setData(uri, factory, query);
   }

   @Override
   protected URI internalNewURI(HornetQConnectionFactory bean) throws Exception
   {
      return null;
   }

   public static DiscoveryGroupConfiguration getDiscoveryGroupConfiguration(URI uri, Map<String, String> query, String host, int port, String name) throws Exception
   {
      UDPBroadcastGroupConfiguration endpointFactoryConfiguration = new UDPBroadcastGroupConfiguration()
            .setGroupAddress(host)
            .setGroupPort(port);

      URISchema.setData(uri, endpointFactoryConfiguration, query);

      DiscoveryGroupConfiguration dgc = URISchema.setData(uri, new DiscoveryGroupConfiguration(), query)
            .setName(name).setBroadcastEndpointFactoryConfiguration(endpointFactoryConfiguration );

      URISchema.setData(uri, dgc, query);
      return dgc;
   }
}