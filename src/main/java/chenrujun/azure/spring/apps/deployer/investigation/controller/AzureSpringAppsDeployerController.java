package chenrujun.azure.spring.apps.deployer.investigation.controller;

import chenrujun.azure.spring.apps.deployer.investigation.service.AzureSpringAppsDeployerService;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.annotation.RegisteredOAuth2AuthorizedClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AzureSpringAppsDeployerController {

    AzureSpringAppsDeployerService service;

    public AzureSpringAppsDeployerController(AzureSpringAppsDeployerService service) {
        this.service = service;
    }

    @GetMapping("/deployer")
    public String deployer(@RegisteredOAuth2AuthorizedClient("azure") OAuth2AuthorizedClient client,
                           @RequestParam String gitHubRepoUrl,
                           @RequestParam String moduleName) {
        return service.deployAzureSpringApps(client.getAccessToken().getTokenValue(), gitHubRepoUrl, moduleName);
        //return service.deployAzureSpringApps(client.getAccessToken().getTokenValue());
    }

}
