
package org.metavrp.GA.executors;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import org.metavrp.GA.GeneList;
import org.metavrp.GA.VRPGARun;
import org.metavrp.GA.operators.OperatorsAndParameters;
import org.metavrp.VRP.CostMatrix;
import org.metavrp.VRP.Customer;
import org.metavrp.VRP.Vehicle;

/**
 *
 * @author David Pinheiro
 */
public class GoldenWasilKellyChao1998 implements Runnable{
    
    private int nrRun;
    private int nrRuns;
    
    private OperatorsAndParameters operators;   // GA operators

    private int nrVehicles;             // The number of vehicles
    private int vehicleCapacity;        // The vehicles capacity
    private float[] customerDemand;     // The customer's demand
    private int nrCustomers;            // The number of customers (nodes except depot)
    
    private GeneList geneList;          // Object with the genes (vehicles and customers)
    private CostMatrix costMatrix;      // Object with the costs between nodes
    
    File instanceFile = null;           // The file with the instance
    File statsFile = null;              // File to save the desired statistics

    public GoldenWasilKellyChao1998(int runs) {
        this.nrRuns=runs;
    }
    
    
    /*
     * Runs this instances
     */
    @Override
    public void run(){

        for (int i=1;i<=nrRuns;i++){
            VRPGARun run = createInitialParameters(i);

            // Start in a new thread
            Thread vrpThread = new Thread(run, "GoldenWasilKellyChao1998");
            vrpThread.start();
        }
    }
    
    
    public VRPGARun createInitialParameters(int nrRun){
                
        instanceFile = new File("instances\\vrp\\GoldenWasilKellyChao1998\\Instances\\www.rhsmith.umd.edu\\Golden_12.vrp");
        String instanceFileName = instanceFile.getAbsolutePath();
        
        statsFile = new File("stats\\GoldenWasilKellyChao1998\\Golden_12.stats");
        String statsFileName = statsFile.getAbsolutePath();
        
        nrVehicles=19;
        vehicleCapacity=1000;
        nrCustomers=483;
        
        int maxNrGenerationsWtImprovement=100;  // The maximum number of generations without improvement
        
        // Create the operators and parameters
        operators = new OperatorsAndParameters();
        // Population
        operators.setPopulationSize(nrCustomers+1);       // Size of the population
        // Selection
        operators.setSelectionOperator("Selection.tournamentSelection");
        operators.setSelectionPercentage(1);
        operators.setSelectionParam(2);
        // Crossover
        operators.setCrossoverOperator("PMX.PMX");
        operators.setCrossoverProb(0.8f);       // Probability of crossover (0..1)
        // Mutation
        operators.setMutationOperator("SwapNextMutation.swapNextMutation");
        operators.setMutationProb(1/(nrCustomers+1));   // Probability of mutation (0..1)
        // Replacement
        operators.setReplacementOperator("Replacement.populationReplacement");
        operators.setReplacementElitism(0.1f);  // Use of elitism from 0 (no elitism) to 1 
        // Constraints
        operators.setInnerDepotPenalty(0.01f);
        
        // Create the cost matrix
        costMatrix = new CostMatrix(instanceFileName, true); //Open the cost matrix from a .vrp formatted file
        
        // Create the Gene List
        customerDemand = getCustomerDemand(instanceFileName, nrCustomers);
        geneList = generateVRPGeneList(nrVehicles, vehicleCapacity, nrCustomers, customerDemand);
        
        // The runner
        VRPGARun run = new VRPGARun(operators, geneList, costMatrix, statsFileName, nrRun, maxNrGenerationsWtImprovement);
        
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
    public static GeneList generateVRPGeneList(int nrVehicles, int vehicleCapacity, int nrCustomers, float[] customerDemand){
        
        // Generate the customers array
        ArrayList<Customer> customers = new ArrayList<Customer>(nrCustomers);
        
        // Add the customers
        for (int i=1;i<=nrCustomers;i++){
            customers.add(new Customer(i, i, customerDemand[i]));
        }
        
        // Generate the vehicles array
        ArrayList<Vehicle> vehicles = new ArrayList<Vehicle>(nrVehicles);
        
        // Add the vehicles
        // All of them start from the first node (node 0)
        for (int i=0;i<nrVehicles;i++){
            vehicles.add(new Vehicle(-1-i, 0, vehicleCapacity));
        }

        return new GeneList(customers, vehicles);        

    }

}
