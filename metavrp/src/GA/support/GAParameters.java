
package GA.support;

/**
 *
 * @author David Pinheiro
 */
public class GAParameters {
    
    private int popSize;
    private int nrVehicles;
    private float elitism;
    private float mutationProb;
    private float crossoverProb;
    private int generations;

    public GAParameters(int popSize, int nrVehicles, float elitism, float mutationProb, float crossoverProb, int generations) {
        this.popSize = popSize;
        this.nrVehicles = nrVehicles;
        this.elitism = elitism;
        this.mutationProb = mutationProb;
        this.crossoverProb = crossoverProb;
        this.generations = generations;
    }

    public float getCrossoverProb() {
        return crossoverProb;
    }

    public void setCrossoverProb(float crossoverProb) {
        this.crossoverProb = crossoverProb;
    }

    public float getElitism() {
        return elitism;
    }

    public void setElitism(float elitism) {
        this.elitism = elitism;
    }

    public int getGenerations() {
        return generations;
    }

    public void setGenerations(int generations) {
        this.generations = generations;
    }

    public float getMutationProb() {
        return mutationProb;
    }

    public void setMutationProb(float mutationProb) {
        this.mutationProb = mutationProb;
    }

    public int getNrVehicles() {
        return nrVehicles;
    }

    public void setNrVehicles(int nrVehicles) {
        this.nrVehicles = nrVehicles;
    }

    public int getPopSize() {
        return popSize;
    }

    public void setPopSize(int popSize) {
        this.popSize = popSize;
    }
    
    
}
