
package org.metavrp.GA;

import java.util.*;
import org.metavrp.VRP.CostMatrix;

/**
 * TODO: Change ArrayList to Array
 * TODO: Actualmente todos os veículos partem do mesmo depot (o node 0).
 * TODO: colocar a possibilidade de partirem de depots diferentes 
 * TODO: e de possuirem capacidades diferentes
 * TODO: e de fazerem as descargas em locais diferentes.
 * 
 * TODO: Verificar se todas as alterações aos cromossomas são seguidas da 
 * TODO: avaliação do fitness.
 * 
 * @author David Pinheiro
 */
public class Population implements Cloneable, Comparator<Chromosome>{

    private Chromosome[] chromosomes;

    private float bestFitness;          //Best fitness value in the population
    private float worstFitness;         //Worst fitness value in the population
    private float averageFitness;       //Average fitness value in the population
    private float medianFitness;        //The median fitness value in the population

    private int chromosomeLength;       //Size of the chromosomes. Number of genes.
    private int nrNodes;                //Number of nodes in each chromosome
    private int nrVehicles;             //Number of vehicles in each chromosome
    
    private GeneList geneList;          // A list of the possible genes (vehicles and nodes)
    
    private CostMatrix costMatrix;      //The (very important) cost matrix

    
    //**************
    // Constructors
    //**************
    
    // Creates a randomly generated population, given some population size (number
    // of chromosomes), some number of vehicles and the cost matrix.
    // All vehicles start (and end) from the first node of the cost matrix.
    public Population(int popSize, GeneList geneList, CostMatrix costMatrix) {
        //Randomly generate the population
        this.chromosomes = generateChromosomes(popSize, geneList, costMatrix); 
        this.chromosomeLength = this.chromosomes[0].getLenght();
        this.nrNodes = geneList.getNrNodes();
        this.nrVehicles = geneList.getNrVehicles();
        fitnessSort();  //Sort the population
        this.geneList=geneList;
        this.costMatrix = costMatrix;
        this.statistics();
    }
    
    // The population is given as an array of chromosomes. The rest 
    // (until popSize) is randomly generated.
    // All vehicles start (and end) from the first node of the cost matrix
    public Population(Chromosome[] chromosomes, int popSize, GeneList geneList, CostMatrix costMatrix) {
        this.chromosomes = generateMissingChromosomes(chromosomes, popSize, geneList, costMatrix);
        this.chromosomeLength = this.chromosomes[0].getLenght();
        this.nrNodes = geneList.getNrNodes();
        this.nrVehicles = chromosomes[0].getNrVehicles();
            if (nrVehicles!=geneList.getNrVehicles())
                throw new AssertionError("[ERROR] The number of vehicles of the "
                        + "chromosome is different from the GeneList.");
        fitnessSort(); //Sort the population
        this.geneList=geneList;
        this.costMatrix = costMatrix;
        this.statistics();
    }
    
    // Not enought? Yet another constructor
    // This time the population is given as two arrays of chromosomes that are merged. 
    // All vehicles start (and end) from the first node of the cost matrix.
    public Population(Chromosome[] newChromosomes, Chromosome[] elitistChromosomes, GeneList geneList, CostMatrix costMatrix) {
        Chromosome[] concatChromosomes = mergeChromosomes(newChromosomes, elitistChromosomes);
        this.chromosomes = concatChromosomes;
        this.chromosomeLength = this.chromosomes[0].getLenght();
        this.nrVehicles = concatChromosomes[0].getNrVehicles();
            if (nrVehicles!=geneList.getNrVehicles())
                throw new AssertionError("[ERROR] The number of vehicles of the "
                        + "chromosomes is different from the GeneList.");        
        fitnessSort(); //Sort the population
        this.geneList = geneList; 
        this.costMatrix = costMatrix;
        this.statistics();
    }
    
    
    //******************************
    // Population support functions
    //******************************
    // TODO: Criar um gerador de cromossomas, que não crie cromossomas aleatoriamente mas que 
    // TODO: crie bons cromossomas (cujas distâncias entre nós sejam reduzidas) e distantes
    // TODO: entre si (arranjar algum algoritmo para isto - por exemplo criando matrix de proximidades).
    
    // Create a population of random chromosomes
    private Chromosome[] generateChromosomes(int popSize, GeneList geneList, CostMatrix costMatrix){
        chromosomes = new Chromosome[popSize];
        // Construct each chromosome
        for (int i=0; i < popSize; i++) {
            chromosomes[i] = new Chromosome(geneList, costMatrix); // Call the constructor
        }
        return chromosomes;
    }
    
    // Create the missing chromosomes of some array
    private Chromosome[] generateMissingChromosomes(Chromosome[] chromosomes, int popSize, GeneList geneList, CostMatrix costMatrix) {
        Chromosome[] newChromosomes;    //A new array of chromosomes to create a new population

        if (chromosomes.length<popSize) { // In this case we have to generate the rest of the chromosomes
            newChromosomes = new Chromosome[popSize]; //Initialize the array 
            newChromosomes = Arrays.copyOf(chromosomes, popSize);   //Copy the given chromosomes
            for (int i=chromosomes.length; i < popSize; i++) {    // Randomly generate the rest
                newChromosomes[i] = new Chromosome(geneList, costMatrix); // Call the constructor
            }
        } else if (chromosomes.length == popSize) {
            newChromosomes=chromosomes;
        } else { // chromosomes.length>popSize
            //Just in case of some mistake on the popSize value
            System.out.println("[ERROR] Trying to create a population with a smaller size "
                    + "than the array of chromosomes. Using the size of the array."); 
            newChromosomes=chromosomes;
        }
        return newChromosomes;
    }
    
    // Merge chromosomes from two arrays
    private Chromosome[] mergeChromosomes(Chromosome[] newChromosomes, Chromosome[] elitistChromosomes) {
        // A new array of chromosomes to create a new population
        // Initialy made by copying the first array of chromosomes
        Chromosome[] concatChromosomes = Arrays.copyOf(newChromosomes, newChromosomes.length + elitistChromosomes.length);
        // Next copy all the chromosomes from the second array
        for (int i=newChromosomes.length, j=0; i < newChromosomes.length + elitistChromosomes.length; i++, j++) {
            concatChromosomes[i] = elitistChromosomes[j]; // Copy the chromosome
        }
        return concatChromosomes;
    }

    //*******************
    // Fitness Functions
    //*******************
    
    // Sort the population's chromosomes by cost ascending (the best fitness is the least cost value)
    private boolean fitnessSort() {
        // This class implements a comparator
        Arrays.sort(chromosomes, this);
        return true;    // Now the chromosomes are sorted
    }

    // Get the best n elements of the population
    public Chromosome[] getTop(int n) {
        Chromosome[] best;
        fitnessSort();   // Sort the population.

        // Now copy the best n elements to 'best'
        best = Arrays.copyOfRange(this.chromosomes, 0, n);
        return (best);
    }

    
    // Computes all the statistics
    public final void statistics(){
        // First sort the population inversely by cost
        fitnessSort();  
        
        // The best chromosome is the first
        this.bestFitness=this.chromosomes[0].getFitness();                      
        
        // The worst chromosome is the last
        this.worstFitness=this.chromosomes[chromosomes.length-1].getFitness();  
        
        // Average fitness value
        double totalFitness=0;
        for (int i=0;i<chromosomes.length;i++){
            totalFitness+=chromosomes[i].getFitness();
        }
        this.averageFitness=(float) totalFitness/chromosomes.length; // The average fitness
        
        // Median fitness value
        //If the number of chromosomes in the population is odd, just return the 
        //fitness value of the middle chromosome. If it is even, return the average 
        //of the two middle values.
        if (chromosomes.length%2!=0){ // Its odd
            //Return the middle chromosome's fitness value
            this.medianFitness = chromosomes[chromosomes.length/2].getFitness();
        }
        else {  // Its even
            int topMiddle=(chromosomes.length/2)-1;
            int bottomMiddle=chromosomes.length/2;
            this.medianFitness = (chromosomes[topMiddle].getFitness()+chromosomes[bottomMiddle].getFitness())/2;
        }

    }
    
    /*
     * Returns the number of times that the Fitness evaluation function was called
     * on this population.
     */
    public long getNrFitnessEvaluations(){
        long totalFitnessEvaluations=0;
        for (Chromosome chr:chromosomes){
            totalFitnessEvaluations += chr.getNrFitnessEvaluations();
        }
        return totalFitnessEvaluations;
    }
    
    /*
     * Resets the number of times that the Fitness evaluation function was called
     * on this population.
     */
    public void resetNrFitnessEvaluations(){
        for (Chromosome chr:chromosomes){
            chr.resetNrFitnessEvaluations();
        }
    }
    
    /*
     * Measure improvements
     * 
     * Get the corresponding fitness value of the given population.
     * Measure it's improvement as the ratio between:
     * ( given corresponding fitness value - current corresponding fitness value ) / given corresponding fitness value
     */
    
    // Measure the improvement of the best fitness value
    public float calcBestImprovement(Population givenPop) {
        float givenBestFitness = givenPop.getBestFitness(); 
        float ratio = (givenBestFitness-bestFitness) / givenBestFitness;
        return ratio;
    }
    
    // Measure the improvement of the average fitness value
    public float calcAverageImprovement(Population givenPop) {
        float givenAverageFitness = givenPop.getAverageFitness(); 
        float ratio = (givenAverageFitness-averageFitness) / givenAverageFitness;
        return ratio;
    }

    // Measure the improvement of the worst fitness value
    public float calcWorstImprovement(Population givenPop) {
        float givenWorstFitness = givenPop.getWorstFitness(); 
        float ratio = (givenWorstFitness-worstFitness) / givenWorstFitness;
        return ratio;
    } 
    

    /*
     * Support Functions
     */
    // We need to implement this method for sorting
    @Override
    public int compare(Chromosome chr1, Chromosome chr2) {
        return chr1.compareTo(chr2);
    }
    
    // Clone method
    @Override
    public Object clone() throws CloneNotSupportedException {

        Chromosome[] newChromosomes = new Chromosome[popSize()];
        for (int i=0;i<popSize();i++){
            newChromosomes[i]=(Chromosome)chromosomes[i].clone();
        }
        Population newPop = new Population(newChromosomes, popSize(), geneList, costMatrix);
        
        return newPop;
    }
    
  
    //***************************
    // TODO: Getters and Setters
    //***************************

    public int popSize(){
        return chromosomes.length;
    }
    
    public Chromosome[] getChromosomes() {
        return chromosomes;
    }
    
    public float getAverageFitness() {
        return averageFitness;
    }

    public float getBestFitness() {
        return bestFitness;
    }

    public float getWorstFitness() {
        return worstFitness;
    }

    // The size (number of chromossomes) of the population 
    public int getPopSize() {
        return chromosomes.length;
    }
    

}
