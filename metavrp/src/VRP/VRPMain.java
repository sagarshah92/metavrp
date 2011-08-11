
package VRP;

import GA.VRPGARun;
import GA.support.*;

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
        
        int popSize=1000;
        int nrVehicles=1;
        float elitism=0.01f;
        float mutationProb=0.1f;
        float crossoverProb=0.8f;
        int generations=1000;
        
        String fileName = "c:/vrp-tsp/dm171.txt";
        
        CostMatrix costMatrix = new CostMatrix(fileName, false);

        GAParameters params = new GAParameters(popSize, nrVehicles, elitism, mutationProb, crossoverProb, generations);

        VRPGARun run = new VRPGARun(params, costMatrix);

    }

}
