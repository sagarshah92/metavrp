package org.metavrp.GA.operators;

import org.metavrp.GA.Chromosome;
import org.metavrp.GA.Gene;
import org.metavrp.GA.support.Randomizer;
import java.util.Arrays;

/*********************
 * Mutation Operators
 * *******************
 * 
 * TODO: Insert Mutation, Swap Mutation, Inversion Mutaion, ...
 * 
 * @author David Pinheiro
 */
public class Mutation {

    // Picks one gene at random and swaps his position with the one before.
    // Preserves most of adjacency information (2 links broken)
    // If the probability is higher than 1 we swap more than once
    public static Chromosome swapNextMutation(float probability, Chromosome genes) {
        //We can use a probability higher than 1 to enable more than 1 swap between genes
//        if (probability > 1){
//            System.out.println("[Warning] The swapNext mutation probability is higher than 1. "
//                    + "I'll do more than one swapping.");
//        }
        // Executes the swapping (more than once if probability > 1)
        do {
            if (Randomizer.doIt(probability)){
                // Declare the first swapping point
                int a;
                a=Randomizer.randomInt(genes.getLenght());
                
                if (a==0) { // If its the first node, swap it with the last one
                    genes.swapGenes(a,genes.getLenght()-1);
                }
                else {      // Else swap it with the gene before
                    genes.swapGenes(a,a-1);
                }
            }
//else {System.out.println("Não foi efectuado nextMutation neste cromossoma");}            
            probability--;  //Decrement probability 
        } while (probability > 0);
        
        // Measure the new fitness of the chromosome
        genes.updateFitness();
        
        // Return the modified chromosome
        return genes;
    }
    
    // Picks two genes at random and swaps their positions
    // Preserves most of adjacency information (4 links broken)
    // If the probability is higher than 1 we swap more than once
    public static Chromosome swapMutation(float probability, Chromosome genes) {
        //We can use a probability higher than 1 to enable more than 1 swap between genes
//        if (probability > 1){
//            System.out.println("[Warning] The swap mutation probability is higher than 1. "
//                    + "We'll do more than one swapping.");
//        }
        boolean done = false;
        // Executes the swapping (more than once if probability > 1)
        while (Randomizer.doIt(probability)){
            done = true;
            int a,b;
            do {
               a=Randomizer.randomInt(genes.getLenght()); //Choose the first gene position
               b=Randomizer.randomInt(genes.getLenght()); //Choose the second gene position
            } while (a==b);
            genes.swapGenes(a,b); // Do the actual swapping
            probability--;  // Reduce one to the probability (for the case that probability
                            // is bigger than 1 and we do more than one swapping
        }
        
        // Measure the new fitness of the chromosome
        if (done) genes.updateFitness();
        
        // Return the modified chromosome
        return genes;
    }

    // Picks two genes at random and reverses their positions and all the genes in between.
    // Preserves most of adjacency information (2 links broken) but disrupts the order.
    // If the probability is higher than 1 we swap more than once.
    // TODO: Verificar se este método está a funcionar bem.
    public static Chromosome inversionMutation(float probability, Chromosome genes) {
        //We can use a probability higher than 1 to enable more than 1 swaping operation
//        if (probability > 1){
//            System.out.println("[Warning] The inversion mutation probability is higher than 1. "
//                    + "We'll do more than one swapping");
//        }
 
        while (Randomizer.doIt(probability)){
            int a,b;
            do {
               a=Randomizer.randomInt(genes.getLenght()); //Choose the first gene position
               b=Randomizer.randomInt(genes.getLenght()); //Choose the second gene position
            } while (a==b);

            // Do the actual swapping
            // Sort a and b
            if (b<a){int c=a; a=b; b=c;}
            // Copy the specified range to another array
            Gene[] subGenes = Arrays.copyOfRange(genes.getGenes(), a, b+1);
            // Copy back, in reverse order, to the original array
            for (int i=subGenes.length-1; i>=0; i--){
                genes.setGene(subGenes[i], a);
                a++;
            }
            probability--;  // Reduce one to the probability (for the case that probability
                            // is bigger than 1 and we do more than one swapping
        }
        
        // Measure the new fitness of the chromosome
        genes.updateFitness();
        
        // Return the modified chromosome
        return genes;
    }
    
    
    //TODO: Adicionar Insert Mutation. Ver Eiben
    
}
