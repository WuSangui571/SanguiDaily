# AI Change Log（AI修改日志）

> 目的：记录每次由 AI 参与的改动，避免后续任务重复造轮子或破坏既有行为。
> 要求：每次合并前必须追加一条记录（倒序）。

---

## [2026-01-10] 首页视频卡片点击直接播放
- 背景/需求：首页视频动态点击应直接播放，非视频区域仍跳转详情
- 修改类型：fix
- 影响范围：前端组件
- 变更摘要：
  1) 列表视频卡片点击切换为内联 video 播放
  2) 详情页仍保持视频播放器
- 涉及文件：
  - `sanguidaily-front/src/components/PostCard.vue`
- 检索与复用策略：
  - 检索关键词：PostCard / video / feed / detail
  - 找到的旧实现：列表点击进入详情后再播放
  - 最终选择：在列表内懒加载 video 播放器
- 风险点：
  - 列表中多个视频可能带来性能开销
- 验证方式：
  - 真机点击首页视频直接播放，点击卡片空白仍进详情（未执行）
- 后续建议：
  - 如需同时只播一个，可加全局播放互斥

## [2026-01-10] 详情页支持视频播放
- 背景/需求：点击视频无反应，需要在详情页可播放
- 修改类型：fix
- 影响范围：前端组件
- 变更摘要：
  1) 详情模式下渲染 video 组件并支持播放
  2) 视频卡片点击在列表中进入详情
- 涉及文件：
  - `sanguidaily-front/src/components/PostCard.vue`
- 检索与复用策略：
  - 检索关键词：PostCard / PostVideoCard / video
  - 找到的旧实现：仅视频封面卡片，无播放能力
  - 最终选择：在详情模式内嵌 video 组件
- 风险点：
  - 需配置视频域名为小程序合法域名
- 验证方式：
  - 真机进入详情播放视频（未执行）
- 后续建议：
  - 如需全屏播放可再接入 video 全屏设置

## [2026-01-10] 更新视频动态示例链接
- 背景/需求：测试视频播放，替换示例视频链接
- 修改类型：fix
- 影响范围：Mock 数据
- 变更摘要：
  1) 将视频动态的 video_url 改为 BigBuckBunny 示例地址
- 涉及文件：
  - `sanguidaily-front/src/stores/postStore.js`
- 检索与复用策略：
  - 检索关键词：video_url / postStore
  - 找到的旧实现：示例视频地址 https://example.com/video.mp4
  - 最终选择：直接替换示例视频地址
- 风险点：
  - 外链视频需可访问且支持小程序播放
- 验证方式：
  - 真机打开视频动态并播放（未执行）
- 后续建议：
  - 如需更稳定可换成本地或自有 CDN

## [2026-01-10] 链接卡片增加复制链接打开方式
- 背景/需求：WebView 样式异常，需提供默认浏览器打开的替代方式
- 修改类型：fix
- 影响范围：前端组件
- 变更摘要：
  1) 链接卡片点击弹出选项：小程序内打开 / 复制链接
  2) 复制链接后提示用户在浏览器打开
- 涉及文件：
  - sanguidaily-front/src/components/PostLinkCard.vue
- 检索与复用策略：
  - 检索关键词：PostLinkCard / clipboard / actionSheet
  - 找到的旧实现：仅 WebView 跳转
  - 最终选择：复用 WebView 跳转并补充复制链接
- 风险点：
  - 复制链接需用户手动打开浏览器
- 验证方式：
  - 真机点击链接卡片可选择打开方式（未执行）
- 后续建议：
  - 如需更自动化打开，可评估平台能力限制

## [2026-01-10] WebView 路由参数兼容 target/url
- 背景/需求：点击链接后 WebView 显示“链接地址为空”
- 修改类型：fix
- 影响范围：前端组件 / 页面
- 变更摘要：
  1) 跳转参数改为 target，WebView 同时兼容 url
- 涉及文件：
  - `sanguidaily-front/src/components/PostLinkCard.vue`
  - `sanguidaily-front/src/pages/webview/index.vue`
- 检索与复用策略：
  - 检索关键词：webview / onLoad / options.url
  - 找到的旧实现：仅读取 url 参数
  - 最终选择：增加 target 参数并兼容读取
- 风险点：
  - 无
- 验证方式：
  - 真机点击链接卡片可正确打开（未执行）
- 后续建议：
  - 如需额外调试可临时显示 options 内容

## [2026-01-10] 修复 PostCard 方法块括号导致的编译错误
- 背景/需求：构建报 Unexpected token
- 修改类型：fix
- 影响范围：前端组件
- 变更摘要：
  1) 修正 PostCard methods 结束括号
- 涉及文件：
  - sanguidaily-front/src/components/PostCard.vue
- 检索与复用策略：
  - 检索关键词：PostCard / methods / Unexpected token
  - 找到的旧实现：多余的右花括号
  - 最终选择：移除多余括号
- 风险点：
  - 无
- 验证方式：
  - 重新构建不再报语法错误（未执行）
- 后续建议：
  - 无

## [2026-01-10] 修复 pages.json BOM 导致解析失败
- 背景/需求：真机编译报 pages.json 非法 JSON（Unexpected token BOM）
- 修改类型：fix
- 影响范围：前端配置
- 变更摘要：
  1) 移除 pages.json 文件 BOM，恢复 JSON 解析
- 涉及文件：
  - sanguidaily-front/src/pages.json
- 检索与复用策略：
  - 检索关键词：pages.json / BOM / JSON 解析失败
  - 找到的旧实现：UTF-8 带 BOM
  - 最终选择：移除 BOM，保留文件内容
- 风险点：
  - 无
- 验证方式：
  - 重新运行构建看是否仍报 JSON 错误（未执行）
- 后续建议：
  - 避免使用会写入 BOM 的工具覆写 JSON

## [2026-01-10] 链接点击无响应提示与事件修正
- 背景/需求：真机点击链接卡片无反应，需要可见反馈
- 修改类型：fix
- 影响范围：前端组件
- 变更摘要：
  1) 链接卡片点击使用 stop 并保留跳转
  2) 跳转失败时提示“检查业务域名”
  3) 卡片媒体区点击使用空处理避免吞掉事件
- 涉及文件：
  - `sanguidaily-front/src/components/PostLinkCard.vue`
  - `sanguidaily-front/src/components/PostCard.vue`
- 检索与复用策略：
  - 检索关键词：PostLinkCard / tap / navigateTo / stop
  - 找到的旧实现：仅 navigateTo，无失败反馈
  - 最终选择：增加失败提示并显式 stop
- 风险点：
  - 仍需配置小程序业务域名才可打开 WebView
- 验证方式：
  - 真机点击链接卡片可跳转或有失败提示（未执行）
- 后续建议：
  - 如需更明确错误，可在 fail 回调打印详细错误码

## [2026-01-10] 链接动态改为 WebView 跳转
- 背景/需求：微信小程序需实际跳转外部链接
- 修改类型：fix
- 影响范围：前端组件 / 页面配置
- 变更摘要：
  1) 链接卡片点击改为跳转到 WebView 页面
  2) 新增 WebView 页面并加入 pages.json
- 涉及文件：
  - `sanguidaily-front/src/components/PostLinkCard.vue`
  - `sanguidaily-front/src/pages/webview/index.vue`
  - `sanguidaily-front/src/pages.json`
- 检索与复用策略：
  - 检索关键词：PostLinkCard / web-view / pages.json
  - 找到的旧实现：弹窗提示“示例阶段不跳转”
  - 最终选择：新增 WebView 页面并复用 navigateTo
- 风险点：
  - 需在小程序业务域名中配置 https://www.sangui.top/
- 验证方式：
  - 真机点击链接卡片打开 WebView 并加载博客（未执行）
- 后续建议：
  - 如需 H5/APP 兼容，可按平台差异处理跳转

## [2026-01-10] 更新链接动态为博客地址
- 背景/需求：示例链接类型动态改为博客地址用于跳转测试
- 修改类型：fix
- 影响范围：Mock 数据
- 变更摘要：
  1) 将链接类型动态 link_url 改为 https://www.sangui.top/
- 涉及文件：
  - sanguidaily-front/src/stores/postStore.js
- 检索与复用策略：
  - 检索关键词：link_url / PostStore / 链接
  - 找到的旧实现：示例链接地址 https://example.com
  - 最终选择：直接替换示例链接地址
- 风险点：
  - 现有链接卡片仍为“示例阶段不跳转”弹窗，需另行改为真实跳转
- 验证方式：
  - 打开链接类型动态确认链接地址显示（未执行）
- 后续建议：
  - 如需实际跳转，可在链接卡片中使用 navigateToMiniProgram 或 openURL 能力（需明确平台）

## [2026-01-10] 去掉查看详情按钮并支持空白区点击
- 背景/需求：去掉“查看详情”按钮，改为空白区域点击等价查看详情
- 修改类型：fix
- 影响范围：前端组件
- 变更摘要：
  1) 移除查看详情按钮，卡片空白区域点击触发查看详情
  2) 交互区（点赞/展开/媒体）阻止冒泡，避免误触详情
- 涉及文件：
  - `sanguidaily-front/src/components/PostCard.vue`
- 检索与复用策略：
  - 检索关键词：PostCard / 查看详情 / view-detail / tap
  - 找到的旧实现：按钮触发查看详情
  - 最终选择：复用 view-detail 事件，改为卡片空白区域触发
- 风险点：
  - 部分可点击区域若未覆盖 stop，可能仍触发详情
- 验证方式：
  - 点赞/展开/媒体不触发详情；点击空白区域进入详情（未执行）
- 后续建议：
  - 如需更精细命中区域，可进一步拆分点击容器

## [2026-01-10] 双指缩放中点补偿基于当前偏移
- 背景/需求：双指缩放仍以图片中心缩放，需要严格以两指中点为中心
- 修改类型：fix
- 影响范围：前端组件
- 变更摘要：
  1) 以当前位移修正中点偏移，缩放时按偏移补偿位移
  2) 根据当前预览项更新可缩放区域位置，确保中点计算正确
- 涉及文件：
  - `sanguidaily-front/src/components/PostImagesGrid.vue`
- 检索与复用策略：
  - 检索关键词：PostImagesGrid / movable-view / scale / change / touch
  - 找到的旧实现：中点偏移计算未考虑当前位移
  - 最终选择：在现有手势逻辑上校正中点偏移
- 风险点：
  - 不同端对 change.source 的支持存在差异
- 验证方式：
  - 真机双指缩放以两指中点为中心放大/缩小（未执行）
- 后续建议：
  - 如仍偏移，可记录事件 detail 调整补偿公式

## [2026-01-10] 预览双指缩放以两指中点为中心
- 背景/需求：双指缩放固定在图片中心，体验不自然；缩放后误触关闭预览
- 修改类型：fix
- 影响范围：前端组件
- 变更摘要：
  1) 双指缩放根据两指中点计算偏移，保持中点区域固定缩放
  2) 缩放结束后延长关闭冷却时间，避免误触关闭
- 涉及文件：
  - `sanguidaily-front/src/components/PostImagesGrid.vue`
- 检索与复用策略：
  - 检索关键词：PostImagesGrid / movable-view / scale / touch
  - 找到的旧实现：固定中心缩放 + 400ms 关闭冷却
  - 最终选择：在现有预览组件上增强手势计算，保持无新增模块改动
- 风险点：
  - 不同平台 movable-view 事件反馈可能有差异
- 验证方式：
  - 真机双指缩放以两指中点为中心激活，放大/缩小后立即点击不关闭（未执行）
- 后续建议：
  - 若仍有误触，可调节冷却时间或增加明确的关闭按钮

## [2026-01-10] 双指缩放后短时间内不触发关闭
- 背景/需求：缩放结束后容易误触关闭，需要短暂冷却。
- 修改类型：fix
- 影响范围：前端组件
- 变更摘要：
  1) 缩放结束后 400ms 内单击不触发关闭
- 涉及文件：
  - `sanguidaily-front/src/components/PostImagesGrid.vue`
- 检索与复用策略：
  - 检索关键词：PostImagesGrid / touch / pinch
  - 找到的旧实现：单击关闭逻辑
  - 最终选择：加入缩放冷却时间
- 风险点：
  - 关闭操作会有轻微延迟
- 验证方式：
  - 真机双指缩放后立即点击验证不关闭（未执行）
- 后续建议：
  - 如需更短/更长，可调节 400ms

## [2026-01-10] 预览关闭改为单击且移除原图按钮
- 背景/需求：左/右滑应切换图片，单击才关闭；不再需要“查看原图”按钮。
- 修改类型：fix
- 影响范围：前端组件
- 变更摘要：
  1) 单指滑动不关闭，只有单击关闭
  2) 移除“查看原图”按钮与相关逻辑
- 涉及文件：
  - `sanguidaily-front/src/components/PostImagesGrid.vue`
- 检索与复用策略：
  - 检索关键词：PostImagesGrid / touch / preview / original
  - 找到的旧实现：单指松手关闭 + 原图按钮
  - 最终选择：触摸判定改为单击关闭，精简功能
- 风险点：
  - 单击判定阈值过小可能不灵敏
- 验证方式：
  - 真机左右滑切图 + 单击关闭（未执行）
- 后续建议：
  - 如仍误触，可适当调大移动阈值

## [2026-01-10] 单指关闭与双指缩放互不干扰
- 背景/需求：原图状态下无法关闭，且双指缩放失效。
- 修改类型：fix
- 影响范围：前端组件
- 变更摘要：
  1) 触摸判定简化为“单指松手即关闭，双指缩放不关闭”
  2) 移除触摸事件的 prevent，避免阻断缩放手势
- 涉及文件：
  - `sanguidaily-front/src/components/PostImagesGrid.vue`
- 检索与复用策略：
  - 检索关键词：PostImagesGrid / touch / scale
  - 找到的旧实现：触摸判定逻辑
  - 最终选择：在现有逻辑上简化判定
- 风险点：
  - 单指滑动也会触发关闭
- 验证方式：
  - 真机单指点击/滑动关闭 + 双指缩放验证（未执行）
- 后续建议：
  - 若需保留单指滑动切换，可增加“滑动距离阈值”

## [2026-01-10] 预览点击关闭改为触摸判定
- 背景/需求：查看原图后点击图片无法关闭，需兼容缩放/拖拽场景。
- 修改类型：fix
- 影响范围：前端组件
- 变更摘要：
  1) 在 movable-view 上增加触摸判定，单指轻触关闭预览
  2) 触摸移动/双指缩放时不触发关闭
- 涉及文件：
  - `sanguidaily-front/src/components/PostImagesGrid.vue`
- 检索与复用策略：
  - 检索关键词：PostImagesGrid / movable-view / touch
  - 找到的旧实现：点击关闭逻辑
  - 最终选择：在现有组件内加入触摸判定
- 风险点：
  - 触摸阈值过小可能误触关闭
- 验证方式：
  - 真机点击/缩放/拖拽混合验证（未执行）
- 后续建议：
  - 若误触频繁，可放大阈值或延长判定时间

## [2026-01-10] 预览缩放与关闭交互修正
- 背景/需求：真机缩放不准确、点击图片无法关闭、切换图片索引不更新。
- 修改类型：fix
- 影响范围：前端组件
- 变更摘要：
  1) movable-view 在缩放=1 时不拦截滑动，恢复 swiper 切换
  2) 预览项与 movable-view 均可点击关闭
  3) scale-area 打开并居中布局，改善缩放体验
- 涉及文件：
  - `sanguidaily-front/src/components/PostImagesGrid.vue`
- 检索与复用策略：
  - 检索关键词：PostImagesGrid / movable-view / swiper / close
  - 找到的旧实现：自定义预览与缩放逻辑
  - 最终选择：在现有组件内微调交互与缩放属性
- 风险点：
  - 双指缩放体验仍受 movable-view 平台差异影响
- 验证方式：
  - 真机双指缩放 + 左右切换 + 点击图片关闭（未执行）
- 后续建议：
  - 若仍不准，可考虑回退系统预览或引入第三方手势组件

## [2026-01-10] 预览交互修复与支持双指缩放
- 背景/需求：预览无法点击关闭、可上下滚动影响体验、真机原图加载不稳定、需支持双指缩放。
- 修改类型：fix
- 影响范围：前端组件
- 变更摘要：
  1) 预览遮罩阻止页面滚动，避免上下滑动动态
  2) 预览改为 movable-view 支持双指缩放与拖拽
  3) “查看原图”仅切换显示策略，不再强制下载，避免真机下载失败
  4) 关闭预览时清理会话状态
- 涉及文件：
  - `sanguidaily-front/src/components/PostImagesGrid.vue`
- 检索与复用策略：
  - 检索关键词：PostImagesGrid / movable-view / preview / original
  - 找到的旧实现：预览遮罩与原图切换逻辑
  - 最终选择：在现有组件内增强交互，避免新增页面/组件
- 风险点：
  - movable-view 在部分平台的手势体验可能存在差异
- 验证方式：
  - 手动真机双指缩放、切换图片与关闭预览（未执行）
- 后续建议：
  - 如需更平滑，可考虑引入专用图片预览组件（评估后再定）

## [2026-01-10] 原图状态下点击图片仍可关闭预览
- 背景/需求：加载原图后点击图片无法关闭，需要恢复关闭能力。
- 修改类型：fix
- 影响范围：前端组件
- 变更摘要：
  1) 预览项容器与图片均响应点击关闭，避免点击失效
- 涉及文件：
  - `sanguidaily-front/src/components/PostImagesGrid.vue`
- 检索与复用策略：
  - 检索关键词：PostImagesGrid / preview / close
  - 找到的旧实现：图片点击关闭逻辑
  - 最终选择：增强点击命中范围，保持现有交互
- 风险点：
  - 极少数情况下误触关闭
- 验证方式：
  - 手动加载原图后点击图片关闭（未执行）
- 后续建议：
  - 如需防误触，可增加轻触提示或最小延迟

## [2026-01-10] 原图按钮仅在本次预览有效
- 背景/需求：点击“查看原图”后应隐藏按钮，但下次重新打开时应恢复显示小图和按钮。
- 修改类型：fix
- 影响范围：前端组件
- 变更摘要：
  1) 仅在本次预览会话内标记已加载原图
  2) 重新打开预览时重置为小图并显示“查看原图”
- 涉及文件：
  - `sanguidaily-front/src/components/PostImagesGrid.vue`
- 检索与复用策略：
  - 检索关键词：PostImagesGrid / preview / original
  - 找到的旧实现：原图按钮始终显示
  - 最终选择：新增会话内状态，不改缓存策略
- 风险点：
  - 若缓存原图存在，仍需用户点击才切换显示
- 验证方式：
  - 手动打开预览、加载原图、关闭后再打开验证按钮状态（未执行）
- 后续建议：
  - 可增加“已加载原图”提示

## [2026-01-10] 预览点击图片即可关闭
- 背景/需求：预览页的“关闭”按钮体验不好，需要改为点击图片关闭。
- 修改类型：fix
- 影响范围：前端组件
- 变更摘要：
  1) 移除预览工具栏中的关闭按钮
  2) 点击预览图片即触发关闭
- 涉及文件：
  - `sanguidaily-front/src/components/PostImagesGrid.vue`
- 检索与复用策略：
  - 检索关键词：PostImagesGrid / preview / close
  - 找到的旧实现：预览工具栏的关闭按钮
  - 最终选择：复用现有预览遮罩逻辑，仅调整交互
- 风险点：
  - 连续轻触可能误触关闭
- 验证方式：
  - 手动打开预览并点击图片关闭（未执行）
- 后续建议：
  - 可考虑增加轻微提示“再次点击关闭”

## [2026-01-10] 修复图片组件多根节点导致不渲染
- 背景/需求：修改预览逻辑后九宫格不显示，组件整体不渲染。
- 修改类型：fix
- 影响范围：前端组件
- 变更摘要：
  1) 为图片组件增加单一根容器，符合 Vue2/uni-app 模板约束
- 涉及文件：
  - `sanguidaily-front/src/components/PostImagesGrid.vue`
- 检索与复用策略：
  - 检索关键词：PostImagesGrid / template root
  - 找到的旧实现：组件内已有九宫格与预览视图
  - 最终选择：增加单一根容器，不引入新组件
- 风险点：
  - 无
- 验证方式：
  - 手动查看九宫格与预览是否恢复（未执行）
- 后续建议：
  - 保持组件模板单根约束，避免再次回归

## [2026-01-10] 预览改为省流模式并支持查看原图
- 背景/需求：多图预览仍偶发打不开，希望默认省流展示并可手动加载原图。
- 修改类型：fix
- 影响范围：前端组件
- 变更摘要：
  1) 图片预览改为组件内自定义遮罩展示，避免系统预览不稳定
  2) 默认使用缩略/省流地址展示，提供“查看原图”按钮按需下载
  3) 预览失败时显示可点击重试提示
- 涉及文件：
  - `sanguidaily-front/src/components/PostImagesGrid.vue`
- 检索与复用策略：
  - 检索关键词：PostImagesGrid / previewImage / downloadFile / image
  - 找到的旧实现：系统预览 + 下载缓存逻辑
  - 最终选择：复用现有组件内逻辑改造为自定义预览，避免新增模块
- 风险点：
  - 自定义预览不含系统缩放能力
  - 若无缩略图地址，省流效果有限
- 验证方式：
  - 手动点击多图预览、查看原图、重试提示（未执行）
- 后续建议：
  - 如图床支持缩略图参数，可补充 preview_url 以降低流量

## [2026-01-10] 下载失败原因记录与提示
- 背景/需求：多图预览偶发失败需要更明确原因提示，便于定位问题。
- 修改类型：fix
- 影响范围：前端组件
- 变更摘要：
  1) 记录下载失败原因（HTTP/网络/超时）
  2) 预览失败时提示具体原因
- 涉及文件：
  - `sanguidaily-front/src/components/PostImagesGrid.vue`
- 检索与复用策略：
  - 检索关键词：PostImagesGrid / downloadFile / previewImage / toast
  - 找到的旧实现：当前组件内下载与预览逻辑
  - 最终选择：在现有组件内追加原因记录与提示
- 风险点：
  - 提示文字可能在弱网场景频繁出现
- 验证方式：
  - 手动模拟弱网/超时并观察提示（未执行）
- 后续建议：
  - 可按错误类型做降级或自动重试

## [2026-01-10] 限制图片下载并发并复用下载承诺
- 背景/需求：多图动态存在“部分图片打不开/偶发恢复”，疑似并发下载/缓存竞争导致失败。
- 修改类型：fix
- 影响范围：前端组件
- 变更摘要：
  1) 增加全局下载队列与并发上限，避免多图同时下载挤压
  2) 同一 URL 复用下载 Promise，避免并发重复请求
  3) 预览当前图优先入队，确保首图稳定打开
- 涉及文件：
  - `sanguidaily-front/src/components/PostImagesGrid.vue`
- 检索与复用策略：
  - 检索关键词：PostImagesGrid / downloadFile / previewImage / cache
  - 找到的旧实现：当前组件内缓存与下载逻辑
  - 最终选择：在现有组件内限流与复用，避免新增模块
- 风险点：
  - 队列会延迟非当前图片的下载时机
- 验证方式：
  - 手动多次打开 2~9 张图动态观察是否仍出现打不开（未执行）
- 后续建议：
  - 若仍有失败，可记录失败原因并按错误类型降级

## [2026-01-10] 回退到系统预览版本
- 背景/需求：自定义预览页不可用，按用户要求回退。
- 修改类型：fix
- 影响范围：前端组件 / 路由
- 变更摘要：
  1) 移除图片预览页与路由
  2) 恢复系统预览与缓存写入逻辑
- 涉及文件：
  - `sanguidaily-front/src/components/PostImagesGrid.vue`
  - `sanguidaily-front/src/pages.json`
- 检索与复用策略：
  - 检索关键词：image-preview / previewImage
  - 找到的旧实现：系统预览版本
  - 最终选择：回退到上一个可用版本
- 风险点：
  - 系统预览仍可能受网络影响
- 验证方式：
  - 手动反复打开图片（未执行）
- 后续建议：
  - 如仍黑屏，可考虑增加失败重试提示

## [2026-01-10] 自定义预览页改为直接展示远程图片
- 背景/需求：系统预览多图场景出现黑屏转圈，自定义页需更稳定。
- 修改类型：fix
- 影响范围：前端页面 / 组件
- 变更摘要：
  1) 预览页使用 image 直接加载远程 URL
  2) 仅渲染当前与相邻图片，减少并发
  3) 失败可点按重试（追加随机参数）
- 涉及文件：
  - `sanguidaily-front/src/components/PostImagesGrid.vue`
  - `sanguidaily-front/src/pages/image-preview/index.vue`
  - `sanguidaily-front/src/pages.json`
- 检索与复用策略：
  - 检索关键词：image preview / swiper / previewImage
  - 找到的旧实现：系统预览与下载缓存预览
  - 最终选择：自定义预览页直连图片，降低黑屏概率
- 风险点：
  - 预览体验与系统预览不同（无缩放）
- 验证方式：
  - 手动滑动与重试验证稳定性（未执行）
- 后续建议：
  - 若需缩放可引入手势组件

## [2026-01-10] 加入图片缓存队列提升预览稳定性
- 背景/需求：同一动态部分图片可预览、部分黑屏转圈，需尽量在后台缓存已展示图片。
- 修改类型：fix
- 影响范围：前端组件
- 变更摘要：
  1) 图片加载后加入缓存队列，限制并发并重试
  2) 预览时优先使用已缓存路径，减少远程加载
- 涉及文件：
  - `sanguidaily-front/src/components/PostImagesGrid.vue`
- 检索与复用策略：
  - 检索关键词：previewCache / queue / downloadFile
  - 找到的旧实现：单次缓存与无队列重试
  - 最终选择：缓存队列 + 轻量重试
- 风险点：
  - 后台缓存会占用网络与存储
- 验证方式：
  - 手动多次预览与滑动观察稳定性（未执行）
- 后续建议：
  - 可加入缓存清理策略

## [2026-01-10] 回退自定义预览页并恢复系统预览
- 背景/需求：自定义预览页导致图片无法打开，需回退到上一个可用版本。
- 修改类型：fix
- 影响范围：前端组件 / 路由
- 变更摘要：
  1) 移除图片预览页与路由
  2) 恢复系统预览 + 缓存写入逻辑
- 涉及文件：
  - `sanguidaily-front/src/components/PostImagesGrid.vue`
  - `sanguidaily-front/src/pages.json`
- 检索与复用策略：
  - 检索关键词：image-preview / previewImage
  - 找到的旧实现：系统预览版本
  - 最终选择：回退到系统预览的稳定版本
- 风险点：
  - 系统预览仍受网络影响
- 验证方式：
  - 手动多次打开同一图片（未执行）
- 后续建议：
  - 若仍黑屏，可考虑只传已缓存图片或加入重试提示

## [2026-01-10] 预览改为自定义页并支持失败重试
- 背景/需求：系统预览偶发黑屏转圈，且部分图片无法打开。
- 修改类型：fix
- 影响范围：前端页面 / 组件
- 变更摘要：
  1) 九宫格点击跳转自定义预览页
  2) 预览页使用 swiper + 下载缓存 + 失败重试
  3) 预览数据通过本地存储传递，避免参数过长
- 涉及文件：
  - `sanguidaily-front/src/components/PostImagesGrid.vue`
  - `sanguidaily-front/src/pages/image-preview/index.vue`
  - `sanguidaily-front/src/pages.json`
- 检索与复用策略：
  - 检索关键词：previewImage / image-preview / swiper
  - 找到的旧实现：系统预览
  - 最终选择：自定义预览页，控制加载与重试
- 风险点：
  - 预览页与系统预览体验不同
- 验证方式：
  - 手动反复预览与滑动（未执行）
- 后续建议：
  - 如需缩放可扩展手势组件

## [2026-01-10] 增强图片下载失败提示与重试容错
- 背景/需求：同一动态部分图片可打开、部分一直转圈，需要定位失败原因并提高成功率。
- 修改类型：fix
- 影响范围：前端组件
- 变更摘要：
  1) 下载失败记录原因并提示（HTTP/超时/网络失败）
  2) 下载超时放宽至 15 秒
- 涉及文件：
  - `sanguidaily-front/src/components/PostImagesGrid.vue`
- 检索与复用策略：
  - 检索关键词：downloadFile / previewImage / 超时
  - 找到的旧实现：失败无原因提示
  - 最终选择：在现有下载逻辑中增加原因记录
- 风险点：
  - 更长超时会拉长等待时间
- 验证方式：
  - 手动点击多张图片查看失败原因提示（未执行）
- 后续建议：
  - 根据错误类型进一步做降级处理

## [2026-01-10] 预览列表恢复完整计数并减轻预加载
- 背景/需求：预览出现 1/1 且加载变慢，需要恢复完整计数并减轻等待。
- 修改类型：fix
- 影响范围：前端组件
- 变更摘要：
  1) 预览列表使用完整 URL 数组，已缓存则替换本地路径
  2) 移除全量预载，保留相邻预载与加载即缓存
- 涉及文件：
  - `sanguidaily-front/src/components/PostImagesGrid.vue`
- 检索与复用策略：
  - 检索关键词：previewImage / previewUrls / prefetch
  - 找到的旧实现：仅缓存列表导致 1/1、全量预载拖慢
  - 最终选择：完整列表 + 相邻预载
- 风险点：
  - 远程图片仍可能在滑动时加载失败
- 验证方式：
  - 手动滑动查看计数与加载表现（未执行）
- 后续建议：
  - 可对相邻预载加重试次数

## [2026-01-10] 已加载图片自动写入预览缓存
- 背景/需求：预览偶发黑屏转圈，要求把已显示的图片加入缓存。
- 修改类型：fix
- 影响范围：前端组件
- 变更摘要：
  1) 图片加载完成后自动下载并缓存本地文件
  2) 避免重复下载与并发冲突
- 涉及文件：
  - `sanguidaily-front/src/components/PostImagesGrid.vue`
- 检索与复用策略：
  - 检索关键词：PostImagesGrid / image load / previewCache
  - 找到的旧实现：仅预览时缓存
  - 最终选择：在图片加载完成时即缓存
- 风险点：
  - 页面渲染时会触发后台下载
- 验证方式：
  - 反复打开同一图片观察是否仍黑屏（未执行）
- 后续建议：
  - 如需节流可限制并发下载数量

## [2026-01-10] 回退系统预览并改为仅本地缓存列表
- 背景/需求：自定义预览页不可用，系统预览二次打开黑屏需规避。
- 修改类型：fix
- 影响范围：前端组件 / 路由
- 变更摘要：
  1) 移除自定义预览页与路由
  2) 系统预览仅传本地缓存图片，避免远程预载导致黑屏
  3) 背景预载所有图片用于下次打开
- 涉及文件：
  - `sanguidaily-front/src/components/PostImagesGrid.vue`
  - `sanguidaily-front/src/pages.json`
- 检索与复用策略：
  - 检索关键词：previewImage / image-preview
  - 找到的旧实现：自定义预览页
  - 最终选择：回退系统预览并强化缓存策略
- 风险点：
  - 首次打开可能仅显示 1/1，待缓存完成后可滑动更多
- 验证方式：
  - 手动多次打开同一图片观察是否仍黑屏（未执行）
- 后续建议：
  - 如需始终全量可在后台完成预加载提示

## [2026-01-10] 重新启用自定义预览页并移除系统预览依赖
- 背景/需求：系统预览二次打开黑屏转圈，需统一为可控加载方式。
- 修改类型：fix
- 影响范围：前端页面 / 组件
- 变更摘要：
  1) 九宫格改为跳转自定义预览页
  2) 预览页使用 swiper 单图加载 + 相邻预载 + 失败重试
  3) 使用本地存储传递 URL 列表与索引
- 涉及文件：
  - `sanguidaily-front/src/components/PostImagesGrid.vue`
  - `sanguidaily-front/src/pages/image-preview/index.vue`
  - `sanguidaily-front/src/pages.json`
- 检索与复用策略：
  - 检索关键词：previewImage / image-preview / PostImagesGrid
  - 找到的旧实现：系统预览 previewImage
  - 最终选择：自定义预览页避免系统黑屏转圈
- 风险点：
  - 预览页与系统预览体验略有差异
- 验证方式：
  - 手动预览与滑动切换（未执行）
- 后续建议：
  - 如需缩放可引入手势组件

## [2026-01-10] 预览回归轻量加载并持久化缓存
- 背景/需求：全量下载过慢且二次打开仍黑屏，需兼顾速度与稳定性。
- 修改类型：fix
- 影响范围：前端组件
- 变更摘要：
  1) 仅下载当前图后打开预览，预载相邻图
  2) 下载成功后保存到本地文件，避免临时文件失效
- 涉及文件：
  - `sanguidaily-front/src/components/PostImagesGrid.vue`
- 检索与复用策略：
  - 检索关键词：previewImage / saveFile / PostImagesGrid
  - 找到的旧实现：全量下载与临时文件缓存
  - 最终选择：轻量加载 + 持久化缓存
- 风险点：
  - 本地保存文件占用存储空间
- 验证方式：
  - 手动反复打开同一图片观察稳定性（未执行）
- 后续建议：
  - 可在合适时机清理保存文件

## [2026-01-10] 预览统一为全量下载后打开
- 背景/需求：二次打开出现黑屏转圈，需统一为“先加载后预览”方式。
- 修改类型：fix
- 影响范围：前端组件
- 变更摘要：
  1) 预览前下载全部图片，过滤失败项后再打开预览
- 涉及文件：
  - `sanguidaily-front/src/components/PostImagesGrid.vue`
- 检索与复用策略：
  - 检索关键词：previewImage / toLocalUrl / PostImagesGrid
  - 找到的旧实现：仅当前图加载 + 相邻预载
  - 最终选择：全量加载后统一打开，避免黑屏转圈
- 风险点：
  - 首次预览等待时间增加
- 验证方式：
  - 手动多次打开同一图片观察稳定性（未执行）
- 后续建议：
  - 如需更快可改为“当前+相邻”并仅传成功 URL

## [2026-01-10] 回退自定义预览页并恢复系统预览
- 背景/需求：自定义预览页无法正常打开，恢复系统预览以保证可用性。
- 修改类型：fix
- 影响范围：前端组件 / 路由
- 变更摘要：
  1) 移除图片预览页路由与页面
  2) 九宫格恢复系统预览，当前图优先缓存
- 涉及文件：
  - `sanguidaily-front/src/components/PostImagesGrid.vue`
  - `sanguidaily-front/src/pages.json`
- 检索与复用策略：
  - 检索关键词：image-preview / previewImage
  - 找到的旧实现：自定义预览页
  - 最终选择：回退到系统预览，保留缓存校验
- 风险点：
  - 系统预览仍可能受网络影响
- 验证方式：
  - 手动多次打开预览（未执行）
- 后续建议：
  - 如仍不稳定，可再评估轻量自定义预览

## [2026-01-10] 自定义图片预览页替代系统预览
- 背景/需求：系统预览偶发转圈，且希望滑动时再加载相邻图片。
- 修改类型：feat
- 影响范围：前端页面 / 组件
- 变更摘要：
  1) 新增图片预览页，使用 swiper 单图加载
  2) 预览支持相邻预载与失败重试
  3) 九宫格点击跳转到预览页
- 涉及文件：
  - `sanguidaily-front/src/pages/image-preview/index.vue`
  - `sanguidaily-front/src/components/PostImagesGrid.vue`
  - `sanguidaily-front/src/pages.json`
- 检索与复用策略：
  - 检索关键词：previewImage / PostImagesGrid
  - 找到的旧实现：系统预览 previewImage
  - 最终选择：自定义预览页替代系统预览，减少转圈
- 风险点：
  - 预览体验与系统预览略有差异
- 验证方式：
  - 手动点击预览与滑动切换（未执行）
- 后续建议：
  - 可加入手势缩放与保存按钮

## [2026-01-10] 修复预览缓存失效导致再次打开转圈
- 背景/需求：再次打开同一图片出现黑屏转圈，疑似临时文件失效。
- 修改类型：fix
- 影响范围：前端组件
- 变更摘要：
  1) 预览前对远程图片始终校验/刷新缓存
- 涉及文件：
  - `sanguidaily-front/src/components/PostImagesGrid.vue`
- 检索与复用策略：
  - 检索关键词：previewCache / toLocalUrl
  - 找到的旧实现：命中缓存直接返回，未校验文件有效性
  - 最终选择：统一走 ensureCached 校验
- 风险点：
  - 可能增加少量网络请求
- 验证方式：
  - 手动反复打开同一图片观察是否仍转圈（未执行）
- 后续建议：
  - 若网络不稳可增加重试次数

## [2026-01-10] 轻量预载相邻图片并修复详情页取参
- 背景/需求：滑动预览希望更顺滑；“查看详情”偶发显示未找到动态。
- 修改类型：fix
- 影响范围：前端组件 / 详情页
- 变更摘要：
  1) 预览后轻量预载相邻图片
  2) 详情页增加多来源取参，减少 id 丢失
- 涉及文件：
  - `sanguidaily-front/src/components/PostImagesGrid.vue`
  - `sanguidaily-front/src/pages/post-detail/index.vue`
- 检索与复用策略：
  - 检索关键词：previewImage / PostImagesGrid / post-detail
  - 找到的旧实现：仅当前图加载；详情页仅 onLoad 取参
  - 最终选择：轻量预载 + 多源取参，保持复用
- 风险点：
  - 预载仍会触发网络请求
- 验证方式：
  - 手动滑动预览与详情页跳转（未执行）
- 后续建议：
  - 若仍有丢参可记录日志辅助排查

## [2026-01-10] 单图预览支持左右切换
- 背景/需求：单图预览无法左右切换，希望滑动时再加载其他图片。
- 修改类型：fix
- 影响范围：前端组件
- 变更摘要：
  1) 预览列表恢复为整组 URL，仅当前图先行下载
  2) 保持单图优先加载，滑动时按需加载其余图片
- 涉及文件：
  - `sanguidaily-front/src/components/PostImagesGrid.vue`
- 检索与复用策略：
  - 检索关键词：previewImage / PostImagesGrid
  - 找到的旧实现：仅传单图 URL
  - 最终选择：轻量列表预览 + 当前图本地化
- 风险点：
  - 其余图片仍依赖网络加载
- 验证方式：
  - 手动预览左右切换（未执行）
- 后续建议：
  - 如需更稳定可为相邻图加预下载

## [2026-01-10] 预览改为单图并校验缓存有效性
- 背景/需求：多图预览导致加载变慢且部分图片后续打不开，需改为单图预览并避免失效缓存。
- 修改类型：fix
- 影响范围：前端组件
- 变更摘要：
  1) 预览仅打开当前图片，不再预加载整组
  2) 预览缓存若失效自动重新下载
- 涉及文件：
  - `sanguidaily-front/src/components/PostImagesGrid.vue`
- 检索与复用策略：
  - 检索关键词：previewImage / downloadFile / PostImagesGrid
  - 找到的旧实现：批量下载并预览整组
  - 最终选择：单图预览 + 缓存校验
- 风险点：
  - 每次预览都需确保当前图可下载
- 验证方式：
  - 手动多次点击同一图片验证稳定性（未执行）
- 后续建议：
  - 如需滑动查看可改为“当前+相邻1张”预载

## [2026-01-10] 预览失败时仅展示可下载图片
- 背景/需求：仍有图片预览转圈，需要避免失败 URL 进入预览列表。
- 修改类型：fix
- 影响范围：前端组件
- 变更摘要：
  1) 预览前下载所有图片并过滤失败项
  2) 当前图下载失败则提示并中止预览
- 涉及文件：
  - `sanguidaily-front/src/components/PostImagesGrid.vue`
- 检索与复用策略：
  - 检索关键词：previewImage / downloadFile / PostImagesGrid
  - 找到的旧实现：预览列表包含失败 URL
  - 最终选择：过滤失败 URL 并确保 current 可用
- 风险点：
  - 首次预览等待时间增加
- 验证方式：
  - 手动多次预览观察稳定性（未执行）
- 后续建议：
  - 若仍有失败可加入重试次数或降级为单图预览

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













