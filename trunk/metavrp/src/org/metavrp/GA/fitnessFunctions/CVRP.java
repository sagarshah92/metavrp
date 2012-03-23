
package org.metavrp.GA.fitnessFunctions;

import org.metavrp.GA.Chromosome;
import org.metavrp.GA.Gene;

/**
 *
 * @author David Pinheiro
 */
public class CVRP {
    
    float innerDepotPenalty = 0.0f;

    /*
     * Constructor
     */
    public CVRP(float innerDepotPenalty) {
        this.innerDepotPenalty=innerDepotPenalty;
    }
        
    /*
     * Measure the fitness of this chromosome. The cost associated with it.
     */
    public float measureCost(Chromosome chr){
        return measureCost(chr, innerDepotPenalty);
    }
    
    /*
     * Measure the fitness of this chromosome. The cost associated with it.
     */
    // TODO: Add support for maximum tour lenght
    // TODO: Add the objective that each vehicle visits the same customers or gets 
    // almost the same cost.
    public static float measureCost(Chromosome chr, float innerDepotPenalty){
        
        int nVehicles = chr.countVehicles();            // Count the number of vehicles
        float[] vCosts = new float[nVehicles];          // The fitness (routing costs) of the vehicles
        int indexCurrentVehicle = 0;                    // The number of the current vehicle
        
        // Start with the first vehicle
        Gene firstVehicle = chr.getFirstVehicle();      // The first vehicle
        Gene currentVehicle = firstVehicle;             // the current vehicle
        float currentVehicleCapacity = currentVehicle.getSize();// The capacity of the current vehicle
        
        float[] vLoad = new float[nVehicles];           // The load of the vehicles
        
        Gene currentGene = firstVehicle;                // The current gene
        Gene nextGene = chr.getGeneAfter(firstVehicle); // The next gene
        
        int nrDepotsInsideTour = 0;                     // The number of times that the depot is used inside tours?
        
        // Count the fitness of each vehicle
        do {
            // Starting from the first vehicle, go throught every gene on the chromosome 
            // summing the cost from it to the next one, and returning to the depot 
            // when necessary to load/unload.

            // If the next gene is a vehicle
            if (nextGene.getIsVehicle()){
                
                // If the current gene is a vehicle, the cost of this vehicle is 0
                if(currentGene.getIsVehicle()){
                    vCosts[indexCurrentVehicle] = 0;
                } else {
                    // If the current gene is a node, sum the cost
                    vCosts[indexCurrentVehicle] += chr.getCost(currentGene, currentVehicle);
                }
                // Move to the next vehicle
                indexCurrentVehicle++;
                currentVehicle = nextGene;
                currentVehicleCapacity = currentVehicle.getSize();
            } else {
                // If the next gene is a customer, update the current vehicle's cost
                
                // Measure if there will be available capacity on the vehicle to 
                // load the demand of the customer
                float futureLoad = vLoad[indexCurrentVehicle] + nextGene.getSize();
                
                // If there's not enought capacity to visit the next customer,
                // go to the depot and return to visit the customer.
                if (futureLoad > currentVehicleCapacity){
                    // Sum the cost to go to the depot
                    vCosts[indexCurrentVehicle] += chr.getCost(currentGene, currentVehicle);
                    // Sum the cost to com back from the depot to the next customer
                    vCosts[indexCurrentVehicle] += chr.getCost(currentVehicle, nextGene);
                    // Reset the load
                    vLoad[indexCurrentVehicle] = 0;
                    // The Depot was used inside a tour. Update the variable
                    nrDepotsInsideTour++;
                } else {
                    // Else go to the next customer and update the vehicle's load
                    vCosts[indexCurrentVehicle] += chr.getCost(currentGene, nextGene);
                    vLoad[indexCurrentVehicle] = futureLoad;
                }
            }
            
            currentGene = nextGene;
            nextGene = chr.getGeneAfter(currentGene);
                    
        } while (currentGene != firstVehicle);
        
        // Set the fitness (cost) of each vehicle
        chr.setVehiclesCosts(vCosts);
        
        // Sum the costs of the vehicles
        float globalCost=0;
        for (int i=0; i<vCosts.length; i++){
            globalCost += vCosts[i];
        }
        
        // Increment the number of fitness evaluations
        chr.incrementFitnessEvaluations();
        
        // Penalise the global cost by the number of times that the vehicles had to
        // go to the depot inside tours
        globalCost *= (1 + nrDepotsInsideTour * innerDepotPenalty);
        
        // Return the global cost
        return globalCost;
    }
}
