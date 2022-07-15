package org.jiantsquid.core.smartcontractgui;

import javax.swing.JComponent;

import org.jiantsquid.core.sandbox.SwitchableSecurityManager;


public abstract class SmartContractGUI<T extends JComponent> {

    private T component ;
    private SmartContractAPI api ;
    
    protected abstract T createGUI() ;
    public abstract void attachActions() ;
    
    final public void activate( SmartContractAPI api ) {
        this.api = api ;
        
        component = createGUI() ;
        attachActions() ;
    }
    
    final public T getComponent() {
        return component ;
    }
    
    final public SmartContractAPI getSmartContractAPI() {
        return api ;
    }
}
