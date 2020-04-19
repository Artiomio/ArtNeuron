class NewTest extends NetworkOne{
    
    NewTest(int a, int[] b) {
        super(a,b);
    }
    
    protected double activation(double arg) {
         System.out.println("*");
         return arg;
    }

    public static void main(String[] args) {
        int[] layers = {3, 10, 10, 1};
        double[] inputs = {1.2, 34, 5};
        NetworkOne network = new NewTest(3, layers);
        network.InitializeWeightsWithRandomNumbers();
   
        long startTime = System.currentTimeMillis();               
        for (int i=0; i<1000000; i++){
            network.feedForward(inputs);
        }
        System.out.println(System.currentTimeMillis() - startTime);
    }     

}