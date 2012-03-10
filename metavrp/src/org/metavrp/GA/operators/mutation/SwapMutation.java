
package org.metavrp.GA.operators.mutation;

import org.metavrp.GA.Chromosome;
import org.metavrp.GA.Gene;
import org.metavrp.GA.support.Randomizer;

/*********************
 * Swap Mutation
 * *******************
 * 
 * For a description look at 
 * A. Eiben e J. Smith, Introduction to Evolutionary Computing, Springer-Verlag, 2003
 * or other related resource
 * 
 * @author David Pinheiro
 */
public class SwapMutation{

    /*
     * For each gene of the Chromosome draw a random number, if it is lower than
     * the probability parameter, swap that gene with another, randomly chosen, one.
     * Preserves most of the adjacency information (only 3 links are broken).
     */
    public static Chromosome swapMutation(float probability, Chromosome genes) {
        // For each gene
        for (Gene geneA:genes.getGenes()){
            // If we should do it...
            if (Randomizer.doIt(probability)){
                // Choose another gene
                Gene geneB = null;
                do{
                    int randomIndex = Randomizer.randomInt(genes.getLenght());
                    geneB = genes.getGene(randomIndex);
                } while(geneA==geneB);
                
                // Swap these genes
                genes.swapGenes(geneA, geneB);
            }
        }
     
        // Measure the new fitness of the chromosome
        genes.updateFitness();
        
        // Return the modified chromosome
        return genes;
    }
}