package cn.chengzhimeow.mhdftools.bukkit.hook;

import lombok.Getter;

@Getter
public abstract class Hook {
    private boolean enable = false;

    protected void setEnable(boolean enable) {
        this.enable = enable;
    }

    public abstract void hook();

    public abstract void unhook();
}
