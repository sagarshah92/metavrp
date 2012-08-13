package org.metavrp.algorithm.GA;

/**
 * Represents a simple gene
 * 
 * @author David Pinheiro
 */
public interface Gene {
 

    /* ------------------- */
    /* Getters and Setters */
    /* ------------------- */

    // The Identification of the gene. Can be a description.
    public String getId();

    // Gene's node.
    // In case of a vehicle, corresponds to its initial node (depot).
    // Warning! This node should have a direct mapping to the corresponding index of the cost matrix!
    public int getNode();

    // This gene is a vehicle?
    public boolean getIsVehicle();
    
    // This gene is a customer?
    public boolean getIsCustomer();
    
    // This gene is a depot?
    public boolean getIsDepot();
    
    // This genes's size.
    public float getSize();

    // Transform the gene's node value into a string
    @Override
    public String toString();

    // Print all the gene information
    public String print();

    // Clone this Gene
    public Object clone();
  }