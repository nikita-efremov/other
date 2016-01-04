import java.io.File;
import java.lang.String;
import java.lang.System;

/**
 * Created by nefremov on 06.07.2015.
 *
 * @author Nikita Efremov
 */
public class FileNameAppender {

    public static void main(String ... args) throws Exception {
        System.out.print("Enter prefix for files: ");
        String prefix = System.console().readLine();
        String currentDirectory = System.getProperty("user.dir");
        System.out.println("Current path is " + currentDirectory);
        File directory = new File(currentDirectory);

        String extension = ".flac";
        if (args.length > 0) {
            extension = args[0];
        }

        File[] oldFiles = directory.listFiles();
        String[] oldFileNames = directory.list();

        //check if folder is empty
        if ((oldFiles == null) || (oldFiles.length == 0)) {
            System.out.println("Directory is empty. Nothing to remove");
            return;
        }

        //print directory content
        System.out.println("Directory content: ");
        for (String oldFileName1 : oldFileNames) {
            System.out.println("      " + oldFileName1);
        }
        System.out.println();

        //print a list of files, that will be renamed
        System.out.println("Files that will be renamed: ");
        for (String oldFileName : oldFileNames) {
            if (isCanBeRenamed(oldFileName, extension)) {
                System.out.println("      " + oldFileName);
            }
        }

        //renames files
        for (File oldFile : oldFiles) {
            String currentFilename = oldFile.getName();
            if (isCanBeRenamed(currentFilename, extension)) {
                boolean success = oldFile.renameTo(new File(prefix + currentFilename));
                if (!success) {
                    throw new RuntimeException("Renaming failed due to unknown reasons");
                }
            }
        }

        //print result
        System.out.println();
        System.out.println("Rename success!");
        for (String newFilename : directory.list()) {
            if (isCanBeRenamed(newFilename, extension)) {
                System.out.println("      " + newFilename);
            }
        }
    }

    private static boolean isCanBeRenamed(String filename, String extension) {
        int j = filename.lastIndexOf(extension);
        return j > 0;
    }
}
