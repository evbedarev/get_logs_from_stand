import logs_getter.ConfigParser;

import java.util.List;

public class TestFindConfigFile {
    public static void main(String[] args) throws Exception {
        ConfigParser configParser = new ConfigParser();
        String fileConf = configParser.findConfigFile("/home/mj/fx.pricing","hosts.st1");
       List<String> array = configParser.readLines(fileConf);
//        for (String s : array) {
//            System.out.println(s);
//        }
        configParser.createMapRoleIp(array);
    }
}
