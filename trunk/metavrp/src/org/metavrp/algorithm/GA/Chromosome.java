package org.metavrp.algorithm.GA;

import java.util.ArrayList;
import java.util.Arrays;
import org.metavrp.algorithm.GA.fitnessFunctions.CVRP;
import org.metavrp.algorithm.GA.operators.OperatorsAndParameters;
import org.metavrp.algorithm.GA.support.Randomizer;
import org.metavrp.algorithm.GA.phenotype.Tours;
import org.metavrp.problem.CostMatrix;
import org.metavrp.algorithm.GA.phenotype.CVRPTours;

/**
 * 
 * 
 * @author David Pinheiro
 */
public class Chromosome implements Cloneable, Comparable<Chromosome>{

    private int nrVehicles;                     // The number of vehicles in the chromosome
    private int nrNodes;                        // The number of nodes not vehicles
    private int nrDepots;   // TODO: Support multi-depots
    
    private CostMatrix costMatrix;              // Bi-dimentional array with the distances between any two nodes

    private Gene[] genes;                       // The actual chromosome: an Array of genes
    
    private OperatorsAndParameters operators;   // The chosen GA's operators and parameters

    private float fitness;                      // Chromosome's fitness value (Corresponds to the total cost)
    private boolean isFitnessOutdated = false;  // True if we need to reevaluate the fitness of this chromosome
    private int nrFitnessEvaluations = 0;       // The number of fitness evaluations

    private float[] vehiclesCosts;              // Costs (fitness values) of the vehicles
    
    
    // Constructor.
    // Creates a chromosome from a cost matrix. Genes are randomly generated.
    public Chromosome(GeneList geneList, CostMatrix dm, OperatorsAndParameters operators) {
        this.operators = operators;
        this.nrNodes = geneList.getNrNodes();
        if (this.nrNodes < 3){
            throw new AssertionError("[Error] This Library only works with more than two nodes.");
        }
        this.nrVehicles= geneList.getNrVehicles();
        this.costMatrix = dm;
        this.genes = generateRandomChromosome(geneList);
        verifyGenes();
        this.isFitnessOutdated=true;
    }

    // Constructor.
    // Creates a chromosome from all the given data
    public Chromosome(Gene[] genes, CostMatrix costMatrix, int nrVehicles, int nrNodes,  float fitness, float[] vehiclesFitness, OperatorsAndParameters operators) {
        this.operators = operators;
        this.nrVehicles = nrVehicles;
        this.nrNodes = nrNodes;
        this.costMatrix = costMatrix;
        this.genes = genes;
        this.fitness = fitness;
        this.vehiclesCosts = vehiclesFitness;
    }
    
    // Constructor. 
    // Creates a chromosome from an array of genes with the vehicles already there.
    // All the other data (nrVehicles, nrNodes, fitness, vehiclesFitness) is measured,
    // which creates an unnecessary overhead.
    public Chromosome(Gene[] genes, CostMatrix dm, OperatorsAndParameters operators) {
        this.operators = operators;
        this.costMatrix = dm;
        this.genes = genes;
        this.nrVehicles = countVehicles();
        this.nrNodes = genes.length - this.nrVehicles;
        if (this.nrNodes < 3){
            throw new AssertionError("[Error] This Library only works with more than two nodes.");
        }
        verifyGenes();
        this.isFitnessOutdated=true;
    }
    
    /*
     * Verifies that the genes of this chromosome are correct. That there are no duplicates,
     * or some missing genes.
     * This is just to help the developer certify that his operators (crossover, mutation)
     * are generating correct results.
     */
    public final void verifyGenes(){
        ArrayList<Gene> genesList = new ArrayList<Gene>(Arrays.asList(genes));
        for (Gene gene : genes){
            if (!genesList.contains(gene)){
                throw new AssertionError("There's a gene missing!");
            }
            genesList.remove(gene);
        }
        if (!genesList.isEmpty()){
            throw new AssertionError("There are duplicated genes!");
        }
        if ((costMatrix.getSize()+nrVehicles-1)!=genes.length)
            throw new AssertionError("Some genes are missing in the chromosome!");
    }
    
    //****************************
    // Generate a random chomosome
    //****************************
    private Gene[] generateRandomChromosome(GeneList geneList) {
        // Create a temporary array of the cloned List of Genes. 
        // Cloned because we want to make changes without destroying the original list.
        ArrayList<Gene> temp_chr = geneList.getClonedGenes();
        
        int size = geneList.getSize();  // Get the size of the list
        
        // Now scramble those genes (with the vehicles)
        ArrayList<Gene> random_chr = new ArrayList(size);   // Array to store the newly created chromosome
        for (int i = 0; i < size; i++) {
            // Grab the gene (randomly) from temp_chr and put it on rand_chr
            random_chr.add(temp_chr.remove(Randomizer.randomInt(size - i))); 
        }
        
        // Declare the kind of array to return
        Gene[] chromossome = new Gene[size];
        return random_chr.toArray(chromossome);   // Transform the ArrayList to that kind of array
    }
    
    
    // Counts the number of vehicles present in the genes
    private int countVehicles(){
        int count=0;
        for (int i=0;i<this.genes.length;i++){
            if (this.genes[i].getIsVehicle()){
                count++;
            }
        }
        return count;
    }


    //*******************
    // Fitness Functions
    //*******************
    
    // Measure the fitness of this chromosome. The cost associated with it.
    private float measureFitness(){
        // TODO: The second parameter of CVRP.measureCost needs to be automated
        float cvrpCost = CVRP.measureCost(this, operators.getInnerDepotPenalty());

//        float simplevrpCost = SimpleVRP.measureCost(this);
//   
//        if (cvrpCost == simplevrpCost){
//            System.out.println("Fitnesses iguais");
//        } else {
//            System.out.println("Fitnesses diferentes -> Foi necessário visitar o deposito");
//        }
        
        // Return the global cost

        return cvrpCost;
    }
    
    /*
     * Returns the cost of going from the node who's gene is given on the first parameter
     * to the node who's gene is given on the second one.
     */
    public float getCost(Gene geneA, Gene geneB){
        int nodeA = geneA.getNode();
        int nodeB = geneB.getNode();
        return getCost(nodeA, nodeB);
    }
    
    /*
     * Returns the cost of going from the node who's index is given on the 
     * first parameter to the node who's index is given on the second one.
     */
    public float getCost(int nodeA, int nodeB){
        return costMatrix.getCost(nodeA, nodeB);
    }

    
    // Updates the fitness value of this chromosome if it is outdated
    public void updateFitness() {
        if (isFitnessOutdated){
            this.fitness = measureFitness();
            this.isFitnessOutdated=false;
        }
    }
    
    /*
     * Resets the number of fitness evaluations (the number of times that the 
     * fitness evaluation fuction is called)
     */
    public void resetNrFitnessEvaluations() {
        this.nrFitnessEvaluations = 0;
    }
    
    /*
     * Increments by one the number of fitness evaluations.
     * @returns Number of fitness evaluations of this chromosome.
     */
    public int incrementFitnessEvaluations(){
        this.nrFitnessEvaluations++;
        return this.nrFitnessEvaluations;
    }
    
    
    //**********************
    //  Auxiliary functions
    //**********************
    
    // Swaps two genes, defined by positions 'a' and 'b'
    public void swapGenes(int a, int b){
        if (a<0 || b<0 || a > genes.length || b > genes.length){
            throw new AssertionError("[ERROR] Cannot swap genes because one of them is out of the chromosome!");
        }
        Gene gene1 = genes[a];
        Gene gene2 = genes[b];
        this.setGene(gene2, a);
        this.setGene(gene1, b);
    }
    
    // Swaps two given genes
    public void swapGenes(Gene a, Gene b){
        int indexA=this.indexOf(a); //Find their indexes
        int indexB=this.indexOf(b);
        // If one of the indexes is -1, that gene doesn't exist on the chromosome
        if (indexA==-1){
            throw new AssertionError("[ERROR] The gene "+a.toString()+" doesn't exist on the chromosome "+this.toString());
        }
        if (indexB==-1){
            throw new AssertionError("[ERROR] The gene "+b.toString()+" doesn't exist on the chromosome "+this.toString());
        }
        
        this.setGene(b, indexA);    // Swap them using the indexes
        this.setGene(a, indexB);
    }
    
    // Swaps a given gene with the next one
    public void swapWithNextGene(Gene gene){
        int indexGene=this.indexOf(gene); //Find his index
        
        if (indexGene==-1){     
            // If the index is -1, the gene doesn't exist on the chromosome
            throw new AssertionError("[ERROR] The gene "+gene.toString()+" doesn't exist on the chromosome "+this.toString());
        }
        
        if (indexGene==this.getLenght()-1){ 
            // If this gene is the last one, swap it with the first one
            swapGenes(indexGene,0);
        } else {    
            // Otherwise swap it with the next one
            swapGenes(indexGene,indexGene+1);
        }
    }
    
    // Finds the index of the given gene
    // @throws java.lang.NullPointerException in the case that the gene is null
    public int indexOf(Gene g) {
        int index = -1;
        for (int i = 0; i < this.getLenght(); i++) {
                if (((Object)genes[i]).equals(g))
                    return index = i;
        }
        return index; // Returns -1 if it doesn't exist
    }
    
    // Finds the index of the first vehicle
    public int indexFirstVehicle(){
        for (int i=0; i<genes.length;i++){
            if (genes[i].getIsVehicle()){
                return i;
            }
        }
        throw new AssertionError("ERROR: The array of genes doesn't have any vehicles!");
    }
    
    // Gets the (gene of the) first vehicle
    public Gene getFirstVehicle(){
        return getGene(indexFirstVehicle());
    }
    
    // Verifies if this chromosome has a specified Gene
    public boolean hasGene(Gene g){
        for (int i = 0; i < genes.length; i++) {
            if (genes[i]!=null){
                if (genes[i]==g){
                    return true;
                }
            } else if (genes[i]==null && g==null){
                return true;
            }
        }
        return false; // Returns true if the gene exists
    }
    
    // Print all information about this chromosome.   
    public String print() {
        String gene = "";
        for (int i=0; i < genes.length; i++){
            gene += genes[i].toString()+" ";
        }
        String text = "Genes: [ " + gene +"]";
        text+= "\n Fitness: " + Float.toString(this.getFitness());
        String vFitness = "";
        for (int i=0; i < vehiclesCosts.length; i++){
            vFitness += Float.toString(vehiclesCosts[i])+" ";
        }
        text+= "\n Vehicles fitness: " + vFitness;
        System.out.println(text);
        return text;
    }
    
    // Put all information about this chromosome in a string.
    @Override
    public String toString(){
        String chrPrint = "[";
        for (int i=0; i<genes.length;i++){
            chrPrint += genes[i].getNode()+"|";
        }
        chrPrint += "]";
        return chrPrint;
    }
    
    // We need to implement this method to permit comparisons.
    @Override
    public int compareTo(Chromosome chr) {
        return Float.compare(this.getFitness(), chr.getFitness()) ;
    }

    // Clones this Chromosome
    @Override
    public Object clone() throws CloneNotSupportedException {

        Gene[] newGenes = new Gene[genes.length];
        for (int i=0; i<genes.length;i++){
            newGenes[i]=genes[i];
        }
        Chromosome newChromosome = new Chromosome(newGenes, costMatrix, nrVehicles, nrNodes,  fitness, vehiclesCosts, operators);
        
        return newChromosome;
    }

    
    //**************************
    // Getters and Setters
    //**************************
    // Gets the gene before the given one
    public Gene getGeneBefore (Gene g){
        int index = indexOf(g);     // Gets the gene's index
        if (index==0){
            // If its the first gene, return the last gene
            return getGene(this.getLenght()-1);
        }
        else return getGene(index-1);   // Returns the previous gene
    }
    
    // Gets the gene before the given index
    public Gene getGeneBefore (int index){
        if (index==0){
            // If its the first gene, return the last gene
            return getGene(this.getLenght()-1);
        }
        else return getGene(index-1);   // Returns the previous gene
    }
    
    // Gets the gene after the given one
    public Gene getGeneAfter (Gene g){
        int index = indexOf(g);     // Gets the gene's index
        if (index >= (this.getLenght()-1)){
            // If its the last gene, return the first
            return getGene(0);
        }
        else return getGene(index+1);   // Returns the previous gene
    }
    
    // Gets the gene after the given index
    public Gene getGeneAfter (int index){
        if (index >= (this.getLenght()-1)){
            // If its the last gene, return the first
            return getGene(0);
        }
        else return getGene(index+1);   // Returns the next gene
    }
    

    public float getFitness() {
        updateFitness();
        return fitness;
    }

    public Gene[] getGenes() {
        return genes;
    }

    public Gene getGene(int i){
        return genes[i];
    }
    
    // TODO: remove this method and put a swap or switch method. 
    // The reason is that with this method we can erroneously increase or decrease the
    // number of vehicles or customers.
    public void setGene(Gene g, int i){
//System.out.println("Set gene: "+g.print());        
        this.genes[i]=g;
        
        // With some change of the genes, the fitness value is outdated and needs to be evaluated again
        isFitnessOutdated=true; 
//System.out.println("To: "+this.genes[i].print());        
    } 
    
    public int getLenght() {
        return genes.length;
    }

    public int getNrVehicles() {
        return nrVehicles;
    }

    public int getNrNodes() {
        return nrNodes;
    }
    
    public float[] getVehiclesFitness() {
        updateFitness();
        return vehiclesCosts;
    }

    public void setVehiclesCosts(float[] vehiclesCosts) {
        this.vehiclesCosts = vehiclesCosts;
    }

    public CostMatrix getCostMatrix() {
        return costMatrix;
    }

    public int getNrFitnessEvaluations() {
        return nrFitnessEvaluations;
    }

    public OperatorsAndParameters getOperators() {
        return operators;
    }

}
