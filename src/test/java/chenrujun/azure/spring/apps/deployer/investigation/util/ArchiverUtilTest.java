package chenrujun.azure.spring.apps.deployer.investigation.util;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;

public class ArchiverUtilTest {

    @Disabled
    @Test
    public void test() throws IOException {
        // File got from here: https://github.com/chenrujun/azure-spring-apps-deployer-target-application/archive/refs/heads/master.tar.gz
        ArchiverUtil.removeRootFolderInTarFile(
                new File("src/test/resources/azure-spring-apps-deployer-target-application-master.tar.gz"),
                new File("src/test/resources/output.tar.gz"));
    }

}
