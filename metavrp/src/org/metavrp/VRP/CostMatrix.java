/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.metavrp.VRP;

import org.metavrp.GA.support.*;

import java.io.*;

/**
 *
 * @author David
 */
public class CostMatrix {
    
    float[][] costMatrix;   // The bi-dimentional array
    
    int size;   // The size of the matrix. By size we mean the vertical or horizontal size
                // (as the matrix needs to be square), which corresponds to the number 
                // of elements in the matrix.
    
    
    // Constructor.
    // Given a Cost Matrix as a bi-dimentional float matrix, creates CostMatrix objects
    public CostMatrix(float[][] costMatrix){
        this.size=costMatrixSize(costMatrix);
        this.costMatrix=costMatrix;
    }
    
    // Constructor.
    // Given a formatted file, build a Cost Matrix.
    // If tspType is true, then the file has point coordinates.
    // If tspType is false, then the file has costs between points.
    public CostMatrix(String fileName, boolean tspType){
        if (tspType){
            this.costMatrix = openCostMatrix(fileName); // TODO: falta implementar para este caso.
        } else {
            this.costMatrix = openCostMatrix(fileName); 
        }
        this.size=costMatrixSize(costMatrix);
    }
    
    //*******************************************************************
    // Measure the lenght of the chromosome from the cost matrix size
    //*******************************************************************
    private static int costMatrixSize (float[][] dm){
        int length=dm.length;
        if (length!=dm[0].length){
            throw new AssertionError("ERROR: The cost matrix is not a square matrix!");
        }
        return length;
    }
    
    // Returns the cost between two nodes.
    public float getCost(int origin, int destination){
        return costMatrix[origin][destination];
    }

    // Returns the size of this square matrix
    public int getSize() {
        return size;
    }
    

    //*******************************************************************
    // Various static methods to create the cost matrix from files.
    //*******************************************************************       
    //Build a randomly generated cost matrix
    public static float[][] buildRandomCostMatrix(int nrRows, int nrColumns) {
        
        float[][] costMatrix = new float[nrRows][nrColumns];
 
        for (int i=0; i<nrRows; i++){
            for (int j=0; j<nrColumns; j++){
                costMatrix[i][j]=Randomizer.randomInt(100);
            }
        }
        return costMatrix;
    }    
    
    //Opens a cost matrix on a text file
    public static float[][] openCostMatrix(String fileName){

        int x,y;    // The coordinates
        float cost; // The cost

        int size = measureCostMatrixSize(fileName);     // The size of the matrix needs to be measured
        float[][] costMatrix = new float[size][size];
        
        // Open the file for reading
        try{
            FileInputStream fistream = new FileInputStream(fileName);
            DataInputStream in = new DataInputStream(fistream);
            BufferedReader br = new BufferedReader(new InputStreamReader(in));
            String fullLine=null;
            String[] lineRead=null;
            
            //Read file, line by line
            fullLine = br.readLine();
            while (fullLine != null){
                lineRead=fullLine.split("\t");      // Split on tabs
                x=Integer.parseInt(lineRead[0]);    //The first point, x
                y=Integer.parseInt(lineRead[1]);    //The second point, y
                cost=Float.parseFloat(lineRead[3]); //The distance (cost) from x to y its on the fourth column
                costMatrix[x][y]=cost;
                fullLine = br.readLine();
            }
            //Close the stream
            in.close();
        }catch (Exception e){//Catch exception if any
            e.printStackTrace();
        }
        return costMatrix;
    }
    
    // Given a file that represents the cost matrix, measure its size
    public static int measureCostMatrixSize(String fileName){

        int x,y;            // The coordinates
        int maxX=0, maxY=0; // Their maximum values
            
        try{
            FileInputStream fistream = new FileInputStream(fileName);
            DataInputStream in = new DataInputStream(fistream);
            BufferedReader br = new BufferedReader(new InputStreamReader(in));
            
            //Read file, line by line
            String fullLine = br.readLine();
            String[] lineRead=null;
            while (fullLine != null){
                lineRead=fullLine.split("\t");      // Split on tabs
                x=Integer.parseInt(lineRead[0]);    //The first point, x
                y=Integer.parseInt(lineRead[1]);    //The second point, y
                if (x>maxX) maxX=x;
                if (y>maxY) maxY=y;
                fullLine = br.readLine();
            }
            //Close the stream
            in.close();
        }catch (Exception e){//Catch exception if any
            e.printStackTrace();
        }
        
        if (maxX!=maxY){
            throw new AssertionError("ERROR: The file represents a cost matrix that isn't square!");
        }
        return maxX+1;
    }

}
