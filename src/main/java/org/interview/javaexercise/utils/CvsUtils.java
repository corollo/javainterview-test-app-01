package org.interview.javaexercise.utils;

import java.util.Date;

/**
 * CVS utility class.
 * @author luigi.corollo
 */
public class CvsUtils {

    /**
     * Add double quotes to the input field.
     * @param field
     * @return
     */
    public static String addDoubleQuote(String field){
        StringBuffer exportFormat = new StringBuffer();
        if(field!=null) {
            exportFormat.append("\"").append(field.replace("\n", "")).append("\"");
        }
        return exportFormat.toString();
    }

    /**
     * Add double quotes to the input field
     * @param field
     * @return
     */
    public static String addDoubleQuote(Date field){
        StringBuffer exportFormat = new StringBuffer();
        if(field!=null) {
            exportFormat.append("\"").append(field).append("\"");
        }
        return exportFormat.toString();
    }

}
