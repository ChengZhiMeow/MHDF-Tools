package cn.chengzhiya.mhdftools.entity.database.data.cmi;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
@Setter
@DatabaseTable
public final class CmiUserData {
    @DatabaseField(canBeNull = false)
    private int id;
    @DatabaseField(columnName = "player_name")
    private UUID playerUuid;
    @DatabaseField
    private String username;
    @DatabaseField
    private String nickname;
    @DatabaseField(columnName = "LogOutLocation")
    private String logOutLocation;
    @DatabaseField(columnName = "DeathLocation")
    private String deathLocation;
    @DatabaseField(columnName = "TeleportLocation")
    private String teleportLocation;
    @DatabaseField(columnName = "Homes")
    private String homes;
    @DatabaseField(columnName = "LastLoginTime")
    private Long lastLoginTime;
    @DatabaseField(columnName = "LastLogoffTime")
    private Long lastLogoffTime;
    @DatabaseField(columnName = "TotalPlayTime")
    private Long totalPlayTime;
    @DatabaseField(columnName = "tFly")
    private Long tFly;
    @DatabaseField(columnName = "tGod")
    private Long tGod;
    @DatabaseField(columnName = "Glow")
    private String glow;
    @DatabaseField(columnName = "Ips")
    private String ips;
    @DatabaseField(columnName = "Cuffed")
    private Boolean cuffed;
    @DatabaseField(columnName = "AlertUntil")
    private Long alertUntil;
    @DatabaseField(columnName = "AlertReason")
    private String alertReason;
    @DatabaseField(columnName = "JoinedCounter")
    private Boolean joinedCounter;
    @DatabaseField(columnName = "LockedIps")
    private String lockedIps;
    @DatabaseField(columnName = "pTime")
    private Long pTime;
    @DatabaseField(columnName = "Kits")
    private String kits;
    @DatabaseField(columnName = "Charges")
    private String charges;
    @DatabaseField(columnName = "Cooldowns")
    private String cooldowns;
    @DatabaseField(columnName = "Balance")
    private BigDecimal balance;
    @DatabaseField(columnName = "Notes")
    private String notes;
    @DatabaseField(columnName = "Rank")
    private String rank;
    @DatabaseField(columnName = "BannedUntil")
    private Long bannedUntil;
    @DatabaseField(columnName = "BannedAt")
    private Long bannedAt;
    @DatabaseField(columnName = "BannedBy")
    private String bannedBy;
    @DatabaseField(columnName = "BanReason")
    private String banReason;
    @DatabaseField(columnName = "Ignores")
    private String ignores;
    @DatabaseField(columnName = "Vanish")
    private String vanish;
    @DatabaseField(columnName = "Economy")
    private String economy;
    @DatabaseField(columnName = "Mail")
    private String mail;
    @DatabaseField(columnName = "FlightCharge")
    private BigDecimal flightCharge;
    @DatabaseField(columnName = "UserMeta")
    private String userMeta;
    @DatabaseField(columnName = "JailedUntil")
    private Long jailedUntil;
    @DatabaseField(columnName = "FakeAccount")
    private Boolean fakeAccount;
    @DatabaseField(columnName = "PlaytimeOptimized")
    private Long playTimeOptimized;
    @DatabaseField(columnName = "flightChargeEnabled")
    private Boolean flightChargeEnabled;
    @DatabaseField(columnName = "Skin")
    private String skin;
    @DatabaseField(columnName = "Collision")
    private Boolean collision;
    @DatabaseField(columnName = "NamePrefix")
    private String namePrefix;
    @DatabaseField(columnName = "NameSuffix")
    private String nameSuffix;
    @DatabaseField(columnName = "Warnings")
    private String warnings;
    @DatabaseField(columnName = "NameColor")
    private String nameColor;
    @DatabaseField(columnName = "muted")
    private String muted;
    @DatabaseField(columnName = "AFRecharge")
    private String afRecharge;
    @DatabaseField(columnName = "DisplayName")
    private String displayName;
    @DatabaseField(columnName = "Options")
    private String options;
    @DatabaseField(columnName = "ChatColor")
    private String chatColor;
}
