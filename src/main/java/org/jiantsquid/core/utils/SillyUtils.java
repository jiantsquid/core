package org.jiantsquid.core.utils;

public class SillyUtils {

    public static void todo( String todo, Exception e ) {
        
        System.err.println( "*********************** BEGIN TO DO ************************" ) ;
        System.err.println( todo );
        if( e != null ) {
            e.printStackTrace();
        }
        System.err.println( "*********************** END TO DO ************************" ) ;
    }
}
