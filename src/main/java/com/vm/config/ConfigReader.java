package com.vm.config;

import org.yaml.snakeyaml.Yaml;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public class ConfigReader {
    Map<String, Integer> map = new HashMap<>();

    public ConfigReader(){
        loadConfiguration();
    }

    public void loadConfiguration() {
        Yaml yaml = new Yaml();
        InputStream is = this.getClass().getClassLoader()
                .getResourceAsStream("inventory.yaml");
        map = yaml.load(is);
    }
    public Integer getValue(String key){
        if(map.size() == 0)
            loadConfiguration();
        return map.get(key);
    }
}
