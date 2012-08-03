
package org.metavrp.algorithm.GA;

import org.metavrp.problem.Customer;
import org.metavrp.problem.Vehicle;
import java.util.ArrayList;

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
// TODO: See if this class can be eliminated.
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
    
    public int getSize(){
        int size = getNrNodes() + getNrVehicles();
        if (size != genes.size()) 
            throw new AssertionError("[ERROR] on GeneList: nº of customers + nº of vehicles != size of the genes");
        return size;
    }
    
}
