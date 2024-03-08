import java.util.Stack;

public class Calculation extends InfixToPostfix {
    public Calculation(String infix) {
        super(infix);
    }

    /*
    * pushes values into stack so method calculation() has values to work with
    *
    * returns: the result of the mathematical expression
    */
    public double result() {
        String postfix = postfix();
        String[] detectedOperands = arrayCleanUp(infix.split("[+*/()^\\s-]")); //array of values
        Stack<Double> operands = new Stack<>(); // values we are going to push if they are detected in postfix
         /*
         int offset = 0;
         Problem: everytime operator is detected it's going to skip a value
         Solution: offsets the reading of values
        */
        int offset = 0;
        for (int i = 0; i < postfix.length(); i++) {
            if (String.valueOf(postfix.charAt(i)).matches("[0-9]")) {

                operands.push(Double.parseDouble(detectedOperands[i - offset]));
                /*
                this if statement is for numbers that has more digits than one
                makes an offset based on the length of the number
                 */
                if (detectedOperands[i - offset].length() > 1) {
                    int i1 = i;
                    i = i + detectedOperands[i - offset].length() - 1; //offsets reading of the postfix
                    offset = offset + detectedOperands[i1 - offset].length() - 1; //offsets the array values
                }

            } else {
                offset++;
                operands.push(calculation(String.valueOf(postfix.charAt(i)), operands));
            }
        }
        return operands.pop(); // result of the whole expression
    }
    /*
    *this method takes the infix and takes all values needed for evaluation
    * using String method split
    * however there are sometime blank spaces between values because of 2 operators are next to each other for example 2*(5+4)
    *
    * parameters: String method split
    * returns: array of values with no blank spaces or functions
     */

    private static String[] arrayCleanUp(String[] elements) {
        String[] detectedOperands = new String[elements.length];
        for (int i = 0, j = 0; i < elements.length; i++) {
            switch (elements[i]) {
                case "sin", "", "sqrt", "log", "tan", "cos", "exp":
                    break;
                default:
                    detectedOperands[j++] = elements[i];
                    break;
            }
        }
        return detectedOperands;
    }


    /*
    *  this method takes values from the operand stack and uses whatever operator is inputted
    * parameters:operator used from postfix, Stack of operands
    * returns: double pushed into a stack
    * */
    private double calculation(String operator, Stack<Double> operands) {
        double result = 0D;
        if (operator.matches("[cstlqe]")) {
            //switch case to find what function is used
            switch (operator) {
                case "s":
                    result = Math.sin(operands.pop());
                    break;
                case "q": // exception for sqrt is that it cannot have numbers smaller than 0
                    if (operands.peek() > 0){
                        result = Math.sqrt(operands.pop());
                    break;
                }else {
                    throw new ArithmeticException("argument of a sqrt cannot be less then 0");
                }
                case "e":
                    result = Math.exp(operands.pop());
                    break;
                case "t":
                    result = Math.tan(operands.pop());
                    break;
                case "c":
                    result = Math.cos(operands.pop());
                    break;
                case "l":
                    if (operands.peek() > 0){  // exception for log is that it cannot have numbers smaller than 1
                    result = Math.log10(operands.pop());
                    break;
            }else {
                        throw new ArithmeticException("argument of a log cannot be less then 1");
                    }
            }
        }
        //minus at the start of the expression is handled here
        else if (operands.size() == 1) {
            if (operator.matches("[+-]")){
            if (operator.equals("-")) {
                result = -operands.pop();
            } else {
                result = operands.pop();
            }
        } else {
                throw new ArithmeticException("invalid argument");
            }
        } else {
            double operand2;
            double operand1;
            if (operands.size()>=2) {
                 operand2 = operands.pop();
                 operand1 = operands.pop();
            }else {
                throw new IllegalArgumentException("unexpected chars or invalid expression");
            }
             switch (operator) {
                 case "+" : result = operand1 + operand2; break;
                 case "-" : result =operand1 - operand2; break;
                 case "*" : result =operand1 * operand2;break;
                 case "/" : {
                    if (operand2 == 0) {
                        throw new ArithmeticException("you cant divide " + operand1 + " by 0");
                    }
                     result = operand1 / operand2;
                } break;

                 case "^" : result = Math.pow(operand1, operand2);

            }
        }
        return result;
    }
}
