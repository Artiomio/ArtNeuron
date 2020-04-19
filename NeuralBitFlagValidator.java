/** 
 * Класс для валидации работы ванильной сети, выходные нейроны которой являются битами-флагами
 * определения одного из классов, например цифр.
 * Uдеальными при этом считаются выходы вида [0  0  1  0  0  0  0   0   0   0]
 * E.g. выходные нейроны: [0  0.1  0.8  0.3  -1  0.5  10  0  0  0] соответствуют 2, так как 0.8 ближе всех к единице
 *
 */
class NeuralBitFlagValidator {
    NetworkOne network;
    double[][] validationInputs;
    int[] desiredNeuronSet;
    

    public NeuralBitFlagValidator(NetworkOne network, double[][] validationInputs, int[] desiredNeuronSet) {
        
        if (validationInputs.length != desiredNeuronSet.length) {
            System.out.println("Error in validator constructor: validationInputs and desiredNeuronSet have different lengths!");
            throw new ArrayIndexOutOfBoundsException();
        }

        this.network = network;
        this.validationInputs = validationInputs;
        this.desiredNeuronSet = desiredNeuronSet;
    }

    public int validate(int startPos, int endPos) {
        int positives = 0;

        for (int i=startPos; i <= endPos; i++) {
            network.feedForward(validationInputs[i]);
            int closestToOne = ArtiomArrayUtils.closestToOneIndex(network.output);
            if (closestToOne == desiredNeuronSet[i]) {
                positives++;
            }

            
        }
        return positives;
    }


    public int validateAndPrintTemp(int startPos, int endPos) {
        int positives = 0;

        for (int i=startPos; i <= endPos; i++) {
            network.feedForward(validationInputs[i]);
            int closestToOne = ArtiomArrayUtils.closestToOneIndex(network.output);
            if (closestToOne == desiredNeuronSet[i]) {
                positives++;
            } else
            System.out.print(" " + i);

            
        }
        return positives;
    }


}