class NetworkOne {

    double[][][] weight;
    double[][] charge;
    //double[] input;

    private int numberOfLayers;
    private int numberOfInputs;
    private int numberOfOutputNeurons;

    
    /**
     * Заполнение массива весов случайными числами
     * @param numberOfInputs - количество входов
     * @param numberOfNeuronsInEachLayer - массив с количеством нейронов в каждом слое
     */
    NetworkOne(int numberOfInputs,
               int[] numberOfNeuronsInEachLayer) {
            
        this.numberOfLayers = numberOfNeuronsInEachLayer.length;
        this.numberOfInputs = numberOfInputs;
        this.numberOfOutputNeurons = numberOfNeuronsInEachLayer[numberOfNeuronsInEachLayer.length - 1];
        System.out.println("Number of neurons in the output layer: " + this.numberOfOutputNeurons);

        /* Сначала создаём массив выходных контактов с входами первого слоя */
        //input = new double[numberOfInputs];
        


 
        /* Теперь создаём массив выходов нейронов - там будут храниться "выходные заряды"
           Т.к. кол-во нейронов разное в разных слоях - делаем это в цикле:
           После этого, чтобы получить выходной заряд на i-м нейроне
           из j-го слоя, нужно обратиться к элементу charge[j][i].
           
           При этом, если последний - выходной нейрон - один, то это должно
           быть отражено в массиве.                               
           */
        
        charge = new double[numberOfLayers][];
        for (int i = 0; i < numberOfLayers; i++) {
            charge[i] = new double[numberOfNeuronsInEachLayer[i]];    
        }


        /* Теперь каждому нейрону создаём массив весов
           Сначала создаем элементы первое измерение - индекс слоёв.
        */
        weight = new double[numberOfLayers][][];


        /*   Во избежание путаницы - массив весов для первого слоя (связь со входами) -
           инициализируем отдельно.
        */
        
        int numberOfNeuronsInTheFirstLayer = numberOfNeuronsInEachLayer[0];
        weight[0] = new double[numberOfNeuronsInTheFirstLayer][];
        for (int currentNeuron = 0; currentNeuron < numberOfNeuronsInTheFirstLayer; currentNeuron++) {
            /* У каждого нейрона в первом слое количество весов равно количеству входов.
               При этом считаем, что первый вес - это связь с первым входом, второй - со втором и т.д. */
            weight[0][currentNeuron] = new double[numberOfInputs];
        } 


       
        /* Теперь переходим ко второму слою, который уже типичен, в отличие от первого, каждый нейрон которого
           был связан не с нейроном, а со входом.
           Теперь у каждого нейрона количество весов (и входов) равно числу нейронов в предыдущем слое. 
        */        
        for (int currentLayer = 1; currentLayer < numberOfLayers; currentLayer++) {
            /* Каждый weight[currentLayer] - это двумерный массив, который состоит из нескольких одномерных.
               В нашем случае кол-во таких одномерных массивов - numberOfNeuronsInEachLayer[currentLayer]
               При этом кол-во элементов в них уже всегда одинаковое - numberOfNeuronsInEachLayer[currentLayer - 1]
            */
            weight[currentLayer] = new double[numberOfNeuronsInEachLayer[currentLayer]][];

            for(int currentNeuron = 0; currentNeuron < numberOfNeuronsInEachLayer[currentLayer]; currentNeuron++) {
                weight[currentLayer][currentNeuron] = new double[numberOfNeuronsInEachLayer[currentLayer - 1]];

            }
        }
        /* Теперь чтобы обратиться к весу с номером x - нейрона с номером y - из слоя с номером z - нужно
           обратиться к элементу weight[z][y][x]        
        */

    }


    /**
     * Заполнение массива весов случайными числами
     */
    void InitializeWeightsWithRandomNumbers() {
        for (int currentLayer = 0; currentLayer < weight.length; currentLayer++) {
            for (int currentNeuron = 0; currentNeuron < weight[currentLayer].length; currentNeuron++){
                for (int currentWeight = 0; currentWeight < weight[currentLayer][currentNeuron].length; currentWeight++) {
                    weight[currentLayer][currentNeuron][currentWeight] = Math.random();
                }
            }
        }


    }

    
    void feedForward(double[] inputValue) throws ArrayIndexOutOfBoundsException {

        /* Проверяем соответствие размерностей */
        if (numberOfInputs != inputValue.length){
            System.err.println("Количество входных датчиков не соответствует количеству переданных данных");        
            throw new ArrayIndexOutOfBoundsException();
        }

        /* Чтобы избежать неоднозначностей, сначала вычислим заряды на первом слое нейронов
        */
        
        for (int currentNeuron = 0; currentNeuron < weight[0].length; currentNeuron++){
            double s = 0;
            for (int currentWeight = 0; currentWeight < weight[0][currentNeuron].length; currentWeight++){
                s = s + weight[0][currentNeuron][currentWeight] * inputValue[currentWeight];
            }
            charge[0][currentNeuron] = activation(s);                          
        }

        /* Теперь - остальное */
        for (int currentLayer = 1; currentLayer < weight.length; currentLayer++){
            for (int currentNeuron = 0; currentNeuron < weight[currentLayer].length; currentNeuron++){
                double s = 0;
                for (int currentWeight = 0; currentWeight < weight[currentLayer][currentNeuron].length; currentWeight++){
                    s = s + weight[currentLayer][currentNeuron][currentWeight] * charge[currentLayer - 1][currentWeight];
                }
                charge[currentLayer][currentNeuron] = activation(s);            
            }
        }
    }

    protected double activation(double arg) {
//        return arg;
//        System.out.printf("Calculating sin at %f\n ", arg);
//        System.out.println(arg);
        return Math.sin(arg);
    
    }
 
    
       
   
    
    /* Возвращает среднеквадратичную ошибку предсказаний нейронной сети относительно
       набора переданных данных. 
       Первый параметр - передаются массив массивов входных данных:
         - первый индекс первого параметра - это номер образца.
         - второй индекс первого параметра - номер "входного контакта" (e.g. 0 и 1, если входные - только x и y)
         
       Второй параметр:
         - первый индекс - номер образца
         - второй индекс - номер выходного нейрона, желаемый заряд которого и передаётся в массиве

           
    */
    public double getMeanSquareError(double[][] inputs, double[][] desiredOutputs) throws ArrayIndexOutOfBoundsException {
    
        /* Удостоверяемся в совпадении количества входных образцов и выходных эталонных данных */
        if (inputs.length != desiredOutputs.length){
            System.err.println("Количество входных и выходных образцов не совпадает");
            throw new ArrayIndexOutOfBoundsException();
        }
 
        /* Удостоверяемся в том, что каждый образец содержит данные именно для необходимого числа "входных контактов" */ 
        for (int i=0; i < inputs.length ; i++){
            if (inputs[i].length != numberOfInputs){
                System.err.println("Количество входных данных в образце с номером " + i + " не совпадает с количеством входных контактов нейронной сети");
                throw new ArrayIndexOutOfBoundsException();
            }
        }
    
        /* Удостоверяемся в том, что каждый эталон содержит данные именно для необходимого числа "вЫходных контактов" */ 
        for (int i=0; i < inputs.length ; i++){
            if (desiredOutputs[i].length != numberOfOutputNeurons){
                System.err.println("Количество выходных данных в образце с номером " + i + " не совпадает с количеством вЫходных контактов нейронной сети");
                throw new ArrayIndexOutOfBoundsException();
            }
        }
       
        /* Здесь заканчиваются проверки размеров массивов */
       
        double sumError = 0;
        double sampleSumError;

        for (int currentSample = 0; currentSample < inputs.length; currentSample++){
            /* Сначала прогоняем входные данные через нейронную сеть, после чего */
            /* результат окажется на выходном слое нейронов */
            feedForward(inputs[currentSample]);

            /* Суммируем */
            sampleSumError = 0;
            for (int currentNeuron = 0; currentNeuron < numberOfOutputNeurons; currentNeuron++){
                System.out.println("( "+ (numberOfLayers-1) + "," + currentNeuron+ ")" +charge[numberOfLayers-1][currentNeuron]);
                sampleSumError = sampleSumError + 
                                  (charge[numberOfLayers-1][currentNeuron]-desiredOutputs[currentSample][currentNeuron]) * 
                                  (charge[numberOfLayers-1][currentNeuron]-desiredOutputs[currentSample][currentNeuron]);
            }                         
 
            /* Усреднённая ошибка для образца с номером currentSample */
            sumError = sumError + 1 * sampleSumError / numberOfOutputNeurons;
        }
 
        /* Делим на количество образцов */ 
        sumError = sumError / inputs.length;

        System.out.println("Mean Square Error = " + sumError);

        return sumError;
  
    }
    
    
    
    public static void main(String[] args){
        int[] layers = {2, 3, 1};
        double[] inputs = {1f, 2f, 3f};
        NetworkOne network = new NetworkOne(3, layers);
//        network.InitializeWeightsWithRandomNumbers();

        network.weight[0][0][0] = 7f;
        network.weight[0][0][1] = 5f;        
        network.weight[0][0][2] = 11f;                
        

        network.weight[0][1][0] = 3f;
        network.weight[0][1][1] = 4f;        
        network.weight[0][1][2] = 19f;                
        



        network.weight[1][0][0] = 100f;
        network.weight[1][0][1] = 10f;        

        network.weight[1][1][0] = 0.1;
        network.weight[1][1][1] = 0.3;        

        network.weight[1][2][0] = 5f;
        network.weight[1][2][1] = 7f;        

 
        network.weight[2][0][0] = 3f;
        network.weight[2][0][1] = 5f;
        network.weight[2][0][2] = 7f;        
 
        
        
        

//        System.out.println("Заряд нейрона " + network.charge[4][0]);
        
        long startTime = System.currentTimeMillis();               
        System.out.println("Starting feedforward");
        for (int i=0; i<1000000; i++){
            network.feedForward(inputs);
        }
        System.out.println(" **** Feedforward took " + (System.currentTimeMillis() - startTime) + " milliseconds");        
        System.out.println("The output neuron has charge: " + network.charge[2][0]);




        double[][] desiredOutputs = {{3,6},{3.14,6.28},{0, 0}};
        double[][] sampleInputs = {{1,2,3}, {3,4,5}, {5,6,7}};           

//        System.out.println("Mean error = " + network.getMeanSquareError(sampleInputs, desiredOutputs));



     
    }
}