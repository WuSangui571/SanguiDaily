> 此项目是一个完整的微信小程序的前后端的程序，正在构想中。文档开始编写时间：2026-01-09

---

## 项目简介
三桂日常（SanguiDaily）是一个面向微信小程序的前后端一体化项目，包含前端 uni-app 与后端 Spring Boot 服务，提供动态发布与浏览、媒体上传、点赞、置顶、垃圾箱等基础能力，适合用于搭建轻量级日常记录与分享类应用。

## 技术栈
- 前端：uni-app（Vue2）
- 后端：Spring Boot（Java 21，JDBC）
- 数据库：MySQL

## 目录结构
- `sanguidaily-front`：前端项目（uni-app）
- `sanguidaily-back`：后端项目（Spring Boot）
- `uploads`：上传资源目录（运行时需可读写）
- `db_backups`：数据库备份目录
- `sanguidaily_db.sql`：数据库初始化/参考脚本
- `fake-nginx-conf`：示例 Nginx 配置
- `scripts`：运维/脚本工具

## 快速开始

### 前端（uni-app）
前端建议使用 HBuilderX 或 Node 环境构建。

```bash
cd sanguidaily-front
npm install
# 微信小程序开发
npm run dev:mp-weixin
# H5 预览
npm run dev:h5
```

构建命令（按需选择目标平台）：
```bash
npm run build:mp-weixin
npm run build:h5
```

### 后端（Spring Boot）
```bash
cd sanguidaily-back
# Windows
./mvnw.cmd spring-boot:run
# macOS/Linux
./mvnw spring-boot:run
```

## 关键配置（部署前必看）

### 后端配置
- 环境切换：`SPRING_PROFILES_ACTIVE`（dev/prod），默认读取 `application.properties` 中的配置
- 数据库连接：
  - `application-dev.properties` / `application-prod.properties` 中的 `spring.datasource.url`
  - 账号密码：环境变量 `DATASOURCE_USERNAME` / `DATASOURCE_PASSWORD`
- 微信登录：环境变量 `WECHAT_APPID` / `WECHAT_SECRET`
- CORS：`APP_CORS_ALLOWED_ORIGINS`（逗号分隔）
- 上传与视频封面：
  - `app.upload-root`
  - `app.ffmpeg-path`
  - `app.public-base`（生产环境对外访问前缀）
- JWT：`JWT_SECRET` / `app.jwt.expire-days`
- 作者识别：`APP_OWNER_OPENID`

### 前端配置
- 微信小程序 AppID：`sanguidaily-front/src/manifest.json` 中的 `mp-weixin.appid`（部署时请替换为自己的 AppID）
- 小程序域名校验：`mp-weixin.setting.urlCheck`（生产建议开启）
- API 基址：默认 `https://sangui.top/sanguidaily`，可通过 `VUE_APP_API_BASE` 环境变量或本地存储 `apiBaseUrl` 覆盖

## 部署注意事项
- 数据库初始化：可参考 `sanguidaily_db.sql` 导入结构与数据
- 上传目录：确保 `uploads` 目录在服务器存在且可读写
- Nginx 反代示例：见 `fake-nginx-conf/nginx.conf`
  - `/sanguidaily/` 反代后端（示例为 8080）
  - `/uploads/` 映射到上传目录
- 若使用 HTTPS，请确保小程序后台域名、后端 CORS 与前端 API 基址保持一致

## 运维脚本
- 数据库备份同步：`scripts/sync_db.bat`（将服务器备份同步到本机 `db_backups`）

## 许可与说明
当前仓库为项目开发过程文档与代码集合，部署前请替换敏感配置并自检安全策略。
