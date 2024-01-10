//package cn.esuny.bt.config;
//
//import com.moandjiezana.toml.Toml;
//import lombok.Data;
//import lombok.Getter;
//import lombok.SneakyThrows;
//import java.io.File;
//import java.io.Serializable;
//import java.util.List;
//import java.util.Map;
//@Data
//public class LoadConfig implements Serializable {
//    private String mainPrefix = loadToml().getString("main_prefix");
//    private Map<String, Object> configServerList = loadToml().getTable("sub_prefix").toMap();
//    private List<Object> mcdrCommandPrefix = loadToml().getList("mcdr_command_prefix");
//    private Integer wait = Integer.valueOf(loadToml().getString("waitForCheck"));
//    private String server = loadToml().getString("server");
//    private String client = loadToml().getString("client");
//    private String client_type = loadToml().getString("client_type");
//    private String uuid = loadToml().getString("uuid");
//
//    @SneakyThrows
//    public static Toml loadToml() {
//        File configFile = new File("./plugins/BTAPI/config.toml");
//        return new Toml().read(configFile);
//    }
//
//    public Map<String, Object> getConfigServerList() {
//        return configServerList;
//    }
//}

package cn.esuny.bt.config;

import cn.esuny.bt.BT;
import com.moandjiezana.toml.Toml;
import lombok.Data;
import lombok.Getter;
import lombok.SneakyThrows;

import java.io.File;
import java.io.Serializable;
import java.util.List;
import java.util.Map;

@Data
public class LoadConfig implements Serializable {
    private String mainPrefix = loadToml().getString("mainPrefix");
    private Map<String, Object> configServerList = loadToml().getTable("sub_prefix").toMap();
    private List<Object> mcdrCommandPrefix = loadToml().getList("mcdr_command_prefix");
    private Long wait = loadToml().getLong("wait");
    private String server = loadToml().getString("server");
    private String client = loadToml().getString("client");
    private String client_type = loadToml().getString("client_type");
    private String uuid = loadToml().getString("uuid");
    private String chat = loadToml().getString("ChatFormat");

    @SneakyThrows
    private static Toml loadToml() {
        File configFile = new File("./plugins/BTAPI/config.toml");
        return new Toml().read(configFile);
    }


}
