
package org.metavrp.algorithm.GA.executors;

import java.io.*;
import java.util.ArrayList;
import org.metavrp.Problem;
import org.metavrp.algorithm.GA.VRPGARun;
import org.metavrp.algorithm.GA.operators.OperatorsAndParameters;
import org.metavrp.algorithm.GeneticAlgorithm;
import org.metavrp.problem.CostMatrix;
import org.metavrp.problem.Customer;
import org.metavrp.problem.Depot;
import org.metavrp.problem.Vehicle;

/**
 *
 * @author David Pinheiro
 */
public class GoldenWasilKellyChao1998 implements Runnable{
    
    private int nrRun;
    private int nrRuns;
    
    int maxNrGenerationsWtImprovement=484;
    
    private OperatorsAndParameters operators;   // GA operators

    private int nrVehicles;             // The number of vehicles
    private int vehicleCapacity;        // The vehicles capacity
    private float[] customerDemand;     // The customer's demand
    private int nrCustomers;            // The number of customers (nodes except depot)
    
    private Problem problem;            // Definition of the problem
    private GeneticAlgorithm ga;        // Genetic Algorithm's options
    private CostMatrix costMatrix;      // Object with the costs between nodes
    
    File instanceFile = null;           // The file with the instance
    File statsFile = null;              // File to save the desired statistics

    public GoldenWasilKellyChao1998(int runs) {
        this.nrRuns=runs;
        
        // Create the operators and parameters
        operators = new OperatorsAndParameters();
    }
    
    
    /*
     * Runs this instances
     */
    @Override
    public void run(){
        int nrGenes=502;
        if (nrRuns<=10){
            maxNrGenerationsWtImprovement=50*10;
            operators.setPopulationSize(502/10);
            VRPGARun run = createInitialParameters(nrRuns);
            run.run();
        } else if(nrRuns<=20){
            maxNrGenerationsWtImprovement=50*2;
            operators.setPopulationSize(502/2);
            VRPGARun run = createInitialParameters(nrRuns);
            run.run();
        } else if(nrRuns<=30){
            maxNrGenerationsWtImprovement=50;
            operators.setPopulationSize(502);
            VRPGARun run = createInitialParameters(nrRuns);
            run.run();
        } else if(nrRuns<=40){
            maxNrGenerationsWtImprovement=50/2;
            operators.setPopulationSize(502*2);
            VRPGARun run = createInitialParameters(nrRuns);
            run.run();
        } else if(nrRuns<=50){
            maxNrGenerationsWtImprovement=50/10;
            operators.setPopulationSize(502*10);
            VRPGARun run = createInitialParameters(nrRuns);
            run.run();
        } else if(nrRuns<=60){
            maxNrGenerationsWtImprovement=60/20;
            operators.setPopulationSize(502*20);
            VRPGARun run = createInitialParameters(nrRuns);
            run.run();
        } 


//        for (int i=1;i<=nrRuns;i++){
//            VRPGARun run = createInitialParameters(i);
//
//            // Start in a new thread
//            Thread vrpThread = new Thread(run, "GoldenWasilKellyChao1998");
//            vrpThread.start();
//        }
    }
    
    
    public VRPGARun createInitialParameters(int nrRun){
                
        instanceFile = new File("instances\\vrp\\GoldenWasilKellyChao1998\\Instances\\www.rhsmith.umd.edu\\Golden_12.vrp");
        String instanceFileName = instanceFile.getAbsolutePath();
        
        statsFile = new File("stats\\GoldenWasilKellyChao1998\\Golden_12.stats");
        String statsFileName = statsFile.getAbsolutePath();
        
        nrVehicles=19;
        vehicleCapacity=1000;
        nrCustomers=483;
        
//        int maxNrGenerationsWtImprovement=nrCustomers;  // The maximum number of generations without improvement
        
        // Population
//        operators.setPopulationSize(nrCustomers+1);       // Size of the population
        // Selection
        operators.setSelectionOperator("Selection.tournamentSelection");
        operators.setSelectionPercentage(1);
        operators.setSelectionParam(2);
        // Crossover
        operators.setCrossoverOperator("PMX.PMX");
        operators.setCrossoverProb(0.8f);       // Probability of crossover (0..1)
        // Mutation
        operators.setMutationOperator("SwapMutation.swapMutation");
        operators.setMutationProb(1f/(nrCustomers+1));   // Probability of mutation (0..1)
        // Replacement
        operators.setReplacementOperator("Replacement.populationReplacement");
        operators.setReplacementElitism(0.05f);  // Use of elitism from 0 (no elitism) to 1 
        // Constraints
        operators.setInnerDepotPenalty(0.05f);
        
        // Create the cost matrix
        costMatrix = new CostMatrix(instanceFileName, true); //Open the cost matrix from a .vrp formatted file
        
        // Create the Gene List
        customerDemand = getCustomerDemand(instanceFileName, nrCustomers);
        problem = generateVRPProblem(costMatrix, nrVehicles, vehicleCapacity, nrCustomers, customerDemand);
        
        ga = new GeneticAlgorithm(operators, problem);
        
        // The runner
        VRPGARun run = new VRPGARun(ga, costMatrix, statsFileName, nrRun, maxNrGenerationsWtImprovement);
        
        return run;
    }
    
    
    /*
     * Returns the customer's demand
     * TODO: Needs to garantee that the nrCustomers is correct, that there aren't more 
     * customers on the file
     */
    public static float[] getCustomerDemand(String fileName, int nrCustomers){

        float[] demand = new float[nrCustomers+1]; // The customer demand
        
        try{
            FileInputStream fistream = new FileInputStream(fileName);
            DataInputStream in = new DataInputStream(fistream);
            BufferedReader br = new BufferedReader(new InputStreamReader(in));
            
            // Read file, line by line
            String fullLine = br.readLine();
            String[] lineRead=null;

            // Keep reading the file until it finds DEMAND_SECTION
            while (!br.readLine().contains("DEMAND_SECTION")){};

            // Then start updating the customers demand until it finds DEMAND_SECTION
            fullLine = br.readLine();
            while (!fullLine.contains("DEPOT_SECTION")){
                lineRead=fullLine.split("\\s+");    // Split on tabs and spaces
                // If the line readed isn't empty, update the customer demand
                if (!lineRead[0].equals("")){
                    int node = Integer.parseInt(lineRead[0]);    // The node number
                    demand[node] = Integer.parseInt(lineRead[1]);// The node's (customer) demand
                }
                fullLine = br.readLine();
            }

            //Close the stream
            in.close();
        }catch (Exception e){//Catch exception if any
            e.printStackTrace();
        }

        return demand;
    }
    
    
    /*
     * Given some number of vehicles and nodes, the vehicle's capacity and a customer's
     * demand matrix, creates the corresponding list of genes.
     */
    public static Problem generateVRPProblem(CostMatrix costMatrix,int nrVehicles, int vehicleCapacity, int nrCustomers, float[] customerDemand){
        
        // Generate the customers array
        ArrayList<Customer> customers = new ArrayList<Customer>(nrCustomers);
        // Add the customers
        for (int i=1;i<=nrCustomers;i++){
            customers.add(new Customer(i, customerDemand[i]));
        }
        
        // Add the depot
        ArrayList<Depot> depots = new ArrayList<Depot>();
        depots.add(new Depot(0));
        
        // Generate the vehicles array
        ArrayList<Vehicle> vehicles = new ArrayList<Vehicle>(nrVehicles);
        // Add the vehicles
        // All of them start from the first node (node 0)
        for (int i=0;i<nrVehicles;i++){
            vehicles.add(new Vehicle(0, vehicleCapacity));
        }

        return new Problem(costMatrix, customers, vehicles, depots);  

    }

}
