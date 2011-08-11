
package GA.operators.crossover;

import GA.support.Randomizer;
import GA.*;

/**
 *
 * @author David Pinheiro
 */
public class Order1 {
    
    
    public static Chromosome[] Order1 (Chromosome[] parents, float probability){
        // Should we do the crossover, based on the given probability? 
        // If not just return the parents.
        if (!Randomizer.doIt(probability)){
            return parents;
        }
        
        // Warning of out of scope probability
        if (probability < 0 || probability >1){
            System.out.println("[WARNING] Probability of Order1 crossover is "+probability+
                    ", but should be between 0 and 1.");
        }
        // Warning of more that two parents
        if (parents.length>2){
            System.out.println("[WARNING] Tried to crossover more than two parents with Order1.");
            System.out.println("[WARNING] Only the first two chromosomes will be used.");
        }
        
        // Clone the parents 
        // TODO: Remover isto e criar o filho como um array de genes, como no Edge3
        Chromosome[] childs = parents.clone();
        try{
        for (int i=0; i<parents.length;i++){
            childs[i] = (Chromosome)parents[i].clone();
        }
        } catch (Exception e){
            throw new AssertionError("[ERROR] Error cloning parents on Order1 crossover.");
        }
        
        // Just for clearance and usability...
        Chromosome parent1 = parents[0];
        Chromosome parent2 = parents[1];
        Chromosome child1 = childs[0];
        Chromosome child2 = childs[1];
        int lenght = parent1.getLenght();

        // Put all the genes of the sons at null
        for (int i=0;i<lenght;i++){
            child1.setGene(null, i);
            child2.setGene(null, i);
        }

//System.out.println("Parent 0: "+childs[0].print());
//System.out.println("Parent 1: "+childs[1].print());
//System.out.println("Ponto 0: "+crossoverPoints[0]+"\nPonto 1: "+secondPoint);
        
        // 1. Choose an arbitrary part from the parent
        int[] crossoverPoints = Randomizer.randomCrossoverPoints(parent1,2);
        int firstPoint = crossoverPoints[0];
        int secondPoint = crossoverPoints[1];

        // 2. Copy this parts do the childs
        for (int i=firstPoint;i<=secondPoint;i++){
            Gene gene;
            gene = parent1.getGene(i);
            child1.setGene(gene, i);
            gene = parent2.getGene(i);
            child2.setGene(gene, i);
        }

        // 3. From the second cutting point, look on the other parent for genes
        // that aren't yet on the child, and copy them.
        int indexChild1 = secondPoint+1;
        int indexChild2 = secondPoint+1;
        for (int i=secondPoint+1; i<lenght; i++){
            Gene gene = parent2.getGene(i);
            if (!child1.hasGene(gene)){
                if (indexChild1>=lenght) indexChild1=0;
                child1.setGene(gene, indexChild1);
                indexChild1++;
            }
            gene = parent1.getGene(i);
            if (!child2.hasGene(gene)){
                if (indexChild2>=lenght) indexChild2=0;
                child2.setGene(gene, indexChild2);
                indexChild2++;
            }
        }
        // Then start from the beginning of the parents trought the second crossover point
        for (int i=0; i<=secondPoint; i++){
            Gene gene = parent2.getGene(i);
            if (!child1.hasGene(gene)){
                if (indexChild1>=lenght) indexChild1=0;
                child1.setGene(gene, indexChild1);
                indexChild1++;
            }
            gene = parent1.getGene(i);
            if (!child2.hasGene(gene)){
                if (indexChild2>=lenght) indexChild2=0;
                child2.setGene(gene, indexChild2);
                indexChild2++;
            }
        }
        
        return childs;
    }
}
