
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
   
   // What's this customer's goods size, that need to be transported? It's measure (size, volume, quantity, etc...)
   private float size;

   // Constructs a Customer with infinite capacity. 
    public Customer (int id, int node){
        this.id=id;
        this.node=node;
        this.size=Float.MAX_VALUE;
    }
    
    // Constructs a Customer with capacity
    public Customer (int id, int v, float size){
        this.id=id;
        this.node=v;
        this.size=size;
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
    
    public void setSize(float size){
        this.size=size;
    }

    @Override
    public float getSize(){
        return this.size;
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
        float clonedSize = this.size;
        
        Customer newCustomer = new Customer (clonedId, clonedNode, clonedSize);

// TODO: delete this        
int hashOriginal = this.hashCode();
int hashClone = newCustomer.hashCode();
        
        return newCustomer;
    }
  }