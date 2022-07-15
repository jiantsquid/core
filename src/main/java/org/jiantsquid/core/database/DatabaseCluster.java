package org.jiantsquid.core.database;

import com.hazelcast.config.Config;
import com.hazelcast.config.JoinConfig;
import com.hazelcast.config.ManagementCenterConfig;
import com.hazelcast.config.NetworkConfig;
import com.hazelcast.core.Hazelcast;

import io.vertx.core.Vertx;
import io.vertx.core.VertxOptions;
import io.vertx.core.spi.cluster.ClusterManager;
import io.vertx.spi.cluster.hazelcast.HazelcastClusterManager;

public class DatabaseCluster {

	private String IPAdress ;
	private int port ;
	
	public DatabaseCluster( String IPAdress, int port ) {
		this.IPAdress = IPAdress ;
		this.port     = port ;
	}
	
	public boolean init() {
		boolean status = true ;
		 Config config = new Config();
		 config.setClusterName( "JIANTSQUID" ) ;

		 NetworkConfig network = config.getNetworkConfig();
	     network.setPort( port ) ;
	     network.setPortAutoIncrement(true);
	     
	     JoinConfig join = network.getJoin();
	     join.getMulticastConfig().setEnabled(false);
	     join.getTcpIpConfig().addMember(IPAdress).setRequiredMember(IPAdress).setEnabled(true);
	     
	     network.setJoin( join ) ;
	     //config.getCPSubsystemConfig().setCPMemberCount(3);
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
		 
		//Hazelcast.newHazelcastInstance(config);
		 
		return status ;
	}
}
