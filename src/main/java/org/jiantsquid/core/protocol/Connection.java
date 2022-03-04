package org.jiantsquid.core.protocol;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;

import org.jiantsquid.core.network.p2p.message.Request;
import org.jiantsquid.core.network.p2p.message.Response;


public class Connection  {

	private Socket socket ;
	
	private String host ;
	private int port ;
	
	private InputStream  in ;
	private OutputStream out ;
	
	private Protocol protocol = new Protocol() ;
	
	public Connection( String host, int port ) throws IOException {
		
		this.host = host ;
		this.port = port ;
		// connect to a host
		socket = new Socket() ;
	}
	
	public Connection( Socket socket ) {
		
		// receive a connection
		this.socket = socket ;
		host = socket.getInetAddress().getHostAddress() ;
		port = socket.getPort() ;
	}
	
	public void connect() throws IOException {
		socket.connect( new InetSocketAddress( host, port ) );
	}
	
	public String getHost() {
		return host ;
	}
	
	public int getPort() {
		return port ;
	}
	
	
	private void initStreams() throws IOException {
		if( out == null ) {
			out = socket.getOutputStream() ;
		}
		if( in == null ) {
			in = socket.getInputStream() ;
		}
	}
	public void close() throws IOException {
		socket.close(); 
	}
	
	public void respond( Response response ) throws IOException {
		initStreams() ;
		protocol.respond( out, response );
	}
	
	public Response sendRequest( Request request ) throws IOException {
		initStreams() ;
		return protocol.sendRequest(in,  out, request );
	}
	
	public Request readRequest() throws IOException {
		initStreams() ;
		return protocol.readRequest( in ) ;
	}
}
