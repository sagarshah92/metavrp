/*
 * 
 */
package org.metavrp;

import org.metavrp.solution.Statistics;
import org.metavrp.solution.StopCondition;

/**
 *
 * @author David Pinheiro
 */
public class Solution {
    // The stopping condition
    private StopCondition stopCondition;
    
    // Statistics output definition
    private Statistics statistics;

    /**
     * Constructor that doesn't initialize any instance variable
     */
    public Solution() {
        
    }

    /**
     * Constructor that initializes the stop condition and the statistics definitions
     * @param stopCondition
     * @param statistics 
     */
    public Solution(StopCondition stopCondition, Statistics statistics) {
        this.stopCondition = stopCondition;
        this.statistics = statistics;
    }

    /*
     * Getters and Setters
     */
    
    public Statistics getStatistics() {
        return statistics;
    }

    public void setStatistics(Statistics statistics) {
        this.statistics = statistics;
    }

    public StopCondition getStopCondition() {
        return stopCondition;
    }

    public void setStopCondition(StopCondition stopCondition) {
        this.stopCondition = stopCondition;
    }
    
}
