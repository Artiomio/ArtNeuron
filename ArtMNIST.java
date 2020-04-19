import java.io.IOException;
import static java.awt.event.KeyEvent.*;
import java.util.Arrays;

class ArtMNIST {

    public static void main(String[] args) {

        try {

            /* создаём экземпляр считывателя базы данных цифр MNIST */
            MNISTReader mnistReader = new MNISTReader("../../mnist/data");
            mnistReader.normalize();

            /* теперь создаём экзмепляр нейронную сеть */
            NetworkOne network = new NetworkOne(28 * 28, /* Количество входных нейронов */
                                               new int[] {30, 10} /* Первый и второй (последний) слои */);

            network.initializeWeightsWithNormalRandomNumbers(0.1);

            /* создаём валидатор */
            NeuralBitFlagValidator validator = new NeuralBitFlagValidator(network, mnistReader.testImagesArr, mnistReader.testDigit);
            
            /* устанавливаем набор данных обучения */
            network.setTrainingSet(mnistReader.trainImagesArr, mnistReader.trainBinaryLabels);


            ArtConsole console = new ArtConsole(25, 50);
            int keyCode = 0;


            console.println("Preliminary validation (must be ~10%): " + validator.validate(0, mnistReader.numberOfTestingImages - 1) + " / " + mnistReader.numberOfTestingImages);



            /* инициализируем параметры обучения */
            network.LEARNING_RATE = 30;   /* Хорошо работает набор в 30 нейронов, LR=10, mb=10 */
            int miniBatchSize = 10;
            
            
            network.DELTA_W = 0.01;
            network.randomWeightAdditionRange = 1;

            
            double error = 0;

            boolean backpropagationAllowed = true;
            int numberOfBackpropCycles = 50;

            boolean randomWalkAllowed = false;
            int numberOfRandomWalkCycles = 20;




            network.setCurrentMiniBatchRange(0 , mnistReader.numberOfTrainingImages - 1);
            error = network.getMeanSquareError();
            System.err.println(error);

            int epoch = 0;
            
            /* Сохраняем время запуска программы */
            long programStartTime = System.currentTimeMillis();

            while (true) {
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


                    if (keyCode == VK_S) { /* Сохраняем веса */
                        ArtiomArrayUtils.serialize3DArray(network.weight, "neural_weights_ArtMNIST.serialized");
                    }

                    if (keyCode == VK_L) { /* Восстанавливаем веса */
                        network.weight = ArtiomArrayUtils.loadSerialized3DArray("neural_weights_ArtMNIST.serialized");
                    }




                    if (keyCode == VK_P) {
                        validator.validateAndPrintTemp(0, mnistReader.numberOfTestingImages - 1);
                    }




                } /* Обработка клавиш */



                epoch++;
                long cycleStartTime = System.currentTimeMillis();

                for (int i=0; i + miniBatchSize - 1 <= mnistReader.numberOfTrainingImages - 1; i += miniBatchSize) {

                    /* Устанавливаем границы мини-пакета в цикле по тренировочным данным */
                    network.setCurrentMiniBatchRange(i, i + miniBatchSize - 1);

                    /* Минимизируем функцию ошибки на данном мини-пакете */
                    network.teachWithBackpropagation(1);

                    if (randomWalkAllowed) network.teachByShakingWeights(1);

                    if (i % 5000 == 0) {
                        console.print("▒");
                    }



                  
                    
                }



                /* Вычисляем ошибку на всём тренировочном наборе (можно заменить на случайный промежуток впоследствии) */
                network.setCurrentMiniBatchRange(0 , mnistReader.numberOfTrainingImages - 1);

                error = network.getMeanSquareError();

                console.clearScreen();

                /* Печать ошибки, количество успешных распознаваний и модуль градиента */
                System.err.println(error);
                console.println("\nCost function: " + error);
                console.println("Validation: " + validator.validate(0, mnistReader.numberOfTestingImages - 1) + " / " + mnistReader.numberOfTestingImages + "\n");
                console.println("Cost gradient absolude value: " + ArtiomArrayUtils.abs(network.costGradient));

                console.println("Backpropagation: " + (backpropagationAllowed? "ON " : "OFF") + "\nRandom walk: " + (randomWalkAllowed? "ON" : "OFF"));
                console.println("Training with:\n    Learning rate: " + network.LEARNING_RATE + 
                                "\n    Random max step: " + network.randomWeightAdditionRange +
                                "\nMini-batch size: " + miniBatchSize + "\n");
            
                
                console.println("Time spent on the last training cycle: " + (System.currentTimeMillis() - cycleStartTime) + " ms");
                console.println("Time elapsed: " + (System.currentTimeMillis() - programStartTime) / 1000 + " seconds");
                console.println("Epoch number: " + epoch);
                
                
            }

            /*
            System.out.println("Bye! See you soon!");
            System.exit(0);
            */



       } /* Выше - если не было ошибки с открытием базы данных с цифрами MNIST */

       catch (IOException e) {
           System.out.println("Error opening MNIST data base! Exiting.");
       }
    }

}

