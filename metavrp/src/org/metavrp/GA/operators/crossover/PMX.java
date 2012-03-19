package org.metavrp.GA.operators.crossover;

import java.util.ArrayList;
import java.util.Arrays;
import org.metavrp.GA.Chromosome;
import org.metavrp.GA.Gene;
import org.metavrp.GA.support.Randomizer;

/*********************
 * PMX (Partially Mapped Crossover)
 * *******************
 * For a description look at:
 * 
 * D. Whitley. "Permutations", in T. Bäck, D. Fogel, Z. Michalewicz. "Evolutionary Computation 1: 
 * Basic Algorithms and Operators", Institute of Physics Publishing, 2000, Cap. 33.3, Pag 275-276
 * 
 * or
 * 
 * A. Eiben, J. Smith. "Introduction to Evolutionary Computing", Springer-Verlag, 2003, pag 52-54
 * 
 * @author David Pinheiro
 */
public class PMX {
    
    /*
     * PMX Crossover.
     * Returns, with some given probability, two chromosomes (the childs) as the
     * crossover between parents.
     */
    public static Chromosome[] PMX (Chromosome[] parents, float probability){
        
        // Warning of out of scope probability
        if (probability < 0 || probability >1){
            System.out.println("[WARNING] Probability of PMX crossover is "+probability+
                    ", but should be between 0 and 1.");
        }
        // Warning of more that two parents
        if (parents.length>2){
            System.out.print("[WARNING] Tried to crossover more than two parents with PMX.");
            System.out.println(" Only the first two chromosomes will be used.");
        }
        
        // Should we do the crossover, based on the given probability? 
        // If not just return the parents.
        if (!Randomizer.doIt(probability)){
            return parents;
        }
        
        // 1. Choose two random crossover points
        int[] crossoverPoints = Randomizer.randomCrossoverPoints(parents[0],2);
        int firstPoint = crossoverPoints[0];
        int secondPoint = crossoverPoints[1];
        
        // Clone the parents 
        Chromosome[] childs = parents.clone();
        try{
            for (int i=0; i<parents.length;i++){
                childs[i] = (Chromosome)parents[i].clone();
            }
        } catch (Exception e){
        }
        
        // Just for clearance and usability...
        Chromosome parent1 = parents[0];
        Chromosome parent2 = parents[1];
        Chromosome child1 = childs[0];
        Chromosome child2 = childs[1];

        // Put all the genes of the sons at null
        for (int i=0;i<childs[0].getLenght();i++){
            child1.setGene(null, i);
            child2.setGene(null, i);
        }

        
        // 2. Copy the segment between the crossover points from the parent1 to the child1
        
        // All genes between the crossover points are directly copied to the childs
        for (int i=firstPoint;i<=secondPoint;i++){
            Gene gene;
            gene = parent1.getGene(i);
            child1.setGene(gene, i);
            gene = parent2.getGene(i);
            child2.setGene(gene, i);
        }
        
        // 3. Starting from the first crossover point look for elements on that segment
        // of the parent2 that have not been copied.
        // 4. For each of these (say i) look in the child1 to see what element (say j)
        // has been copied in its place from Parent1.
        // 5. Place i in the position occupied by j in Parent2.
        Gene geneParent1;
        Gene geneParent2;
        
        // Between the crossover points, for each gene in the other parent
        for (int i=firstPoint;i<=secondPoint;i++){

            geneParent1=parent1.getGene(i);
            geneParent2=parent2.getGene(i);
            
            // If the gene has already been copied to the child, take no action.
            // Otherwise get the gene that is on the same index on the other parent.
            // Find the position that that gene occupies on this parent.
            if (!child1.hasGene(geneParent2)){
                int indexP2j = parent2.indexOf(geneParent1);
                
                Gene geneP2j= child1.getGene(indexP2j);
                if (geneP2j == null){
                    child1.setGene(geneParent2, indexP2j);
                } else {
                    // 6. If the place occupied by j in Parent2 has already been filled
                    // in child1, by an element k, put i in the position occupied by k in Parent2.
                    int indexP2k = parent2.indexOf(geneP2j);
                    
                    // There's a bug in the original algorithm.
                    // Here we have to verify that the child has no gene on this position.
                    if (child1.getGene(indexP2k) == null){
                        child1.setGene(geneParent2, indexP2k);
                    }
                    
                }
            }
            
            if (!child2.hasGene(geneParent1)){
                int indexP1j = parent1.indexOf(geneParent2);
                
                // 
                Gene geneP1j= child2.getGene(indexP1j);
                if (geneP1j == null){
                    child2.setGene(geneParent1, indexP1j);
                } else {
                    int indexP1k = parent1.indexOf(geneP1j);
                    if (child2.getGene(indexP1k) == null){
                        child2.setGene(geneParent1, indexP1k);
                    }
                }
            }
        }
        
        // 7. The remaining elements are placed by directly copying their positions from Parent2.
        // This is another bug on the original definition. We can't directly copy the genes 
        // from Parent2 into the empty slots of Child1.
        // We need to verify which are the genes in Parent2 that don't exist in Child1 and copy them.
        
        // 7.1 Transform the arrays in lists, for easy element removal
        ArrayList<Gene> parent2List = new ArrayList<Gene>(Arrays.asList(parent2.getGenes()));
        ArrayList<Gene> parent1List = new ArrayList<Gene>(Arrays.asList(parent1.getGenes()));
        
        // 7.2 Define a storage to put the indexes of the null elements in Child1
        ArrayList<Integer> emptyChild1 = new ArrayList<Integer>();
        ArrayList<Integer> emptyChild2 = new ArrayList<Integer>();
        
        // 7.3 For each element in Child1, if it is null, put his index in emptyChild1,
        // otherwise remove the element from Parent2List.
        for (int i=0; i<child1.getLenght();i++){
            Gene tempGene = child1.getGene(i);
            if (tempGene == null){
                emptyChild1.add(i);
            } else {
                parent2List.remove(tempGene);
            }
        }
        for (int i=0; i<child2.getLenght();i++){
            Gene tempGene = child2.getGene(i);
            if (tempGene == null){
                emptyChild2.add(i);
            } else {
                parent1List.remove(tempGene);
            }
        }
        
        // 7.4 Now put every gene (that was left) from parent2List in the empty places
        // of child1 (their indexes are on emptyChild1).
        for (int i:emptyChild1){
            child1.setGene(parent2List.remove(0), i);
        }
        for (int i:emptyChild2){
            child2.setGene(parent1List.remove(0), i);
        }
        
        // TODO: Remove/Comment this!
        child1.verifyGenes();
        child2.verifyGenes();
        
        return childs;
    }
}