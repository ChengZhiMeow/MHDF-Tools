package cn.chengzhiya.mhdftools.hook;

import lombok.Getter;

@Getter
public abstract class AbstractHook implements Hook {
    public boolean enable = false;
}
