/*
 * 
 */
package org.metavrp.solution;

/**
 *
 * @author David Pinheiro
 */
public class StopCondition {
    
    // Maximum nº of generations. When this nº of generations is reached, the algorithm stops.
    private int maxNumGenerations;
    
    // Maximum nº of generations without any improvement. When there's no improvement for this number 
    // of generations, the algorithm stops.
    private int maxNumGenerationsWtImprovement;
    
    // Maximum nº of generations without a given improvement percentage
    private int maxNumGenerationsWtImprovingPercentage;
    private float improvingPercentage;

    /**
     * Constructor that doesn't initialize any instance variable
     */
    public StopCondition() {
    }

    /**
     * Constructor that initializes the maximum nº of generations
     * @param maxNumGenerations
     */
    public StopCondition(int maxNumGenerations) {
        this.maxNumGenerations = maxNumGenerations;
    }
    
    
    /*
     * Getters and Setters
     */

    public int getMaxNumGenerations() {
        return maxNumGenerations;
    }

    public void setMaxNumGenerations(int maxNumGenerations) {
        this.maxNumGenerations = maxNumGenerations;
    }

    public int getMaxNumGenerationsWtImprovement() {
        return maxNumGenerationsWtImprovement;
    }

    public void setMaxNumGenerationsWtImprovement(int maxNumGenerationsWtImprovement) {
        this.maxNumGenerationsWtImprovement = maxNumGenerationsWtImprovement;
    }
    
}
