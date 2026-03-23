import java.io.*;
import java.util.*;

public class Main {
    public static void main(String[] args) throws Exception {
        Scanner scanner = new Scanner(System.in);

        boolean xyz = true;
        while (xyz) {
            System.out.print("$ ");
            String input = scanner.nextLine();

            String words[] = input.split(" ");
            String command = words[0];
            String rest[] = Arrays.copyOfRange(words, 1, words.length);
            String result = String.join(" ", rest);

            if (Objects.equals(command, "exit")) {
                xyz = false;
            }
            else if (Objects.equals(command, "echo")) {
                System.out.println(result);
            }
            else if (command.equals("type")) {
                System.out.println(type(result));
            }
            else if (command.equals("pwd")) {
                System.out.println(System.getProperty("user.dir"));
            }
            else {
                String path_commands = System.getenv("PATH");
                String[] path_command = path_commands.split(File.pathSeparator);

                boolean found = false;

                for(int i = 0; i<path_command.length; i++) {
                    File file = new File(path_command[i], command);

                    if(file.exists() && file.isFile() && file.canExecute()) {
                        try {
                            List<String> cmd = new ArrayList<>();
                            cmd.add(command);

                            //add Args
                            for(int j = 0; j<rest.length; j++) {
                                cmd.add(rest[j]);
                            }

                            ProcessBuilder pb = new ProcessBuilder(cmd);
                            pb.directory(new File(path_command[i]));
                            pb.inheritIO();

                            Process p = pb.start();
                            p.waitFor();

                            found = true;
                            break;
                        }
                        catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
                if (!found) {
                    System.out.println(command + ": command not found");
                }
            }
        }

        scanner.close();
    }

    public static String type(String command) {
        String commands[] = {"exit", "echo", "type", "pwd"};
        String path_commands = System.getenv("PATH");
        String path_command[] = path_commands.split(File.pathSeparator);;

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
