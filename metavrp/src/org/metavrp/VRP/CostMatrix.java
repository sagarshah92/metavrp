/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.metavrp.VRP;

import java.awt.geom.Point2D;
import org.metavrp.GA.support.*;

import java.io.*;
import java.util.ArrayList;

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
    public CostMatrix(String fileName, boolean vrpType){
        if (vrpType){
            this.costMatrix = getVRPCostMatrix(fileName);
        } else {
            this.costMatrix = getGvSIGCostMatrix(fileName); 
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
    
    // Opens a cost matrix on a text file
    // This method was called measureCostMatrix()
    public static float[][] getGvSIGCostMatrix(String fileName){

        int x,y;    // The coordinates
        float cost; // The cost

        int size = measureGvSIGCostMatrixSize(fileName);     // The size of the matrix needs to be measured
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
    public static int measureGvSIGCostMatrixSize(String fileName){

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
    
    /*
     * Gets the cost matrix of a .vrp formatted instance.
     * It goes to the section named NODE_COORD_SECTION and measures the distances 
     * between every two nodes and puts on a float[][] matrix. It goes to the
     * DEPOT_SECTION and measures the distance between the depot and every other node.
     * The depot will be on position 0 of the distance matrix.
     */
    public static float[][] getVRPCostMatrix(String fileName){
        
        // Initializations
        int size = measureVRPCostMatrixSize(fileName);      // The size of the matrix needs to be measured
        float[][] costMatrix = new float[size+1][size+1];   // Create the matrix. +1 element to the depot
        float[][] coordinates = new float[size+1][2];       // The coordinates in a n by 2 matrix
        
        try{
            FileInputStream fistream = new FileInputStream(fileName);
            DataInputStream in = new DataInputStream(fistream);
            BufferedReader br = new BufferedReader(new InputStreamReader(in));
            String fullLine=null;
            String[] lineRead=null;
            
            // Go to the NODE_COORD_SECTION
            while (!br.readLine().contains("NODE_COORD_SECTION")){};
        
            // Get the coordinates of the nodes
            fullLine = br.readLine();
            while (!fullLine.contains("DEMAND_SECTION")){
                lineRead = fullLine.split("\\s+");    // Split on tabs and spaces
                // If the line readed isn't empty, get the node coordinates
                if (!lineRead[0].equals("")){
                    int node = Integer.parseInt(lineRead[0]);               // The node number
                    coordinates[node][0] = Float.parseFloat(lineRead[1]);   // The x coordinate
                    coordinates[node][1] = Float.parseFloat(lineRead[2]);   // The y coordinate
                }

                fullLine = br.readLine();   // Read the next line
            }
        
            // Go to DEPOT_SECTION
            while (!br.readLine().contains("DEPOT_SECTION")){};
            
            // Read depot's coordinates
            do{ // Get rid of empty lines
                fullLine = br.readLine();
                lineRead = fullLine.split("\\s+");    // Split on tabs and spaces
            } while (lineRead[0].equals(""));
            coordinates[0][0] = Float.parseFloat(lineRead[0]);  // Depot's x coordinate
            coordinates[0][1] = Float.parseFloat(lineRead[1]);  // Depot's y coordinate
            
            // Measure the distances between every two nodes
            for (int i=0; i <= size; i++){
                for (int j=0; j<= size; j++){
                    double x1 = coordinates[i][0];
                    double y1 = coordinates[i][1];
                    double x2 = coordinates[j][0];
                    double y2 = coordinates[j][1];
                    costMatrix[i][j] = (float) Point2D.distance(x1, y1, x2, y2);
                }
            }
        }catch (Exception e){//Catch exception if any
            e.printStackTrace();
        }

        return costMatrix;
    }
    
    
    // Given a .vrp format file, measure the number of nodes
    public static int measureVRPCostMatrixSize(String fileName){

        int x,y;        // The coordinates
        int maxNode=0;  // The maximum node
        int nrNodes=0;  // The number of nodes
            
        try{
            FileInputStream fistream = new FileInputStream(fileName);
            DataInputStream in = new DataInputStream(fistream);
            BufferedReader br = new BufferedReader(new InputStreamReader(in));
            
            // Read file, line by line
            String fullLine = br.readLine();
            String[] lineRead=null;

            // Keep reading the file until it finds NODE_COORD_SECTION
            while (!br.readLine().contains("NODE_COORD_SECTION")){};

            // Then start counting the number of nodes until it finds DEMAND_SECTION
            fullLine = br.readLine();
            while (!fullLine.contains("DEMAND_SECTION")){
                lineRead=fullLine.split("\\s+");    // Split on tabs and spaces
                // If the line readed isn't empty, measure the number of nodes
                if (!lineRead[0].equals("")){
                    int node = Integer.parseInt(lineRead[0]);    //The node number
                    if (node > maxNode){
                        maxNode = node;
                    }
                    nrNodes++;  // Increment the number of nodes
                }
                fullLine = br.readLine();
            }

            //Close the stream
            in.close();
        }catch (Exception e){//Catch exception if any
            e.printStackTrace();
        }
        
        if (maxNode != nrNodes){
            throw new AssertionError("ERROR: The file contains a number of nodes "
                    + "different than the numbers given to the nodes!");
        }
        return nrNodes;
    }
}
