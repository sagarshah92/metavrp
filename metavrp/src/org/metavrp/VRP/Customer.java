
package org.metavrp.VRP;

import org.metavrp.GA.*;

/**
 * Represents a simple Customer
 * Implements the interface Gene
 * 
 * @author David Pinheiro
 */

    // TODO: For the VRP with Time-Windows, there's a need to define the operating time of the customer.
    // TODO: Create a variable for the service time, that this customer will consume from the vehicle.

public class Customer implements Gene, Cloneable {

   // The ID of the customer. 
   // This ID is positive or zero for customers and negative for vehicles.
   private int id;
   
   // Node number
   // TODO: shouldn't this field be removed? And the ID used instead? From the class Vehicle too.
   private int node;
   
   // What's this customer's goods demand, that need to be transported? It's measure (size, volume, quantity, etc...)
   private float demand;

   // Constructs a Customer with no demand. 
    public Customer (int id, int node){
        this.id=id;
        this.node=node;
        this.demand=0;
    }
    
    // Constructs a Customer with a given demand
    public Customer (int id, int v, float demand){
        this.id=id;
        this.node=v;
        this.demand=demand;
    }

    /* ------------------- */
    /* Getters and Setters */
    /* ------------------- */
    
    @Override
    public int getId() {
        return id;
    }
    
    @Override
    public int getNode(){
        return this.node;
    } 

    @Override
    public boolean getIsVehicle(){
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
        int clonedId = this.id;
        int clonedNode = this.node;
        float clonedSize = this.demand;
        
        Customer newCustomer = new Customer (clonedId, clonedNode, clonedSize);
        
        return newCustomer;
    }
  }