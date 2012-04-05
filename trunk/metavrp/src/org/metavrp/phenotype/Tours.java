
package org.metavrp.phenotype;

import java.util.ArrayList;
import org.metavrp.GA.Gene;
import org.metavrp.VRP.Customer;
import org.metavrp.VRP.Vehicle;

/**
 *
 * @author David Pinheiro
 */
public class Tours {
    
    ArrayList<Tour> tours = new ArrayList<Tour>();

    // Constructor
    public Tours(ArrayList<Tour> tours){
        this.tours = tours;
    }
    
        
    // Get the list of tours
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
        private ArrayList<Gene> customers;

        
        public Tour (Vehicle vehicle, ArrayList<Gene> customers){
            this.customers = customers;
            this.vehicle = vehicle;
        }
        
        /*
         * This constructor take care of the capacities of the vehicles
         */
        public Tour (Vehicle vehicle, ArrayList<Customer> customers, float capacity){
            this.vehicle = vehicle;
            
        }
        
        // Human friendly string, representing this tour
        @Override
        public String toString(){
            String stringTour = Integer.toString(vehicle.getDepot())+": [";
            for(Gene customer:customers){
                stringTour += " "+customer.toString();
            }
            return stringTour + " ]";
        }
        
        /*
         * Getters and Setters
         */

        public ArrayList<Gene> getCustomers() {
            return customers;
        }

        public void setCustomers(ArrayList<Gene> customers) {
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
