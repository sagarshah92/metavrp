/*
 * 
 */
package org.metavrp;

import java.util.ArrayList;
import org.metavrp.problem.CostMatrix;
import org.metavrp.problem.Customer;
import org.metavrp.problem.Depot;
import org.metavrp.problem.Vehicle;

/**
 *
 * @author David Pinheiro
 */
public class Problem {
    
    // Cost Matrix
    private CostMatrix costMatrix;
    
    // Customers
    private ArrayList<Customer> customers;
    
    // Vehicles
    private ArrayList<Vehicle> vehicles;
    
    // Depots
    private ArrayList<Depot> depots;

    
    /**
     * Constructor who doesn't initialize the Cost Matrix but creates empty lists of
     * Customers, Vehicles and Depots
     */
    public Problem() {
        this.customers = new ArrayList<Customer>();
        this.vehicles = new ArrayList<Vehicle>();
        this.depots = new ArrayList<Depot>();
    }

    /**
     * Constructor that initializes the Cost Matrix, Customers, Vehicles and Depots.
     * @param costMatrix
     * @param customers
     * @param vehicles
     * @param depots 
     */
    public Problem(CostMatrix costMatrix, ArrayList<Customer> customers, ArrayList<Vehicle> vehicles, ArrayList<Depot> depots) {
        this.costMatrix = costMatrix;
        this.customers = customers;
        this.vehicles = vehicles;
        this.depots = depots;
    }
    
    /*
     * Add elements
     */
    
    /**
     * Add one more customer
     * @param customer 
     */
    public void addCustomer(Customer customer){
        customers.add(customer);
    }
    
    /**
     * Add one more Vehicle
     * @param vehicle 
     */
    public void addVehicle(Vehicle vehicle){
        vehicles.add(vehicle);
    }
    
    /**
     * Add one more Depot
     * @param depot 
     */
    public void addDepot(Depot depot){
        depots.add(depot);
    }
        
    
    /*
     * Getters and Setters
     */

    public CostMatrix getCostMatrix() {
        return costMatrix;
    }

    public void setCostMatrix(CostMatrix costMatrix) {
        this.costMatrix = costMatrix;
    }

    public ArrayList<Customer> getCustomers() {
        return customers;
    }

    public void setCustomers(ArrayList<Customer> customers) {
        this.customers = customers;
    }

    public ArrayList<Depot> getDepots() {
        return depots;
    }

    public void setDepots(ArrayList<Depot> depots) {
        this.depots = depots;
    }

    public ArrayList<Vehicle> getVehicles() {
        return vehicles;
    }

    public void setVehicles(ArrayList<Vehicle> vehicles) {
        this.vehicles = vehicles;
    }
    
}
