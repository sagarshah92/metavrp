package org.metavrp.trash;

import org.metavrp.GA.Chromosome;
import org.metavrp.GA.Gene;
import org.metavrp.GA.support.Randomizer;

/**
 *
 * @author David Pinheiro
 */
public class PMX_copy {
    
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
        
        // 2. Make a mapping between parents
        //Create a mapping matrix
        Gene[][] mapping = new Gene[secondPoint-firstPoint+1][2];
        //Put the mappings on the matrix
        for (int i=0; i<mapping.length; i++)
            for (int j=0;j<mapping[0].length;j++)
                mapping[i][j]=parents[j].getGene(i+firstPoint); 
        
        // 3. Do the actual PMX crossover
        
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
        
//System.out.println("Parent 0: "+childs[0].print());
//System.out.println("Parent 1: "+childs[1].print());
//System.out.println("Ponto 0: "+crossoverPoints[0]+"\nPonto 1: "+secondPoint);
        
        // Swap the genes (between the starting and ending nodes), between childs
        for (int i=firstPoint;i<=secondPoint;i++){
            Gene gene;
            gene = parent2.getGene(i);
            child1.setGene(gene, i);
            gene = parent1.getGene(i);
            child2.setGene(gene, i);
        }
        
        Gene geneParent1;
        Gene geneParent2;
        // For the other gene, not copied yet, copy every ocorrence of the gene 
        // on the parent to the respective child. If it already exists there, we have to run
        // throught the mapping matrix looking for correspondences.
        for (int i=0;i<firstPoint;i++){
            geneParent1=parent1.getGene(i);
            geneParent2=parent2.getGene(i);
            if (child1.hasGene(geneParent1)){
                child1.setGene(getMappedGene(geneParent1,0,mapping), i);
            } else {
                child1.setGene(geneParent1, i);
            }
            if (child2.hasGene(geneParent2)){
                child2.setGene(getMappedGene(geneParent2,1,mapping), i);
            } else {
                child2.setGene(geneParent2, i);
            }
        }
        for (int i = secondPoint + 1; i < parent1.getLenght(); i++) {
            geneParent1=parent1.getGene(i);
            geneParent2=parent2.getGene(i);
            if (child1.hasGene(geneParent1)){
                child1.setGene(getMappedGene(geneParent1,0,mapping), i);
            } else {
                child1.setGene(geneParent1, i);
            }
            if (child2.hasGene(geneParent2)){
                child2.setGene(getMappedGene(geneParent2,1,mapping), i);
            } else {
                child2.setGene(geneParent2, i);
            }
        }

//System.out.println("Foi feito o swapping entre os genes "+mapping[i][0].toString()+" e "+mapping[i][1].toString());    
//System.out.println(childs[j].print());
            
//System.out.println("Child "+j+": "+childs[j].print());            

        return childs;
    }


    // Runs throught the mapping matrix, looking for matching genes, until no more matchings are found
    private static Gene getMappedGene(Gene geneParent, int parent, Gene[][] mapping){
        // What's the number of the other parent?
        int otherParent;
        if (parent==1) otherParent=0;
        else otherParent=1;
        
        // Find the mappings
        Gene gene = null;
        boolean found;
        do{
            found=false;
            // Run throught all the mapping matrix, looking for the gene
            for (int i=0; i<mapping.length; i++){
                if (mapping[i][parent].equals(geneParent)){
                    found = true;
                    gene = mapping[i][otherParent];
                    continue;
                    }
            }
        } while (found);
        return gene;
    }
}