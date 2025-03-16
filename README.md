# ABN Amro

## Test assignment
Project is modularized and has next form: 
<img width="635" alt="The UML diagram of the project" src="https://github.com/user-attachments/assets/3aa898f3-8c0a-47ae-9e24-40c1b0529bf0" />

**:foundation-kotlin** is a module which shares common code (e.g. ResultOf)
Each of the feature hosts own **:domain** and **:data** modules. Sometimes :domain module can be shared between other features. 
