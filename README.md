# GitHub Application investigation

This repository is used to investigate deploying to Azure Spring Apps with one endpoint.

# Steps to Run this application

1. Fill `client-id` and `client-secret` in application.yml.
2. Start `AzureSpringAppsDeployerApplication`.
3. Access endpoint like `http://localhost:8080/deployer?gitHubRepoUrl=https%3A%2F%2Fgithub.com%2Fchenrujun%2Fazure-spring-apps-deployer-target-application%2F&moduleName=hello-world`.
4. Login with your microsoft account. Note: MSFT account can not be used. For example: rujche@microsoft.com can not be used, it may require admin permission. Account like rujun@806258199qq.onmicrosoft.com is OK. The reason can be found [here](https://github.com/AzureAD/microsoft-authentication-library-for-js/issues/1564#issuecomment-621507432).
5. After few minutes(5 - 10 minutes), you will get response like this:
```text
 https://deployer-service-202205052030-deployer-app-202205052030.azuremicroservices.io
```
6. Access the URL you got in previous step, you will get response successfully. It means the application has deployed to Azure Spring Cloud successfully.

# Workaround in Current Implementation

Check the source code of `AzureSpringAppsDeployerService#deployAzureSpringApps`. There are 2 ways to get source code file:
1. https://github.com/weidongxu-microsoft/azure-sdk-for-java-management-tests/raw/master/spring-cloud/piggymetrics.tar.gz
2. https://github.com/chenrujun/azure-spring-apps-deployer-target-application/archive/refs/heads/master.tar.gz

`1` can work but `2` can not. Because after unzipping the second file, all files are included in a folder. To use the source-code deploy, there should be a file like "pom.xml" in the root path. All files included in a folder will hide the "pom.xml" file.

# Further steps:

1. Fix the problem: GitHub downloaded tar.gz file contains a root folder, which make it can not be deployed directly.
2. Support deploy by clicking button in GitHub README. Get GitHub url by [Referer header](https://developer.mozilla.org/en-US/docs/Web/HTTP/Headers/Referer) instead of request parameter.
3. Support choosing `subscription / resource-group / Azure Spring Cloud service` before deploy.
4. Make module name optional. Support single module project.
5. Give more information when deploying. For example, show deploy status.
6. Support open the deployed app automatically.

