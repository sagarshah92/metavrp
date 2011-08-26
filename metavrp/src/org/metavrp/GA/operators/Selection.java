package org.metavrp.GA.operators;

import org.metavrp.GA.Chromosome;
import org.metavrp.GA.Population;
import org.metavrp.GA.support.Randomizer;
import java.util.Arrays;

/**********************
 * Selection Operators
 **********************
 *
 * @author David Pinheiro
 */
public class Selection {
    
    // Tournament selection 
    // Pick n members at random and return the best of them
    public static Chromosome tournamentSelection(int n, Population pop) {
        Chromosome[] tournamentPool = new Chromosome[n]; //Create a tournament pool
        Chromosome[] chromosomes = pop.getChromosomes();
        for (int i=0; i<n; i++){
            // Add random chromosomes to the pool
            tournamentPool[i]=chromosomes[Randomizer.randomInt(chromosomes.length)];
        }
        Arrays.sort(tournamentPool, pop); //Sort the pool
        return tournamentPool[0];// Return the best chromosome (the first)
    }
    
    
}



    //TODO: Write another selection operators

//    public ArrayList<Chromosome> selection_rouletteWheel(int pop_perc, ArrayList<Chromosome> pop) {
//        //copy
//        ArrayList<Chromosome> temppop = new ArrayList();
//        try {
//            DeepCopyUtil deepCopyUtil = new DeepCopyUtil();
//            temppop = deepCopyUtil.deepCopy(pop);
//        } catch (DeepCopyException ex) {
//            Logger.getLogger(GA.class.getName()).log(Level.SEVERE, null, ex);
//        }
//        selectionPool.clear();
//        //probability table
//        int totalF = totalFitness(temppop);
//        float probs[]= new float[temppop.size()];
//        for(int i=0;i<probs.length;i++){
//            int x = temppop.get(i).fitness*100;
//            float p = x/totalF;
//            //minimizing problem...smalest fitness gets the highest value 
//            probs[i]=1-p;
//        }
//        Random randomGenerator = new Random();
//        int x = (int) ((popSize * pop_perc) / 100);        
//        for (int i = 0; i < x; i++) {        
//            //random number
//            float a = randomGenerator.nextInt(popSize)/100;
//            int postosave = -1;
//            float previous=0;
//            for (int z=0;z<probs.length;z++){
//                //interval found save position
//                if(a>previous &&  a < probs[z] ){
//                    postosave = z;
//                    break;
//                }
//                previous = probs[z];
//            }
//            if(postosave !=-1){
//                selectionPool.add(temppop.get(postosave));
//            }
//        }
//        // System.out.println("Selction Pool\n" + selection_pool.toString());
//        return selectionPool;
//    }
//    public ArrayList<Chromosome> selection_fitnessPorpotion(int perc,ArrayList <Chromosome> pop){
//        ArrayList<Chromosome> temppop = new ArrayList();
//        try {
//            DeepCopyUtil deepCopyUtil = new DeepCopyUtil();
//            temppop = deepCopyUtil.deepCopy(pop);
//        } catch (DeepCopyException ex) {
//            Logger.getLogger(GA.class.getName()).log(Level.SEVERE, null, ex);
//        }
//        //order
//        ArrayList<Chromosome> chr_ord = new ArrayList();
//        for (int i = 0; i < popSize; i++) {
//            Chromosome ctemp = temppop.get(i);
//            for (int z = 0; z < popSize; z++) {
//                if (chr_ord.isEmpty() == true) {
//                    chr_ord.add(temppop.get(0));
//                    break;
//                }
//                Chromosome ordtemp = chr_ord.get(z);
//                if (ctemp.fitness <= ordtemp.fitness) {
//                    chr_ord.add(z, ctemp);
//                    break;
//                    //continue;
//                } else {
//                    if (chr_ord.get(chr_ord.size() - 1) == ordtemp) {
//                        chr_ord.add(ctemp);
//                        //continue;
//                        break;
//                    }
//                }
//            }
//        }
//        //insert only a percentage of pop
//        selectionPool.clear();
//        int x = (int) ((popSize * perc) / 100);
//        for (int i = 0; i < x; i++) {
//            selectionPool.add(chr_ord.get(i));
//        }
//        return selectionPool;
//    }
//
//   public int totalFitness(ArrayList <Chromosome> pop){
//    int t=0;
//    for(int i =0;i<pop.size();i++){
//        t += pop.get(i).fitness;
//    }
//   return t;
//   }


