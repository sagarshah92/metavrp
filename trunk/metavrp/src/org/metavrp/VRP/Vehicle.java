
package org.metavrp.VRP;

import org.metavrp.GA.*;

/**
 * Represents a simple vehicle
 * Implements the interface Gene
 * 
 * @author David Pinheiro
 */

   // TODO: For the VRP with Time-Windows, there's a need to define the operating time of the vehicle.

public class Vehicle implements Gene, Cloneable {


   // The ID of the Vehicle. Vehicles ID's are negative, compared to the usual customers, which are positive or zero.
   private int id;
    
   // Node number.
   // In case of a vehicle (this case), corresponds to its depot.
   private int node;
   
   // What's this vehicle's capacity?
   private float capacity;
   
   // Constructs a vehicle with infinite capacity. 
    public Vehicle (int id, int depot){
        this.id=id;
        this.node=depot;
        this.capacity=Float.MAX_VALUE;
    }
    
    // Constructs a gene with capacity
    public Vehicle (int id, int depot, float capacity){
        this.id=id;
        this.node=depot;
        this.capacity=capacity;
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
        return getDepot();
    } 
    
    public int getDepot(){
        return this.node;
    } 

    @Override
    public boolean getIsVehicle(){
        return true;
    }

    public float getCapacity(){
        return this.capacity;
    }
    
    public float getSize(){
        return getCapacity();
    }
    
    // Transform the integer value of the node (depot) into a string
    @Override
    public String toString(){
        return Integer.toString(node);
    }
    
    // Return all the vehicle information, as a string, to be printed
    @Override
    public String print(){
        String geneString = "Depot: " + getDepot() + ", isVehicle:" + getIsVehicle()+ ", capacity:" + getCapacity();
        return geneString;
    }
    
    // Clone this Vehicle
// TODO: update this if you add more variables to this class.    
    @Override
    public Vehicle clone(){
        int clonedId = this.id;
        int clonedNode = this.node;
        float clonedCapacity = this.capacity;
        
        Vehicle newVehicle = new Vehicle(clonedId, clonedNode, clonedCapacity);

// TODO: delete this        
int hashOriginal = this.hashCode();
int hashClone = newVehicle.hashCode();
        
        return newVehicle;
    }
  }