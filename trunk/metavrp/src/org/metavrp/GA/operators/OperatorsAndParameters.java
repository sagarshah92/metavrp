
package org.metavrp.GA.operators;

/**
 * This class represents the chosen genetic operators used to create a new population.
 * @author David Pinheiro
 */
public class OperatorsAndParameters {
    private int populationSize;         // Size of the population
    
    private String selectionOperator;   // The chosen selection operator
    private float selectionPercentage;  // The selection percentage as a float between 0 and 1
    private int selectionParam;         // Used for the number of elements on the tournament selection
    
    private String crossoverOperator;   // The chosen crossover operator 
    private float crossoverProb;        // The probability of crossover
    
    private String mutationOperator;    // The chosen mutation operator
    private float mutationProb;         // The probability of mutation
    
    private String replacementOperator; // The chosen replacement operator
    private float replacementElitism;   // The elitism that should be used
    
    private float innerDepotPenalty;    // Penalty given to the solutions in which the vehicle visits the depot inside a tour

    /*
     * Initialize with default values
     */
    public OperatorsAndParameters() {
        // Default Values
        this.populationSize = 1000;
        this.selectionOperator = "Selection.tournamentSelection";
        this.selectionPercentage = 1f;
        this.selectionParam = 2;
        this.crossoverOperator = "Order1.Order1";
        this.crossoverProb = 0.8f;
        this.mutationOperator = "SwapNextMutation.swapNextMutation";
        this.mutationProb = 0.01f;
        this.replacementOperator = "Replacement.populationReplacement";
        this.replacementElitism = 0.1f;
        this.innerDepotPenalty = 0.0f;
    }

    
    public int getPopulationSize() {
        return populationSize;
    }

    public void setPopulationSize(int populationSize) {
        this.populationSize = populationSize;
    }
        
    public String getSelectionOperator() {
        return selectionOperator;
    }

    public void setSelectionOperator(String selectionOperator) {
        this.selectionOperator = selectionOperator;
    }
    
    public int getSelectionParam() {
        return selectionParam;
    }

    public void setSelectionParam(int selectionParam) {
        this.selectionParam = selectionParam;
    }

    public float getSelectionPercentage() {
        return selectionPercentage;
    }

    public void setSelectionPercentage(float selectionPercentage) {
        this.selectionPercentage = selectionPercentage;
    }
    
    public String getCrossoverOperator() {
        return crossoverOperator;
    }

    public void setCrossoverOperator(String crossoverOperator) {
        this.crossoverOperator = crossoverOperator;
    }

    public float getCrossoverProb() {
        return crossoverProb;
    }

    public void setCrossoverProb(float crossoverProb) {
        this.crossoverProb = crossoverProb;
    }

    public String getMutationOperator() {
        return mutationOperator;
    }

    public void setMutationOperator(String mutationOperator) {
        this.mutationOperator = mutationOperator;
    }

    public float getMutationProb() {
        return mutationProb;
    }

    public void setMutationProb(float mutationProb) {
        this.mutationProb = mutationProb;
    }

    public String getReplacementOperator() {
        return replacementOperator;
    }

    public void setReplacementOperator(String replacementOperator) {
        this.replacementOperator = replacementOperator;
    }
    
    public float getReplacementElitism() {
        return replacementElitism;
    }

    public void setReplacementElitism(float replacementElitism) {
        this.replacementElitism = replacementElitism;
    }

    public float getInnerDepotPenalty() {
        return innerDepotPenalty;
    }

    public void setInnerDepotPenalty(float innerDepotPenalty) {
        this.innerDepotPenalty = innerDepotPenalty;
    }

}
