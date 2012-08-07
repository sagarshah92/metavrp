 
package org.metavrp.algorithm.GA.executors;

import java.awt.geom.Point2D;
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
public class ChristofidesMingozziToth1979 implements Runnable{
    
    private int maxNrGenerationsWtImprovement;
    
    private String parameterToTest="undefined";
    
    private OperatorsAndParameters operators;   // GA operators

    private int nrVehicles;             // The number of vehicles
    private int vehicleCapacity;        // The vehicles capacity
    private float[] customerDemand;     // The customer's demand
    private int nrCustomers;            // The number of customers (nodes except depot)
    
    private int nrNodes;                // The number of nodes (nrCustomers + nrVehicles)
    
    private Problem problem;            // Definition of the problem
    private GeneticAlgorithm ga;        // Genetic Algorithm's options
    private CostMatrix costMatrix;      // Object with the costs between nodes
    
    File instanceFile = null;           // The file with the instance
    File statsFile = null;              // File to save the desired statistics

    public ChristofidesMingozziToth1979() {
        
        // Create the operators and parameters
        operators = new OperatorsAndParameters();
        
        // Change the initial parameters to suit this problem
        createInitialParameters();
    }
    
    
    /*
     * Runs this instances
     */
    @Override
    public void run(){
        
//        VRPGARun run = createInitialParameters(i);
//
//        // Start in a new thread
//        Thread vrpThread = new Thread(run, "GoldenWasilKellyChao1998");
//        vrpThread.start();
        
    }
    
    /* 
     * Runs a given instance
     */
    public void run(String fileName){
        
        VRPGARun run = createRunner(1, fileName);
        run.run();

//        // Start in a new thread
//        Thread vrpThread = new Thread(run, "GoldenWasilKellyChao1998");
//        vrpThread.start();
        
    }
    
    // Test an instance, given in the file name.
    public void testInstance(int nrRuns, int op, String fileName){
        System.out.println("Running ChristofidesMingozziToth1979");
        System.out.println("File: "+fileName);
        
        // Test different population sizes
        parameterToTest="None";
        for (int i=1; i<=nrRuns; i++){
            VRPGARun run = createRunner(i, fileName);
            run.run();
        }
    }
    
    /*
     * Runs tests on this instance
     */

    public void testParameters(int nrRuns, int op, String fileName){

        System.out.println("Running ChristofidesMingozziToth1979");

        if(op==1){
            // Test different population sizes
            parameterToTest="Population";
            populationTest(nrRuns, fileName);
        }
        
        if(op==2){
            //Test different tournament sizes
            parameterToTest="TournamentSize";
            tournamentTest(nrRuns, fileName);
        }
        
        if(op==3){
            // Test different crossover operators
            parameterToTest="CrossoverOperator";
            crossoverOperatorTest(nrRuns, fileName);
        }
        
        if(op==4){
            // Test various crossover probabilities
            parameterToTest="CrossoverProbabilities";
            crossoverProbabilityTest(nrRuns, fileName);
        }
        
        if(op==5){
            // Test the mutation operators
            parameterToTest="MutationOperator";
            testMutationOperators(nrRuns, fileName);
        }
        
        if(op==6){
            // Test the mutation probabilities
            parameterToTest="MutationProbabilities";
            testMutationProb(nrRuns, fileName);
        }
        
        if(op==7){
            // Test the elitism effect
            parameterToTest="Elitism";
            testElitism(nrRuns, fileName);
        }
        
        if(op==8){
            // Test inner depot penalty
            parameterToTest="InnerDepotPenalty";
            testInnerDepotPenalty(nrRuns, fileName);
        }
        
        if(op==10){
            parameterToTest="1VehicleNoInnerDepotPenalty";
            for (int i=1; i<=nrRuns; i++){
                VRPGARun run = createRunner(i, fileName);
                run.run();
            }
        }
        
       
//        for (int i=1;i<=nrRuns;i++){
//            VRPGARun run = createInitialParameters(i);
//
//            // Start in a new thread
//            Thread vrpThread = new Thread(run, "GoldenWasilKellyChao1998");
//            vrpThread.start();
//        }
        
    }
    
    public void populationTest(int nrRuns, String fileName){
        int nrGenes=76+14;
        for (int i=1; i<=nrRuns; i++){
            operators.setPopulationSize(10);
            operators.setReplacementElitism(0.12f);
            VRPGARun run = createRunner(i, fileName);
            run.run();
        }
        for (int i=1; i<=nrRuns; i++){
            operators.setPopulationSize(46);
            operators.setReplacementElitism(0.05f);
            VRPGARun run = createRunner(i, fileName);
            run.run();
        } 
        for (int i=1; i<=nrRuns; i++){
            operators.setPopulationSize(nrGenes);
            VRPGARun run = createRunner(i, fileName);
            run.run();
        } 
        for (int i=1; i<=nrRuns; i++){
            operators.setPopulationSize(nrGenes*2);
            VRPGARun run = createRunner(i, fileName);
            run.run();
        }
        for (int i=1; i<=nrRuns; i++){
            operators.setPopulationSize(nrGenes*10);
            VRPGARun run = createRunner(i, fileName);
            run.run();
        }
    }
    
    public void tournamentTest(int nrRuns, String fileName){
        createInitialParameters();
        for (int i=1; i<=nrRuns; i++){
            operators.setSelectionParam(2);
            VRPGARun run = createRunner(i, fileName);
            run.run();
        } 
        for (int i=1; i<=nrRuns; i++){
            operators.setSelectionParam(3);
            VRPGARun run = createRunner(i, fileName);
            run.run();
        } 
        for (int i=1; i<=nrRuns; i++){
            operators.setSelectionParam(5);
            VRPGARun run = createRunner(i, fileName);
            run.run();
        } 
        for (int i=1; i<=nrRuns; i++){
            operators.setSelectionParam(10);
            VRPGARun run = createRunner(i, fileName);
            run.run();
        }
    }
    
    public void crossoverOperatorTest(int nrRuns, String fileName){
        createInitialParameters();
        for (int i=1; i<=nrRuns; i++){
            operators.setCrossoverOperator("Order1.Order1");
            VRPGARun run = createRunner(i, fileName);
            run.run();
        } 
        for (int i=1; i<=nrRuns; i++){
            operators.setCrossoverOperator("PMX.PMX");
            VRPGARun run = createRunner(i, fileName);
            run.run();
        } 
        for (int i=1; i<=nrRuns; i++){
            operators.setCrossoverOperator("Edge3.Edge3");
            VRPGARun run = createRunner(i, fileName);
            run.run();
        }
    }
    
    public void crossoverProbabilityTest(int nrRuns, String fileName){
        createInitialParameters();
        for (int i=1; i<=nrRuns; i++){
            operators.setCrossoverProb(1f);
            VRPGARun run = createRunner(i, fileName);
            run.run();
        } 
        for (int i=1; i<=nrRuns; i++){
            operators.setCrossoverProb(0.9f);
            VRPGARun run = createRunner(i, fileName);
            run.run();
        } 
        for (int i=1; i<=nrRuns; i++){
            operators.setCrossoverProb(0.8f);
            VRPGARun run = createRunner(i, fileName);
            run.run();
        } 
        for (int i=1; i<=nrRuns; i++){
            operators.setCrossoverProb(0.5f);
            VRPGARun run = createRunner(i, fileName);
            run.run();
        } 
        for (int i=1; i<=nrRuns; i++){
            operators.setCrossoverProb(0.0f);
            VRPGARun run = createRunner(i, fileName);
            run.run();
        }
    }
    
    public void testMutationOperators(int nrRuns, String fileName){
        createInitialParameters();
        for (int i=1; i<=nrRuns; i++){
            operators.setMutationOperator("SwapNextMutation.swapNextMutation");
            VRPGARun run = createRunner(i, fileName);
            run.run();
        } 
        for (int i=1; i<=nrRuns; i++){
            operators.setMutationOperator("SwapMutation.swapMutation");
            VRPGARun run = createRunner(i, fileName);
            run.run();
        } 
        for (int i=1; i<=nrRuns; i++){
            operators.setMutationOperator("InsertMutation.insertMutation");
            VRPGARun run = createRunner(i, fileName);
            run.run();
        } 
        for (int i=1; i<=nrRuns; i++){
            operators.setMutationOperator("InversionMutation.inversionMutation");
            VRPGARun run = createRunner(i, fileName);
            run.run();
        }
    }
    
    public void testMutationProb(int nrRuns, String fileName){
        createInitialParameters();
        for (int i=1; i<=nrRuns; i++){
            operators.setMutationProb(10f/89);
            VRPGARun run = createRunner(i, fileName);
            run.run();
        } 
        for (int i=1; i<=nrRuns; i++){
            operators.setMutationProb(1f/89);
            VRPGARun run = createRunner(i, fileName);
            run.run();
        } 
        for (int i=1; i<=nrRuns; i++){
            operators.setMutationProb(1f/890);
            VRPGARun run = createRunner(i, fileName);
            run.run();
        } 
        for (int i=1; i<=nrRuns; i++){
            operators.setMutationProb(0f);
            VRPGARun run = createRunner(i, fileName);
            run.run();
        }
    }

    public void testElitism(int nrRuns, String fileName){
        createInitialParameters();
        for (int i=1; i<=nrRuns; i++){
            operators.setReplacementElitism(0.0f);
            VRPGARun run = createRunner(i, fileName);
            run.run();
        } 
        for (int i=1; i<=nrRuns; i++){
            operators.setReplacementElitism(1f/90);
            VRPGARun run = createRunner(i, fileName);
            run.run();
        } 
        for (int i=1; i<=nrRuns; i++){
            operators.setReplacementElitism(0.01f);
            VRPGARun run = createRunner(i, fileName);
            run.run();
        } 
        for (int i=1; i<=nrRuns; i++){
            operators.setReplacementElitism(0.05f);
            VRPGARun run = createRunner(i, fileName);
            run.run();
        }
        for (int i=1; i<=nrRuns; i++){
            operators.setReplacementElitism(0.1f);
            VRPGARun run = createRunner(i, fileName);
            run.run();
        }
        for (int i=1; i<=nrRuns; i++){
            operators.setReplacementElitism(0.2f);
            VRPGARun run = createRunner(i, fileName);
            run.run();
        }
        for (int i=1; i<=nrRuns; i++){
            operators.setReplacementElitism(0.3f);
            VRPGARun run = createRunner(i, fileName);
            run.run();
        }
        for (int i=1; i<=nrRuns; i++){
            operators.setReplacementElitism(0.5f);
            VRPGARun run = createRunner(i, fileName);
            run.run();
        }
    }
    
    public void testInnerDepotPenalty(int nrRuns, String fileName){
        createInitialParameters();
        for (int i=1; i<=nrRuns; i++){
            operators.setInnerDepotPenalty(0.0f);
            VRPGARun run = createRunner(i, fileName);
            run.run();
        } 
        for (int i=1; i<=nrRuns; i++){
            operators.setInnerDepotPenalty(0.05f);
            VRPGARun run = createRunner(i, fileName);
            run.run();
        } 
        for (int i=1; i<=nrRuns; i++){
            operators.setInnerDepotPenalty(0.1f);
            VRPGARun run = createRunner(i, fileName);
            run.run();
        } 
        for (int i=1; i<=nrRuns; i++){
            operators.setInnerDepotPenalty(1f);
            VRPGARun run = createRunner(i, fileName);
            run.run();
        }
    }

    
    public final void createInitialParameters(){
        
        nrVehicles=7;
        vehicleCapacity=200;
        nrCustomers=120;
        
        nrNodes = nrCustomers + nrVehicles;
        
        maxNrGenerationsWtImprovement=Integer.MAX_VALUE;  // The maximum number of generations without improvement
        
        // Population
//        operators.setPopulationSize(nrNodes);
        operators.setPopulationSize(2*nrNodes);       // Size of the population
        // Selection
        operators.setSelectionOperator("Selection.tournamentSelection");
        operators.setSelectionPercentage(1);
        operators.setSelectionParam(2);
        // Crossover
        operators.setCrossoverOperator("Edge3.Edge3");
        operators.setCrossoverProb(0.8f);       // Probability of crossover (0..1)
        // Mutation
//        operators.setMutationOperator("SwapMutation.swapMutation");
        operators.setMutationOperator("InversionMutation.inversionMutation");
        operators.setMutationProb(1f/nrNodes);   // Probability of mutation (0..1)
        // Replacement
        operators.setReplacementOperator("Replacement.populationReplacement");
        operators.setReplacementElitism(0.1f);  // Use of elitism from 0 (no elitism) to 1
        // Constraints
        operators.setInnerDepotPenalty(0.0f);

    }
    
    
    public VRPGARun createRunner(int nrRun, String fileName){
        
System.out.println("Run nº "+nrRun);

        instanceFile = new File("instances\\vrp\\ChristofidesMingozziToth1979\\Instances\\branchandcut.org\\"+fileName);
        String instanceFileName = instanceFile.getAbsolutePath();
        
        statsFile = new File("stats\\ChristofidesMingozziToth1979\\n."+parameterToTest+"."+fileName+".stats");
        String statsFileName = statsFile.getAbsolutePath();
        
        
        // Create the cost matrix
        costMatrix = new CostMatrix(getVRPCostMatrix(instanceFileName, nrCustomers)); // Create the CostMatrix object
        
        // Create the Gene List
        customerDemand = getCustomerDemand(instanceFileName, nrCustomers);
        problem = generateVRPProblem(costMatrix, nrVehicles, vehicleCapacity, nrCustomers, customerDemand);
        
        ga = new GeneticAlgorithm(operators, problem);
        
        // The runner
        VRPGARun run = new VRPGARun(ga, costMatrix, statsFileName, nrRun, maxNrGenerationsWtImprovement);
        
        // What's the parameter to test?
        run.setParameterToTest(parameterToTest);
        
        // What's the period at which we should log the best element of the population?
        run.setStatsPeriod(1000);
        
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

    public void setNrCustomers(int nrCustomers) {
        this.nrCustomers = nrCustomers;
    }

    public void setNrVehicles(int nrVehicles) {
        this.nrVehicles = nrVehicles;
    }

    public void setVehicleCapacity(int vehicleCapacity) {
        this.vehicleCapacity = vehicleCapacity;
    }

}
