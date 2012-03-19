package org.metavrp.GA;

import org.metavrp.GA.support.Randomizer;
import org.metavrp.VRP.CostMatrix;
import java.util.*;

/**
 * 
 * 
 * @author David Pinheiro
 */
public class Chromosome implements Cloneable, Comparable<Chromosome>{

    private int nrVehicles;                     // The number of vehicles in the chromosome
    private int nrNodes;                        // The number of nodes not vehicles
    
    private CostMatrix costMatrix;              // Bi-dimentional array with the distances between any two nodes

    private Gene[] genes;                       // The actual chromosome: an Array of genes

    private float fitness;                      // Chromosome's fitness value (Corresponds to the total cost)
    private boolean isFitnessOutdated = false;  // True if we need to reevaluate the fitness of this chromosome
    private int nrFitnessEvaluations = 0;         // The number of fitness evaluations

    private float[] vehiclesFitness;            // Fitness of the vehicles
    
    
    // Constructor.
    // Creates a chromosome from a cost matrix. Genes are randomly generated.
    public Chromosome(GeneList geneList, CostMatrix dm) {
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
    public Chromosome(Gene[] genes, CostMatrix costMatrix, int nrVehicles, int nrNodes,  float fitness, float[] vehiclesFitness) {
        this.nrVehicles = nrVehicles;
        this.nrNodes = nrNodes;
        this.costMatrix = costMatrix;
        this.genes = genes;
        this.fitness = fitness;
        this.vehiclesFitness = vehiclesFitness;
    }
    
    // Constructor. 
    // Creates a chromosome from an array of genes with the vehicles already there.
    // All the other data (nrVehicles, nrNodes, fitness, vehiclesFitness) is measured,
    // which creates an unnecessary overhead.
    public Chromosome(Gene[] genes, CostMatrix dm) {
        this.costMatrix = dm;
        this.genes = genes;
        this.nrVehicles = countVehicles(genes);
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
        if (costMatrix.getSize()!=genes.length-nrVehicles+1)
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
    private int countVehicles(Gene[] genes){
        int count=0;
        for (int i=0;i<genes.length;i++){
            if (genes[i].getIsVehicle()){
                count++;
            }
        }
if (count!=1){ System.out.println("O cromosoma: " + this.print()+ "Tem "+count+" veiculos");}        
        
        return count;

    }


    //*******************
    // Fitness Functions
    //*******************
    
    // Measure the fitness of this chromosome.
    // Rebuild this method for simplicity, clarity and to incorporate the CVRP.
    private float measureFitness(){
        
        int nVehicles = countVehicles(genes);       // Count the number of vehicles
        
        float[] vFitness = new float[nVehicles];    // The fitness (routing cost) of the vehicles
        int vehicle=0;                              // The number of the vehicle
        
        int indexFirstVehicle = indexFirstVehicle();
        int currentNode=indexFirstVehicle;
        int nextNode=indexFirstVehicle+1;
        
        int vehicleStartNode=0;                     // To use later: The node from where this vehicle started
        
        // First: count the fitness of each vehicle
        do {
            // Starting from the first gene, go throught every gene on the chromosome 
            // summing the cost from him to the next one

            if (currentNode==genes.length-1){
                // If the current gene is the last one, the next will be the first one
                nextNode=0;
            }

            if (genes[currentNode].getIsVehicle() && genes[nextNode].getIsVehicle()){
                vehicle++;
                if (vehicle>=nVehicles){vehicle=0;} // If we pass the last vehicle (at the end of the chromosome) 
                                                    // we need to sum the node's cost to the first one
                // If this node and the next one are vehicles, don't sum nothing and go to the next vehicle
                vFitness[vehicle]=0;

            } else if (genes[currentNode].getIsVehicle()){
                // If this node is a vehicle, and the one before is a normal node,
                // sum the cost and go to the next vehicle
                vehicle++;
                if (vehicle>=nVehicles){vehicle=0;} // If we pass the last vehicle (at the end of the chromosome) 
                                                    // we need to sum the node's cost to the first one
                vFitness[vehicle]+=costMatrix.getCost(genes[currentNode].getNode(), genes[nextNode].getNode());
                vehicleStartNode=currentNode;
            } else if (genes[nextNode].getIsVehicle()){
                // If the next node is another vehicle, sum the cost from this node to the
                // departure node of this vehicle 
                vFitness[vehicle]+=costMatrix.getCost(genes[currentNode].getNode(), genes[vehicleStartNode].getNode());
            } else {
                // If this is a normal node we get de cost between the node and the one after him
                vFitness[vehicle]+=costMatrix.getCost(genes[currentNode].getNode(), genes[nextNode].getNode());
            }
            
            currentNode=nextNode;
            nextNode++;
                    
        } while (currentNode!=indexFirstVehicle);
        
        this.vehiclesFitness=vFitness;
        
        // Second: sum the fitness of the vehicles
        float globalFitness=0;
        // For all vehicles, sum their fitness
        for (int i=0; i<vFitness.length; i++){
            globalFitness += vFitness[i];
        }
        
        // Increment the number of fitness evaluations
        this.nrFitnessEvaluations++;
System.out.println("Foi feito um update ao fitness: "+nrFitnessEvaluations);
//System.out.println("O fitness do cromossoma "+this.toString()+" é: "+globalFitness);        
        return globalFitness;
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
//TODO: remove this
//System.out.println("Cromossoma antes do swap: "+this.print());
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
//TODO: remove this
//System.out.println("Cromossoma após o swap: "+this.print());
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
    
    // Verifies if this chromosome has a specified Gene
    public boolean hasGene(Gene g){
        for (int i = 0; i < genes.length; i++) {
            if (genes[i]!=null){
//TODO: remove this. Is very slow.                
//if (((Object)genes[i]).equals(g)){
                if (genes[i]==g){
                    return true;
                }
            } else if (genes[i]==null && g==null){
                return true;
            }
        }
        return false; // Returns true if the gene exists
    }

    
    // Put all information about this chromosome in a string.
    @Override
    public String toString() {
        String gene = "";
        for (int i=0; i < genes.length; i++){
            gene += genes[i].toString()+" ";
        }
        String text = "Genes: [ " + gene +"]";
        text+= "\n Fitness: " + Float.toString(this.getFitness());
        String vFitness = "";
        for (int i=0; i < vehiclesFitness.length; i++){
            vFitness += Float.toString(vehiclesFitness[i])+" ";
        }
        text+= "\n Vehicles fitness: " + vFitness;
        return text;
    }
    
    // Print all information about this chromosome.
    public String print(){
        String chrPrint = "[ ";
        for (int i=0; i<genes.length;i++){
            if (genes[i].getIsVehicle()){
                chrPrint += "V ";
            }
            else {
                chrPrint += genes[i].getNode()+" ";
            }
        }
        chrPrint += "]";
        return chrPrint;
    }
    
    // We need to implement this method to permit comparisons.
    @Override
    public int compareTo(Chromosome chr) {
        return (int) (this.getFitness() - chr.getFitness()) ;
    }

    // Clones this Chromosome
    @Override
    public Object clone() throws CloneNotSupportedException {
//        Gene[] newGenes = genes.clone();
        Gene[] newGenes = new Gene[genes.length];
        for (int i=0; i<genes.length;i++){
            newGenes[i]=genes[i];
        }
        Chromosome newChromosome = new Chromosome(newGenes, costMatrix, nrVehicles, nrNodes,  fitness, vehiclesFitness);
        
//int hashOld = this.genes.hashCode();
//int hashNew = newChromosome.genes.hashCode();
//System.out.println("A clonar cromossomas:\nHashOld: "+hashOld+"\n HashNew: "+hashNew+"\n\n");
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
        return vehiclesFitness;
    }

    public CostMatrix getCostMatrix() {
        return costMatrix;
    }

    public int getNrFitnessEvaluations() {
        return nrFitnessEvaluations;
    }
}
