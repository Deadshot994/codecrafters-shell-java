import java.sql.SQLOutput;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

public class Main {
    public static void main(String[] args) throws Exception {

        Scanner scanner = new Scanner(System.in);

        //Built-in commands
        Set<String> builtins = new HashSet<>(Arrays.asList("echo", "exit", "type"));

        while(true) {
            System.out.print("$ ");
            String input = scanner.nextLine();

            if(input.equals("exit")) {
                break;
            }
            else if(input.startsWith("echo")) {
                System.out.println(input.substring(5));
            }
            else if(input.startsWith("type")){
                String[] parts = input.split(" ");
                if(parts.length < 2) continue;

                String arg = parts[1];

                if(arg.equals("echo") || arg.equals("exit") || arg.equals("type")) {
                    System.out.println(arg + " command is a shell builtin");
                }
                else {
                    System.out.println(arg + ": not found");
                }
            }
            else {
                System.out.println(input + ": command not found");
            }
        }




    }
}
