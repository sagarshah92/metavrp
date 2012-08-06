/*
 * 
 */
package org.metavrp.algorithm;

import java.util.ArrayList;
import org.metavrp.Algorithm;
import org.metavrp.Problem;
import org.metavrp.algorithm.GA.Gene;
import org.metavrp.algorithm.GA.operators.OperatorsAndParameters;

/**
 * 
 * @author David Pinheiro
 */
public class GeneticAlgorithm implements Algorithm{
    
    // The Genetic Algorithm's operators and parameters
    private OperatorsAndParameters operatorsAndParameters;
    
    // The Problem definition
    private Problem problem;

    /**
     * Constructor that sets the problem definition
     * @param problem 
     */
    // TODO: Verify that the problem has a minimum of 2 customers, 1 depot and 1 vehicle
    public GeneticAlgorithm(Problem problem) {
        this.problem = problem;
    }

    /**
     * Constructor that initializes the operators and parameters and the problem
     * @param operatorsAndParameters 
     */
    // TODO: Verify that the problem has a minimum of 2 customers, 1 depot and 1 vehicle
    public GeneticAlgorithm(OperatorsAndParameters operatorsAndParameters, Problem problem) {
        this.operatorsAndParameters = operatorsAndParameters;
        this.problem = problem;
    }

    /*
     * Getters and Setters
     */

    /**
     * Gets the operators and parameters
     * @return 
     */
    public OperatorsAndParameters getOperatorsAndParameters() {
        return operatorsAndParameters;
    }

    /**
     * Sets the GA's operators and parameters
     * @param op 
     */
    public void setOperatorsAndParameters(OperatorsAndParameters op) {
        this.operatorsAndParameters = op;
    }
    
    
    /**
     * Returns a list of all the genes
     */
    public ArrayList<Gene> getGenes() {
        // Create an array of all the possible genes (vehicles and customers)
        ArrayList<Gene> genes = new ArrayList<Gene>(problem.getCustomers().size() + problem.getVehicles().size());
        
        // Add the customers
        genes.addAll(problem.getCustomers());
        
        // Add the vehicles
        genes.addAll(problem.getVehicles());
        
        return genes;
    }
    
    /**
     * Returns a list of all the genes properly cloned
     * @return 
     */
    // TODO: This method is necessary?
    public ArrayList<Gene> getClonedGenes() {
        return (ArrayList<Gene>)getGenes().clone();
    }
    
    /**
     * Get the number of genes that the GA will use
     * This is the number of costumers + the number of vehicles
     * @return 
     */
    public int getNrGenes(){
        return getNrCustomers() + getNrVehicles();
    }

    /**
     * Returns the number of customers, as defined on the problem
     * @return 
     */
    public int getNrCustomers() {
        return problem.getCustomers().size();
    }
    
    /**
     * Returns the number of vehicles, as defined on the problem
     * @return 
     */
    public int getNrVehicles() {
        return problem.getVehicles().size();
    }
    
    /**
     * Returns the number of depots, as defined on the problem
     * @return 
     */
    public int getNrDepots() {
        return problem.getDepots().size();
    }

    /**
     * Returns the problem that this GA will try to solve
     * @return 
     */
    public Problem getProblem() {
        return problem;
    }

}
