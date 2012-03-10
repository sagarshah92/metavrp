/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.metavrp.GA.support;

import java.util.ArrayList;
import org.metavrp.GA.Chromosome;
import org.metavrp.VRP.Customer;
import org.metavrp.VRP.Vehicle;

/**
 *
 * @author David Pinheiro
 */
public class Tours {
    
    ArrayList<Tour> tours = new ArrayList<Tour>();
    Chromosome chromosome = null;

    // Constructor
    public Tours(Chromosome chromosome){
        this.chromosome = chromosome;
        measureTours();
    }
    
    // Calculate the tours
    private void measureTours(){
        Vehicle currentVehicle = null;
        ArrayList<Customer> currentCustomers = new ArrayList<Customer>();
        
        int i = chromosome.indexFirstVehicle();
        currentVehicle = (Vehicle)chromosome.getGene(i);
        
        // Go from the gene next to the first vehicle on the chromosome to the last gene, and create the tours
        for (int j=i+1; j<chromosome.getLenght(); j++){
            if (chromosome.getGene(j).getIsVehicle()==true){
                // If the next Gene is another vehicle, create the tour
                tours.add(new Tour(currentVehicle, currentCustomers));
                // Assign the new currentVehicle and clean the currentCustomers
                currentVehicle = (Vehicle)chromosome.getGene(j);
                currentCustomers.clear();
            } else {
                // else add the gene (a customer) to the current tour
                currentCustomers.add((Customer)chromosome.getGene(j));
            }
        }
        
        // Go from the first gene on the chromosome until the first vehicle and create the tour
        for(int j=0;j<=i;j++){
            if (chromosome.getGene(j).getIsVehicle()==true){
                // If the next Gene is another vehicle, create the tour
                tours.add(new Tour(currentVehicle, currentCustomers));
            } else {
                // else add the next gene (a customer) to the current tour
                currentCustomers.add((Customer)chromosome.getGene(j));
            }
        }
        
        i++;
    }
        
    // Get this chromosome in a human friendly manner, by giving the tours
    // that the different vehicles need to travel
    public ArrayList<Tour> getTours(){
        return tours;
    }
    
    // Get a given tour, by vehicle
    public Tour getTour(Vehicle vehicle){
        for (Tour tour:tours){
            if (tour.getVehicle()==vehicle){
                return tour;
            }
        }
        return null;
    }
    
    // Get a given tour, by index. First tour is index 0.
    public Tour getTour(int index){
        return tours.get(index);
    }
    
    // Get the number of tours
    public Integer nTours(){
        return tours.size();
    }
    
    // Prints a human friendly representation of the tours
    public void print(){
        for (Tour tour:tours){
            System.out.println(tour.toString());
        }
    }
    
    // Inner class that represents a tour.
    // A tour is a vehicle that visits customers.
    // The vehicle starts the tour on the depot and then visits some customers.
    // Between the visits the vehicle may have to return to the main depot or go to other 
    // depots to grab or deposit goods.
    // At the end of the tour, the vehicle could come back to the depot
    public class Tour {
        
        private Vehicle vehicle;
        private ArrayList<Customer> customers;

        
        public Tour (Vehicle vehicle, ArrayList<Customer> customers){
            this.customers = customers;
            this.vehicle = vehicle;
        }
        
        // Human friendly string, representing this tour
        @Override
        public String toString(){
            String stringTour = Integer.toString(vehicle.getDepot())+": [";
            for(Customer customer:customers){
                stringTour += " "+customer.toString();
            }
            return stringTour + "]";
        }
        
        /*
         * Getters and Setters
         */

        public ArrayList<Customer> getCustomers() {
            return customers;
        }

        public void setCustomers(ArrayList<Customer> customers) {
            this.customers = customers;
        }

        public Vehicle getVehicle() {
            return vehicle;
        }

        public void setVehicle(Vehicle vehicle) {
            this.vehicle = vehicle;
        }
    }
}
