package com.jdotsoft.jarloader;

public class Launcher {

    public static void main(String[] args) {
        JarClassLoader jcl = new JarClassLoader();
        //get the App-Main-Class from the manifest
        String appMainClass = jcl.getManifestAppMainClass();
        if(appMainClass == null){
            String message = String.format("App-Main-Class Manifest attribute is missing!");
            throw new RuntimeException(message);
        }

        try {
            jcl.invokeMain(appMainClass, args);
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }
}
