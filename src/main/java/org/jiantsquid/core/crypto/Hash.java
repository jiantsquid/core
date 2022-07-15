package org.jiantsquid.core.crypto;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Hash {

	public static String hash( String dataToHash ) {
		MessageDigest digest;
		StringBuilder buffer = new StringBuilder();
		try {
			digest = MessageDigest.getInstance("SHA-256");
			byte[] bytes = digest.digest(dataToHash.getBytes(StandardCharsets.UTF_8.name()));
			
			for (byte b : bytes) {
				buffer.append(String.format("%02x", b));
			}
		} catch (NoSuchAlgorithmException | UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		
		return buffer.toString();
	}
}
