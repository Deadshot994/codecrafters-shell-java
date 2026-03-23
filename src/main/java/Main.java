import java.io.*;
import java.util.*;

public class Main {
    public static void main(String[] args) throws Exception {

        Scanner scanner = new Scanner(System.in);

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
                    System.out.println(arg + " is a shell builtin");
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

    public static String type(String command) {
        String commands[] = {"exit", "echo", "type"};
        String path_commands = System.getenv("PATH");
        String path_command[] = path_commands.split(":");

        boolean isTrue = false;

        for(int i = 0; i<commands.length; i++) {
            if(Objects.equals(commands[i], command)) {
                return command + " is a shell builtin";
            }
        }

        for(int i = 0; i<path_command.length; i++) {
            File file = new File(path_command[i], command);
            if(file.exists() && file.canExecute()) {
                return command + " is " + file.getAbsolutePath();
            }
        }

        return command + ": not found";
    }
}
