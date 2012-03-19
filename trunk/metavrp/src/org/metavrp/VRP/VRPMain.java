
package org.metavrp.VRP;

import java.io.File;
import java.util.ArrayList;

import org.metavrp.GA.support.*;
import org.metavrp.GA.*;

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
        
        int popSize=30;
        int nrVehicles=1;
        float elitism=0.0f;
        float mutationProb=0.005848f;
//        float mutationProb=0.142857f;
        float crossoverProb=0.8f;
        int generations=100;
        
        File file = new File("instances\\vrp\\dm171.txt");  
//        File file = new File("instances\\vrp\\dm7.txt");  
        String fileName = file.getAbsolutePath();
System.out.println(fileName);        
        CostMatrix costMatrix = new CostMatrix(fileName, false);
        
        // Create the Gene List
        GeneList geneList = generateGeneList(nrVehicles, costMatrix.size-1, 100, 1000);

        GAParameters params = new GAParameters(popSize, elitism, mutationProb, crossoverProb, generations);

        VRPGARun run = new VRPGARun(params, geneList, costMatrix);

        // Start in a new thread
        Thread vrpThread = new Thread(run, "metaVRP");
        vrpThread.start();

    }
    
    
    // Given some number of nodes and of vehicles, creates a simple list of genes.
    // This is a very simplified (and useless?) constructor, as all the vehicles start from node 0.
    public static GeneList generateGeneList(int nrVehicles, int nrNodes, int maxDemand, int capacity){
        
        // Generate the customers array
        ArrayList<Customer> customers = new ArrayList<Customer>(nrNodes);
        
        // Add the customers
        for (int i=1;i<nrNodes+1;i++){
            customers.add(new Customer(i,i,Randomizer.randomInt(maxDemand)));
        }
        
        // Generate the vehicles array
        ArrayList<Vehicle> vehicles = new ArrayList<Vehicle>(nrVehicles);
        
        // Add the vehicles
        // All of them start from the first node (node 0)
        for (int i=0;i<nrVehicles;i++){
            vehicles.add(new Vehicle(-1-i, capacity));
        }

        return new GeneList(customers, vehicles);        
    }

}
