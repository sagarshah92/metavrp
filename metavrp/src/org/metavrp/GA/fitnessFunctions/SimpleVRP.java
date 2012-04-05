
package org.metavrp.GA.fitnessFunctions;

import org.metavrp.GA.Chromosome;
import org.metavrp.GA.Gene;

/**
 *
 * @author David Pinheiro
 */
public class SimpleVRP {
    
    // Measure the fitness of this chromosome. The cost associated with it.
    // TODO: Add support for maximum tour lenght
    // TODO: Add the objective that each vehicle visits the same customers or gets 
    // almost the same cost.
    public static float measureCost(Chromosome chr){
        
        int nVehicles = chr.getNrVehicles();            // Count the number of vehicles
        float[] vCosts = new float[nVehicles];          // The fitness (routing costs) of the vehicles
        int indexCurrentVehicle = 0;                    // The number of the current vehicle
        
        // Start with the first vehicle
        Gene firstVehicle = chr.getFirstVehicle();      // The first vehicle
        Gene currentVehicle = firstVehicle;             // the current vehicle
        
        Gene currentGene = firstVehicle;                // The current gene
        Gene nextGene = chr.getGeneAfter(firstVehicle); // The next gene
        
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
            } else {
                // If the next gene is a customer, update the current vehicle's cost
                vCosts[indexCurrentVehicle] += chr.getCost(currentGene, nextGene);
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
        
        // Return the global cost
        return globalCost;
    }
}
