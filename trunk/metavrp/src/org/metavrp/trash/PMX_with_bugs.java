package org.metavrp.trash;

import org.metavrp.algorithm.GA.Chromosome;
import org.metavrp.algorithm.GA.Gene;
import org.metavrp.algorithm.GA.support.Randomizer;

/**
 *
 * @author David Pinheiro
 */
public class PMX_with_bugs {
    
    /*
     * PMX Crossover.
     * Returns, with some given probability, two chromosomes (the childs) as the
     * crossover between parents.
     * 
     * @author David Pinheiro
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
        
System.out.println("Parent 1: "+parent1.print());
System.out.println("Parent 2: "+parent2.print());
System.out.println("Ponto 1: "+crossoverPoints[0]+"\nPonto 2: "+secondPoint);

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
                    
                    // Here's a bug in the original algorithm.
                    // First we need to verify that the child has no gene on this position.
                        child1.setGene(geneParent2, indexP2k);
                    
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
                    if (child2.getGene(indexP1k) != null){
                        child2.setGene(geneParent1, indexP1k);
                    }
                }
            }
        }
        
        // 7. The remaining elements are placed by directly copying their positions from Parent2.
        for (int i=0; i<child1.getLenght(); i++){
            if (child1.getGene(i)==null){
                child1.setGene(parent2.getGene(i),i);
            }
            if (child2.getGene(i)==null){
                child2.setGene(parent1.getGene(i),i);
            }
        }
        
System.out.println("Child 1:"+child1.print());
System.out.println("Child 2:"+child2.print());
        
        child1.verifyGenes();
        child2.verifyGenes();
        
        return childs;
    }

}