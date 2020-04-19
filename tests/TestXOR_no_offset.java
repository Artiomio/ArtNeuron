import java.util.Scanner;
class TestXOR_no_offset {
    public static void main(String[] args) {

        Scanner in = new Scanner(System.in);


        //       ֍ network = new NetworkOne(inputs=3, layers = [1]); ֍
        NetworkOne network = new NetworkOne(2,        new int[] {2,4,4,1});

        //    ֍ Initialize weights with random numbers ֍
//        network.initializeWeightsWithRandomNumbers();
//        Почему-то при каждом запуске начинается всё с разных чисел

        double[][] sampleInputs = {
                                      {0, 0}, 
                                      {0, 1},
                                      {1, 0},
                                      {1, 1}
                                  };
             
        double[][] desiredOutputs = {{0}, {1}, {1}, {0}};

        network.feedForward(new double[] {1, 2});
        System.out.println("Result of ff is " + network.output[0]);

        double error = network.getMeanSquareError(sampleInputs, desiredOutputs);
        System.out.println("Error =" + error);


        while (true) {

            network.teachByShakingWeights(100000, sampleInputs, desiredOutputs);


            System.out.println("____________testing_______________");
            network.feedForward(sampleInputs[0]);
            System.out.println("it must be zero: "+ network.output[0]);

            network.feedForward(sampleInputs[1]);
            System.out.println("it must be one: "+ network.output[0]);

            network.feedForward(sampleInputs[2]);
            System.out.println("it must be one: "+ network.output[0]);

            network.feedForward(sampleInputs[3]);
            System.out.println("it must be zero: "+ network.output[0]);
            System.out.println("______________________________\n");


            
//            network.printWeights();
//            in.nextLine();


        }



    }
}