package org.jiantsquid.core.crypto;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

public class KeyPairGeneration {

    private KeyPairGenerator keyGen;
   
    public KeyPairGeneration( String algorithm, int keylength, byte[] seed ) throws NoSuchAlgorithmException {
        this.keyGen = KeyPairGenerator.getInstance( algorithm );
        this.keyGen.initialize(keylength, new SecureRandom( seed ) ) ;
    }

    public KeyPair createKeys() {
        return keyGen.generateKeyPair();
    }
}
