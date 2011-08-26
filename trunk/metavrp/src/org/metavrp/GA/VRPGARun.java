
package org.metavrp.GA;

import org.metavrp.GA.support.GAParameters;
import org.metavrp.GA.operators.*;
import org.metavrp.GA.operators.crossover.*;
import org.metavrp.VRP.CostMatrix;

/**
 *
 * @author David Pinheiro
 */
public class VRPGARun {
    
    private Population pop;
    
    public VRPGARun(GAParameters params, GeneList geneList, CostMatrix costMatrix){
        
        int popSize = params.getPopSize();
        float elitism = params.getElitism();
        float mutationProb = params.getMutationProb();
        float crossoverProb= params.getCrossoverProb();
        int generations = params.getGenerations();
        
        int generation=0;
        long start = System.currentTimeMillis();
        
        // Create Population
        pop = new Population(popSize, geneList, costMatrix);
        
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
        System.out.println("Average cost value: "+pop.getAverageFitness());
        System.out.println("Worst cost value: "+pop.getWorstFitness());
        System.out.println("Time spent on this generation: "+time+"ms\n\n");
    }
    
    public Population getPopulation(){
        return pop;
    }
    
}
