## How to run the application locally
1. create a new folder "env" in the project root dir
2. add local folder to the env folder
3. create new ".env" file in the local folder
4. add the following env in the .env file
    ```
    POSTGRES_USER=malabaak
    POSTGRES_DB=malabaakdb
    POSTGRES_PASSWORD=mysecretpassword
    DATASOURCE_URL=jdbc:postgresql://localhost:5432/malabaakdb
    DB_NAME=malabaakdb
    DB_USER=malabaak
    DB_PASSWORD=mysecretpassword
    SPRING_PROFILES_ACTIVE=dev
    
    #mail properties
    APP_MAIL_ACCOUNT=<email that will act as a sender>
    APP_MAIL_PASSWORD=jmonppgongftheey
  ```