# AI Change Log（AI修改日志）

> 目的：记录每次由 AI 参与的改动，避免后续任务重复造轮子或破坏既有行为。
> 要求：每次合并前必须追加一条记录（倒序）。

---

## [2026-01-10] 扩充线上测试图片并保证同动态不重复
- 背景/需求：增加测试图片数量，且同一动态内不重复图片链接。
- 修改类型：feat
- 影响范围：Mock 数据
- 变更摘要：
  1) 扩充 demoImages 到 10 张线上图片
- 涉及文件：
  - `sanguidaily-front/src/stores/imageStore.js`
- 检索与复用策略：
  - 检索关键词：imageStore / demoImages
  - 找到的旧实现：3 张线上图片
  - 最终选择：扩充图片源并复用生成逻辑
- 风险点：
  - 依赖外部域名可用性
- 验证方式：
  - 手动查看 1~10 张动态是否有重复（未执行）
- 后续建议：
  - 如需更多数量可继续追加链接

## [2026-01-10] 预览前下载全部图片并缓存
- 背景/需求：大图预览偶发转圈，需要更稳定的加载方式。
- 修改类型：fix
- 影响范围：前端组件
- 变更摘要：
  1) 预览时先下载所有图片并使用临时路径
  2) 增加预览缓存，减少重复下载
- 涉及文件：
  - `sanguidaily-front/src/components/PostImagesGrid.vue`
- 检索与复用策略：
  - 检索关键词：previewImage / downloadFile / PostImagesGrid
  - 找到的旧实现：仅下载当前图片
  - 最终选择：批量下载并缓存，提高成功率
- 风险点：
  - 首次预览会有等待时间
- 验证方式：
  - 手动多次预览验证稳定性（未执行）
- 后续建议：
  - 如需更快首屏可只下载当前与相邻图

## [2026-01-09] 预览大图使用下载缓存降低转圈概率
- 背景/需求：线上图片预览偶发转圈，需提高加载成功率。
- 修改类型：fix
- 影响范围：前端组件
- 变更摘要：
  1) 预览前先下载当前图片并使用临时路径
  2) 缓存已下载图片，减少重复请求
- 涉及文件：
  - `sanguidaily-front/src/components/PostImagesGrid.vue`
- 检索与复用策略：
  - 检索关键词：previewImage / downloadFile / PostImagesGrid
  - 找到的旧实现：直接使用远程 URL 预览
  - 最终选择：在现有组件内增加下载与缓存
- 风险点：
  - 初次预览会多一次下载请求
- 验证方式：
  - 手动多次预览观察是否仍转圈（未执行）
- 后续建议：
  - 可加入超时与失败提示

## [2026-01-09] 替换为线上图片资源进行预览测试
- 背景/需求：本地静态图预览转圈，改用可访问的线上图片测试大图预览。
- 修改类型：fix
- 影响范围：Mock 数据
- 变更摘要：
  1) imageStore 使用 3 个线上图片 URL 作为测试源
- 涉及文件：
  - `sanguidaily-front/src/stores/imageStore.js`
- 检索与复用策略：
  - 检索关键词：imageStore / demoImages
  - 找到的旧实现：本地 demo 图片
  - 最终选择：替换为线上图片，继续复用生成逻辑
- 风险点：
  - 依赖外部域名可用性
- 验证方式：
  - 手动预览大图（未执行）
- 后续建议：
  - 如需离线可改回本地图片并补充预览转换逻辑

## [2026-01-09] 修复预览大图转圈无法加载
- 背景/需求：点击预览大图一直转圈，需支持本地静态图片预览。
- 修改类型：fix
- 影响范围：前端组件
- 变更摘要：
  1) 预览前将本地静态图转换为可预览的本地路径
- 涉及文件：
  - `sanguidaily-front/src/components/PostImagesGrid.vue`
- 检索与复用策略：
  - 检索关键词：previewImage / getImageInfo / PostImagesGrid
  - 找到的旧实现：直接使用 image_url 作为预览链接
  - 最终选择：在现有组件内补充转换逻辑
- 风险点：
  - 多图预览时会触发多次 getImageInfo
- 验证方式：
  - 手动点开预览查看是否仍转圈（未执行）
- 后续建议：
  - 如图片数量更多可加入缓存

## [2026-01-09] 引入本地真实图片资源用于预览测试
- 背景/需求：测试图片点开后原比例展示，需要真实图片资源。
- 修改类型：feat
- 影响范围：Mock 数据 / 静态资源
- 变更摘要：
  1) 新增本地 demo 图片资源
  2) imageStore 使用 demo 图片生成测试数据
- 涉及文件：
  - `sanguidaily-front/src/static/demo/img-01.png`
  - `sanguidaily-front/src/static/demo/img-02.png`
  - `sanguidaily-front/src/static/demo/img-03.png`
  - `sanguidaily-front/src/static/demo/img-04.png`
  - `sanguidaily-front/src/static/demo/img-05.png`
  - `sanguidaily-front/src/static/demo/img-06.png`
  - `sanguidaily-front/src/static/demo/img-07.png`
  - `sanguidaily-front/src/static/demo/img-08.png`
  - `sanguidaily-front/src/static/demo/img-09.png`
  - `sanguidaily-front/src/static/demo/img-10.png`
  - `sanguidaily-front/src/stores/imageStore.js`
- 检索与复用策略：
  - 检索关键词：imageStore / static
  - 找到的旧实现：logo.png 占位图
  - 最终选择：新增本地 demo 图片替换占位
- 风险点：
  - 静态资源体积增加
- 验证方式：
  - 手动点开预览查看原比例（未执行）
- 后续建议：
  - 如需更丰富测试可补充更多分辨率图片

## [2026-01-09] 修复多图预览比例为正方形
- 背景/需求：多张图片预览仍为长条形，仅单图正常。
- 修改类型：fix
- 影响范围：前端组件
- 变更摘要：
  1) 使用伪元素撑开高度，确保多列布局也保持正方形
- 涉及文件：
  - `sanguidaily-front/src/components/PostImagesGrid.vue`
- 检索与复用策略：
  - 检索关键词：PostImagesGrid / padding-top / 正方形
  - 找到的旧实现：padding-top 写在 item 本体导致按父宽度计算
  - 最终选择：改为伪元素撑高，避免多列比例失真
- 风险点：
  - 无
- 验证方式：
  - 手动查看 2~9 张图片布局（未执行）
- 后续建议：
  - 如需更复杂排版可引入 grid

## [2026-01-09] 修复九宫格预览为正方形比例
- 背景/需求：Feed/Detail 预览图仍呈长条形，需要统一正方形。
- 修改类型：fix
- 影响范围：前端组件
- 变更摘要：
  1) 九宫格使用 padding-top 比例并强制 content-box，确保正方形
- 涉及文件：
  - `sanguidaily-front/src/components/PostImagesGrid.vue`
- 检索与复用策略：
  - 检索关键词：PostImagesGrid / 正方形
  - 找到的旧实现：测量宽度计算尺寸
  - 最终选择：回归 CSS 比例并覆盖 box-sizing
- 风险点：
  - 无
- 验证方式：
  - 手动查看单图与多图预览（未执行）
- 后续建议：
  - 若需更大单图，可扩展为占满宽度的正方形

## [2026-01-09] 统一预览图为正方形并用真实宽度计算尺寸
- 背景/需求：Feed 预览图仍出现非正方形，需确保渲染尺寸严格一致。
- 修改类型：fix
- 影响范围：前端组件
- 变更摘要：
  1) 读取容器实际宽度，按列数计算正方形尺寸
  2) 预览图仍保留原图比例的打开方式
- 涉及文件：
  - `sanguidaily-front/src/components/PostImagesGrid.vue`
- 检索与复用策略：
  - 检索关键词：PostImagesGrid / 正方形
  - 找到的旧实现：CSS padding-top 比例方式
  - 最终选择：在现有组件内补充测量逻辑
- 风险点：
  - 列表中多组件测量带来轻微性能开销
- 验证方式：
  - 手动观察单图/多图预览是否为正方形（未执行）
- 后续建议：
  - 如需优化性能可缓存宽度

## [2026-01-09] 调整预览图为统一正方形比例
- 背景/需求：Feed 预览图要求统一正方形，点开预览保持原比例。
- 修改类型：fix
- 影响范围：前端组件
- 变更摘要：
  1) 单图预览改为正方形网格比例
- 涉及文件：
  - `sanguidaily-front/src/components/PostImagesGrid.vue`
- 检索与复用策略：
  - 检索关键词：PostImagesGrid / 预览比例
  - 找到的旧实现：单图 3:2 比例
  - 最终选择：回归统一正方形网格，保留预览原比例
- 风险点：
  - 无
- 验证方式：
  - 手动查看单图与多图预览（未执行）
- 后续建议：
  - 如需单图更大展示可引入“点击放大前占位”策略

## [2026-01-09] 优化图片比例与补充多数量图片测试数据
- 背景/需求：图片比例异常，需优化显示；补充 1~10 张图片的动态以观察布局。
- 修改类型：fix
- 影响范围：前端组件 / Mock 数据
- 变更摘要：
  1) 单图采用 3:2 比例展示，多图保持正方形网格
  2) 新增多条图片动态与对应图片数据（1~10 张）
- 涉及文件：
  - `sanguidaily-front/src/components/PostImagesGrid.vue`
  - `sanguidaily-front/src/stores/postStore.js`
  - `sanguidaily-front/src/stores/imageStore.js`
- 检索与复用策略：
  - 检索关键词：PostImagesGrid / imageStore / postStore
  - 找到的旧实现：现有九宫格与图片动态
  - 最终选择：修改现有组件与 Mock 数据，避免新增重复结构
- 风险点：
  - 单图比例改为 3:2，若需还原原始比例需进一步调整
- 验证方式：
  - 手动查看 1~10 张图片动态排版与 +x（未执行）
- 后续建议：
  - 如需原图比例展示，可在单图模式引入宽高字段

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
