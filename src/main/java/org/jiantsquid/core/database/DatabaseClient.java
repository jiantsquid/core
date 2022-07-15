package org.jiantsquid.core.database;

import java.util.ArrayList;
import java.util.List;

import com.hazelcast.client.HazelcastClient;
import com.hazelcast.client.config.ClientConfig;
import com.hazelcast.client.config.ClientNetworkConfig;
import com.hazelcast.core.HazelcastInstance;

public class DatabaseClient {

	private HazelcastInstance client ; 
	
	public DatabaseClient( String IPAdress, int port ) {
		ClientConfig clientConfig = new ClientConfig();
	    clientConfig.setClusterName("JIANTSQUID");
	    ClientNetworkConfig conf = new ClientNetworkConfig() ;
	    
	    List<String> addresses = new ArrayList<>() ;
	    addresses.add(IPAdress) ;
	    conf.setAddresses(addresses) ;
	    clientConfig.setNetworkConfig(conf) ;
	    clientConfig.getNetworkConfig().addAddress(IPAdress,IPAdress + ":" + port /*"32010"*/);
	    client = HazelcastClient.newHazelcastClient(clientConfig);
	}
	
	public HazelcastInstance getClient() {
		return client ;
	}
}
