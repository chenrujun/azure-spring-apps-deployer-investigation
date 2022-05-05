package chenrujun.azure.spring.apps.deployer.investigation.service;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

public class AzureSpringAppsDeployerServiceTest {

    @Disabled
    @Test
    public void testDeployAzureSpringApps(){
        AzureSpringAppsDeployerService deployer = new AzureSpringAppsDeployerService();
        deployer.deployAzureSpringApps("");
    }
}
