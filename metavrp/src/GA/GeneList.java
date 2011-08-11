/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package GA;

import java.util.ArrayList;

/**
 * This class has all the genes that are needed. 
 * Every chromosome is made from this list and not directly from the genes.
 * This has the benefit that we can compare genes by hashvalue and not by his state (by the values of his variables).
 * 
 * @author David
 */
public class GeneList {
    
    private ArrayList<Gene> genes;      // The actual list: an Array of genes
    
    private int nrVehicles;             // The number of vehicles in this list
    private int nrNodes;                // The number of nodes (not vehicles) in this list
    
    private int indexFirstVehicle;      // The index of the first vehicle on the array
    
    // Constructor. 
    // Given a Cost Matrix and the number of vehicles, creates a list of genes.
    // TODO: all the vehicles start from the position 0? Precisa ser mudado, para 
    // TODO: que possam ser definidos diferentes depots.
    public GeneList(int nrVehicles, int nrNodes){
        genes = new ArrayList<Gene>(nrVehicles+nrNodes);
        
        // Add the nodes
        for (int i=0;i<nrNodes;i++){
            genes.add(new Gene(i,i,false));
        }
        
        this.indexFirstVehicle=genes.size();
        
        // Add the vehicles
        // All of them start from the first node
        for (int i=0;i<nrVehicles;i++){
            genes.add(new Gene(-1-i,0,true));
        }
        
        this.nrNodes=nrNodes;
        this.nrVehicles=nrVehicles;
    }

    
    /* ------------------- */
    /* Getters and Setters */
    /* ------------------- */
    
    public ArrayList<Gene> getGenes() {
        return genes;
    }
    
    public ArrayList<Gene> getClonedGenes() {
        return (ArrayList<Gene>)genes.clone();
    }

    public int getIndexFirstVehicle() {
        return indexFirstVehicle;
    }

    public int getNrNodes() {
        return nrNodes;
    }

    public int getNrVehicles() {
        return nrVehicles;
    }
    
    
}
