/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.metavrp.GA.support;

import org.metavrp.GA.Chromosome;
import java.util.Random;
import java.util.Arrays;
import java.util.ArrayList;

/**
 *
 * @author David Pinheiro
 */
public class Randomizer {
    
    static final Random randomGenerator = new Random();
    
    // Should something be done, based on some probability?
    public static boolean doIt(float probability){
        //Random randomGenerator = new Random();
        float randomFloat=randomGenerator.nextFloat();
        // Returns true if randomFloat < probability. False otherwise.
        return  (randomFloat < probability);
    }
   
    // Returns a random int between 0 (inclusive) and a given max value (exclusive)
    public static int randomInt(int max){
        // Returns a randomly generated int
        return randomGenerator.nextInt(max);
    }
    
    // Returns a random float between 0.0f (inclusive) to 1.0f (exclusive)
    public static float randomFloat(){
        // Returns a randomly generated int
        return randomGenerator.nextFloat();
    }
    
    
    // Returns n randomly choosen crossover points, sorted!
    public static int[] randomCrossoverPoints(Chromosome chr, int n){
        
        ArrayList<Integer> crossoverPoints = new ArrayList();
        int[] crossoverPointsArray = new int[n];
        
        for (int i=0;i<n;i++){
            int rand;
            do {
                rand=Randomizer.randomInt(chr.getLenght());
            }
            while (crossoverPoints.contains(rand));             
            crossoverPoints.add(rand);
        }

        // Copy the arraylist to the array
        for (int i=0;i<crossoverPoints.size();i++){
            crossoverPointsArray[i]=crossoverPoints.get(i);
        }
        
        Arrays.sort(crossoverPointsArray); //Sort the points

        return crossoverPointsArray;
    }
    
}
