
package org.metavrp.VRP;

import java.io.File;
import java.util.ArrayList;

import org.metavrp.GA.support.*;
import org.metavrp.GA.*;
import org.metavrp.GA.executors.GoldenWasilKellyChao1998;
import org.metavrp.GA.operators.OperatorsAndParameters;

//import java.util.logging.Level;
//import java.util.logging.Logger;


/**
 *
 * @author David Pinheiro
 */
public class VRPMain {
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {


        // Run GoldenWasilKellyChao1998 instances
        GoldenWasilKellyChao1998 run = new GoldenWasilKellyChao1998(1);

        // Run a random problem
//        VRPGARun run = runRandomProblem(1);

        // Start in a new thread
        Thread vrpThread = new Thread(run, "metaVRP");
        vrpThread.start();


    }
    
    
    // Run a randomly generated problem
    public static VRPGARun runRandomProblem(int nrRun){
        File file = new File("instances\\vrp\\dm171.txt");  
//        File file = new File("instances\\vrp\\dm7.txt");
        String fileName = file.getAbsolutePath();
        CostMatrix costMatrix = new CostMatrix(fileName, false);
        
        File statsFile = new File("stats\\dm171.stats");  
        String statsFileName = statsFile.getAbsolutePath();
        
        int nrVehicles=1;
        float vehicleCapacity=20;
        float customerDemand=1;
        int maxNrGenerationsWtImprovement=100;

        // Create the Gene List
        GeneList geneList = generateRandomGeneList(nrVehicles, costMatrix.size-nrVehicles, customerDemand, vehicleCapacity);

        // Create the operators and parameters
        OperatorsAndParameters operators = new OperatorsAndParameters();
        operators.setPopulationSize(172);
        operators.setCrossoverOperator("PMX.PMX");
        operators.setCrossoverProb(0.8f);
        operators.setMutationOperator("SwapMutation.swapMutation");
        operators.setMutationProb(1/costMatrix.size);
        operators.setReplacementElitism(0.1f);
        operators.setInnerDepotPenalty(0.01f);

        // The runner
        VRPGARun run = new VRPGARun(operators, geneList, costMatrix, statsFileName, nrRun, maxNrGenerationsWtImprovement);
        
        return run;
    }
    
    
    // Given some number of nodes and of vehicles, creates a simple list of genes.
    // This is a very simplified (and useless?) constructor, as all the vehicles start from node 0.
    public static GeneList generateRandomGeneList(int nrVehicles, int nrNodes, float demand, float capacity){
        
        // Generate the customers array
        ArrayList<Customer> customers = new ArrayList<Customer>(nrNodes);
        
        // Add the customers
        for (int i=1;i<nrNodes+1;i++){
            customers.add(new Customer(i, i, demand));
        }
        
        // Generate the vehicles array
        ArrayList<Vehicle> vehicles = new ArrayList<Vehicle>(nrVehicles);
        
        // Add the vehicles
        // All of them start from the first node (node 0)
        for (int i=0;i<nrVehicles;i++){
            vehicles.add(new Vehicle(-1-i, 0, capacity));
        }

        return new GeneList(customers, vehicles);        
    }

}
