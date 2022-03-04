package org.jiantsquid.core.protocol;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.StringWriter;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

import org.jiantsquid.core.network.p2p.message.Request;
import org.jiantsquid.core.network.p2p.message.Response;

import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

public class Protocol {

	public static final String VERSION = "Jiant Squid protocol version 1.0" ;
	
	ObjectMapper objectMapper ;
	
	Protocol() {
		init() ;
	}
	
	private void init() {
		
		JsonFactory jsonFactory = new JsonFactory();
		jsonFactory.disable( JsonGenerator.Feature.AUTO_CLOSE_TARGET ) ;
		jsonFactory.disable( JsonParser.Feature.AUTO_CLOSE_SOURCE ) ;
		jsonFactory.disable( JsonGenerator.Feature.IGNORE_UNKNOWN ) ;
		
		objectMapper = new ObjectMapper(jsonFactory);
		objectMapper.setVisibility(PropertyAccessor.GETTER, Visibility.NONE);
		objectMapper.setVisibility(PropertyAccessor.FIELD, Visibility.ANY);
		objectMapper.configure( SerializationFeature.FAIL_ON_EMPTY_BEANS, false ) ;
	}
	
	private String convert( Object o ) throws IOException {
		StringWriter writer = new StringWriter() ;
		objectMapper.writeValue( writer, o );
		return writer.toString() ;
	}
	
	private <T> T convert( InputStream in, Class<T> clazz ) throws IOException {
		
		StringBuilder buffer = new StringBuilder() ;
		byte[] b = new byte[1024] ;
		int read ;
		
		while( ( read = in.read( b ) ) > -1 ) {
			buffer.append( new String(  Arrays.copyOf( b, read ), StandardCharsets.UTF_8) ) ;
			
			if( in.available() == 0 ) {
				String str = buffer.toString();
				return objectMapper.readValue (str, clazz ) ;
			}
		}
		
		return objectMapper.readValue (buffer.toString(), clazz ) ;
	}
	
	Response sendRequest( InputStream in,  OutputStream out, Request request ) throws IOException {
		out.write( convert( request ).getBytes() );
		return readResponse( in ) ;
	}
	
	void respond( OutputStream out, Response response ) throws IOException {
		out.write( convert( response ).getBytes() );
	}
	
	void writeResponse( OutputStream out, Response response  ) throws IOException {
		out.write( convert( response ).getBytes() );
	}
	
	Request readRequest( InputStream in ) throws IOException {
		//return objectMapper.readValue( in, Request.class );
		return convert( in, Request.class ) ;
	}
	
	private Response readResponse( InputStream in ) throws IOException {
		return objectMapper.readValue( in, Response.class );
		//return convert( in, Response.class ) ;
	}
}
