
package org.metavrp.algorithm.GA.phenotype;

import java.util.ArrayList;
import org.metavrp.algorithm.GA.Chromosome;
import org.metavrp.algorithm.GA.Gene;
import org.metavrp.algorithm.GA.GeneList;
import org.metavrp.problem.Customer;
import org.metavrp.problem.Vehicle;
import org.metavrp.algorithm.GA.phenotype.Tours.Tour;

/**
 * Goal: Grab a genotype and create a phenotype, for the CVRP. 
 * Given some chromosome, measure the tours that the vehicles will make, considering the 
 * vehicle's capacity and customer demand.
 * 
 * @author David Pinheiro
 */
public class CVRPTours{
    
    private ArrayList<Tour> tourList = new ArrayList<Tour>();
    private Tours tours = new Tours(tourList);
    
    /*
     * The Constructor
     */
    // TODO: This class should extend Tours
    public CVRPTours(Chromosome chr){
        tourList = measureTours(chr);
        tours = new Tours(tourList);
    }
    
    // Calculate the tours
    private ArrayList<Tour> measureTours(Chromosome chromosome){

        ArrayList<Gene> currentCustomers = new ArrayList<Gene>();
        
        Gene currentGene = chromosome.getFirstVehicle();
        Gene nextGene = chromosome.getGeneAfter(currentGene);
        
        Vehicle firstVehicle = (Vehicle)chromosome.getFirstVehicle();
        Vehicle currentVehicle = firstVehicle;
        
        // Go from the gene next to the first vehicle on the chromosome to the last gene
        // and again to the first vehicle, and create the tours.
        do {
            // If the next gene is a vehicle, create the tour
            if (nextGene.getIsVehicle()){
                ArrayList<Gene> repairedCustomerList = repairCustomerList(currentVehicle, currentCustomers);
                tourList.add(tours.new Tour(currentVehicle, repairedCustomerList));
                // Assign the new currentVehicle and clean the currentCustomers
                currentVehicle=(Vehicle)nextGene;
                currentCustomers.clear();
            } else {
                // else add the gene (a customer) to the current tour
                currentCustomers.add((Customer)nextGene);
            }
            currentGene = nextGene;
            nextGene = chromosome.getGeneAfter(currentGene);
            
        } while (currentGene != firstVehicle);
        
        return tourList;
    }
    
    /*
     * Repair the customer list, by putting the inner depot accesses in the list
     */
    private ArrayList<Gene> repairCustomerList(Vehicle currentVehicle, ArrayList<Gene> currentCustomers){
        float vehicleCapacity = currentVehicle.getCapacity();   // Capacity of the vehicle
        float currentLoad = 0;                                  // Vehicle's current load
        ArrayList<Gene> repairedCurrentCustomers = new ArrayList<Gene>(currentCustomers);   // Repaired list of customers
        
        int j=0;    // The number of inner depot accesses
        for (int i=0; i<currentCustomers.size(); i++){
            float currentDemand = currentCustomers.get(i).getSize();    // Current customer demand
            currentLoad += currentDemand;                               // Update the current load of the vehicle
            if (currentLoad > vehicleCapacity){
                // If the current load of the vehicle is bigger than his capacity, add vehicle to repaired customer's list
                repairedCurrentCustomers.add(i+j, (Gene)currentVehicle);
                currentLoad = currentDemand;    // The current vehicle's demand will be the demand of this customer
                j++;                            // Increment the number of vehicles used
            }
        }
        
        return repairedCurrentCustomers;
    }
    
    public Tours getTours(){
        return tours;
    }
}
