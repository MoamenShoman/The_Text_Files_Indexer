import java.io.*;
import java.util.*;

public class The_Text_File_Indexer {
    private static HashMap<String, HashSet<Integer>> map;
    private static File[] files;
    private static HashSet<Integer> indexed;
    private static HashSet<String> specialChar;

    public static void index(File file, int i, String splitter) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(file));
        String line;
        StringBuilder sb = new StringBuilder();

        if (splitter.equals(" "))
            while ((line = br.readLine()) != null) {
                sb.append(line.toLowerCase() + " ");
            }
        else
            while ((line = br.readLine()) != null) {
                sb.append(line.toLowerCase());
            }

        makeSpecialCharSet();

        if (splitter.equals(" "))
            splitter = "\\s+";
        else if (specialChar.contains(splitter))
            splitter = "\\" + splitter;
        else splitter = " " + splitter;

        String[] arr = (sb.toString()).split(splitter.toLowerCase());
        for (String s : arr) {
            HashSet<Integer> temp;
            if (map.containsKey(s))
                temp = map.get(s);
            else
                temp = new HashSet<>();

            temp.add(i);
            map.put(s, temp);
        }
        indexed.add(i);
    }

    public static void showAllIndexed() throws InterruptedException {
        if (files.length == 0)
            System.out.println("There are no indexed text files.");
        else {
            System.out.println("The indexed file(s):");
            for (int i : indexed)
                System.out.println(files[i].getName() + "   The path is (" + files[i].getPath() + ")");
        }
        System.out.println("----------------------------------------------");
        optionsLines();
    }

    public static void makeSpecialCharSet() {
        specialChar = new HashSet<>();
        specialChar.add(".");
        specialChar.add("^");
        specialChar.add("$");
        specialChar.add("?");
        specialChar.add("*");
        specialChar.add("(");
        specialChar.add(")");
        specialChar.add("{");
        specialChar.add("}");
        specialChar.add("[");
        specialChar.add("]");
        specialChar.add(".");
        specialChar.add("+");
        specialChar.add("|");
        specialChar.add("=");
        specialChar.add("-");
        specialChar.add("@");
        specialChar.add("&");
        specialChar.add("#");
        specialChar.add(";");
        specialChar.add("\\".charAt(0) + "");
    }

    public static void getFile(String word) throws InterruptedException {
        HashSet<Integer> set = map.get(word.toLowerCase());
        if (set == null) {
            System.out.println("There is no word " + "'" + word + "'" + " in the files");
        } else {
            System.out.println("The file(s) containing " + "'" + word + "' :");
            for (int i : set) {
                System.out.println(files[i].getName() + "   The path is (" + files[i].getPath() + ")");
            }
        }
        System.out.println("----------------------------------------------");
        optionsLines();
    }

    public static void optionsLines() throws InterruptedException {
        Thread.sleep(1000); //Just for a better experience for the user
        System.out.println();
        System.out.println("To list the already indexed text files enter (l)...");
        System.out.println("To search for a specific word enter (s)...");
        System.out.println("To exit The Text Files Indexer enter (q)...");
    }

    public static void main(String[] args) {
        try {
            System.out.println("Welcome to The Text Files Indexer.\nPlease Enter the path of the " +
                    "directory containing the text files you want indexed...");
            BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
            String dir = br.readLine();
            while (dir.trim().isEmpty())
                dir = br.readLine();
            System.out.println("How would you like your text files splitted?\nto split by a specific word, type the word...\n" +
                    "to split by white spaces, enter a white space...");
            String splitter = br.readLine();
            while (splitter.trim().isEmpty()) {
                if (splitter.equals(" "))
                    break;
                splitter = br.readLine();
            }
            indexed = new HashSet<>();
            map = new HashMap<String, HashSet<Integer>>();
            File f = new File(dir);
            FilenameFilter filter = new FilenameFilter() {
                @Override
                public boolean accept(File f, String name) {
                    return name.endsWith(".txt");
                }
            };
            files = f.listFiles(filter);
            int i = 0;
            for (File file : files) {
                index(file, i++, splitter);
            }
            optionsLines();
            while (true) {
                String input = br.readLine();
                while (input.trim().isEmpty())
                    input = br.readLine().toLowerCase();
                if (input.equals("l")) {
                    showAllIndexed();
                } else if (input.equals("s")) {
                    System.out.println("Please enter the word you want to search for...");
                    String word = br.readLine();
                    while (word.trim().isEmpty())
                        word = br.readLine();
                    getFile(word);
                } else if (input.equals("q")) {
                    System.out.println("Thank you for using The Text Files Indexer.");
                    return;
                }
            }
        } catch (Exception e) {
            System.out.println("Please check the directory given again.");
        }
    }
}
