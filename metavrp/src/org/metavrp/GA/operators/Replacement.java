package org.metavrp.GA.operators;

import org.metavrp.GA.Chromosome;
import org.metavrp.GA.Population;
import org.metavrp.VRP.CostMatrix;
import org.metavrp.GA.GeneList;
import java.util.Arrays;

/**
 *
 * @author David Pinheiro
 */
public class Replacement {
    // Creates the new population, replacing the old one.
    // The elitism defines the percentage of chromosomes that are copied 
    // from the old to the new population.
    public static Population populationReplacement(Chromosome[] newPop, Population pop, float elitism, GeneList geneList, CostMatrix costMatrix){
        int nr_chromosomes = (int) (pop.getPopSize()*elitism); //Number of chromosomes to keep
        
        //
        if (newPop.length < pop.getPopSize() - nr_chromosomes){
            throw new AssertionError("[ERROR] The number of generated chromossomes is "
                    + "insufficient to create a new population!");
        }
        
        // Get the top chromosomes (the elite)
        Chromosome[] elitistChromosomes = pop.getTop(nr_chromosomes);
        
        // Get the rest of the chromosomes from the new population
        Chromosome[] newChromosomes = Arrays.copyOfRange(newPop, 0, pop.getPopSize() - nr_chromosomes);
        
        // The new population is the union of the two
        Population newPopulation = new Population(newChromosomes, elitistChromosomes, geneList, costMatrix);
        
        return newPopulation;
    }
}
