
package org.metavrp.problem;

import org.metavrp.algorithm.GA.Gene;

/**
 * Represents a Customer
 * Implements the interface Gene
 * 
 * @author David Pinheiro
 */

    // TODO: For the VRP with Time-Windows, there's a need to define the operating time of the customer.
    // TODO: Create a variable for the service time, that this customer will consume from the vehicle.

public class Customer implements Gene, Cloneable {

   // Node number
   // Warning! This node should have a direct mapping to the corresponding index of the cost matrix!
   private int node;
   
   // What's this customer's goods demand, that need to be transported? It's measure (size, volume, quantity, etc...)
   private float demand;

   // Constructs a Customer with no demand. 
    public Customer (int node){
        this.node=node;
        this.demand=0;
    }
    
    // Constructs a Customer with a given demand
    public Customer (int node, float demand){
        this.node=node;
        this.demand=demand;
    }

    /* ------------------- */
    /* Getters and Setters */
    /* ------------------- */
    
    @Override
    public int getId() {
        return Object.class.hashCode();
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
        return false;
    }
    
    @Override
    public boolean getIsCustomer(){
        return false;
    }

    public float getDemand() {
        return demand;
    }

    public void setDemand(float demand) {
        this.demand = demand;
    }
    
    // The same as setDemand
    public void setSize(float size){
        setDemand(size);
    }

    // The same as getDemand
    @Override
    public float getSize(){
        return getDemand();
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
        float clonedSize = this.demand;
        
        Customer newCustomer = new Customer (clonedNode, clonedSize);
        
        return newCustomer;
    }
  }