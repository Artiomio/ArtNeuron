import java.util.Scanner;
class TestCircleWithSlowGradientDescent {
  
    public static void main(String[] args) {

        Scanner in = new Scanner(System.in);


        //       ֍ network = new NetworkOne(inputs=3, layers = [1]); ֍
        NetworkOne network = new NetworkOne(3,        new int[] {1, 300, 300, 1});

        //    ֍ Initialize weights with random numbers ֍
        network.initializeWeightsWithRandomNumbers();
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

        //network.feedForward(new double[] {11, 23, 1});
        //System.out.println("Result of ff is " + network.output[0]);

        double error = network.getMeanSquareError(sampleInputs, desiredOutputs);

        /* Устанавливаем тренировочные данные */
        network.setTrainingSet(sampleInputs, desiredOutputs);

        network.LEARNING_RATE = 0.003;
        network.DELTA_W = 0.0001;
        network.randomWeightAdditionRange = 0.01; /* Для случайного блуждания */

        network.setCurrentMiniBatchRange(0, 19);


        long teachingStartTime = System.currentTimeMillis();
        while (true) {
            long optimizationStartTime = System.currentTimeMillis();

            //network.slowGradientDescent(100); /* Медленный градиентный спуск */


            /* Large random walk */
            double error0 = network.getMeanSquareError(sampleInputs, desiredOutputs);
            network.randomWeightAdditionRange = 10; /* Для случайного блуждания */
            network.teachByShakingWeights(100);
            long timeStamp1 = System.currentTimeMillis();


            /* Small random walk */
            double error1 = network.getMeanSquareError(sampleInputs, desiredOutputs);
            network.randomWeightAdditionRange = 0.01; /* Для случайного блуждания */
            network.teachByShakingWeights(100);
            long timeStamp2 = System.currentTimeMillis();

            
            double error2 = network.getMeanSquareError(sampleInputs, desiredOutputs);
            network.teachWithBackpropagation(100);
            long timeStamp3 = System.currentTimeMillis();            

            double error3 = network.getMeanSquareError(sampleInputs, desiredOutputs);

            //ArtiomArrayUtils.print3DArray(network.costGradient, new String[] {"Layer", "Neuron", "Weight"});
            //in.nextLine();





            long optTimeSpent = System.currentTimeMillis() - optimizationStartTime;
        

            System.out.print("\033[2J\033[;H"); //Clear screen using an ANSI seq
            System.out.println("____________Start of next cycle_______________");


            /* Печать того, что должно быть и что есть */
            /*
            for (int i = 0; i < sampleInputs.length; i++) {
                network.feedForward(sampleInputs[i]);
                System.out.println("it must be " + desiredOutputs[i][0]+ " : "+ network.output[0]);
            }
            */



            
            
            
            
            long errorCalculationStart = System.currentTimeMillis();
            System.out.println("Error = " + network.getMeanSquareError(sampleInputs, desiredOutputs) + "                (took " + (System.currentTimeMillis() - errorCalculationStart) + " ms)");
            System.out.println("______________________________\n");
            System.out.println("Time spent over one cycle: " + optTimeSpent + " ms");
            System.out.println("Total time spent: " + (System.currentTimeMillis() - teachingStartTime)/1000 + " sec");


            System.out.println("\n\nA bit of statistics:\n" + 
                                "Big random walk benefit:   " +  (error0 - error1) + "          spent " + (timeStamp1 - optimizationStartTime) + "ms\n" +
                                "Small random walk benefit: " + (error1 - error2) + "          spent " + (timeStamp2 - timeStamp1) + "ms\n" +
                                "Backpropagation benefit:   " + (error2 -error3) + "          spent " + (timeStamp3 - timeStamp2) + "ms\n");


            // System.out.println("Press Enter to continue"); in.nextLine();
           
            /*
            if ((error2 - error3) < 1e-5) {
                System.out.println("Increasing Learning rate!");
                network.LEARNING_RATE =  network.LEARNING_RATE * 10;
            }
            else 
                network.LEARNING_RATE = 0.001;  */
            

        }



    }
}