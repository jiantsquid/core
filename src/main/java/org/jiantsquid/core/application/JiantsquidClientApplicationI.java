package org.jiantsquid.core.application;

import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;

public interface JiantsquidClientApplicationI {

    void openService( String service ) throws NoSuchAlgorithmException, NoSuchProviderException ;
}
