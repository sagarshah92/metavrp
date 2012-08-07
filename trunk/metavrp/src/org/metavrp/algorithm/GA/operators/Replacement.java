package org.metavrp.algorithm.GA.operators;

import java.util.Arrays;
import org.metavrp.algorithm.GA.Chromosome;
import org.metavrp.algorithm.GA.Population;
import org.metavrp.problem.CostMatrix;

/**
 *
 * @author David Pinheiro
 */
public class Replacement {
    // Creates the new population, replacing the old one.
    // The elitism defines the percentage of chromosomes that are copied 
    // from the old to the new population.
    public static Population populationReplacement(Chromosome[] matingPool, Population pop, float elitism, CostMatrix costMatrix){
//System.out.println("Population Replacement with elitism %: "+(elitism*100));
        int nrEliteChromosomes = (int) (pop.getPopSize()*elitism); //Number of chromosomes to keep
        
        //
        if (matingPool.length < pop.getPopSize() - nrEliteChromosomes){
            throw new AssertionError("[ERROR] The number of generated chromossomes is "
                    + "insufficient to create a new population!");
        }
        
        // Get the top chromosomes (the elite)
        Chromosome[] elitistChromosomes = pop.getTop(nrEliteChromosomes);
        
        // Get the rest of the chromosomes from the new population
        Chromosome[] newChromosomes = Arrays.copyOfRange(matingPool, 0, pop.getPopSize() - nrEliteChromosomes);
        
        // The new population is the union of the two
        Population newPopulation = new Population(newChromosomes, elitistChromosomes, costMatrix, pop.getGeneticAlgorithm());
        
        return newPopulation;
    }
}
