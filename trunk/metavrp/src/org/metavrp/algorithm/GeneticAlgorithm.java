/*
 * 
 */
package org.metavrp.algorithm;

import org.metavrp.Algorithm;
import org.metavrp.algorithm.GA.operators.OperatorsAndParameters;

/**
 *
 * @author David Pinheiro
 */
public class GeneticAlgorithm implements Algorithm{
    
    // The Genetic Algorithm operators and parameters
    private OperatorsAndParameters operatorsAndParameters;

    /**
     * Constructor that doesn't initialize any instance variable
     */
    public GeneticAlgorithm() {
    }

    /**
     * Constructor that initializes the operators and parameters
     * @param operatorsAndParameters 
     */
    public GeneticAlgorithm(OperatorsAndParameters operatorsAndParameters) {
        this.operatorsAndParameters = operatorsAndParameters;
    }

    /*
     * Getters and Setters
     */
    
    /**
     * 
     * @return 
     */
    public OperatorsAndParameters getOperatorsAndParameters() {
        return operatorsAndParameters;
    }

    /**
     * 
     * @param op 
     */
    public void setOperatorsAndParameters(OperatorsAndParameters op) {
        this.operatorsAndParameters = op;
    }
    
    
}
