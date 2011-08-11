package GA;

import GA.support.Randomizer;
import VRP.CostMatrix;
import java.util.*;

/**
 * TODO: Verify if Cost Matrix shouldn't be on the variables
 * TODO: Verify that after every mutation/crossover, the fitness is measured
 * TODO: Or put the mutation methods in the Population class
 * TODO: O algoritmo apenas deve correr se o cromossoma tiver um tamanho decente (pelo menos dois nodos)
 * @author David Pinheiro
 */
public class Chromosome implements Cloneable, Comparable<Chromosome>{

    private int nrVehicles;             // The number of vehicles in the chromosome
    private int nrNodes;                // The number of nodes not vehicles
    
    private CostMatrix costMatrix;   // Bi-dimentional array with the distances between any two nodes

    private Gene[] genes;               // The actual chromosome: an Array of genes

    private float fitness;              // Chromosome's fitness value (Corresponds to the total cost)

    private float[] vehiclesFitness;    // Fitness of the vehicles
    
    
    // Constructor.
    // Creates a chromosome from a cost matrix. Genes are randomly generated.
    public Chromosome(GeneList geneList, CostMatrix dm) {
        this.nrNodes = geneList.getNrNodes();
        this.nrVehicles= geneList.getNrVehicles();
        this.costMatrix = dm;
        this.genes = generateRandomChromosome(geneList);
verificarGenes();
        this.fitness = measureFitness();
    }

    // Constructor.
    // Creates a chromosome from an array of genes with the vehicles already there
    public Chromosome(Gene[] genes, CostMatrix dm) {
        this.costMatrix = dm;
        this.genes = genes;
        this.nrVehicles = countVehicles(genes);
        this.nrNodes = genes.length - this.nrVehicles;
verificarGenes();        
        this.fitness = measureFitness();
    }
public void verificarGenes(){
    // TODO: criar nova geneList
    ArrayList<Gene> genesList = new ArrayList<Gene>();
    genesList.addAll(Arrays.asList(genes));
    for (Gene gene : genes){
        if (!genesList.contains(gene)){
            throw new AssertionError("Falta um gene!");
        }
        genesList.remove(gene);
    }
    if (!genesList.isEmpty()){
        throw new AssertionError("Está um gene a mais!");
    }
    if (costMatrix.getSize()!=genes.length-1)
        throw new AssertionError("Faltam genes no cromosoma!");
}
    
    //****************************
    // Generate a random chomosome
    //****************************
    private Gene[] generateRandomChromosome(GeneList geneList) {
        // Create a temporary array of the cloned List of Genes. 
        // Cloned because we want to make changes without destroying the original list.
        ArrayList<Gene> temp_chr = geneList.getClonedGenes();
        
        int size = geneList.getGenes().size();  // Get the size of the list
        
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
    
    // Measure the fitness of this chromosome
    // TODO: Verificar se é possível acelerar este método e reescrevê-lo para ficar mais bonito e curto
    private float measureFitness(){
        
        int nVehicles = countVehicles(genes);       // Count the number of vehicles
        
        float[] vFitness = new float[nVehicles];    // The fitness (routing cost) of the vehicles
        int vehicle=0;                              // The number of the vehicle
        
        int indexFirstVehicle = indexFirstVehicle(genes);
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
//System.out.println("O fitness do cromossoma "+this.toString()+" é: "+globalFitness);        
        return globalFitness;
    }
    
    // Updates the fitness value of this chromosome
    public void updateFitness() {
        this.fitness = measureFitness();
    }
    
    
    //**********************
    //  Auxiliary functions
    //**********************
    
    // Swaps two genes, defined by positions 'a' and 'b'
    public void swapGenes(int a, int b){
        if (a > genes.length || b > genes.length){
            throw new AssertionError("[ERROR] Cannot swap genes because one of them is out of the chromosome!");
        }
        Gene gene1 = genes[a];
        Gene gene2 = genes[b];
        this.setGene(gene2, a);
        this.setGene(gene1, b);
    }
    
    // Swaps two given genes
    public void swapGenes(Gene a, Gene b){
//System.out.println("Cromossoma antes do swapping: "+this.print());
        int indexA=this.indexOf(a); //Find their indexes
        int indexB=this.indexOf(b);
        this.setGene(b, indexA);    //Swap them using the indexes
        this.setGene(a, indexB);
//System.out.println("Cromossoma após o swapping: "+this.print());
    }
    
    // Find the index of the given gene
    public int indexOf(Gene g) {
        int index = -1;
        for (int i = 0; i < this.getLenght(); i++) {
                if (((Object)genes[i]).equals(g))
                    return index = i;
        }
        return index; // Returns -1 if it doesn't exist
    }
    
    // Finds the index of the first vehicle
    public int indexFirstVehicle(Gene[] genes){
        for (int i=0; i<genes.length;i++){
            if (genes[i].getIsVehicle()){
                return i;
            }
        }
        throw new AssertionError("ERROR: The array of genes doesn't have any vehicles!");
    }
    
    // Verifies if this chromosome has a specified Gene
    public boolean hasGene(Gene g){
        for (int i = 0; i < this.getLenght(); i++) {
            if (genes[i]!=null){
                if (((Object)genes[i]).equals(g)){
                    return true;
                }
            } else if (genes[i]==null && g==null){
                return true;
            }
        }
        return false; // Returns true if the gene exists
    }
    
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

    
    // Put all information about this chromosome in a string.
    @Override
    public String toString() {
        String gene = "";
        for (int i=0; i < genes.length; i++){
            gene += genes[i].toString()+" ";
        }
        String text = "Genes: [ " + gene +"]";
        text+= "\n Fitness: " + Float.toString(fitness);
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
                chrPrint += "!VVVVV! ";
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
        return (int) (this.fitness - chr.fitness) ;
    }

    // Clones this Chromosome
    @Override
    public Object clone() throws CloneNotSupportedException {
//        Gene[] newGenes = genes.clone();
        Gene[] newGenes = new Gene[genes.length];
        for (int i=0; i<genes.length;i++){
            newGenes[i]=genes[i];
        }
        Chromosome newChromosome = new Chromosome(newGenes, costMatrix);
        
//int hashOld = this.genes.hashCode();
//int hashNew = newChromosome.genes.hashCode();
//System.out.println("A clonar cromossomas:\nHashOld: "+hashOld+"\n HashNew: "+hashNew+"\n\n");
        return newChromosome;
    }

    
    //**************************
    // Getters and Setters
    //**************************

    public float getFitness() {
        return fitness;
    }

    public Gene[] getGenes() {
        return genes;
    }

    public Gene getGene(int i){
        return genes[i];
    }
    
    // TODO: verificar se é mesmo necessário este método. Não será melhor usar apenas o swap e defini-lo como private?
    public void setGene(Gene g, int i){
//System.out.println("Set gene: "+g.print());        
        this.genes[i]=g;
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
        return vehiclesFitness;
    }

    public CostMatrix getCostMatrix() {
        return costMatrix;
    }
    
}
