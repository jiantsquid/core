package org.jiantsquid.core.identity;

import java.io.Serializable;
import java.security.*;

import org.jiantsquid.core.crypto.KeyPairGeneration;

public class Wallet implements Serializable {

    private static final long serialVersionUID = 8856789145775832590L;

    private transient KeyPair keyPair ;
    
    private transient KeyPairGeneration gk ;
    
    private PublicKey  publicKey ;
    private PrivateKey privateKey ;
    
    public Wallet() throws NoSuchAlgorithmException, NoSuchProviderException{
        generateKeyPair();  
    }
        
    public void generateKeyPair() throws NoSuchAlgorithmException, NoSuchProviderException {
        gk         = new KeyPairGeneration( "DSA", 1024, "".getBytes() );
        keyPair    = gk.createKeys();    
        publicKey  = keyPair.getPublic() ;
        privateKey = keyPair.getPrivate() ;
    }
    
    public PublicKey getPublicKey() {
        return publicKey ;
    }
    
    public PrivateKey getPrivateKey() {
        return privateKey ;
    }
    
}
