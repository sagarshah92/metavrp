
package org.metavrp.GA;

import java.util.Random;
import org.metavrp.GA.support.GAParameters;
import org.metavrp.GA.operators.*;
import org.metavrp.GA.operators.crossover.*;
import org.metavrp.GA.operators.mutation.InsertMutation;
import org.metavrp.GA.operators.mutation.InversionMutation;
import org.metavrp.GA.operators.mutation.SwapMutation;
import org.metavrp.GA.operators.mutation.SwapNextMutation;
import org.metavrp.GA.support.Tours;
import org.metavrp.VRP.CostMatrix;

/**
 *
 * @author David Pinheiro
 */
// TODO: if generations=0 never stop running
public class VRPGARun implements Runnable{
    
    private Population pop;             // Current population
    private static Population firstPop; // The first population, the one that was created randomly
    
    private int popSize;                // Size of the population
    private float mutationProb;         // Probability of mutation (0..1)
    private float crossoverProb;        // Probability of crossover (0..1)
    private int generations;            // Number of generations to run. 
    private float elitism;              // Use of elitism from 0 (no elitism) to 1 
                                        // (every chromosome on the new population comes from the old one)    
    private long acumulatedNrFitnessEvaluations = 0;    // The acumulated number of fitness evaluations on this run of the GA
    
    private GeneList geneList;          // Object with the genes (vehicles and customers)
    private CostMatrix costMatrix;      // Object with the costs between nodes
    
    private int generation=0;           // The current generation's number


    public VRPGARun(GAParameters params, GeneList geneList, CostMatrix costMatrix){
        
        this.popSize = params.getPopSize();
        this.elitism = params.getElitism();
        this.mutationProb = params.getMutationProb();
        this.crossoverProb= params.getCrossoverProb();
        this.generations = params.getGenerations();
        
        this.geneList = geneList;
        this.costMatrix = costMatrix;

    }

    @Override
    public void run(){
        
        long start = System.currentTimeMillis();
        
        // Randomly create the initial Population
        pop = new Population(popSize, geneList, costMatrix);
        
        // Copy the first population
        try{
            firstPop = (Population)pop.clone();
        } catch (CloneNotSupportedException ex){
            ex.printStackTrace();
        }
        
        // Some statistics
        float startBest = pop.getBestFitness();
        float startAverage = pop.getAverageFitness();
        float startWorst = pop.getWorstFitness();
        
        Population newPop = pop;
        long end; 
        
        do {
                // Mark the time at which the last population ended
                end = System.currentTimeMillis();
                
                // Acumulate the number of fitness evaluations
                acumulatedNrFitnessEvaluations += pop.getNrFitnessEvaluations();   

                // Print the population statistics
                printPopulationStatistics(pop, generation, end - start);

                // Reset the number of fitness evaluations
                pop.resetNrFitnessEvaluations();

                // Mark the time at which the next population starts
                start = System.currentTimeMillis();
         
                // Lets clone the old population
                // TODO: There's no need to this. We can just use one population.
                try{
                    newPop = (Population)pop.clone();
                } catch (CloneNotSupportedException ex){
                    ex.printStackTrace();
                }

                // Create a temporary array of chromosomes to be used on the next population
                Chromosome[] matingPool = new Chromosome[popSize];

                // Run the GA: Selection, Crossover, Mutation and Replacement
                for (int i=0;i<(popSize-(popSize*elitism));i=i+2) {
                    // If the population size is odd, on the last single element 
                    // we just do selection and mutation
                    if ((i+2)>popSize){
                        Chromosome parent = Selection.tournamentSelection(2,newPop);
                        Chromosome child = SwapNextMutation.swapNextMutation(mutationProb, parent);
                        matingPool[i] = child;
                    }
                    
                    else {

                        // 1. Selection
                        Chromosome[] parents = new Chromosome[2];
                        parents[0] = Selection.tournamentSelection(2,newPop);
                        parents[1] = Selection.tournamentSelection(2,newPop);

                        // 2. Crossover (with a given probability)
                        Chromosome[] childs;

//                        childs = PMX.PMX(parents,crossoverProb);
//                        childs = Edge3.Edge3(parents,crossoverProb);
                        childs = Order1.Order1(parents,crossoverProb);

                        // 3. Mutation (with a given probability)
                        childs[0]=SwapNextMutation.swapNextMutation(mutationProb, childs[0]);
                        childs[1]=SwapNextMutation.swapNextMutation(mutationProb, childs[1]);

                        matingPool[i]=childs[0];
                        matingPool[i+1]=childs[1];

                    }
                }

                // After Selection, Crossover, Mutation in all the chromosomes of the population,
                // lets do the replacement
                newPop = Replacement.populationReplacement(matingPool, pop, elitism, geneList, costMatrix);

                pop = newPop;   // Bye bye old population. Be trash collected!!!
                
                generation++;
            
        } while (generation<generations);
        
        // Print the final population stats
        System.out.println("\n");
        printPopulationStatistics(pop, generation, end - start);
        
        // Print the best element
        System.out.println("\nBest solution:" + pop.getTop(1)[0].toString());
        
        float endBest = pop.getBestFitness();
        float endAverage = pop.getAverageFitness();
        float endWorst = pop.getWorstFitness();
        
        System.out.println("\nImprovement of the best element, on this run: "+ ((startBest-endBest)/startBest));
        System.out.println("Improvement of the average element, on this run: "+ ((startAverage-endAverage)/startAverage));
        System.out.println("Improvement of the worst element, on this run: "+ ((startWorst-endWorst)/startWorst));
        System.out.println("Average number of fitness evaluations: "+ (acumulatedNrFitnessEvaluations/generation));
        System.out.println("Total number of fitness evaluations: "+acumulatedNrFitnessEvaluations);
    }
    
    
    // Prints some statistics about population
    public static void printPopulationStatistics(Population pop, int generation, long time){
        System.out.println("\n\nGeneration: "+generation);
        System.out.println("Best cost value: "+pop.getBestFitness());
        System.out.println("Best cost improvement: "+pop.calcBestImprovement(firstPop)*100 + " %");
        System.out.println("Average cost value: "+pop.getAverageFitness());
        System.out.println("Average cost improvement: "+pop.calcAverageImprovement(firstPop)*100 + " %");
        System.out.println("Worst cost value: "+pop.getWorstFitness());
        System.out.println("Worst cost improvement: "+pop.calcWorstImprovement(firstPop)*100 + " %");
        System.out.println("Time spent on this generation: "+time+"ms");
        System.out.println("Number of fitness evaluations: "+pop.getNrFitnessEvaluations());
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
