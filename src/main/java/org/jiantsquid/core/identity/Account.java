package org.jiantsquid.core.identity;

import java.io.Serializable;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;

public class Account extends Identity implements Serializable {

    public Account() throws NoSuchAlgorithmException, NoSuchProviderException {
        super();
    }

    private static final long serialVersionUID = -7298719052231649706L;

    @Override
    public boolean is(Identity entity) {
        return entity.getId().equals( getId()) ;
    }

    @Override
    public String getId() {
        return getWallet().getPublicKey().toString() ;
    }

}
