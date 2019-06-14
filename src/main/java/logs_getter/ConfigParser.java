package logs_getter;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ConfigParser {

    public String findConfigFile(String confFilePath, String fileName) throws Exception {
        List<String> configFile = Files.find(Paths.get(confFilePath),
                Integer.MAX_VALUE,
                (filePath, fileAttr) -> fileAttr.isRegularFile())
                .filter((filePath) -> filePath.endsWith(fileName))
                .map(Path::toString)
                .collect(Collectors.toList());
        if (configFile.size() == 0) {
            throw new Exception("get list of file = 0. No config files found");
        }
        if (configFile.size() > 1) {
            throw new Exception("get list of file > 1. To much files find");
        }
        return configFile.get(0);
    }

    public List<String> getContextFile(String confFilePath) {
        try (Stream<String> stream = Files.lines(Paths.get(confFilePath))) {
            return stream.collect(Collectors.toList());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<String> readLines(String confFilePath) throws IOException {
        int c;
        char ch;
        List<StringBuffer> textByLine = new ArrayList<>();
        StringBuffer stringBuffer = new StringBuffer();

        try (FileReader fileReader = new FileReader(confFilePath)) {
            while ((c = fileReader.read()) != -1) {
                ch = (char) c;
                if (ch == '\n') {
                    textByLine.add(stringBuffer);
                    stringBuffer = new StringBuffer();
                } else {
                    stringBuffer.append(ch);
                }
            }
        }
        return Arrays.asList(textByLine.toString().split(","));
    }

    public LinkedHashMap<String, List<String>> createMapRoleIp(List<String> configList) {
        Pattern pattern = Pattern.compile("(?:\\[((?:[a-zA-Z0-9]|[-])*)\\])");
        Pattern patternIp = Pattern.compile("(\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}\\.\\d{1,3})");
        Matcher matcher;
        String tag = "";
        LinkedHashMap<String, List<String>> configMap = new LinkedHashMap<>();
        List<String> listIP = new ArrayList<>();

        for (String s : configList) {
            matcher = pattern.matcher(s);
            if (matcher.find()) {
                if (listIP.size() > 0) {
                    configMap.put(tag, listIP);
                    listIP = new ArrayList<>();
                }
                tag = matcher.group(1);
                continue;
            }
            matcher = patternIp.matcher(s);
            if (matcher.find()) {
                listIP.add(matcher.group(1));
            }
        }
        return configMap;
    }
}

//        for (Map.Entry<String, List<String>> entry : configMap.entrySet()) {
//        System.out.println("Tag = " + entry.getKey()+ ": ");
//        for (String ip:entry.getValue()) {
//        System.out.println(ip);
//        }
//        }