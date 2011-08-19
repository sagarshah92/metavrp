package org.metavrp.GA;

/**
 * Represents a simple gene
 * 
 * @author David Pinheiro
 */
public interface Gene {
 

    /* ------------------- */
    /* Getters and Setters */
    /* ------------------- */

    // The ID of the gene. If positive, is a normal node. If negative, is a vehicle.
    public int getId();

    // Gene's value (node number)
    // In case of a vehicle, corresponds to its initial node (depot)
    public int getNode();

    // This gene is a vehicle?
    public boolean getIsVehicle();
    
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