package cli;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public final class FileCollector {

    private FileCollector() {
    }

    public static List<File> collect(File root) {
        List<File> files = new ArrayList<>();
        collectRecursive(root, files);
        return files;
    }

    private static void collectRecursive(File root, List<File> files) {
        File[] children = root.listFiles();
        if (children == null) {
            return;
        }

        for (File child : children) {
            if (child.isFile()) {
                files.add(child);
            } else if (child.isDirectory()) {
                collectRecursive(child, files);
            }
        }
    }
}