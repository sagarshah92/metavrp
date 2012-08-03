/*
 * 
 */
package org.metavrp.solution;

import java.io.File;

/**
 *
 * @author David Pinheiro
 */
public class Statistics {
    
    /** The file that will be used to output statistics */
    private File statsFile;

    /**
     * Constructor that doesn't initialize nothing
     */
    public Statistics() {
    }
    
    /**
     * Constructor that initializes the statistics file
     * @param statsFile 
     */
    public Statistics(File statsFile) {
        this.statsFile = statsFile;
    }
    
    /*
     * Getters and Setters
     */

    public File getStatsFile() {
        return statsFile;
    }

    public void setStatsFile(File statsFile) {
        this.statsFile = statsFile;
    }

}
