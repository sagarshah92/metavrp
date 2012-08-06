
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
import org.metavrp.problem.Depot;
import org.metavrp.problem.Vehicle;
import org.metavrp.solution.Statistics;
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
        GeneticAlgorithm ga = new GeneticAlgorithm(problem1);
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
        int nrCustomers = costMatrix.getSize()-1; // -1 for the depot

        for (int i=1; i<=nrCustomers; i++){
            problem1.addCustomer(new Customer(i,customerDemand));
        }
        
        // 3. Depots
        problem1.addDepot(new Depot(0));
        
        // 4. Vehicles
        int nrVehicles=1;
        float vehicleCapacity=10;
        int depotIndex = problem1.getDepots().get(0).getNode();
        
        for (int i=1; i<=nrVehicles; i++){
            problem1.addVehicle(new Vehicle(depotIndex,vehicleCapacity));
        }
        
        
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
        solution.setStatistics(new Statistics(statsFile));
        
        // The runner
        VRPGARun run = new VRPGARun(ga, costMatrix, statsFile.getAbsolutePath(), nrRun, maxNrGenerationsWtImprovement);
        
        return run;
    }

}
