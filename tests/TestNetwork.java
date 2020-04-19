class TestNetwork {
    public static void main(String[] args){
        int[] layers = {3, 10, 10, 2};
        double[] inputs = {1.2, 34, 5};
        NetworkOne network = new NetworkOne(3, layers);
        network.InitializeWeightsWithRandomNumbers();
   
        long startTime = System.currentTimeMillis();               
        for (int i=0; i<10000; i++){
            network.feedForward(inputs);
        }
        System.out.println("Тест прямого прохода сети");
  


        double[][] desiredOutputs = {{3,6}, {3.14,6.28}, {0, 0}};
        double[][] sampleInputs = {{1,2,3}, {3,4,5}, {5,6,7}};

        System.out.println("Mean error = " + network.getMeanSquareError(sampleInputs, desiredOutputs));

        System.out.println("Time spent: " + (System.currentTimeMillis() - startTime));
     
    }

}