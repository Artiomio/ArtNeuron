import java.util.Scanner;
class TestLinear {
    public static void main(String[] args) {

        Scanner in = new Scanner(System.in);


        //       ֍ network = new NetworkOne(inputs=3, layers = [1]); ֍
        NetworkOne network = new NetworkOne(3,        new int[] {1,3,5,1} );
        

        //    ֍ Initialize weights with random numbers ֍
//        network.initializeWeightsWithRandomNumbers();
//        Почему-то при каждом запуске начинается всё с разных чисел

        double[][] sampleInputs = {
                                      {1,   2,   1}, 
                                      {2,   3,   1},
                                      {3,   1,   1},
                                      {4,   3,   1}
                                  };
             
        double[][] desiredOutputs = {{1}, {1}, {0}, {0}};

        network.feedForward(new double[] {11, 23, 1});
        System.out.println("Result of ff is " + network.output[0]);

        double error = network.getMeanSquareError(sampleInputs, desiredOutputs);
        System.out.println("Error =" + error);


        while (true) {
            //  ֍  error_1 = get_Mean_Square_Error() ֍
            double error_1 = network.getMeanSquareError(sampleInputs, desiredOutputs);

            //  ֍   add Random_Number_To_Random_Weight() ֍
            network.addRandomNumberToRandomWeight();

            //  ֍  error_2 = get_Mean_Square_Error() ֍
            double error_2 = network.getMeanSquareError(sampleInputs, desiredOutputs);

            // ֍ if (error_1 < error_2) then undo
            if (error_1 < error_2) {
                network.undoAddingRandomNumberToRandomWeight();
            }



            System.out.println("error_1 =" + error_1 + " and error2_=" + error_2);
         
            

//            System.out.println("weight1=" + network.weight[0][0][0]);
//           System.out.println("weight2=" + network.weight[0][0][1]);
//            System.out.println("weight2=" + network.weight[0][0][2]);


            System.out.println("____________testing_______________");
            network.feedForward(sampleInputs[0]); double out1 = network.output[0];
            System.out.println("it must be one: "+ network.output[0]);

            network.feedForward(sampleInputs[1]); double out2 = network.output[0];
            System.out.println("it must be one: "+ network.output[0]);

            network.feedForward(sampleInputs[2]); double out3 = network.output[0];
            System.out.println("it must be zero: "+ network.output[0]);

            network.feedForward(sampleInputs[3]); double out4 = network.output[0];
            System.out.println("it must be zero: "+ network.output[0]);

            System.out.println("internal error =" + ((1-out1)*(1-out1) +(1-out2)*(1-out2) +out3*out3 + out4*out4)/4);
            System.out.println("_________________________________________________________________\n");


            network.printWeights();
            
//            in.nextLine();


        }



    }
}