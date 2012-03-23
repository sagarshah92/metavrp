
package org.metavrp.GA;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.util.Random;
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

public class VRPGARun implements Runnable{
    
    private String statsFileName;
    private FileWriter statsFileWriter;
    private int run;
            
    // -----------------------
    // Variables for this run
    // -----------------------
    private Population pop;             // Current population
    private static Population firstPop; // The first population, the one that was created randomly
    
    private GeneList geneList;          // Object with the genes (vehicles and customers)
    private CostMatrix costMatrix;      // Object with the costs between nodes
    
    private int generation=0;           // The current generation's number
    private int nrUnimprovedGenerations;// Number of generations without improvement of the best element
    
    private long acumulatedNrFitnessEvaluations = 0;    // The acumulated number of fitness evaluations on this run of the GA
    
    private Chromosome bestChromosome;  // Keep the best chromosome of all the run
    private int bestChromosomeGeneration;   // keep the generation number of the best chromosome found
    private long bestChromosomeNrFitnessEvaluations;    // Number of fitness evaluations when the best chromossome was foud
    
    private int stopValue;              // The number of unimproved generations at wich this run will stop
    
    // -----------------------
    // GA implementation's specific operators and parameters
    // -----------------------
    private OperatorsAndParameters operators;
    // Population
    private int popSize;                // Size of the population
    // Selection
    private boolean tournamentSelection;// Use tournament selection?
    private int selectionParam;         // Used for the number of elements on the tournament selection
    // Crossover
    private boolean crossoverOrder1;    // Use Order1 Crossover
    private boolean crossoverPMX;       // Use PMX Crossover
    private boolean crossoverEdge3;     // Use Edge3 Crossover
    private float crossoverProb;        // Probability of crossover (0..1)
    // Mutation
    private boolean swapNextMutation;   // Use Swap Next Mutation
    private boolean swapMutation;       // Use Swap Mutation
    private boolean insertMutation;     // Use Insert Mutation
    private boolean inversionMutation;  // Use Invertion Mutation
    private float mutationProb;         // Probability of mutation (0..1)
    // Replacement
    private float elitism;              // Use of elitism from 0 (no elitism) to 1 
                                        // (every chromosome on the new population comes from the old one)

    /*
     * Constructor.
     * Initializes the parameters.
     */
    public VRPGARun(OperatorsAndParameters operators, GeneList geneList, CostMatrix costMatrix, String statsFileName, int run, int stopValue){
        this.run=run;
        this.statsFileName=statsFileName;
        this.operators = operators;
        // Population
        this.popSize = operators.getPopulationSize();
        // Selection
        String selectionOperator = operators.getSelectionOperator();
        if (selectionOperator.equals("Selection.tournamentSelection")){
            this.tournamentSelection=true;
        }
        this.selectionParam = operators.getSelectionParam();
        // Crossover
        String crossoverOperator = operators.getCrossoverOperator();
        if (crossoverOperator.equals("Order1.Order1")){
            this.crossoverOrder1 = true;
        } else if (crossoverOperator.equals("PMX.PMX")){
            this.crossoverPMX = true;
        } else if (crossoverOperator.equals("Edge3.Edge3")){
            this.crossoverEdge3 = true;
        }
        this.crossoverProb= operators.getCrossoverProb();
        // Mutation
        String mutationOperator = operators.getMutationOperator();
        if (mutationOperator.equals("SwapNextMutation.swapNextMutation")){
            this.swapNextMutation = true;
        } else if (mutationOperator.equals("SwapMutation.swapMutation")){
            this.swapMutation = true;
        } else if (mutationOperator.equals("InsertMutation.insertMutation")){
            this.insertMutation = true;
        } else if (mutationOperator.equals("InversionMutation.inversionMutation")){
            this.inversionMutation = true;
        }
        this.mutationProb = operators.getMutationProb();
        // Replacement
        this.elitism = operators.getReplacementElitism();
                
        // Problem specific
        this.geneList = geneList;
        this.costMatrix = costMatrix;
        
        this.stopValue = stopValue; 
    }
    

    @Override
    public void run(){
        
        statsFileWriter = openFileForWrite(statsFileName);
        BufferedWriter buffer = new BufferedWriter(statsFileWriter);
        
        long start = System.currentTimeMillis();
        
        // Randomly create the initial Population
        pop = new Population(popSize, geneList, costMatrix, operators);
        
        // Acumulate the number of fitness evaluations
        acumulatedNrFitnessEvaluations += pop.getNrFitnessEvaluations();   
        
        // Copy the first population
        try{
            firstPop = (Population)pop.clone();
        } catch (CloneNotSupportedException ex){
            ex.printStackTrace();
        }
        
        // Keep the best chromosome
        bestChromosome = pop.getBestChromosome();
        
        // Some statistics
        float startBest = pop.getBestFitness();
        float startAverage = pop.getAverageFitness();
        float startWorst = pop.getWorstFitness();
        float endBest = startBest;
        float endAverage = startAverage;
        float endWorst = startWorst;
        
        Population newPop = pop;
        long end; 
        
        do {
                // Mark the time at which the last population ended
                end = System.currentTimeMillis();
                
                // Print the population statistics
                printPopulationStatistics(pop, generation, end - start);
                
                // Save stats to log file
                writeStats(buffer, run, generation, acumulatedNrFitnessEvaluations, bestChromosome.getFitness());
                
                // Reset the number of fitness evaluations
                pop.resetNrFitnessEvaluations();
                
                // Increase the generation number
                generation++;

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
//                for (int i=0;i<(popSize-(popSize*elitism));i=i+2) {
                for (int i=0;i<(popSize);i=i+2) {
                    // If the population size is odd, on the last single element 
                    // we just do selection and mutation
                    if ((i+2)>popSize){
                        Chromosome parent = Selection.tournamentSelection(selectionParam,newPop);
                        Chromosome child = SwapNextMutation.swapNextMutation(mutationProb, parent);
                        matingPool[i] = child;
                    }
                    
                    else {

                        // 1. Selection
                        Chromosome[] parents = new Chromosome[2];
                            parents[0] = Selection.tournamentSelection(selectionParam,newPop);
                            parents[1] = Selection.tournamentSelection(selectionParam,newPop);

                        // 2. Crossover (with a given probability)
                        Chromosome[] childs = null;
                        if (crossoverOrder1){
                            childs = Order1.Order1(parents,crossoverProb);
                        } else if (crossoverPMX){
                            childs = PMX.PMX(parents,crossoverProb);
                        } else if (crossoverEdge3){
                            childs = Edge3.Edge3(parents,crossoverProb);
                        }

                        // 3. Mutation (with a given probability)
                        if (swapNextMutation){
                            childs[0]=SwapNextMutation.swapNextMutation(mutationProb, childs[0]);
                            childs[1]=SwapNextMutation.swapNextMutation(mutationProb, childs[1]);
                        } else if (swapMutation){
                            childs[0]=SwapMutation.swapMutation(mutationProb, childs[0]);
                            childs[1]=SwapMutation.swapMutation(mutationProb, childs[1]);
                        } else if (insertMutation){
                            childs[0]=InsertMutation.insertMutation(mutationProb, childs[0]);
                            childs[1]=InsertMutation.insertMutation(mutationProb, childs[1]);
                        } else if (inversionMutation){
                            childs[0]=InversionMutation.inversionMutation(mutationProb, childs[0]);
                            childs[1]=InversionMutation.inversionMutation(mutationProb, childs[1]);
                        }

                        matingPool[i]=childs[0];
                        matingPool[i+1]=childs[1];

                    }
                }

                // After Selection, Crossover, Mutation in all the chromosomes of the population,
                // lets do the replacement
                newPop = Replacement.populationReplacement(matingPool, pop, elitism, geneList, costMatrix);
                
                // If the best element of the population is the best element of all the run, keep it.
                keepBestChromosome(newPop, generation);
                
                // Acumulate the number of fitness evaluations
                acumulatedNrFitnessEvaluations += newPop.getNrFitnessEvaluations();  
                    
                pop = newPop;   // Bye bye old population. Be trash collected!!!
            
        } while (nrUnimprovedGenerations(generation) < stopValue);
        
        System.out.println("The run stopped! No improvement from generation " + bestChromosomeGeneration);
        
        // Print the final population stats
        System.out.println("\n");
        printPopulationStatistics(pop, generation, end - start);
        
        // Print the best element
        System.out.println("\nBest solution:" + pop.getTop(1)[0].toString());
        
        endBest = pop.getBestFitness();
        endAverage = pop.getAverageFitness();
        endWorst = pop.getWorstFitness();
        
        // Save stats to log file
        writeStats(buffer, run, generation, acumulatedNrFitnessEvaluations, endBest);
        // Close the file
        closeFile(statsFileWriter);
        // Save just the best element
        writeBestElement(statsFileName, run, bestChromosomeGeneration, bestChromosomeNrFitnessEvaluations, endBest);
        
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
     * Keep the best chromosome of the entire run.
     * If the best chromosome of the given population is better than the best chromossome
     * of the previous populations, keep the first. Its the best of the entire run.
     */
    private void keepBestChromosome(Population pop, int generation){
        if (pop.getBestChromosome().getFitness() < bestChromosome.getFitness()){
            this.bestChromosome = pop.getBestChromosome();
            this.bestChromosomeGeneration = generation;
            this.bestChromosomeNrFitnessEvaluations = acumulatedNrFitnessEvaluations;
        }
    }
    
    /*
     * Measure the number of generations without improvements on the best chromosome.
     */
    public int nrUnimprovedGenerations(int generation){
            return (generation - bestChromosomeGeneration);
    }
    
    /*
     * Open the given file for writing
     */
    public FileWriter openFileForWrite(String fileName){
        try{
            FileWriter fileWriter = new FileWriter(fileName, true);
            return fileWriter;
        } catch (Exception ex){
            ex.printStackTrace();
        }
        return null;
     }
    
    /*
     * Close the given file
     */
    public void closeFile(FileWriter statsFileWriter){
        try{
            statsFileWriter.close();
        } catch (Exception ex){
            ex.printStackTrace();
        }
    }
    
    /*
     * Write the stats to a given file
     */
    public void writeStats(BufferedWriter buffer, int run, int generation, long nrFitnessEvaluations, float bestCost){
        try{
            buffer.write(Integer.toString(run));
            buffer.write(";");
            buffer.write(Integer.toString(generation));
            buffer.write(";");
            buffer.write(Long.toString(nrFitnessEvaluations));
            buffer.write(";");
            buffer.write(Float.toString(bestCost));
            buffer.write(";");
            buffer.newLine();
            buffer.flush();
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }
    
    public void writeBestElement(String statsFileName, int run, int generation,long fitnessEvals, float bestCost){
        try{
            FileWriter fileWriter = new FileWriter(statsFileName+".resume", true);
            BufferedWriter buffer = new BufferedWriter(fileWriter);
            
            buffer.write(Integer.toString(run));
            buffer.write(";");
            buffer.write(Integer.toString(generation));
            buffer.write(";");
            buffer.write(Long.toString(fitnessEvals));
            buffer.write(";");
            buffer.write(Float.toString(bestCost));
            buffer.write(";");
            buffer.newLine();
            buffer.flush();
            
            buffer.close();
            fileWriter.close();
            
        } catch (Exception ex){
            ex.printStackTrace();
        }
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
