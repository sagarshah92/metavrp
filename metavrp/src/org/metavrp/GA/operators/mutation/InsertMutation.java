
package org.metavrp.GA.operators.mutation;

import org.metavrp.GA.Chromosome;
import org.metavrp.GA.Gene;
import org.metavrp.GA.support.Randomizer;

/*********************
 * Insert Mutation
 * *******************
 * 
 * For a description look at 
 * A. Eiben e J. Smith, Introduction to Evolutionary Computing, Springer-Verlag, 2003
 * or other related resource
 * 
 * @author David Pinheiro
 */
public class InsertMutation{

    /*
     * For each gene of the Chromosome draw a random number, if it is lower than
     * the probability parameter, randomly choose another gene, then keep moving
     * the other gene until it gets next to this one.
     * Preserves most of the adjacency information (only 3 links are broken).
     */
    public static Chromosome insertMutation(float probability, Chromosome genes) {
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

                // Now keep swapping the gene with the biggest index, backwards on
                // the chromossome, until it is next to the other one.
                while (indexA + 1 < indexB){
                    // If the index of the gene next to geneA is lower than the index of geneB,
                    // keep moving geneB backwards on the chromosome until it is the neighbour of geneA
                    genes.swapGenes(indexB, indexB-1);
                    indexB--;
                }
                while(indexA - 1 > indexB){
                    // If the index of the gene before geneA is bigger than the index of geneB,
                    // keep moving geneB forward on the chromosome until it is the neighbour of geneA
                    genes.swapGenes(indexB, indexB+1);
                    indexB++;
                }
            }
        }
     
        // Measure the new fitness of the chromosome
        genes.updateFitness();
        
        // Return the modified chromosome
        return genes;
    }
}