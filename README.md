# <img src="https://github.com/user-attachments/assets/df86b661-fc64-4f61-969f-51be9d4de990" width="20" /> ABN Amro 

![Main workflow](https://github.com/akimaleo/Android-ABNAmro-test/actions/workflows/android.yml/badge.svg)

## Test assignment
Project structure UML diagram 

<img width="635" alt="The UML diagram of the project" src="https://github.com/user-attachments/assets/3aa898f3-8c0a-47ae-9e24-40c1b0529bf0" />

- **:foundation-kotlin** is a module which shares common code (e.g. **ResultOf**).
- Each feature module have own **:domain** and **:data** modules.
- **:domain** module can be shared between features. 

# Installation proccess
- [Generate a GitHub API key](https://github.com/settings/tokens). All of the rights access checkboxes can be left empty.
- Create a **secrets.properties** as it described in [secrets gradle plugin documentation](https://developers.google.com/maps/documentation/places/android-sdk/secrets-gradle-plugin?hl=ru).
- Add a generated key to the file. **GITHUB_API_KEY=your_key_here**
  
# UI Tests
Instrumentation UI 
- DetailsScreenTest.kt. 
  - Verify Loading state
  - Verify Error state and 'retry' button click handler
  - Verify Content state displayed correctly and 'Open in Github' button click handler

# Unit tests
Modules covered by unit tests:
- :feature-details
- :feature-details:domain

# Screenshot testing
Module :feature-details
- DetailsScreenScreenshotTest

  
Usefull comands:
- Verify screenshot tests: `./gradlew validateDebugScreenshotTest`
- Update screenshot tests: `./gradlew updateDebugScreenshotTest`


# Repositories List
List of repositories is getting fetched from https://api.github.com/users/abnamrocoesd/repos?page=1&per_page=10 using Retrofit library. Pagination 
 and caching of the data being managed by Room database and Pagination 3 Library which has Jetpack Compose Support.


