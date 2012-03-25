
package org.metavrp.GA.executors;

import java.awt.geom.Point2D;
import java.io.*;
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
public class ChristofidesEilon1971 implements Runnable{
    
    private int nrRun;
    private int nrRuns;
    
    int maxNrGenerationsWtImprovement;
    
    private OperatorsAndParameters operators;   // GA operators

    private int nrVehicles;             // The number of vehicles
    private int vehicleCapacity;        // The vehicles capacity
    private float[] customerDemand;     // The customer's demand
    private int nrCustomers;            // The number of customers (nodes except depot)
    
    private GeneList geneList;          // Object with the genes (vehicles and customers)
    private CostMatrix costMatrix;      // Object with the costs between nodes
    
    File instanceFile = null;           // The file with the instance
    File statsFile = null;              // File to save the desired statistics

    public ChristofidesEilon1971(int runs) {
        this.nrRuns=runs;
        
        // Create the operators and parameters
        operators = new OperatorsAndParameters();
    }
    
    
    /*
     * Runs this instances
     */
    @Override
    public void run(){

        // Test different population sizes
//        populationTest();
        
        //Test different tournament sizes
//        tournamentTest();
        
        // Test different crossover operators
        crossoverOperatorTest();
        
        
//        for (int i=1;i<=nrRuns;i++){
//            VRPGARun run = createInitialParameters(i);
//
//            // Start in a new thread
//            Thread vrpThread = new Thread(run, "GoldenWasilKellyChao1998");
//            vrpThread.start();
//        }
        
        System.out.println("Run nº "+nrRuns);
    }
    
    public void populationTest(){
        int nrGenes=76+14;
        if (nrRuns<=20){
            operators.setPopulationSize(nrGenes/10);
            VRPGARun run = createInitialParameters(nrRuns);
            operators.setReplacementElitism(0.12f);
            run.run();
        } else if(nrRuns<=40){
            operators.setPopulationSize((nrGenes/2)+1);
            VRPGARun run = createInitialParameters(nrRuns);
            run.run();
        } else if(nrRuns<=60){
            operators.setPopulationSize(nrGenes);
            VRPGARun run = createInitialParameters(nrRuns);
            run.run();
        } else if(nrRuns<=80){
            operators.setPopulationSize(nrGenes*2);
            VRPGARun run = createInitialParameters(nrRuns);
            run.run();
        } else if(nrRuns<=100){
            operators.setPopulationSize(nrGenes*10);
            VRPGARun run = createInitialParameters(nrRuns);
            run.run();
        } 
    }
    
    public void tournamentTest(){
        if (nrRuns<=20){
            VRPGARun run = createInitialParameters(nrRuns);
            operators.setSelectionParam(2);
            run.run();
        } else if(nrRuns<=40){
            VRPGARun run = createInitialParameters(nrRuns);
            operators.setSelectionParam(3);
            run.run();
        } else if(nrRuns<=60){
            VRPGARun run = createInitialParameters(nrRuns);
            operators.setSelectionParam(5);
            run.run();
        } else if(nrRuns<=80){
            VRPGARun run = createInitialParameters(nrRuns);
            operators.setSelectionParam(10);
            run.run();
        }
    }
    
    public void crossoverOperatorTest(){
        if (nrRuns<=20){
            VRPGARun run = createInitialParameters(nrRuns);
            operators.setCrossoverOperator("Order1.Order1");
            run.run();
        } else if(nrRuns<=40){
            VRPGARun run = createInitialParameters(nrRuns);
            operators.setCrossoverOperator("PMX.PMX");
            run.run();
        } else if(nrRuns<=60){
            VRPGARun run = createInitialParameters(nrRuns);
            operators.setCrossoverOperator("Edge3.Edge3");
            run.run();
        }
    }
    
    
    public VRPGARun createInitialParameters(int nrRun){
                
        instanceFile = new File("instances\\vrp\\ChristofidesEilon1971\\Instances\\Vrp-Set-E\\E-n76-k14.vrp");
        String instanceFileName = instanceFile.getAbsolutePath();
        
        statsFile = new File("stats\\ChristofidesEilon1971\\E-n76-k14.stats");
        String statsFileName = statsFile.getAbsolutePath();
        
        nrVehicles=14;
        vehicleCapacity=100;
        nrCustomers=75;
        
        int nrNodes = nrCustomers + nrVehicles + 1;     // +1 for parity reasons
        
        maxNrGenerationsWtImprovement=Integer.MAX_VALUE;  // The maximum number of generations without improvement
        
        // Population
        operators.setPopulationSize(nrNodes);       // Size of the population
        // Selection
        operators.setSelectionOperator("Selection.tournamentSelection");
        operators.setSelectionPercentage(1);
        operators.setSelectionParam(2);
        // Crossover
        operators.setCrossoverOperator("PMX.PMX");
        operators.setCrossoverProb(0.8f);       // Probability of crossover (0..1)
        // Mutation
        operators.setMutationOperator("SwapMutation.swapMutation");
        operators.setMutationProb(1f/nrNodes);   // Probability of mutation (0..1)
        // Replacement
        operators.setReplacementOperator("Replacement.populationReplacement");
        operators.setReplacementElitism(0.05f);  // Use of elitism from 0 (no elitism) to 1 
        // Constraints
        operators.setInnerDepotPenalty(0.05f);
        
        // Create the cost matrix
        costMatrix = new CostMatrix(getVRPCostMatrix(instanceFileName, nrCustomers)); // Create the CostMatrix object
        
        // Create the Gene List
        customerDemand = getCustomerDemand(instanceFileName, nrCustomers);
        geneList = generateVRPGeneList(nrVehicles, vehicleCapacity, nrCustomers, customerDemand);
        
        // The runner
        VRPGARun run = new VRPGARun(operators, geneList, costMatrix, statsFileName, nrRun, maxNrGenerationsWtImprovement);
        
        return run;
    }
    
        /*
     * Gets the cost matrix of a .vrp formatted instance.
     * It goes to the section named NODE_COORD_SECTION and measures the distances 
     * between every two nodes and puts on a float[][] matrix. It goes to the
     * DEPOT_SECTION and measures the distance between the depot and every other node.
     * The depot will be on position 0 of the distance matrix.
     */
    public static float[][] getVRPCostMatrix(String fileName, int nrCustomers){
        
        // Initializations
        int size = nrCustomers+1;      // The size of the matrix needs to be measured. +1 for the depot
        float[][] floatCostMatrix = new float[size][size];   // Create the matrix. +1 element to the depot
        float[][] coordinates = new float[size][2];       // The coordinates in a n by 2 matrix
        
        try{
            FileInputStream fistream = new FileInputStream(fileName);
            DataInputStream in = new DataInputStream(fistream);
            BufferedReader br = new BufferedReader(new InputStreamReader(in));
            String fullLine=null;
            String[] lineRead=null;
            
            // Go to the NODE_COORD_SECTION
            while (!br.readLine().contains("NODE_COORD_SECTION")){};
        
            // Get the coordinates of the nodes
            fullLine = br.readLine();
            while (!fullLine.contains("DEMAND_SECTION")){
                lineRead = fullLine.split("\\s+");    // Split on tabs and spaces
                // If the line readed isn't empty, get the node coordinates
                if (!lineRead[0].equals("")){
                    int node = Integer.parseInt(lineRead[0]);               // The node number
                    coordinates[node-1][0] = Float.parseFloat(lineRead[1]);   // The x coordinate. -1 because the depot has index 1
                    coordinates[node-1][1] = Float.parseFloat(lineRead[2]);   // The y coordinate. -1 because the depot has index 1
                }

                fullLine = br.readLine();   // Read the next line
            }
        
            // Measure the distances between every two nodes
            for (int i=0; i < size; i++){
                for (int j=0; j< size; j++){
                    double x1 = coordinates[i][0];
                    double y1 = coordinates[i][1];
                    double x2 = coordinates[j][0];
                    double y2 = coordinates[j][1];
                    floatCostMatrix[i][j] = (float) Point2D.distance(x1, y1, x2, y2);
                }
            }
        }catch (Exception e){//Catch exception if any
            e.printStackTrace();
        }

        return floatCostMatrix;
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
                    demand[node-1] = Integer.parseInt(lineRead[1]);// The node's (customer) demand. -1 because the depot has the number 1
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
