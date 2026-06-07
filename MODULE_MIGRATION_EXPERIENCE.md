# 模块迁移经验总结

本文记录 `MHDF-Tools` 旧功能迁移到新模块架构时需要遵守的经验和检查点。后续迁移功能时，优先参考现有新模块，尤其是 `Vanish` 的实现风格。

## 迁移目标

每个旧功能应迁移成一个独立的 `module_*` 模块，功能行为尽量保持不变，但实现方式要切到新架构。

- 配置放进模块自己的 `resources/module/<模块名>/config.yml`。
- 语言放进模块自己的 `resources/module/<模块名>/lang.yml`。
- 命令、监听器、占位符分别放进模块内对应包。
- 功能状态优先通过 `MHDFToolsPlayer` 等现有封装读写，不直接绕到更底层的数据管理器。
- 旧功能的命令注册、监听器注册、配置项、语言项和无用代码在确认不参与新构建后清理掉。

## 推荐目录结构

```text
module_xxx/
  build.gradle.kts
  src/main/java/.../module/xxx/
    ModuleMain.java
    config/
      ConfigSetting.java
      LangSetting.java
    command/
      Xxx.java
    listener/
      XxxListener.java
    placeholder/
      XxxPlaceholder.java
    menu/
      XxxMenu.java
  src/main/resources/module/xxx/
    config.yml
    lang.yml
    menu/
      xxx.yml
```

如果功能不需要监听器、占位符或菜单，就不要硬加空实现；如果旧功能有对应 Placeholder 或菜单，迁移时要补上新模块版本，避免功能只迁移了命令。

## 配置迁移

模块配置建议只保留当前模块需要的结构，不做旧配置兼容。

```yaml
enable: true
commands:
  - "xxx"
```

复杂功能可以在模块配置下继续拆分业务节点，例如快速时间和快速天气：

```yaml
time:
  早上:
    time: 1000
    commands:
      - "day"
```

配置读取应由模块自己的 `ConfigSetting` 负责，命令和监听器不要直接散落读取 YAML。常用做法是把配置解析成 record 或不可变集合，命令执行时只消费已经整理好的结构。

## 菜单配置迁移

旧功能如果引用了 `menu/*.yml` 或自定义菜单配置，迁移时必须同步迁移菜单文件和菜单 Setting，不能因为命令能打开临时 Inventory 就丢掉原有菜单配置能力。

推荐按 `MHDF-ItemBind` 的最新菜单规范处理：

- 菜单类放在模块自己的 `menu` 包，例如 `menu/ArmorMenu.java`。
- 菜单配置放进模块资源目录，例如 `resources/module/<模块名>/menu/armor.yml`。
- 菜单配置由独立 Setting 负责，例如 `ArmorMenuSetting`，并在 `ModuleMain.onLoad()` 中按 `saveDefaultFile()`、`update()`、`reload()` 顺序加载。
- 菜单 YAML 优先使用 `title`、`slots`、`open_actions`、`close_actions`、`items` 结构；`slots` 用字符布局声明界面，`items` 用同名字符定义图标、条件和点击动作。
- 菜单项解析后缓存为 `MenuItem` 或同等数据结构，菜单渲染只消费已解析的数据，不在点击或构建 Inventory 时散落读取 YAML。
- 如果某些槽位承载业务语义，例如装备栏的 helmet/chestplate/leggings/boots，要在菜单配置中显式声明这些字符到业务槽位的映射，避免把槽位数字硬编码进菜单类。
- 打开/关闭/点击动作沿用当前轻量 action/condition 体系，使用 `open_actions`、`close_actions` 和菜单项 `actions`，不要回退到旧 `openActions`、`closeActions` 或旧 ActionUtil。

## 语言迁移

语言文本应进入模块自己的 `LangSetting`。消息构建要参考 `Vanish` 的方式：

- 使用模块 `lang.yml` 保存业务文本。
- 需要前缀时组合 `GlobalLangSetting` 的 prefix。
- 返回 `TextComponent`，不要在命令里拼一堆裸字符串。
- 变量替换集中在发送前处理，保持调用点清楚。

命令里的提示、成功消息和错误消息不要继续引用旧语言节点。

## 命令实现

命令类优先对齐 `Vanish` 的代码风格。

- 构造时读取模块配置中的 `enable` 和 `commands`。
- `handler` 里先处理参数数量、目标玩家、权限和玩家在线状态。
- 权限判断使用当前命令体系已有的 `.give` 规则。
- 用 `GlobalLangSetting` 发送通用 usage、no-permission、not-online 等提示。
- 业务分支内部完成状态修改和消息发送，不把多个分支混在一起。

例如昵称功能里，关闭匿名和设置匿名应保持清晰分支：

```java
MHDFToolsPlayer mhdfPlayer = MHDFToolsAPI.getInstance().getPlayerManager().getPlayer(target.getUniqueId());
if (args[0].equals("off")) {
    mhdfPlayer.deleteNick();
    // 构造并发送关闭提示
    return;
} else {
    mhdfPlayer.setNick(args[0]);
}

// 构造并发送设置提示
```

如果 `MHDFToolsPlayer` 已经包装了读写能力，就不要再从命令层直接碰 `DataManager`。`DataManager` 应保持偏底层存储职责，模块业务代码使用已有玩家 API 更干净。

## 监听器实现

监听器放在模块自己的 `listener` 包，并继承新模块架构里的监听器基类。

监听器里需要注意：

- 先检查模块配置开关。
- 只处理当前模块负责的事件。
- 不复用旧监听器里的跨功能逻辑。
- 如果事件需要访问玩家状态，优先从 `MHDFToolsPlayer` 获取。

迁移时不要为了省事保留旧监听器注册，否则新旧逻辑容易重复触发。

## 占位符实现

旧功能如果有 Placeholder，新模块也要实现对应 Placeholder。

占位符应放在模块自己的 `placeholder` 包，并继承新模块架构提供的 `Placeholder` 基类。占位符 ID 要保持旧功能对外使用的名称，例如昵称显示继续使用原来的 `nick_name` 语义，避免外部菜单、计分板或文本配置失效。

实现时不要从旧代码里拉复杂依赖，优先通过 `MHDFToolsPlayer` 取当前状态。

## 旧代码处理

迁移完成后，需要清理旧构建路径里对应的：

- 旧命令类。
- 旧监听器类。
- 旧工具类或旧功能管理类。
- `plugin.yml` 中旧命令注册。
- 全局 `config.yml` 中旧功能配置。
- 全局 `lang.yml` 中旧功能语言。

但如果某个旧目录已经明确不参与构建，就不要为了“看起来完整”去重写旧实现内部逻辑。清理和迁移应该围绕当前构建路径和新模块展开。

## 验证方式

迁移后至少做这些检查：

```powershell
rg "xxxSettings|旧命令名|旧类名"
.\gradlew.bat compileJava
git status --short --branch
git diff --stat
```

如果本机默认 Java 版本导致 Gradle 在编译前失败，切到项目常用的 JDK 21：

```powershell
$env:JAVA_HOME="C:\Users\Administrator\.jdks\azul-21.0.11"
$env:Path="$env:JAVA_HOME\bin;$env:Path"
.\gradlew.bat compileJava
```

中文资源文件在 PowerShell 里可能显示乱码，不能只因为终端显示异常就重写文件；需要结合文件实际编码和构建结果判断。

## Commit 拆分

多个功能迁移应拆成多个 commit，不要混在一起。

推荐格式参考现有提交：

```text
refactor: pvp
refactor: fast change time
refactor: fast change weather
refactor: nick
```

提交前先看 `git diff --cached --stat`，确认当前 commit 只包含一个功能的迁移。共享文件如果同时删了多个功能的旧配置，需要分次 stage 对应 hunks，避免两个模块互相污染提交。

## 迁移 Checklist

- 确认当前参与构建的源码路径和资源路径。
- 找一个新模块作为参考，优先参考 `Vanish`。
- 新建独立 `module_*` 模块。
- 添加模块 `config.yml` 和 `lang.yml`。
- 实现 `ConfigSetting` 和 `LangSetting`。
- 迁移命令，并对齐新命令风格。
- 迁移监听器，仅保留当前模块职责。
- 迁移 Placeholder，保留对外 placeholder ID。
- 使用 `MHDFToolsPlayer` 等现有封装读写玩家状态。
- 删除当前构建路径里的旧配置、旧语言、旧注册和旧类。
- 用 `rg` 检查旧 key 和旧类是否残留。
- 运行 `compileJava` 验证。
- 按功能拆 commit。
