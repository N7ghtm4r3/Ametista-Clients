# Ametista-Clients

**v1.0.0**

This project, based on Java and the Spring Boot framework, is an open source self-hosted issues
tracker and performance
stats collector about Compose Multiplatform applications

Improve the quality and stability of your apps with **Ametista**!

This repository contains the client versions of **Ametista**, so if you want to customize you can
fork
it and work on it, if there are any errors, fixes to do or some idea to upgrade this project, please
open a ticket or contact us to talk about, thanks and good use!

## 🛠 Skills

- Java
- Kotlin

## Architecture

### Engine

The [engine](https://github.com/N7ghtm4r3/Ametista-Engine#readme) is the core component of **Ametista**. It collects performance data and tracks issues to send to your backend instance for
analysis.

### Clients

This project will be constantly developed to reach different platforms to work on, following the
platforms releases
steps:

- Mobile
  - <a href="https://play.google.com/store/apps/details?id=com.tecknobit.ametista">Android</a>
  - iOS -> planned
- <a href="https://github.com/N7ghtm4r3/Ametista-Clients/releases/tag/1.0.0">Ametista desktop
  version</a>

### Backend

- <a href="https://github.com/N7ghtm4r3/Ametista/releases/tag/1.0.0">Backend service
  "out-of-the-box"</a>

## Usages

### Engine integration

Integrate the engine on your clients following the
related [guide](https://github.com/N7ghtm4r3/Ametista-Engine#readme)

### Run your own backend instance

See how to run your own **Ametista** backend instance service reading
the <a href="https://github.com/N7ghtm4r3/Ametista#readme">Ametista backend procedures</a>

## Customize the application

To customize and create your own version of this application you need to have
the <a href="https://github.com/N7ghtm4r3/Ametista/tree/main/core">
core library</a> implemented in your project and published into maven local system

### Clone the core library and publish to maven local

- Clone the repository or download the zip file of the current version available

- Open the folder file in your development environment and publish to maven local with the
  **publishMavenPublicationToMavenLocal** gradle task, take a
  look <a href="https://docs.gradle.org/current/userguide/publishing_maven.html">here</a>
  for a help

### Implement the core library to your application

- #### Gradle (Short)

```gradle
repositories {
  ...
  mavenLocal()
}

dependencies {
  implementation 'com.tecknobit.ametistacore:ametistacore:1.0.0'
}
```

#### Gradle (Kotlin)

```gradle
repositories {
  ...
  mavenLocal()
}

dependencies {
  implementation("com.tecknobit.ametistacore:ametistacore:1.0.0")
}
```

## Desktop appearance

<details>
  <summary>Desktop UI</summary>
  <img src="https://github.com/N7ghtm4r3/Ametista-Clients/blob/main/images/applications.png" alt="applications"/>
  <img src="https://github.com/N7ghtm4r3/Ametista-Clients/blob/main/images/application.png" alt="application"/>
  <img src="https://github.com/N7ghtm4r3/Ametista-Clients/blob/main/images/issues.png" alt="issues"/>
  <img src="https://github.com/N7ghtm4r3/Ametista-Clients/blob/main/images/performances.png" alt="performances"/>
</details>

## Authors

- [@N7ghtm4r3](https://www.github.com/N7ghtm4r3)

## Support

If you need help using the library or encounter any problems or bugs, please contact us via the
following links:

- Support via <a href="mailto:infotecknobitcompany@gmail.com">email</a>
- Support via <a href="https://github.com/N7ghtm4r3/Ametista-Clients/issues/new">GitHub</a>

Thank you for your help!

## Badges

[![](https://img.shields.io/badge/Google_Play-414141?style=for-the-badge&logo=google-play&logoColor=white)](https://play.google.com/store/apps/developer?id=Tecknobit)
[![Twitter](https://img.shields.io/badge/Twitter-1DA1F2?style=for-the-badge&logo=twitter&logoColor=white)](https://twitter.com/tecknobit)

[![](https://img.shields.io/badge/Java-ED8B00?style=for-the-badge&logo=java&logoColor=white)](https://www.oracle.com/java/)
[![](https://img.shields.io/badge/Kotlin-0095D5?&style=for-the-badge&logo=kotlin&logoColor=white)](https://kotlinlang.org/)
[![](https://img.shields.io/badge/Android-3DDC84?style=for-the-badge&logo=android&logoColor=white)](https://play.google.com/store/apps/details?id=com.tecknobit.nova)

## Donations

If you want support project and developer

| Crypto                                                                                              | Address                                        | Network  |
|-----------------------------------------------------------------------------------------------------|------------------------------------------------|----------|
| ![](https://img.shields.io/badge/Bitcoin-000000?style=for-the-badge&logo=bitcoin&logoColor=white)   | **3H3jyCzcRmnxroHthuXh22GXXSmizin2yp**         | Bitcoin  |
| ![](https://img.shields.io/badge/Ethereum-3C3C3D?style=for-the-badge&logo=Ethereum&logoColor=white) | **0x1b45bc41efeb3ed655b078f95086f25fc83345c4** | Ethereum |

If you want support project and developer
with <a href="https://www.paypal.com/donate/?hosted_button_id=5QMN5UQH7LDT4">PayPal</a>

Copyright © 2024 Tecknobit
