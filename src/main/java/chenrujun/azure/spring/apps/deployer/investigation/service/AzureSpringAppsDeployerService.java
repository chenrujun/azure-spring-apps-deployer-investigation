package chenrujun.azure.spring.apps.deployer.investigation.service;

import chenrujun.azure.spring.apps.deployer.investigation.util.ArchiverUtil;
import com.azure.core.credential.AccessToken;
import com.azure.core.credential.TokenCredential;
import com.azure.core.http.policy.HttpLogDetailLevel;
import com.azure.core.management.AzureEnvironment;
import com.azure.core.management.Region;
import com.azure.core.management.profile.AzureProfile;
import com.azure.resourcemanager.AzureResourceManager;
import com.azure.resourcemanager.appplatform.models.SpringApp;
import com.azure.resourcemanager.appplatform.models.SpringService;
import com.azure.resourcemanager.resources.models.ResourceGroup;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.OffsetDateTime;

@Service
public class AzureSpringAppsDeployerService {

    private static final Logger LOGGER = LoggerFactory.getLogger(AzureSpringAppsDeployerService.class);
    private static final String SUFFIX = "-202205052030";
    private static final String RESOURCE_GROUP_NAME = "deployer-resource-group" + SUFFIX;
    private static final String SERVICE_NAME = "deployer-service" + SUFFIX;
    private static final String APP_NAME = "deployer-app" + SUFFIX;
    private static final Region REGION = Region.US_EAST;
    private static final String SOURCE_CODE_ZIP_URL =
            "https://github.com/weidongxu-microsoft/azure-sdk-for-java-management-tests/raw/master/spring-cloud/piggymetrics.tar.gz";

    public String deployAzureSpringApps(String accessToken) {
        return deployAzureSpringApps(SOURCE_CODE_ZIP_URL, "gateway", RESOURCE_GROUP_NAME, REGION, SERVICE_NAME, APP_NAME,  accessToken);
    }

    public String deployAzureSpringApps(String accessToken, String gitHubUrl, String moduleName) {
        return deployAzureSpringApps(toSourceCodeUrl(gitHubUrl), moduleName, RESOURCE_GROUP_NAME, REGION, SERVICE_NAME, APP_NAME,  accessToken);
    }

    // Sample input: https://github.com/chenrujun/piggymetrics/
    // Sample output: https://github.com/chenrujun/piggymetrics/archive/refs/heads/master.tar.gz
    private String toSourceCodeUrl(String gitHubUrl) {
        return gitHubUrl + "archive/refs/heads/master.tar.gz";
    }

    public String deployAzureSpringApps(
            String sourceCodeUrl,
            String deployModuleName,
            String resourceGroupName,
            Region region,
            String springServiceName,
            String SpringAppName,
            String accessToken) {
        File sourceCodeTar = getSourceCodeTarGzAndRemoveRootFolderIfNecessary(sourceCodeUrl);
        AzureResourceManager manager = toAzureResourceManager(accessToken);
        ResourceGroup resourceGroup = manager.resourceGroups()
                .define(resourceGroupName)
                .withRegion(region)
                .create();
        LOGGER.info("Created resource group: {}.", resourceGroupName);
        SpringService springService = manager.springServices()
                .define(springServiceName)
                .withRegion(region)
                .withExistingResourceGroup(resourceGroupName)
                .create();
        LOGGER.info("Created spring service: {}.", springServiceName);
        SpringApp springApp = springService.apps().define(SpringAppName)
                .defineActiveDeployment("default")
                .withSourceCodeTarGzFile(sourceCodeTar)
                .withTargetModule(deployModuleName)
                .attach()
                .withDefaultPublicEndpoint()
                .withHttpsOnly()
                .create();
        LOGGER.info("Created spring app: {}.", SpringAppName);
        springApp.url();
        return "Success. URL = " + springApp.url(); // TODO update this
    }

    private AzureResourceManager toAzureResourceManager(String accessToken) {
        final AzureProfile profile = new AzureProfile(AzureEnvironment.AZURE);
        final TokenCredential credential = toTokenCredential(accessToken);
        return AzureResourceManager
                .configure()
                .withLogLevel(HttpLogDetailLevel.BASIC)
                .authenticate(credential, profile)
                .withDefaultSubscription();
    }

    private TokenCredential toTokenCredential(String accessToken){
        return request -> Mono.just(new AccessToken(accessToken, OffsetDateTime.MAX)); // TODO not use MAX
    }

    private static File getSourceCodeTarGzAndRemoveRootFolderIfNecessary(String url) {
        File sourceCodeTarGz;
        try {
            Path tempDirectory = Files.createTempDirectory("AzureSpringAppsDeployerService_");
            File downloadedFile = tempDirectory.resolve("downloaded.tar.gz").toFile();
            HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
            connection.connect();
            try (InputStream inputStream = connection.getInputStream();
                 OutputStream outputStream = new FileOutputStream(downloadedFile)) {
                IOUtils.copy(inputStream, outputStream);
            }
            connection.disconnect();
            sourceCodeTarGz = tempDirectory.resolve("sourceCode.tar.gz").toFile();
            ArchiverUtil.removeRootFolderInTarFile(downloadedFile, sourceCodeTarGz);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return sourceCodeTarGz;
    }
}
