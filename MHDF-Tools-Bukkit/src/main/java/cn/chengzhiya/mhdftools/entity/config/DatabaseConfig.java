package cn.chengzhiya.mhdftools.entity.config;

import lombok.Getter;
import lombok.Setter;

import java.io.File;

@Getter
@Setter
public final class DatabaseConfig {
    private String type;
    private String host;
    private String database;
    private String user;
    private String password;
    private File file;
}
