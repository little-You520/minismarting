package com.qstsmartbuildings.minismarting.entity;

public class Camera {
    private Integer id;
    private Integer assetId;          // 对应 asset_id
    private String status;            // 对应 status（注意是 String 类型，不是 Integer）
    private String isEnable;          // 对应 is_enable
    private String wvpOpenUrl;         // 对应 wp_open_url（注意不是 wvpOpenUrl！）
    private String aiMonitorType;     // 对应 ai_monitor_type
    private Integer sort;

    // 删除 intField（表中没有这个字段）

    // getter 和 setter
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public Integer getAssetId() { return assetId; }
    public void setAssetId(Integer assetId) { this.assetId = assetId; }

    public String getStatus() { return status; }      // 改为 String
    public void setStatus(String status) { this.status = status; }

    public String getIsEnable() { return isEnable; }  // 改为 String
    public void setIsEnable(String isEnable) { this.isEnable = isEnable; }

    public String getWvpOpenUrl() { return wvpOpenUrl; }
    public void setWpOpenUrl(String wpOpenUrl) { this.wvpOpenUrl = wpOpenUrl; }

    public String getAiMonitorType() { return aiMonitorType; }
    public void setAiMonitorType(String aiMonitorType) { this.aiMonitorType = aiMonitorType; }

    public Integer getSort() { return sort; }
    public void setSort(Integer sort) { this.sort = sort; }
}