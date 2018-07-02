@rem add local.properties file to racine of project
@rem in content put line 
@rem sdk.dir=D\:\\AndroidSdk
start gradlew.bat clean
pause
start gradlew.bat assembleRelease
