# ABN Amro

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

# Screenshost testing
Module :feature-details
- DetailsScreenScreenshotTest

  
Usefull comands:
- Verify screenshot tests: `./gradlew validateDebugScreenshotTest`
- Update screenshot tests: `./gradlew updateDebugScreenshotTest`
  
