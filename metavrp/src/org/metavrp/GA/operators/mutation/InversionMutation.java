
package org.metavrp.GA.operators.mutation;

import org.metavrp.GA.Chromosome;
import org.metavrp.GA.Gene;
import org.metavrp.GA.support.Randomizer;

/*********************
 * Inversion Mutation
 * *******************
 * 
 * For a description look at 
 * A. Eiben e J. Smith, Introduction to Evolutionary Computing, Springer-Verlag, 2003
 * or other related resource
 * 
 * @author David Pinheiro
 */
public class InversionMutation{

    /*
     * For each gene of the Chromosome draw a random number, if it is lower than
     * the probability parameter, randomly choose another gene and invert  the 
     * sub-tour between them.
     * Preserves most of the adjacency information (only 2 links are broken).
     * In Asymmetric problems this can make a big perturbation on the solution.
     */
    public static Chromosome inversionMutation(float probability, Chromosome genes) {
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
                
                // Get the indexes of the genes
                int indexA = genes.indexOf(geneA);
                int indexB = genes.indexOf(geneB);
                
                // Sort the indexes
                int lowerIndex = 0;
                int biggerIndex = 0;
                if (indexA<indexB){
                    lowerIndex=indexA;
                    biggerIndex=indexB;
                } else if (indexA>indexB){
                    lowerIndex=indexB;
                    biggerIndex=indexA;
                }
              
                // While the indexes are different, swap the genes and then adjust the indexes
                while(lowerIndex <= biggerIndex){
                    genes.swapGenes(lowerIndex, biggerIndex);
                    lowerIndex++;
                    biggerIndex--;
                }
            }
        }
     
        // Measure the new fitness of the chromosome
        genes.updateFitness();

        // Return the modified chromosome
        return genes;
    }
}