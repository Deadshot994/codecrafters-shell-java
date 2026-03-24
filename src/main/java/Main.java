import java.io.*;
import java.util.*;

public class Main {
    public static void main(String[] args) throws Exception {
        Scanner scanner = new Scanner(System.in);

        boolean xyz = true;
        String currentDir = System.getProperty("user.dir");

        while (xyz) {
            System.out.print("$ ");
            String input = scanner.nextLine();

            List<String> tokens = parseInput(input);

            if (tokens.size() == 0) continue;

            String command = tokens.get(0);
            List<String> restList = tokens.subList(1, tokens.size());
            String result = String.join(" ", restList);

            if (Objects.equals(command, "exit")) {
                xyz = false;
            }
            else if (Objects.equals(command, "echo")) {
                System.out.println(result);
            }
            else if (command.equals("type")) {
                System.out.println(type(result));
            }
            else if (command.equals("cd")) {
                File dir;

                if(result.equals("~")) {
                    String home = System.getenv("HOME");
                    dir = new File(home);
                }
                else if(result.startsWith("/")) {
                    // absolute path
                    dir = new File(result);
                }
                else {
                    // Relative Path
                    dir = new File(currentDir, result);
                }
                try {
                    File canonical = dir.getCanonicalFile();

                    if(canonical.exists() && canonical.isDirectory()) {
                        currentDir = canonical.getAbsolutePath();
                    }
                    else {
                        System.out.println("cd: " + result + ": No such file or directory");
                    }
                }
                catch (IOException e) {
                    System.out.println("cd: " + result + ": No such file or directory");
                }
            }
            else if (command.equals("pwd")) {
                System.out.println(currentDir);
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
                            for (String arg : restList) {
                                cmd.add(arg);
                            }

                            ProcessBuilder pb = new ProcessBuilder(cmd);
                            pb.directory(new File(currentDir));
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

    public static List<String> parseInput(String input) {
        List<String> result = new ArrayList<>();
        StringBuilder current = new StringBuilder();
        boolean inQuotes = false;

        for(int i = 0; i<input.length(); i++) {
            char c = input.charAt(i);

            if(c == '\'') {
                inQuotes = !inQuotes;
            }
            else if(c == ' ' && !inQuotes){
                if(current.length()>0) {
                    result.add(current.toString());
                    current.setLength(0);
                }
            }
            else {
                current.append(c);
            }
        }
        if (current.length() > 0) {
            result.add(current.toString());
        }
        return result;
    }
}
