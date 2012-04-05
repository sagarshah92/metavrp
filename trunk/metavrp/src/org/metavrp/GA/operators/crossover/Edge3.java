package org.metavrp.GA.operators.crossover;

import org.metavrp.GA.Chromosome;
import org.metavrp.GA.Gene;
import org.metavrp.GA.support.Randomizer;

import java.util.*;
/******************************************
 * Edge Crossover, version 3 by D. Whitley
 * ****************************************
 * For a description look at:
 * 
 * D. Whitley. "Permutations", in T. Bäck, D. Fogel, Z. Michalewicz. "Evolutionary Computation 1: 
 * Basic Algorithms and Operators", Institute of Physics Publishing, 2000, Cap. 33.3, Pag 278-280
 * 
 * or
 * 
 * A. Eiben, J. Smith. "Introduction to Evolutionary Computing", Springer-Verlag, 2003, pag 54-56
 * 
 * @author David Pinheiro
 */
public class Edge3 {
  
    public static Chromosome[] Edge3 (Chromosome[] parents, float probability){
//System.out.println("Edge3 crossover with probability: "+probability);
        // Warning of out of scope probability
        if (probability < 0 || probability >1){
            System.out.println("[WARNING] Probability of Edge crossover is "+probability+
                    ", but should be between 0 and 1.");
        }
        // Warning of more that two parents
        if (parents.length>2){
            System.out.println("[WARNING] Tried to crossover more than two parents with Edge3.");
            System.out.println("[WARNING] Only the first two chromosomes will be used.");
        }
        
        // Should we do the crossover, based on the given probability? 
        // If not just return the parents.
        if (!Randomizer.doIt(probability)){
            return parents;
        }

        // 1. Construct Edge Table
        // TODO: The two tables are equal. We can just clone the first one on the second.
        HashMap edgeTable1 = buildEdgeTable(parents[0],parents[1]);
        HashMap edgeTable2 = buildEdgeTable(parents[1],parents[0]);
        
        // 2. Pick an initial element at random and put it in the offspring
        int size = parents[0].getLenght();

        Gene[] child1 = new Gene[size];
        Gene[] child2 = new Gene[size];
        
        Gene currentElement1 = parents[0].getGene(Randomizer.randomInt(size));
        child1[0]=currentElement1;
        Gene currentElement2 = parents[1].getGene(Randomizer.randomInt(size));
        child2[0]=currentElement2;
        
        // Keep filling the child chromosome
        for (int i=1; i<size; i++){

            // 3. Remove all references to currentElement from the Edge table
            ArrayList<Gene> values1 = getRemoveElement(edgeTable1, currentElement1);
            ArrayList<Gene> values2 = getRemoveElement(edgeTable2, currentElement2);

            // 4. Get the next element
            currentElement1 = getNextElement(edgeTable1, values1);
            currentElement2 = getNextElement(edgeTable2, values2);
            
            child1[i] = currentElement1;
            child2[i] = currentElement2;
        }

        Chromosome[] childs = new Chromosome[2];
        childs[0] = new Chromosome(child1, parents[0].getCostMatrix(), parents[0].getOperators());
        childs[1] = new Chromosome(child2, parents[0].getCostMatrix(), parents[0].getOperators());
       
        // Verify if the genes are correctly created
        childs[0].verifyGenes();
        childs[1].verifyGenes();
        
        return childs;
    }
    
    // Construct Edge Table
    public static HashMap buildEdgeTable(Chromosome P1, Chromosome P2){
        if (P1.getLenght()!=P2.getLenght())
            throw new AssertionError("ERROR: The Chromosomes have different lenght!");

        // Constructs a HashMap of initial size n*n
        HashMap<Gene,ArrayList<Gene>> edgeTable = new HashMap<Gene,ArrayList<Gene>>(P1.getLenght()*P1.getLenght());

        // Puts the adjacent genes from chromosome1 on the HashMap
        for (int i=0;i<P1.getLenght();i++){
            ArrayList<Gene> list = new ArrayList<Gene>();
            list.add(P1.getGeneAfter(i));
            list.add(P1.getGeneBefore(i));
            edgeTable.put(P1.getGene(i), list);
        }
        // Puts the adjacent genes from chromosome2 on the HashMap
        for (int i=0;i<P2.getLenght();i++){
            ArrayList<Gene> list = (ArrayList)edgeTable.get(P2.getGene(i));
            list.add(P2.getGeneAfter(i));
            list.add(P2.getGeneBefore(i));
        }
        
        return edgeTable;
    }
    
        // Remove all references to currentElement from the Edge table
    public static ArrayList<Gene> getRemoveElement(HashMap edgeTable, Gene currentElement){

        // Get the current element's values
        ArrayList<Gene> currentValues = (ArrayList)edgeTable.get(currentElement);

        // Remove the current element from the map
        edgeTable.remove(currentElement);
 
        // Remove any reference to the currentElement gene from the mappings
        for (Gene gene:currentValues){
            ArrayList<Gene> values = (ArrayList)edgeTable.get(gene);
            // Remove the element
            values.remove(currentElement);
//            // Keep removing the (various) ocorrences of the element
//            while (values.remove(currentElement)){}
        }
        
        // Return the current element's values
        return currentValues;
    }
    
    // Returns the next element that should be used on the next iteration of edge-3
    public static Gene getNextElement(HashMap edgeTable, ArrayList<Gene> values){
        // If the list is empty, just return any other (randomly chosen) value
        if (values.isEmpty()){
            // Randomly choose a nonempty entry
            return randomEntry(edgeTable);
        }
        
        // If there is a common edge, pick that to be next element
        Gene nextGene = getCommonEdge(values);
        if (nextGene!=null){
            return nextGene;
        }
        else {
        // Otherwise pick the entry in the list which itself has the shortest list
        return getShortest(edgeTable, values);
        }
    }
    
    // Returns a common gene, if it exists, otherwise returns null
    public static Gene getCommonEdge(ArrayList<Gene> values){
        // 1. Creates a HashSet of the size of the list
        HashSet<Gene> valuesSet = new HashSet<Gene>(values.size(),1);
        for (int i=0;i<values.size();i++){
            // 2. Start adding genes from the list to the set.
            // If the set already contains the current gene, the add operation returns false
            // and we just return that gene, as it is a duplicate.
            if (!valuesSet.add(values.get(i))){
                return values.get(i);
            }
        }
        return null;
    }
    
    // Returns the element in the currentElement list which has the shortest list
    public static Gene getShortest(HashMap edgeTable, ArrayList<Gene> values){
        // Iterate through every value on the list and get the one with the shortest list
        Gene smallestList = null;
        int smallestSize = Integer.MAX_VALUE;
        for (int i=0; i<values.size();i++){
            Gene gene = values.get(i);
            int size = ((ArrayList<Gene>)edgeTable.get(gene)).size();
            if (size < smallestSize){
                smallestSize=size;
                smallestList = gene;
            }
        }
        return smallestList;
    }
    
    // Randomly choose a nonempty entry
    public static Gene randomEntry(HashMap edgeTable){
        // Get the keys as an array
        Gene[] entrySet = (Gene[])edgeTable.keySet().toArray(new Gene[0]);

        // Choose one key, randomly
        int randomIndex = Randomizer.randomInt(entrySet.length);
        
        // Return the respective gene
        return entrySet[randomIndex];
    }
    
}   