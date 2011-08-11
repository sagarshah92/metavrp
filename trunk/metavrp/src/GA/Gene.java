package GA;

/**
 * Represents a simple gene
 * 
 * @author David Pinheiro
 */
public class Gene implements  Cloneable {

   // The ID of the gene. If positive, is a normal node. If negative, is a vehicle.
   private int id;
    
   // Gene's value (node number)
   // In case of a vehicle, corresponds to its initial node
   private int node;
   
   //This gene is a vehicle?
   private boolean isVehicle;
   
   // What's this gene size? In case it's a vehicle, it corresponds to its capacity
   // In case of a node, it corresponds to it's measure unit (size, volume, quantity, etc...)
   private float capacity;

   // Constructs a gene without capacity. 
    public Gene(int id, int v,boolean vehicle){
        this.id=id;
        this.node=v;
        this.isVehicle=vehicle;
    }
    
    // Constructs a gene with capacity
    public Gene(int id, int v,boolean vehicle, float capacity){
        this.id=id;
        this.node=v;
        this.isVehicle=vehicle;
        this.capacity=capacity;
    }

    /* ------------------- */
    /* Getters and Setters */
    /* ------------------- */
    
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
    
    public int getNode(){
        return this.node;
    } 
        
    public void setNode(int v){
        this.node=v;
    }
    
    public void setIsVehicle(boolean isVehicle){
        this.isVehicle=isVehicle;
    }

    public boolean getIsVehicle(){
        return this.isVehicle;
    }
    
    public void setCapacity(float capacity){
        this.capacity=capacity;
    }

    public float getCapacity(){
        return this.capacity;
    }
    
    // Transform the integer value into a string
    @Override
    public String toString(){
        return Integer.toString(node);
    }
    
    // Print all the gene information
    public String print(){
        String geneString = "Node: "+node+", isVehicle: "+isVehicle+", capacity:"+capacity;
        return geneString;
    }
    
    // Clone this Gene
    @Override
    public Gene clone(){
        int clonedId = this.id;
        int clonedNode = this.node;
        boolean clonedIsVehicle = this.isVehicle;
        float clonedCapacity = this.capacity;
        
        Gene newGene = new Gene(clonedId, clonedNode, clonedIsVehicle, clonedCapacity);

// TODO: delete this        
int hashOriginal = this.hashCode();
int hashClone = newGene.hashCode();
        
        return newGene;
    }
  }