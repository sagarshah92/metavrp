
package org.metavrp.GA.operators.mutation;

import org.metavrp.GA.Chromosome;
import org.metavrp.GA.Gene;
import org.metavrp.GA.support.Randomizer;

/*********************
 * Swap Next Mutation
 * *******************
 * 
 * For a description look at 
 * A. Eiben e J. Smith, Introduction to Evolutionary Computing, Springer-Verlag, 2003
 * or other related resource
 * 
 * @author David Pinheiro
 */
public class SwapNextMutation{

    /*
     * For each gene of the Chromosome draw a random number, if it is lower than
     * the probability parameter, swap that gene with the one next to it.
     * Preserves most of the adjacency information (only 2 links are broken).
     */
    public static Chromosome swapNextMutation(float probability, Chromosome genes) {
        // For each gene
        for (Gene gene:genes.getGenes()){
            // If we should do it...
            if (Randomizer.doIt(probability)){                   
                // Swap the gene with the next one
                genes.swapWithNextGene(gene);
            }
        }

        // Return the modified chromosome
        return genes;
    }

}