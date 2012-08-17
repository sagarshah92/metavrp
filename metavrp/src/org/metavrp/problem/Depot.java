
package org.metavrp.problem;

import org.metavrp.algorithm.GA.Gene;

/**
 * Represents a Depot
 * Implements the interface Gene
 * 
 * @author David Pinheiro
 */

public class Depot implements Gene, Cloneable {
    
    // Identification of this depot. Can be a description.
    private String id;
    
    // Node number
    // Warning! This node should have a direct mapping to the corresponding index of the cost matrix!
    private int node;

    // Constructs a Depot
    public Depot (int node){
        this.node=node;
    }
    
    // Constructs a Depot with an identification string
    public Depot (String id, int node){
        this.id=id;
        this.node=node;
    }

    /* ------------------- */
    /* Getters and Setters */
    /* ------------------- */
    
    @Override
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
    
    @Override
    public int getNode(){
        return this.node;
    } 

    @Override
    public boolean getIsVehicle(){
        return false;
    }
    
    @Override
    public boolean getIsDepot(){
        return true;
    }
    
    @Override
    public boolean getIsCustomer(){
        return false;
    }

    // The same as getDemand
    @Override
    public float getSize(){
        throw new AssertionError("[Error] You tried to get the size of a depot. You have some logical error in your program");
    }
    
    // Transform the node's value into a string
    @Override
    public String toString(){
        return Integer.toString(node);
    }
    
    // Print all the Customer information
    @Override
    public String print(){
        String geneString = "Customer: " + getNode() + ", isVehicle: " + getIsVehicle() + ", capacity:" + getSize();
        return geneString;
    }
    
    // Clone this Customer
    @Override
    public Customer clone(){
        int clonedNode = this.node;
        
        Customer newCustomer = new Customer (clonedNode);
        
        return newCustomer;
    }

}