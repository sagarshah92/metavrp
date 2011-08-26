
package org.metavrp.GA.operators;

/**
 * This class represents the chosen genetic operators used to create a new population.
 * @author David Pinheiro
 */
public class Operators {
    private String selectionOperator;   // The chosen selection operator
    private int selectionParam;         // Used for the number of elements on the tournament selection
    
    private String crossoverOperator;   // The chosen crossover operator 
    private float crossoverProb;        // The probability of crossover
    private int crossoverParam;         // TODO: see if this can be used
    
    private String mutationOperator;    // The chosen mutation operator
    private float mutationProb;         // The probability of mutation
    private int mutationParam;          // TODO: see if this can be used
    
    private String replacementOperator; // The chosen mutation operator
    private float replacementElitism;   // The elitism that should be used

    public String getCrossoverOperator() {
        return crossoverOperator;
    }

    public void setCrossoverOperator(String crossoverOperator) {
        this.crossoverOperator = crossoverOperator;
    }

    public int getCrossoverParam() {
        return crossoverParam;
    }

    public void setCrossoverParam(int crossoverParam) {
        this.crossoverParam = crossoverParam;
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

    public int getMutationParam() {
        return mutationParam;
    }

    public void setMutationParam(int mutationParam) {
        this.mutationParam = mutationParam;
    }

    public float getMutationProb() {
        return mutationProb;
    }

    public void setMutationProb(float mutationProb) {
        this.mutationProb = mutationProb;
    }

    public float getReplacementElitism() {
        return replacementElitism;
    }

    public void setReplacementElitism(float replacementElitism) {
        this.replacementElitism = replacementElitism;
    }

    public String getReplacementOperator() {
        return replacementOperator;
    }

    public void setReplacementOperator(String replacementOperator) {
        this.replacementOperator = replacementOperator;
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


    
}
