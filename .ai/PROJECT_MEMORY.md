# Project Memory（AI长期记忆）

> 本文件记录项目级稳定事实：业务边界、关键接口、数据模型、工程约束、坑点。
> AI 在写代码前必须阅读本文件，任何变更若影响边界/契约需更新本文件。

---

## 1. 项目概览
- 项目名称：三桂日常（SanguiDaily）
- 技术栈：uni-app（Vue2）+ Spring Boot（Java 21, JDBC, MySQL）
- 运行方式：前端位于 `sanguidaily-front`
- 后端位于 `sanguidaily-back`
- 目录结构概览：
  - `sanguidaily-front/src/pages`：页面
  - `sanguidaily-front/src/components`：通用组件
  - `sanguidaily-front/src/stores`：数据状态
  - `sanguidaily-front/src/utils`：时间/ID 等工具
  - `sanguidaily-back/src/main/java/com/sangui/sanguidaily`：后端入口与 API
  - `sanguidaily-back/src/main/resources/application.properties`：后端配置

---

## 2. 业务域与模块边界（最重要）

| 业务域 | 职责 | 主要目录 | 对外入口 |
| ------ | ---- | -------- | -------- |
| A业务  | ...  | /src/a/  | /api/a/* |

### 禁止规则（硬约束）
- A业务只能在 `/src/a/` 内实现，禁止在其它模块重复实现同功能。
- 对外接口必须复用已有入口，不允许新增第二套。

---

## 3. 模块地图（Module Map）
| 模块/目录 | 职责 | 入口文件 | 备注 |
| --------- | ---- | -------- | ---- |
| pages/feed | 时间线首页 | `sanguidaily-front/src/pages/feed/index.vue` | 朋友圈风格卡片流 |
| pages/post-detail | 动态详情 | `sanguidaily-front/src/pages/post-detail/index.vue` | 复用卡片完整展示 |
| pages/composer | 发布页 | `sanguidaily-front/src/pages/composer/index.vue` | 按类型提交本地数据 |
| pages/me | 我的 | `sanguidaily-front/src/pages/me/index.vue` | 角色与入口 |
| components | 动态卡片与子组件 | `sanguidaily-front/src/components/*` | PostCard/媒体/点赞 |
| stores | 数据与行为 | `sanguidaily-front/src/stores/*` | user/post/image/like |
| back/api | 后端接口 | `sanguidaily-back/src/main/java/com/sangui/sanguidaily/api/*` | users/posts/images/likes |

---

## 4. 必须复用的关键接口/服务清单（防重复造轮子）
> 新增功能前必须先在这里找是否已有实现。

### 4.1 A业务
- `AService.xxx`
  - 文件：
  - 作用：
  - 注意事项：

### 4.2 后端 API（已落地）
- `GET /api/users/current`
- `POST /api/auth/wechat`
- `GET /api/posts`
- `GET /api/posts/{id}`
- `POST /api/posts`
- `GET /api/post-images`
- `POST /api/post-images/batch`
- `GET /api/post-likes`
- `POST /api/post-likes`
- `DELETE /api/post-likes`

### 4.3 登录与鉴权（已落地）
- 微信登录：前端 code + 昵称/头像 -> 后端换取 openid 并注册/登录
- JWT：后端签发 Bearer token，`/api/users/current` 校验 token
- 相关配置：
  - `wechat.appid` / `wechat.secret`
  - `app.jwt.secret` / `app.jwt.expire-days`
  - `app.owner-openid`（匹配则为 OWNER）

---

## 5. 数据模型与关键约束
| 模型  | 字段   | 约束                |
| ----- | ------ | ------------------- |
| t_user | role | OWNER / VIEWER / SUSPENDED |
| t_post | type | 0 文本 / 1 图片 / 2 链接 / 3 视频 |
| t_post | status | 0 公开 / 1 私密 |
| t_post_image | sort_order | 九宫格按序展示 |

---

## 6. 统一工程规范（命名/错误/日志）
- 命名：
- 错误处理：
- 日志：
- 配置与环境变量：

---

## 7. 已知坑点与注意事项（AI 常犯错）
- 坑点1：
- 坑点2：
- 常见故障排查：

---
