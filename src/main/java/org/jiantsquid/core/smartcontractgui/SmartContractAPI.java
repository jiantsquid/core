package org.jiantsquid.core.smartcontractgui;


import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;

import org.jiantsquid.core.application.JiantsquidClientApplicationI;
import org.jiantsquid.core.identity.Account;
import org.jiantsquid.core.identity.Identity;
import org.jiantsquid.core.utils.SillyUtils;


public class SmartContractAPI {

    private final JiantsquidClientApplicationI application ;
    private final Account entity ;
    
    public SmartContractAPI( JiantsquidClientApplicationI application, Account entity ) {
        this.application = application ;
        this.entity = entity ;
    }
    
    public void open( String service ) throws NoSuchAlgorithmException, NoSuchProviderException  {
        application.openService( service ) ;
    }
    
    public Identity getEntity() {
        return entity ;
    }
    public Account getAccount() {
        return entity ;
    }
    public void todo( String todo, Exception e ) {
        SillyUtils.todo( todo, e) ;
    }
}
