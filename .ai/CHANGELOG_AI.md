# AI Change Log（AI修改日志）

> 目的：记录每次由 AI 参与的改动，避免后续任务重复造轮子或破坏既有行为。
> 要求：每次合并前必须追加一条记录（倒序）。

---

## [2026-01-09] 调整九宫格图片排版规则与展示上限
- 背景/需求：按新规则优化图片动态的布局（1/2/3/4/5~9），超 9 张显示 +x。
- 修改类型：fix
- 影响范围：前端组件 / 时间线渲染
- 变更摘要：
  1) 九宫格改为按数量自适配列数与排列
  2) Feed 图片上限改为 9，超出显示 +x
  3) 详情页仍可展示全部图片
- 涉及文件：
  - `sanguidaily-front/src/components/PostImagesGrid.vue`
  - `sanguidaily-front/src/components/PostCard.vue`
- 检索与复用策略：
  - 检索关键词：PostImagesGrid / imageMaxCount / 九宫格
  - 找到的旧实现：`PostImagesGrid.vue` 固定尺寸布局
  - 最终选择：修改现有组件布局与参数，避免新建重复组件
- 风险点：
  - CSS 计算宽度在极小屏幕的间距表现需验证
- 验证方式：
  - 手动运行前端观察 1~9 张排版与 +x 覆盖（未执行）
- 后续建议：
  - 如需更精细比例可引入图片真实宽高

## [2026-01-09] 初始化朋友圈示例前端页面与 Mock 数据
- 背景/需求：根据 TODOLIST 文档在小程序前端实现朋友圈风格示例 UI，使用本地 Mock 数据。
- 修改类型：feat
- 影响范围：前端页面 / 组件 / Mock 数据
- 变更摘要：
  1) 新增 Feed/Detail/Composer/Me 与占位页，路由更新
  2) 新增 PostCard 与媒体/点赞组件
  3) 新增 stores 与时间/ID 工具，支持本地点赞/发布
- 涉及文件：
  - `sanguidaily-front/src/pages.json`
  - `sanguidaily-front/src/App.vue`
  - `sanguidaily-front/src/pages/feed/index.vue`
  - `sanguidaily-front/src/pages/post-detail/index.vue`
  - `sanguidaily-front/src/pages/composer/index.vue`
  - `sanguidaily-front/src/pages/me/index.vue`
  - `sanguidaily-front/src/components/PostCard.vue`
  - `sanguidaily-front/src/components/PostImagesGrid.vue`
  - `sanguidaily-front/src/stores/postStore.js`
  - `sanguidaily-front/src/stores/likeStore.js`
- 检索与复用策略：
  - 检索关键词：feed / post / like / 朋友圈 / 动态
  - 找到的旧实现：默认模板页 `pages/index/index.vue`
  - 最终选择：复用入口页并新建组件与页面，避免同业务双实现
- 风险点：
  - 文本折叠按长度近似行数
  - 图片与封面使用占位资源
- 验证方式：
  - 手动运行前端检查页面与交互（未执行）
- 后续建议：
  - 若对接后端，统一由接口替换 stores

## [YYYY-MM-DD] <变更标题>
- 背景/需求：
- 修改类型：feat / fix / refactor / docs
- 影响范围：A业务 / B业务 / 公共模块 / 数据模型
- 变更摘要：
  1) ...
  2) ...
- 涉及文件：
  - `/path/file1`
  - `/path/file2`
- 检索与复用策略：
  - 检索关键词：
  - 找到的旧实现：
  - 最终选择：复用/修改/新建（说明原因）
- 风险点：
  - ...
- 验证方式：
  - ...
- 后续建议：
  - ...

---
