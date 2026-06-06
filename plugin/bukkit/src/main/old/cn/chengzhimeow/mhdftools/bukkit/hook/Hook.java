package cn.chengzhimeow.mhdftools.bukkit.hook;

import lombok.Getter;

@Getter
public abstract class Hook {
    public boolean enable = false;

    abstract public void hook();

    abstract public void unhook();
}
