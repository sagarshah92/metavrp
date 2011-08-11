/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package GA;

/**
 * This class represents a generation.
 * We start with the old population and do:
 *  - Selection
 *  - Crossover
 *  - Mutation 
 *  - Replacement 
 * with some parameters (probability, number of elements, etc...) to get a new population
 * @author David Pinheiro
 */
public class Generation {
    
    private Population oldPop; // The old population
    private Population newPop; // The new population
    private int genNumber;      // The number of the generation, witch increments at each new generation
    
    private float bestFitness;      //Best fitness value in the generation
    private float worstFitness;     //Worst fitness value in the generation
    private float averageFitness;   //Average fitness value in the generation
    private float medianFitness;    //The median fitness value in the generation

    public Generation(Population oldPop, int genNumber, Operators operators) {
        this.oldPop = oldPop;
        this.genNumber=genNumber;
        
    }
    
   
    
}
