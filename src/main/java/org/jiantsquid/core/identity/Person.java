package org.jiantsquid.core.identity;

import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;

public class Person extends Identity {

    private static final long serialVersionUID = 2914783986491711458L;

    public Person() throws NoSuchAlgorithmException, NoSuchProviderException {
        super();
    }

    @Override
    public boolean is(Identity entity) {
        return entity.getId().equals( getId()) ;
    }

    @Override
    public String getId() {
        return "" ;
    }

}
