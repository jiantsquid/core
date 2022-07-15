package org.jiantsquid.core.identity;

import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.util.ArrayList;
import java.util.List;


public class Community extends Identity {

    protected Community() throws NoSuchAlgorithmException, NoSuchProviderException {
        super();
    }
    private List<Community> communities = new ArrayList<>();
    private List<Person> persons = new ArrayList<>();
    @Override
    public boolean is(Identity entity) {
        // TODO Auto-generated method stub
        return false;
    }
    @Override
    public String getId() {
        // TODO Auto-generated method stub
        return null;
    }
}
