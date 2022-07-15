package org.jiantsquid.core.database;

import java.util.ArrayList;
import java.util.List;

import org.jiantsquid.core.data.Data;
import org.jiantsquid.core.identity.Identity;

import com.hazelcast.client.HazelcastClient;
import com.hazelcast.client.config.ClientConfig;
import com.hazelcast.client.config.ClientNetworkConfig;
import com.hazelcast.config.Config;
import com.hazelcast.config.JoinConfig;
import com.hazelcast.config.NetworkConfig;
import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.map.IMap;

import io.vertx.core.Vertx;
import io.vertx.core.VertxOptions;
import io.vertx.core.spi.cluster.ClusterManager;
import io.vertx.spi.cluster.hazelcast.HazelcastClusterManager;


abstract class Database {

    public static final String DATABASE_NAME = "DATABASE_NAME" ;
    public static final String INSERT_DATA = "INSERT_DATA" ;
    public static final String GET_DATA = "GET_DATA" ;
    public static final String DATA_KEY = "DATA_KEY" ;
    
	private HazelcastInstance client ; 
	
	private int replicationFactor = 3 ;
	
	
	private Identity entity ;
	
	Database( Identity entity, String IPAdress, int port ) {
	    
	 Config config = new Config();
	 config.setClusterName( "JIANTSQUID" ) ;

	 NetworkConfig network = config.getNetworkConfig();
     network.setPort( port ) ;
     network.setPortAutoIncrement(true);
     
     JoinConfig join = network.getJoin();
     join.getMulticastConfig().setEnabled(false);
     join.getTcpIpConfig().setEnabled( true ) ;
     join.getTcpIpConfig().addMember(IPAdress).setRequiredMember(null).setEnabled(true);
     network.setJoin( join ) ;
     
	/// vertex : cluster management
	 // Now set some stuff on the config (omitted)
	 ClusterManager mgr = new HazelcastClusterManager(config);
	 VertxOptions options = new VertxOptions().setClusterManager(mgr);

	 Vertx.clusteredVertx(options, res -> {
	   if (res.succeeded()) {
	      res.result() ;
	   } else {
	     System.err.println( "Problem with Hazelcast culster" ) ;
	   }
	 });
	 ///////////////////
       
        
        
        
        
        Hazelcast.newHazelcastInstance(config);
        
        //////////////////////////////
	    this.entity = entity ;
	    ClientConfig clientConfig = new ClientConfig();
	    clientConfig.setClusterName("JIANTSQUID");
	    ClientNetworkConfig conf = new ClientNetworkConfig() ;
	    
	    List<String> addresses = new ArrayList<>() ;
	    addresses.add(IPAdress) ;
	    conf.setAddresses(addresses) ;
	    clientConfig.setNetworkConfig(conf) ;
	    clientConfig.getNetworkConfig().addAddress(IPAdress,IPAdress + ":" + port /*"32010"*/);
	    client = HazelcastClient.newHazelcastClient(clientConfig);
	    //
	    
	}
	
	
	public abstract String getName() ;
	
	
	public int getReplicationFactor() {
		return replicationFactor ;
	}
	
	private  IMap<Object, Object> getDataMap() {
	    return client.getMap( getName() ) ;
	}
	
	public final void put( String key, Data value ) {
	    getDataMap().put(key, value) ;
	}
	
	protected final void put( String key, String mapName, Data value ) {
        client.getMap( mapName ).put(key, value) ;
    }
	
	public final Data get( String mapName, String key ) {
        return (Data) client.getMap( mapName ).get(key) ;
    }
	
	public final Data get( String key ) {
	    return (Data) getDataMap().get(key) ;
	}
	
	public void print() {
		for( Object k : client.getMap( getName() ).keySet() ) {
			System.out.println( getDataMap().get( k ).toString() ) ;
		}
	}

}
