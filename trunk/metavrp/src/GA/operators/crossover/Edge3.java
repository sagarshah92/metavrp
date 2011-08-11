/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package GA.operators.crossover;

import GA.Chromosome;
import GA.Gene;
import GA.support.Randomizer;

import java.util.*;
/**
 *
 * @author David Pinheiro
 */
public class Edge3 {
    
    public static Chromosome[] Edge3 (Chromosome[] parents, float probability){
        // Should we do the crossover, based on the given probability? 
        // If not just return the parents.
        if (!Randomizer.doIt(probability)){
            return parents;
        }
        
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
System.out.println("Pai:"+parents[0].print());        
System.out.println("Mae:"+parents[1].print());             
        // 1. Construct Edge Table
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
        childs[0] = new Chromosome(child1, parents[0].getCostMatrix());
        childs[1] = new Chromosome(child2, parents[0].getCostMatrix());
   
System.out.println("Filho:"+childs[0].print());
System.out.println("Filho:"+childs[1].print());
        
        return childs;
    }
    
    // Construct Edge Table
    public static HashMap buildEdgeTable(Chromosome chr1, Chromosome chr2){
        if (chr1.getLenght()!=chr2.getLenght())
            throw new AssertionError("ERROR: The Chromosomes have different lenght!");

        // Constructs a HashMap of initial size n*n
        HashMap<Gene,ArrayList<Gene>> edgeTable = new HashMap<Gene,ArrayList<Gene>>(chr1.getLenght()*chr1.getLenght());

        // Puts the adjacent genes from chromosome1 on the HashMap
        for (int i=0;i<chr1.getLenght();i++){
            ArrayList<Gene> list = new ArrayList<Gene>();
            list.add(chr1.getGeneAfter(i));
            list.add(chr1.getGeneBefore(i));
            edgeTable.put(chr1.getGene(i), list);
        }
        // Puts the adjacent genes from chromosome2 on the HashMap
        for (int i=0;i<chr2.getLenght();i++){
//ArrayList<Gene> list = new ArrayList<Gene>();
            ArrayList<Gene> list = (ArrayList)edgeTable.get(chr2.getGene(i));
//try {
//System.out.println("List size:"+list.size());
//System.out.println("List:"+list.toString());
//System.out.println("i:" +i);
//System.out.println("chr2.getGeneAfter(i)" +chr2.getGeneAfter(i));

Gene newgene = chr2.getGeneAfter(i);
            list.add(newgene);
            list.add(chr2.getGeneBefore(i));
//            edgeTable.put(chr2.getGene(i), list);
//} catch (NullPointerException e){
//    System.out.println("===================================");
//    System.out.println("i:" +i);
//    System.out.println("Edge Table:\n"+edgeTable.toString());
//}
        }
        
        return edgeTable;
    }
    
    // Remove all references to currentElement from the Edge table
    public static ArrayList<Gene> getRemoveElement(HashMap edgeTable, Gene currentElement){
        // Get the keys on an array
        Set keySet = edgeTable.keySet();
        Gene[] keys = (Gene[])keySet.toArray(new Gene[0]);
        // For each of the keys, remove the element, when present
        for (int i=0; i<keys.length; i++){
            ArrayList<Gene> values = (ArrayList)edgeTable.get(keys[i]);
            // Keep removing the (various) ocorrences of the element
            while (values.remove(currentElement)){}
            edgeTable.put(keys[i], values);   // Put the list back on the HashMap
        }
        // Get the current element's values
        ArrayList<Gene> values = (ArrayList)edgeTable.get(currentElement);
        // Remove the current element
        edgeTable.remove(currentElement);
        // Return the current element's values
        return values;
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
        HashSet<Gene> valuesSet = new HashSet<Gene>(values.size(),1);
        for (int i=0;i<values.size();i++){
            // If the set already contained the gene, return it
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
        // Iterate throught the array
        for (Gene gene : entrySet){
            // If there is a non-empty set, return his key (gene)
            if (!((ArrayList<Gene>)edgeTable.get(gene)).isEmpty()){
                return gene;
            }
        }
        return null;
    }
    
}   // End of class definition
