import javax.swing.*;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

import static java.nio.file.StandardOpenOption.CREATE;

public class TagExtractor {
    public static void main(String[] args) {
        Map<String, Integer> freq = new TreeMap<>();
        Set<String> stopWords = new TreeSet<>();
        StringTokenizer token;
        String rec = "";
        String workDirect = "";
        String keyWord = "";
        JFileChooser fileChooser = new JFileChooser();
        File selectedFile;

        int line = 0;
        int curFreq = 0;
        try {
            File workingDirectory = new File(System.getProperty("user.dir"));
            workDirect = workingDirectory.getPath() + "\\src\\English_Stop_Words.txt";

            Path file = Path.of(workDirect);
            InputStream in = new BufferedInputStream(Files.newInputStream(file, CREATE));
            BufferedReader reader = new BufferedReader(new InputStreamReader(in));

            //line = 0;
            while (reader.ready()) {
                rec = reader.readLine();
                line++;
                stopWords.add(rec.toUpperCase());
            }
            reader.close();
            System.out.println("\n\nStop Words Data file Read! " + line + " words");

            if (fileChooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION)
            {
                selectedFile = fileChooser.getSelectedFile();
                file = selectedFile.toPath();

                in = new BufferedInputStream(Files.newInputStream(file, CREATE));
                reader = new BufferedReader(new InputStreamReader(in));

                line = 0;
                while(reader.ready())
                {
                    rec = reader.readLine();
                    line++;
                    rec = rec.replaceAll("\\W", " ");
                    System.out.println("Debug rec: " + rec);

                    token = new StringTokenizer(rec);

                    while(token.hasMoreElements())
                    {
                        keyWord = token.nextToken();
                        if(!stopWords.contains(keyWord.toUpperCase()))
                        {
                            if(freq.containsKey(keyWord.toUpperCase()))
                            {
                                curFreq = freq.get(keyWord.toUpperCase());
                                curFreq++;
                                freq.put(keyWord.toUpperCase(), curFreq);
                                System.out.println("Updated Key Word: " + keyWord);
                            }
                            else
                            {
                                freq.put(keyWord.toUpperCase(), 1);
                                System.out.println("Added Key Word: " + keyWord);
                            }
                        }
                        else
                        {
                            System.out.println("Stop word:\t" + keyWord);
                        }
                    }
                }
                reader.close();
                System.out.println("\n\nFrequency computed.");

                for (Map.Entry<String, Integer> entry : freq.entrySet())
                {
                    System.out.printf("%-30s%5d\n", entry.getKey(), entry.getValue());
                }
            }
            else
            {
                System.out.println("No file selected. Run again and select the correct file.");
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
