package chenrujun.azure.spring.apps.deployer.investigation.util;

import org.rauschig.jarchivelib.ArchiveFormat;
import org.rauschig.jarchivelib.Archiver;
import org.rauschig.jarchivelib.ArchiverFactory;
import org.rauschig.jarchivelib.CompressionType;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class ArchiverUtil {

    public static void removeRootFolderInTarFile(File inputFile, File outputFile) throws IOException {
        File extractedFile = Files.createTempDirectory("ArchiverUtil_").toFile();
        Archiver archiver = ArchiverFactory.createArchiver(ArchiveFormat.TAR, CompressionType.GZIP);
        archiver.extract(inputFile, extractedFile);
        archiver.create(outputFile.getName(), outputFile.getParentFile(), getArchiveSource(extractedFile));
    }

    private static File getArchiveSource(File extractedFile) {
        File[] files = extractedFile.listFiles();
        if (files != null && files.length == 1 && files[0].isDirectory()) {
            return files[0];
        }
        return extractedFile;
    }

}
