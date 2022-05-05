# GitHub Application investigation

This repository is used to investigate deploying to Azure Spring Apps with one endpoint.

# Steps to Run this application

1. Fill `client-id` and `client-secret` in application.yml.
2. Start `AzureSpringAppsDeployerApplication`.
3. Access `Deploy to Azure Spring Cloud` in README of this repo: https://github.com/chenrujun/azure-spring-apps-deployer-target-application
4. Login with your microsoft account. Note: Email like  rujche@microsoft can not be used, it may require admin permission. Account like rujun@806258199qq.onmicrosoft.com is OK.
5. After few minutes, you will get response like this:
```text
 https://deployer-service-202205052030-deployer-app-202205052030.azuremicroservices.io
```
5. Access the URL you got in previous step, you will get response successfully. It means the application has deployed to Azure Spring Cloud successfully.
