import java.util.Scanner;
class TestLinear1   {
    public static void main(String[] args) {

        Scanner in = new Scanner(System.in);


        //       ֍ network = new NetworkOne(inputs=3, layers = [1]); ֍
        NetworkOne network = new NetworkOne(3,        new int[] {1,4,4,1});

        //    ֍ Initialize weights with random numbers ֍
//        network.initializeWeightsWithRandomNumbers();
//        Почему-то при каждом запуске начинается всё с разных чисел

        double[][] sampleInputs = {
                                      {-1,   -2,   1}, 
                                      {-2,   -3,   1},
                                      {-3,   -1,   1},
                                      {-4,   -3,   1}
                                  };
             
        double[][] desiredOutputs = {{1}, {1}, {0}, {0}};

        network.feedForward(new double[] {11, 23, 1});
        System.out.println("Result of ff is " + network.output[0]);

        double error = network.getMeanSquareError(sampleInputs, desiredOutputs);
        System.out.println("Error =" + error);


        while (true) {

            network.teachByShakingWeights(100000, sampleInputs, desiredOutputs);


            System.out.println("____________testing_______________");
            network.feedForward(sampleInputs[0]);
            System.out.println("it must be one: "+ network.output[0]);

            network.feedForward(sampleInputs[1]);
            System.out.println("it must be one: "+ network.output[0]);

            network.feedForward(sampleInputs[2]);
            System.out.println("it must be zero: "+ network.output[0]);

            network.feedForward(sampleInputs[3]);
            System.out.println("it must be zero: "+ network.output[0]);
            System.out.println("______________________________\n");


            
//            network.printWeights();
//            in.nextLine();


        }



    }
}