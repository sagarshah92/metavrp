
package org.metavrp.GA;

import org.metavrp.GA.support.GAParameters;
import org.metavrp.GA.operators.*;
import org.metavrp.GA.operators.crossover.*;
import org.metavrp.GA.support.Tours;
import org.metavrp.VRP.CostMatrix;

/**
 *
 * @author David Pinheiro
 */
// TODO: if generations=0 never stop running
public class VRPGARun implements Runnable{
    
    private Population pop;         // Current population
    private static Population firstPop;    // The first population, the one that was created randomly
    
    private int popSize;            // Size of the population
    private float mutationProb;     // Probability of mutation (0..1)
    private float crossoverProb;    // Probability of crossover (0..1)
    private int generations;        // Number of generations to run. 
    private float elitism;          // Use of elitism from 0 (no elitism) to 1 
                                    // (every chromosome on the new population comes from the old one)    
    private GeneList geneList;      // Object with the genes (vehicles and customers)
    private CostMatrix costMatrix;  // Object with the costs between nodes
    
    private int generation=0;       // The current generation's number

    
    public VRPGARun(GAParameters params, GeneList geneList, CostMatrix costMatrix){
        
        this.popSize = params.getPopSize();
        this.elitism = params.getElitism();
        this.mutationProb = params.getMutationProb();
        this.crossoverProb= params.getCrossoverProb();
        this.generations = params.getGenerations();
        
        this.geneList = geneList;
        this.costMatrix = costMatrix;
        
        // Create Population
        pop = new Population(popSize, geneList, costMatrix);
        
        // Copy the first population
        try{
            firstPop = (Population)pop.clone();
        } catch (CloneNotSupportedException ex){
            ex.printStackTrace();
        }
    }

    @Override
    public void run (){
        
        long start = System.currentTimeMillis();
        
        // Some statistics
        float startBest = pop.getBestFitness();
        float startAverage = pop.getAverageFitness();
        float startWorst = pop.getWorstFitness();
        
        Population newPop = pop;
        long end; 
        
        do {

                end = System.currentTimeMillis();
                printPopulationStatistics(pop, generation, end - start);
                start = System.currentTimeMillis();
         
                try{
                newPop = (Population)pop.clone();
                } catch (CloneNotSupportedException ex){
                    ex.printStackTrace();
                }
                
                Chromosome[] tmpPop = new Chromosome[popSize];
                
                //
                for (int i=0;i<popSize;i=i+2) {
                    // If the population size is odd, on the last single element 
                    // we just do selection and mutation
                    if ((i+2)>popSize){
                        Chromosome parent = Selection.tournamentSelection(2,newPop);
                        Chromosome child = Mutation.swapMutation(mutationProb, parent);
                        tmpPop[i] = child;
                    }
                    
                    else {

                        Chromosome[] parents = new Chromosome[2];
                        parents[0] = Selection.tournamentSelection(2,newPop);
                        parents[1] = Selection.tournamentSelection(2,newPop);

    //    System.out.println("Pai 0: "+parents[0].toString());   
    //    System.out.println("Pai 1: "+parents[1].toString());   
        //System.out.println("Pais: "+parents.hashCode());
                        Chromosome[] childs = new Chromosome[2];

    //                       childs[0]=(Chromosome)parents[0].clone();
    //                       childs[1]=(Chromosome)parents[1].clone();
//                        childs[0]=parents[0];
//                        childs[1]=parents[1];

        //System.out.println("Filhos: "+childs.hashCode());
    //                    childs = Crossover.crossoverPMX(parents,crossoverProb,costMatrix);
//                        childs = Edge3.Edge3(parents,crossoverProb);
                        childs = Order1.Order1(parents,crossoverProb);

                        childs[0]=Mutation.swapMutation(mutationProb, childs[0]);
                        childs[1]=Mutation.swapMutation(mutationProb, childs[1]);

    //    System.out.println("Filho 0: "+childs[0].toString()); 
    //    System.out.println("Filho 1: "+childs[1].toString()); 

                        tmpPop[i]=childs[0];
                        tmpPop[i+1]=childs[1];  //TODO: caso a população seja impar, isto dá barraca
                    }
                }
                
                newPop = Replacement.populationReplacement(tmpPop, pop, elitism, geneList, costMatrix);

                pop = newPop;   // Bye bye old population. Be trash collected!!!
                
                generation++;
            
        } while (generation<generations);
        
        float endBest = pop.getBestFitness();
        float endAverage = pop.getAverageFitness();
        float endWorst = pop.getWorstFitness();
        
        System.out.println("\n\n Best Improvement: "+ ((startBest-endBest)/startBest));
        System.out.println("Average Improvement: "+ ((startAverage-endAverage)/startAverage));
        System.out.println("Worst Improvement: "+ ((startWorst-endWorst)/startWorst));
    }
    
    
    // Prints some statistics about population
    public static void printPopulationStatistics(Population pop, int generation, long time){
        System.out.println("\nGeneration: "+generation);
        System.out.println("Best cost value: "+pop.getBestFitness());
        System.out.println("Best cost improvement: "+pop.calcBestImprovement(firstPop)*100 + " %");
        System.out.println("Average cost value: "+pop.getAverageFitness());
        System.out.println("Average cost improvement: "+pop.calcAverageImprovement(firstPop)*100 + " %");
        System.out.println("Worst cost value: "+pop.getWorstFitness());
        System.out.println("Worst cost improvement: "+pop.calcWorstImprovement(firstPop)*100 + " %");
        System.out.println("Time spent on this generation: "+time+"ms\n\n");
    }
    
    /*
     * Getters and Setters
     */
    // Return the current population
    public Population getPopulation(){
        return pop;
    }
    
    // Return the first population
    public Population getFirstPopulation(){
        return firstPop;
    }

    // Return the generation number
    public int getGeneration(){
        return this.generation;
    }
    
}
