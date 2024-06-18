package com.behave.completion;

import com.behave.Helper;
import kotlinx.html.A;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class DictionaryHelper {
    private static final String STEP_REGEX = "'([^']*)'";

    private static String getPath() {
        var workDir = System.getProperty("user.dir");
        var local = "/src/features/steps";
        return (workDir.contains("bin")) ? "C:/Users/Maxim.Karpov/PycharmProjects/Rockworld-test"
                + local : workDir + local;
    }
    public static List<String> getDictionary() {
        var path = getPath();
        List<String> list = new ArrayList<>();
        getSteps(new File(path), list);
        return list.stream().map(DictionaryHelper::getGherkineLine).toList();
    }

    private static String getGherkineLine(final String line) {
        var cleanLine = Helper.getStringWithPattern(line, STEP_REGEX, 1);
        return line.trim().toLowerCase().split("\\(")[0].replace("@", "") + " " + cleanLine;

    }

    private static void getSteps(File dir, List<String> lines) {
        var dirs = dir.listFiles();
        for (var directory : dirs) {
            if (directory.isDirectory()) {
                getSteps(directory, lines);
            } else if (directory.isFile()) {
                try {
                    var defs = FileUtils.readLines(directory, Charset.defaultCharset());
                    for (var def : defs) {
                        if (def.trim().startsWith("@given") || def.trim().startsWith("@when") || def.trim().startsWith("@then")) {
                            lines.add(def.trim());
                        }
                    }

                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }

        }
    }

}
