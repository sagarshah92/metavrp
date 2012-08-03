
package org.metavrp;

import org.metavrp.algorithm.GA.VRPGARun;
import org.metavrp.algorithm.GA.GeneList;
import java.io.File;
import java.util.ArrayList;

import org.metavrp.algorithm.GA.executors.ChristofidesEilon1971;
import org.metavrp.algorithm.GA.executors.ChristofidesMingozziToth1979;
import org.metavrp.algorithm.GA.executors.GoldenWasilKellyChao1998;
import org.metavrp.algorithm.GA.operators.OperatorsAndParameters;
import org.metavrp.algorithm.GeneticAlgorithm;
import org.metavrp.problem.CostMatrix;
import org.metavrp.problem.Customer;
import org.metavrp.problem.Vehicle;
import org.metavrp.solution.StopCondition;

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
        
        // Run ChristofidesMingozziToth1979 instances
//        String fileName = "M-n121-k7.vrp";
//        int op=0;
//        int nrRuns = 10;
//        ChristofidesMingozziToth1979 run = new ChristofidesMingozziToth1979();
//        run.testInstance(nrRuns, op, fileName);
       

//        // Run ChristofidesEilon1971 instances
//        String fileName = "E-n101-k14.vrp";
//        int op=0;
//        int nrRuns = 10;
//        ChristofidesEilon1971 run = new ChristofidesEilon1971();
//        run.testInstance(nrRuns, op, fileName);

        
        // Run GoldenWasilKellyChao1998 instances
//        for (int i=1; i<=nrRuns; i++){
//            GoldenWasilKellyChao1998 run = new GoldenWasilKellyChao1998(i);
//            run.run();
//        }

        // Run a random problem
        VRPGARun run = runRandomProblem(1);

        // Start in a new thread
        Thread vrpThread = new Thread(run, "metaVRP");
        vrpThread.start();

    }
    
    
    // Run a randomly generated problem
    public static VRPGARun runRandomProblem(int nrRun){
        
        /* 
         * Main objects
         */
        Problem problem1 = new Problem();
        GeneticAlgorithm ga = new GeneticAlgorithm();
        Solution solution = new Solution();
        
        /* 
         * Problem definition
         */
        // 1. Cost Matrix
        File file = new File("instances\\vrp\\dm171.txt");  
//        File file = new File("instances\\vrp\\dm7.txt");
        String fileName = file.getAbsolutePath();
        CostMatrix costMatrix = new CostMatrix(fileName, false);
        problem1.setCostMatrix(costMatrix);
        
        // 2. Customers
        float customerDemand=1;
        
        // 3. Vehicles
        int nrVehicles=1;
        float vehicleCapacity=200;
        
        // 4. Depots
        
        

        
        
        /*
         * Algorithm definition
         */
        // Create the operators and parameters
        OperatorsAndParameters operators = new OperatorsAndParameters();
        operators.setPopulationSize(172);
        operators.setCrossoverOperator("Edge3.Edge3");
        operators.setCrossoverProb(0.8f);
        operators.setMutationOperator("SwapMutation.swapMutation");
        operators.setMutationProb(1f/costMatrix.getSize());
        operators.setReplacementElitism(0.1f);
        operators.setInnerDepotPenalty(0.01f);

        // Set this operators and parameters on the GA
        ga.setOperatorsAndParameters(operators);
        
        
        /*
         * Solution/Output definition
         */
        // StopCondition
        int maxNrGenerationsWtImprovement=100;
        StopCondition stop = new StopCondition();
        stop.setMaxNumGenerationsWtImprovement(maxNrGenerationsWtImprovement);
        solution.setStopCondition(stop);
        
        // Stats filename
        File statsFile = new File("stats\\dm171.stats");
//        File statsFile = new File("stats\\dm7.stats");
        String statsFileName = statsFile.getAbsolutePath();
        solution.setStatsFileName(statsFileName);
        
        
        
        
        // Create the Gene List
        GeneList geneList = generateRandomGeneList(nrVehicles, costMatrix.getSize()-nrVehicles, customerDemand, vehicleCapacity);

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
