# 🏗️ 工地视频监控系统

> 基于 Vue 3 + Nginx-RTMP + FFmpeg 的轻量级多路视频监控平台，支持实时 HLS 流播放、摄像头设备管理、自动切片与循环推流。

## ✨ 功能特性

- 📹 **多路实时预览**：支持同时显示多个摄像头画面，采用 HLS 协议，低延迟。
- 🎛️ **设备管理**：通过数据库管理摄像头信息（设备ID、流名、AI监控类型、在线状态）。
- 🔄 **自动循环推流**：使用 FFmpeg 循环推流视频文件，模拟连续监控场景。
- ⚙️ **灵活配置**：前端支持环境变量（`.env`），后端配置模板化，适配不同部署环境。
- 🌐 **跨域支持**：Nginx 配置跨域头，前端可直接请求 HLS 切片。
- 📊 **实时状态**：在线/离线状态标识，清晰展示每个摄像头的运行情况。

## 🛠️ 技术栈

| 组件 | 技术 |
| :--- | :--- |
| **前端** | Vue 3 (Composition API), Axios, hls.js, Vite |
| **后端** | Java Spring Boot, MyBatis-Plus, MySQL |
| **流媒体** | Nginx (with RTMP module) + FFmpeg |
| **协议** | RTMP (推流) + HLS (播放) |
| **数据库** | MySQL 5.7+ |
| **部署** | 任意支持 Java + Node.js 的服务器，或 Docker |

## 🚀 快速开始

### 环境准备

在开始之前，请确保你已经安装以下工具：

- **Node.js** (>= 16.x) 和 npm
- **Java 8+** (后端 Spring Boot 项目)
- **MySQL** (>= 5.7)
- **Nginx** (带 `--with-http_ssl_module` 和 RTMP 模块) — 推荐使用 [nginx-rtmp-win32](https://github.com/illuspas/nginx-rtmp-win32)（Windows）或通过包管理器安装（Linux/macOS）
- **FFmpeg** (用于推流，[下载地址](https://ffmpeg.org/download.html))

### 克隆项目

```bash
git clone https://github.com/little-You520/minismarting.git
cd minismarting
1. 配置数据库
创建数据库（例如 db_build_safe）：

sql
CREATE DATABASE db_build_safe CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
导入表结构：使用 docs/db_build_safe.sql 文件建表：

bash
mysql -u root -p db_build_safe < docs/db_build_safe.sql
配置后端数据库连接：进入 minismarting-backend/src/main/resources/，复制 application-template.properties 为 application.properties（如果没有模板，则手动创建），填入你的数据库用户名和密码。

2. 配置 Nginx
参考 docs/nginx.conf（或 docs/nginx.conf.example）配置你的 Nginx：

RTMP 接收地址：rtmp://localhost/live/流名

HLS 切片路径：./html/hls（与 root 路径保持一致）

HTTP 端口：8068（前端默认请求此端口）

启动 Nginx（确保 html/hls 目录有写入权限）。

3. 启动后端
bash
cd minismarting-backend
mvn clean install
java -jar target/minismarting-*.jar
默认端口 8080，可在 application.properties 中修改。

4. 启动前端
bash
cd minismarting-frontend
npm install
# 复制 .env.example 为 .env.development（若没有则手动创建）
npm run dev
前端默认运行在 http://localhost:5173。

5. 推流测试
使用 FFmpeg 循环推流一个视频文件到你的流服务器（以 test1 为例）：

bash
ffmpeg -stream_loop -1 -re -i "your_video.mp4" -c copy -f flv rtmp://localhost/live/test1
在数据库 device_camera 表中插入一条记录，wvp_open_url 字段填入 test1，刷新前端页面即可看到画面。

📁 项目结构
text
minismarting/
├── docs/                           # 文档与配置模板
│   ├── db_build_safe.sql           # 数据库表结构（无数据）
│   └── nginx.conf                  # Nginx 配置示例
├── minismarting-backend/           # Java Spring Boot 后端
│   ├── src/
│   └── pom.xml
├── minismarting-frontend/          # Vue 3 前端
│   ├── src/
│   ├── .env.example                # 环境变量模板
│   ├── package.json
│   └── vite.config.js
├── .gitignore
└── README.md
📸 界面预览
![系统截图](docs/屏幕截图%202026-09-03%20151940.png)

⚙️ 环境变量说明
前端 (.env.development)
变量名	说明	默认值
VITE_API_BASE_URL	后端 API 地址	http://localhost:8080
VITE_HLS_BASE_URL	HLS 流服务器地址	http://localhost:8068
后端 (application.properties)
properties
spring.datasource.url=jdbc:mysql://localhost:3306/db_build_safe
spring.datasource.username=root
spring.datasource.password=你的密码
📝 常见问题
Q: 推流成功但前端看不到画面？

确认数据库 wvp_open_url 与推流流名一致。

检查 Nginx 是否开启 HLS 切片（hls on;）且 hls_path 有写入权限。

打开浏览器控制台，查看 Network 中 .m3u8 请求是否成功。

Q: 浏览器无法播放 HLS？

项目已集成 hls.js，请确保使用现代浏览器（Chrome/Edge/Firefox）。

检查 VITE_HLS_BASE_URL 是否正确。

Q: 如何同时推多路流？

使用多个 FFmpeg 进程，每个指定不同的流名（如 live01、live02），并在数据库中分别记录。

🤝 贡献
欢迎提交 Issue 或 Pull Request。如果此项目对你有帮助，欢迎 Star ⭐。

📄 许可证
MIT © 2026 little-You520

🙏 致谢
Vue.js

hls.js

nginx-rtmp-module

FFmpeg

最后更新： 2026-09-03
