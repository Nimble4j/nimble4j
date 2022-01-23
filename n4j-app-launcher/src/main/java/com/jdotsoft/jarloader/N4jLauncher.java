package com.jdotsoft.jarloader;

public class N4jLauncher {

    public static void main(String[] args) {
        JarClassLoader jcl = new JarClassLoader();

        //first preference is runtime parameter
        String mainClassProperty = System.getProperty("App-Main-Class");
        String appMainClass = null;
        if(mainClassProperty != null && !mainClassProperty.isEmpty()){
            appMainClass = mainClassProperty;
        }else {
            //get the App-Main-Class from the manifest
            appMainClass = jcl.getManifestAppMainClass();
        }

        if(appMainClass == null){
            String message = String.format("App-Main-Class System Property / Manifest attribute is missing!");
            throw new RuntimeException(message);
        }

        try {
            jcl.invokeN4jMain("org.sf.n4j.impl.base.Main", args,appMainClass);
        } catch (Throwable e) {
            e.printStackTrace();
        }

    }
}
