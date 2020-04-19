import java.io.IOException;
import static java.awt.event.KeyEvent.*;
import java.util.Arrays;

class ArtMNISTExperiment {

    public static void main(String[] args) {

        /* Сохраняем время запуска программы */
        long programStartTime = System.currentTimeMillis();


        try {

            /* создаём экземпляр считывателя базы данных цифр MNIST */
            MNISTReader mnistReader = new MNISTReader("../../mnist/data");

            /* теперь создаём экзмепляр нейронную сеть */
            NetworkOne network = new NetworkOne(28 * 28, /* Количество входных нейронов */
                                               new int[] {100, 100, 10} /* Первый и второй (последний) слои */);

            /* устанавливаем набор данных обучения */
            network.setTrainingSet(mnistReader.trainImagesArr, mnistReader.trainBinaryLabels);

            



            ArtConsole console = new ArtConsole();
            int keyCode = 0;


            /* инициализируем параметры обучения */
            network.LEARNING_RATE = 0.01;
            network.DELTA_W = 0.01;
            network.randomWeightAdditionRange = 1;

            /* Основной цикл обучения */

            Sys.clearScreen();
            double error = 0;

            boolean backpropagationAllowed = true;
            int numberOfBackpropCycles = 50;

            boolean randomWalkAllowed = false;
            int numberOfRandomWalkCycles = 20;

            int miniBatchSize = 50;



            while (keyCode != VK_ESCAPE && keyCode != VK_Q) {
                
                if (console.keyPressed) { /* Обработка нажатий клавиш */
                    console.clearKey();
                    keyCode = console.keyCode;

                    if (keyCode == VK_ADD) {
                        network.LEARNING_RATE *= 1.1;
                        console.println("Increasing Learning rate! Now Learning rate is " + network.LEARNING_RATE);
                    }

                    if (keyCode == VK_SUBTRACT) {
                        network.LEARNING_RATE /= 1.1;
                        console.println("Decreasing Learning rate! Now Learning rate is " + network.LEARNING_RATE);
                    }

                    if (keyCode == VK_MULTIPLY) {
                        network.LEARNING_RATE *= 10;
                        console.println("Increasing Learning rate! Now Learning rate is " + network.LEARNING_RATE);
                    }

                    if (keyCode == VK_DIVIDE) {
                        network.LEARNING_RATE /= 10;
                        console.println("Decreasing Learning rate! Now Learning rate is " + network.LEARNING_RATE);
                    }


                    if (keyCode == VK_PAGE_UP) {
                        network.randomWeightAdditionRange *= 2;
                        console.println("Increasing Random step max size! Now it is " + network.randomWeightAdditionRange);
                    }

                    if (keyCode == VK_PAGE_DOWN) {
                        network.randomWeightAdditionRange /= 2;
                        console.println("Decreasing random step max size! Now it is " + network.randomWeightAdditionRange);
                    }

                    
                    if (keyCode == VK_R) {
                        console.println("Reinitializing weights");
                        network.initializeWeightsWithNormalRandomNumbers();
                        programStartTime = System.currentTimeMillis();
                    }

                    if (keyCode == VK_Z) {
                        console.println("Zeroing weights");
                        ArtiomArrayUtils.zeroFill3DArray(network.weight);
                        programStartTime = System.currentTimeMillis();
                    }

                    
                    if (keyCode == VK_B)
                        backpropagationAllowed = !backpropagationAllowed;

                    if (keyCode == VK_F)
                        randomWalkAllowed = !randomWalkAllowed;




                } /* Обработка клавиш */



                /* Устанавливаем границы мини-пакета */                
                network.setCurrentMiniBatchRange(1000 + 0, 1000 + miniBatchSize);

                /* Обучение обратным распространением */
                long timeBackpropStart = System.currentTimeMillis();                         /* Засекаем время         */
                if (backpropagationAllowed)
                    network.teachWithBackpropagation(numberOfBackpropCycles);                /* работы backpropagation */
                long timeSpentOnBackprop = System.currentTimeMillis() - timeBackpropStart;


                /* Обучением случайным блужданием и фантазёрством */
                long timeRandomWalkStart = System.currentTimeMillis();        /* Засекаем время */
                if (randomWalkAllowed)
                    network.teachByShakingWeights(numberOfRandomWalkCycles);  /* случайного блуждания */
                long timeSpentOnRandomWalk = System.currentTimeMillis() - timeRandomWalkStart;


                console.clearScreen(); /* Стираем с экрана */


                long timeMeanSquareErrorCalculationStart = System.currentTimeMillis(); /* Засекаем время вычисления простой                 */
                error = network.getMeanSquareError();                                  /*  среднеквадратичной ошибки на текущем мини-пакете */
                long timeSpentOnErrorCalculation = System.currentTimeMillis() - timeMeanSquareErrorCalculationStart;

                console.println("Mean square error: " + error);
                System.err.println(error); /* Перенаправляем в stderr */
                


                console.println("Time elapsed: " + (System.currentTimeMillis() - programStartTime) / 1000 + " seconds");
                console.println("Learning with:\n    Learning rate: " + network.LEARNING_RATE + 
                                   "\n    Random max step: " + network.randomWeightAdditionRange +
                                   "\nMini-batch size: " + miniBatchSize + "\n");
                console.println("Backpropagation: " + (backpropagationAllowed? "ON " : "OFF") + "\nRandom walk: " + (randomWalkAllowed? "ON" : "OFF"));

                console.println("\nNumber of backpropagation SGD cycles: " + numberOfBackpropCycles);
                console.println("Number of random fantasizing cycles: " + numberOfRandomWalkCycles);


                console.println("\nBackpropagation SGD took: " + timeSpentOnBackprop + " ms");
                console.println("Random walk took: " + timeSpentOnRandomWalk + " ms");
                console.println("Error calculation took:" + timeSpentOnErrorCalculation + " ms");

                for (int i=0; i<=10; i++) {
                    
                }

                int n = 212;
                network.feedForward(mnistReader.trainImagesArr[n]);

                console.println(Arrays.toString(network.output));
                console.println(ArtiomArrayUtils.maxIndexInArray(network.output));
                console.println("Digit is known to be " + mnistReader.trainingDigit[n]);




            }



            System.out.println("Bye! See you soon!");
            System.exit(0);



       } /* Выше - если не было ошибки с открытием базы данных с цифрами MNIST */

       catch (IOException e) {
           System.out.println("Error opening MNIST data base! Exiting.");
       }
    }

}

