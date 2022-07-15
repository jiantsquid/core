package org.jiantsquid.core.smartcontractgui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;

import javax.swing.JLabel;
import javax.swing.JPanel;

public abstract class AbstractSmartContractGUI extends SmartContractGUI<JPanel> {

    private JPanel mainPanel ;
    private JPanel centerPanel = new JPanel(); 
    
    public AbstractSmartContractGUI() {} 
    
    protected JPanel createPanel( String title ) {
        mainPanel = new JPanel();
        mainPanel.setLayout( new BorderLayout() ) ;
        
        JPanel titlePanel = new JPanel();
        titlePanel.setPreferredSize( new Dimension( 300, 50 ) ) ;
        titlePanel.setMaximumSize( new Dimension( 10000, 50 ) ) ;
        titlePanel.setBackground( Color.LIGHT_GRAY ) ;
        JLabel label = new JLabel( title ) ;
        
        titlePanel.add( label, BorderLayout.CENTER ) ;
        mainPanel.add( titlePanel, BorderLayout.NORTH ) ;
        
        createCenterPanel() ;
        
        return mainPanel ;
    }
    
    protected JPanel createCenterPanel() {
        
        centerPanel.setLayout( new BorderLayout() ) ;
        centerPanel.setBackground( Color.WHITE ) ;
        mainPanel.add( centerPanel, BorderLayout.CENTER ) ;
        return centerPanel ;
    }
    
    public JPanel getMainPanel() {
        return mainPanel ;
    }
    public JPanel getCenterPanel() {
        return centerPanel ;
    }

}
