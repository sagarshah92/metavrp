
package org.metavrp.GA;

import java.util.ArrayList;
import org.metavrp.VRP.*;

/**
 * This class has all the genes that are needed. 
 * Every chromosome is made from this list and not directly from the genes.
 * This has the benefit that at the beginning of the run of the program,
 * we create all possible genes and use pointers to them all over the run time.
 * This saves time and space, and we can compare genes by hashvalue and not by 
 * it's state (by the values of his variables).
 * 
 * @author David Pinheiro
 */
public class GeneList {
    
    private ArrayList<Gene> genes;      // The actual list: an Array of genes
    
    private int nrVehicles;             // The number of vehicles in this list
    private int nrNodes;                // The number of nodes (not vehicles) in this list
    
    private int indexFirstVehicle;      // The index of the first vehicle on the array
    
    
    // Constructor
    // Creates a list of the possible genes. First the nodes (the customers) and then the vehicles
    public GeneList(ArrayList<Customer> nodes, ArrayList<Vehicle> vehicles){
        // Create an array of all the possible genes (vehicles and customers)
        genes = new ArrayList<Gene>(nodes.size() + vehicles.size());
        
        // Add the nodes
        genes.addAll(nodes);
        
        // The index of the first vehicle is the size of the genes
        this.indexFirstVehicle=genes.size();
        
        // Add the vehicles
        genes.addAll(vehicles);
        
        this.nrNodes=nodes.size();
        this.nrVehicles=vehicles.size();
    }
    
    
    // Constructor. 
    // Given some number of nodes and of vehicles, creates a simple list of genes.
    // This is a very simplified (and useless?) constructor, as all the vehicles start from node 0
    // TODO: remove this useless constructor.
    public GeneList(int nrVehicles, int nrNodes){
        genes = new ArrayList<Gene>(nrVehicles+nrNodes);
        
        // Add the nodes
        for (int i=1;i<nrNodes;i++){
            genes.add(new Customer(i,i));
        }
        
        this.indexFirstVehicle=genes.size();
        
        // Add the vehicles
        // All of them start from the first node
        for (int i=0;i<nrVehicles;i++){
            genes.add(new Vehicle(-1-i,0));
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
