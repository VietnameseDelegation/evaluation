import java.util.Stack;

public class InfixToPostfix {
    protected final String infix; // user input

    protected InfixToPostfix(String infix) {
        this.infix = infix.replaceAll("\\s", "").toLowerCase(); //deletes all whitespaces and makes everything lowercase
    }

    /*
     * infix to postfix converter
     * returns: postfix
     */
    protected final String postfix() {
        StringBuilder postfix = new StringBuilder();
        Stack<Character> operators = new Stack<>();
        for (int i = 0; i < infix.length(); i++) {
            if (String.valueOf(infix.charAt(i)).matches("[a-zA-Z]")) {
                functionCheck(i); //check if letters are actually function or not
            }
            if (String.valueOf(infix.charAt(i)).matches("[0-9]|[.]")) { //if it's a number then put into the string builder
                postfix.append(infix.charAt(i));
            } else { // this branch is for operators
                if (operators.isEmpty()) {
                    if (infix.charAt(i) == ')') {
                        throw new IllegalArgumentException("illegal argument");
                    }
                    if (infix.charAt(i + 1) == 'q') { //checks if this is sqrt
                        operators.push(infix.charAt(i + 1));
                        i+=3; //jumping the other letters in the function
                    } else if (String.valueOf(infix.charAt(i)).matches("[sctleq]")){
                        operators.push(infix.charAt(i));
                        i+=2; //jumping the other letters in the function
                    }
                    else {
                        operators.push(infix.charAt(i));
                    }
                } else {
                    boolean beforeCharIsAnOperator = String.valueOf(infix.charAt(i - 1)).matches("[/*^(]");
                    /*operators have their own priority which this switch solves
                     * +,-      |     *,/    |    ^     |    functions, brackets
                     * whenever there is a lower priority operator about to be pushed
                     * then pop everything that is higher priority
                     * */
                    switch (infix.charAt(i)) {
                        case 's':
                            if (infix.charAt(i + 1) == 'q') { //  problem: sin and sqrt start with a same letter solution: makes a difference
                                operators.push(infix.charAt(i + 1));
                                i = i + 3; //offset because sqrt() has t in it and would detect tan()
                            } else {
                                operators.push(infix.charAt(i));
                                i+=2; // jump "in" in sin
                            }
                            break;
                        case 'c', 't', 'l', 'q', 'e':
                            operators.push(infix.charAt(i));
                            i = i + 2; //jumping the other letters in the function
                            break;
                        case '+', '-':
                            plusMinus(operators, i, postfix, beforeCharIsAnOperator);
                            break;
                        case '*', '/':
                            while (!operators.isEmpty() && !String.valueOf(operators.peek()).matches("[()+-]")) {
                                postfix.append(operators.pop()); //pops every thing higher priority that itself meaning functions and ^ (parentheses are higher priority, but they aren't supposed to be in postfix)
                            }
                            operators.push(infix.charAt(i));
                            break;
                        case ')':
                            //pop everything that is inside the bracket
                            while (operators.peek() != '(') {
                                postfix.append(operators.pop());
                            }
                            operators.pop(); //removes the bracket
                            break;
                        case '(':operators.push(infix.charAt(i)); break;
                        case '^':
                            while (!operators.isEmpty() && !String.valueOf(operators.peek()).matches("[()+/*-]")) {
                                postfix.append(operators.pop());
                            }
                            operators.push(infix.charAt(i));
                            break;
                    }
                }
            }
        }
        // pops out all operators still in stack
        while (!operators.isEmpty()) {
            postfix.append(operators.pop());
        }
        return postfix.toString();
    }

    private void plusMinus(Stack<Character> operators, int i, StringBuilder postfix, boolean beforeCharIsAnOperator) {
        //if +- then push - doesn't work for more than 1 sign tho
        if (infix.charAt(i - 1) == '-') {
            operators.pop();
            while (!operators.isEmpty() && operators.peek() != '(') {
                postfix.append(operators.pop());
            }
            if (infix.charAt(i) == '+') {
                operators.push('-');
            } else {
                operators.push('+');
            }
        } else if (beforeCharIsAnOperator) { //situation where you use this is 4*+1
            char c = operators.pop();
            operators.push(infix.charAt(i));
                operators.push(c);
        } else plusAfterSign(postfix, operators, i);
    }

    //check if the functions are actual function, so they don't put any gibberish
    private void functionCheck(int index) {
        switch (infix.charAt(index)) {
            case 'l', 'c':
                if (!(infix.charAt(index + 1) == 'o')) {
                    throw new IllegalArgumentException("illegal argument");
                }
                break;
            case 's':
                if (!(infix.charAt(index + 1) == 'i' || infix.charAt(index + 1) == 'q')) {
                    throw new IllegalArgumentException("illegal argument");
                }
                break;
            case 'e':
                if (!(infix.charAt(index + 1) == 'x')) {
                    throw new IllegalArgumentException("illegal argument");
                }
                break;
            case 't':
                if (!(infix.charAt(index + 1) == 'a')) {
                    throw new IllegalArgumentException("illegal argument");
                }
                break;
            default:
                throw new IllegalArgumentException("illegal argument");
        }
    }

    //method to skip redundant + that would make an error
    private void plusAfterSign(StringBuilder post, Stack<Character> stack, int i) {
        if (infix.charAt(i - 1) == '+') {
            stack.pop();
        }
        while (!stack.isEmpty() && stack.peek() != '(') {
            post.append(stack.pop());
        }
        stack.push(infix.charAt(i));
    }
}
