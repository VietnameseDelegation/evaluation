import java.util.EmptyStackException;
import java.util.Scanner;
public class Main {
    public static void main(String[] args) {
        System.out.println("""
                WELCOME TO MY (probably) WORKING PROGRAM
                what works?: Functions (exp(),log(),sin(),cos(),tan(),sqrt()), decimal numbers
                multiplications(*), divisions(/),addition(+),subtraction(-), parenthesis(()),exponents(^)
                only numbers work no letters
                example: sqrt(exp(sin(0.8) + cos(1.2))) * log(tan(0.5)) - 9 * 3 / sqrt(729)
                have fun and please dont break anything :)
                """);
        while (true) {
            Scanner sc = new Scanner(System.in);
            try {
                System.out.println("write expression(write exit to exit)");
                String s = sc.nextLine();
                Calculation ex = new Calculation(s);
                if (s.equalsIgnoreCase("exit")) {
                    break;
                }
                System.out.println(s + " = " + ex.result()+"\n");
            } catch (ArithmeticException| IllegalArgumentException e) {
                System.out.println(e.getMessage());
            } catch (EmptyStackException | StringIndexOutOfBoundsException ex) {
                System.out.println("invalid format");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}