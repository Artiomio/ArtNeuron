import java.util.Scanner;
class TestCircle {
    public static void main(String[] args) {

        Scanner in = new Scanner(System.in);


        //       ֍ network = new NetworkOne(inputs=3, layers = [1]); ֍
        NetworkOne network = new NetworkOne(3,        new int[] {1, 4, 4, 4, 1});

        //    ֍ Initialize weights with random numbers ֍
//        network.initializeWeightsWithRandomNumbers();
//        Почему-то при каждом запуске начинается всё с разных чисел

        double[][] sampleInputs = {
                                      { 1,    0,   1}, 
                                      { 1/1.44,   1/1.44,   1},
                                      {1.7/2,   0.5,   1},
                                      {0.5,   1.7/2,   1},


                                      { -1,    0,   1}, 
                                      { -1/1.44,   1/1.44,   1},
                                      {-1.7/2,   0.5,   1},
                                      {-0.5,   1.7/2,   1},


                                      { -1,    0,   1},
                                      { -1/1.44,   -1/1.44,   1},
                                      {-1.7/2,   -0.5,   1},
                                      {-0.5,   -1.7/2,   1},

                                      { 1,    0,   1},
                                      { 1/1.44,  - 1/1.44,   1},
                                      {1.7/2,  - 0.5,   1},
                                      {0.5,   -1.7/2,   1},

                                      {0.3, 0.3, 1},
                                      {-0.3, -0.3, 1},
                                      {-0.3, 0.3, 1},
                                      { 0.3, -0.3, 1},




                                  };

        double[][] desiredOutputs = {{1}, {1},{1}, {1},{1}, {1},{1}, {1},{1}, {1},{1}, {1},{1}, {1},{1}, {1}, {0}, {0},{0}, {0}  };

        network.feedForward(new double[] {11, 23, 1});
        System.out.println("Result of ff is " + network.output[0]);

        double error = network.getMeanSquareError(sampleInputs, desiredOutputs);
        System.out.println("Error =" + error);


        network.setTrainingSet(sampleInputs, desiredOutputs);
        while (true) {



            network.teachByShakingWeights(1000);

            System.out.print("\033[2J\033[;H"); //Clear screen
            System.out.println("____________Start of next cycle_______________");

            for (int i = 0; i < sampleInputs.length; i++) {
                network.feedForward(sampleInputs[i]);
                System.out.println("it must be " + desiredOutputs[i][0]+ " : "+ network.output[0]);
            }

            System.out.println("Error = " + network.getMeanSquareError(sampleInputs, desiredOutputs));
            System.out.println("______________________________\n");


//            network.printWeights();
//            in.nextLine();


        }



    }
}