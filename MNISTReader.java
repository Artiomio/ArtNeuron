import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;

import java.util.Arrays;

public class MNISTReader {
    boolean normalized = false;
    
    final int SIZE = 28; // Размер изображений - 28 x 28

    final int numberOfTrainingImages = 60000;
    final int numberOfTestingImages = 10000;

    /* Два массива с изображениями - каждая точка кодируется числом от 0 до 255 (тип избыточно точный - double) */
    double[][] trainImagesArr = new double[numberOfTrainingImages][SIZE * SIZE];
    double[][] testImagesArr = new double[numberOfTestingImages][SIZE * SIZE];

    /* Два массива с ярлыками. Каждый элемент - это число от 0 до 9 */
    int[] trainingDigit = new int[numberOfTrainingImages];
    int[] testDigit = new int[numberOfTestingImages];
    
    /* Два массива с ярлыками в виде выходных сигналов нейронной сети: */
    /* На i-й позиции каждого ярлыка, соответствующего цифре i стоит 1.*/
    /* e.g. цифре 3 соответствует ярлык {0, 0, 0, 1, 0, 0, 0, 0, 0, 0} */
    double[][] trainBinaryLabels = new double[numberOfTrainingImages][];
    double[][] testingBinaryLabels = new double[numberOfTestingImages][];   

    
    final static String trainingImagesFileName = "train-images.idx3-ubyte";
    final static String trainingLabelsFileName = "train-labels.idx1-ubyte";

    final static String testImagesFileName = "t10k-images.idx3-ubyte";
    final static String testLabelsFileName = "t10k-labels.idx1-ubyte";
   
                        
    static double[][] digitsOut = {{1, 0, 0, 0, 0, 0, 0, 0, 0, 0},  /* 0 */
                                   {0, 1, 0, 0, 0, 0, 0, 0, 0, 0},  /* 1 */
                                   {0, 0, 1, 0, 0, 0, 0, 0, 0, 0},  /* 2 */
                                   {0, 0, 0, 1, 0, 0, 0, 0, 0, 0},  /* 3 */
                                   {0, 0, 0, 0, 1, 0, 0, 0, 0, 0},  /* 4 */
                                   {0, 0, 0, 0, 0, 1, 0, 0, 0, 0},  /* 5 */
                                   {0, 0, 0, 0, 0, 0, 1, 0, 0, 0},  /* 6 */
                                   {0, 0, 0, 0, 0, 0, 0, 1, 0, 0},  /* 7 */
                                   {0, 0, 0, 0, 0, 0, 0, 0, 1, 0},  /* 8 */
                                   {0, 0, 0, 0, 0, 0, 0, 0, 0, 1}}; /* 9 */

    Path trainingImagesPath, trainingLabelsPath,
         testImagesPath, testLabelsPath;


    public MNISTReader(String path) throws IOException {
                
        trainingImagesPath = FileSystems.getDefault().getPath(path, trainingImagesFileName);
        trainingLabelsPath = FileSystems.getDefault().getPath(path, trainingLabelsFileName);

        testImagesPath = FileSystems.getDefault().getPath(path, testImagesFileName);
        testLabelsPath = FileSystems.getDefault().getPath(path, testLabelsFileName);

        byte[] trainingImages = Files.readAllBytes(trainingImagesPath); /* Считали файл с тренировочными изображениями */
        byte[] trainingLabels = Files.readAllBytes(trainingLabelsPath); /* Считали файл с тренировочными ярлыками */


        byte[] testImages = Files.readAllBytes(testImagesPath); /* Считали файл с тестовыми изображениями */
        byte[] testLabels = Files.readAllBytes(testLabelsPath); /* Считали файл с тестовыми ярлыками */

        
        System.out.println("MNIST files loaded successfully");
        
        for (int numberOfImage = 0; numberOfImage < numberOfTrainingImages; numberOfImage++ ) {
            int imageOffset = SIZE * SIZE * numberOfImage;
            for (int i=0; i < SIZE * SIZE; i++) {
                int b = trainingImages[imageOffset + i + 16];
                int unsignedByte = (b >= 0)? b : (256 + b);
                trainImagesArr[numberOfImage][i] = unsignedByte; 
            }
        }

        for (int numberOfImage = 0; numberOfImage < numberOfTestingImages; numberOfImage++ ) {
            int imageOffset = SIZE * SIZE * numberOfImage;
            for (int i=0; i < SIZE * SIZE; i++) {
                int b = testImages[imageOffset + i + 16];
                int unsignedByte = (b >= 0)? b : (256 + b);
                testImagesArr[numberOfImage][i] = unsignedByte; 
            }
        }

        System.out.println("Transformed to the double64 type");

        /* Теперь сохраняем ярлыки - сначала тренировочные */
        for (int i=0; i < numberOfTrainingImages; i++ ) {
            int labelDigit = trainingLabels[i + 8];
            trainingDigit[i] = labelDigit;
            trainBinaryLabels[i] = digitsOut [labelDigit];
        }


        /* Теперь тестовые  */
        for (int i=0; i < numberOfTestingImages; i++ ) {
            int labelDigit = testLabels[i + 8];
            testDigit[i] = labelDigit;
            testingBinaryLabels[i] = digitsOut[labelDigit];
        }



    }

    /**
     * Печатает изображение цифры с заданным номером
     * @param n - номер изображения
     * При этом, в случае, если номер n принадлежит промежутку [0, 59999], т
     * то извлекается изображения из тренировочного набора с номером n,
     * если же n принадлежит промежутку [60000, 6999], то используется
     * изображение номер (n - 60 000) из тестового набора
     */
    public void printTrainingImage(int n) {
        double[] image;
        double[][] BinaryLabels;
        int[] digit;
        if (n < 60000) {
            System.out.println("TRAINING set image #" + n);
            image = trainImagesArr[n];
            BinaryLabels = trainBinaryLabels;
            digit = trainingDigit;
        }
        else {
            System.out.println("TESTING set image #" + n);            
            n = n - numberOfTrainingImages;
            image = testImagesArr[n];
            BinaryLabels = testingBinaryLabels;
            digit = testDigit;
        }

        System.out.println("Bit mask: " + Arrays.toString(BinaryLabels[n]));
        System.out.println("Digit: " + digit[n]);
         
        for (int y=1; y<=28; y++) {
            for (int x=1; x<=28; x++) {
                double pixel = image[x - 1 + 28*(y - 1)];
                if ((!normalized && (pixel > 127)) || (normalized && (pixel > 0.5)))
                   System.out.print("xx");
                else 
                   System.out.print("  ");
            }
            System.out.println("");
        }        

   }



    public void normalize() {
        for (int image=0; image < numberOfTrainingImages; image++) {
            for (int pixel=0; pixel < SIZE * SIZE; pixel++)
                trainImagesArr[image][pixel] /= 255.0;    
        }

        for (int image=0; image < numberOfTestingImages; image++) {
            for (int pixel=0; pixel < SIZE * SIZE; pixel++)
                testImagesArr[image][pixel] /= 255.0;    
        }

        normalized = true;

    }


    
    public static void main(String[] args) {


        MNISTReader reader;            
        try {
            reader = new MNISTReader("../../mnist/data");
            reader.normalize();
            reader.printTrainingImage(21231);

        }
        catch (IOException e) {
            System.out.println("Error reading MNIST files!");
        }

        


    }
  

    
}