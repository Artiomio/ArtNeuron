class NetworkOneTest extends NetworkOne{
    protected double activation(double arg) {
//        return arg;
//        System.out.printf("Calculating sin at %f\n ", arg);
//        System.out.println(arg);
        return Math.sin(arg);
    
    }


    NetworkOneTest(int numberOfInputs,
               int[] numberOfNeuronsInEachLayer) {
               super(numberOfInputs, numberOfNeuronsInEachLayer);
    }



    public static void main(String[] args){
        int[] layers = {2, 1, 3, 1};
        double[] inputs = {8, 9, 10, 11};
        NetworkOneTest network = new NetworkOneTest(4, layers);
//        network.InitializeWeightsWithRandomNumbers();

        network.weight[0][0][0] = 3f;
        network.weight[0][0][1] = 5f;        
        network.weight[0][0][2] = 7f;                
        network.weight[0][0][3] = 2f;                
        
        network.weight[0][1][0] = 9f;
        network.weight[0][1][1] = .5f;        
        network.weight[0][1][2] = 4f;                
        network.weight[0][1][3] = 7f;                

        



        network.weight[1][0][0] = 3f;
        network.weight[1][0][1] = 19f;        


        network.weight[2][0][0] = 5;
        
        network.weight[2][1][0] = 3;        

        network.weight[2][2][0] = 0.1;        

        
        network.weight[3][0][0] = 1f;
        network.weight[3][0][1] = 2f; 
        network.weight[3][0][2] = 3f;        
        
        

//        System.out.println("Заряд нейрона " + network.charge[4][0]);
        
        long startTime = System.currentTimeMillis();               
        System.out.println("Starting feedforward");
        for (int i=0; i<1000000; i++){
            network.feedForward(inputs);
        }
        System.out.println(" **** Feedforward took " + (System.currentTimeMillis() - startTime) + " milliseconds");        
        System.out.println("The output neuron has charge: " + network.charge[3][0]);




        double[][] desiredOutputs = {{3,6},{3.14,6.28},{0, 0}};
        double[][] sampleInputs = {{1,2,3}, {3,4,5}, {5,6,7}};           

//        System.out.println("Mean error = " + network.getMeanSquareError(sampleInputs, desiredOutputs));



     
    }
}