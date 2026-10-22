<div align="center">
  <img src="https://raw.githubusercontent.com/mortuusars/resources/main/media/exposure/main.png" width="225">
  <a href="https://modrinth.com/mod/exposure-unofficial"><img src="https://img.shields.io/modrinth/dt/hB899VmG?logo=modrinth&label=&suffix=%20&style=flat&color=242629&labelColor=5ca424&logoColor=1c1c1c" alt="modrinth"></a>
  <br>
</div>

<h1></h1>

<div align="center">
  <h3>[Unofficial]Camera mod for Minecraft with focus on process and aesthetics</h3>
  <a href="https://modrinth.com/mod/exposure-unofficial">Wiki</a>
</div>

## Minecraft 26.3 / Fabric

此目录为 Fabric 26.3 移植版，使用 Java 25、Fabric Loader 0.19.5、Fabric API 0.163.0+26.3 和 Forge Config API Port 26.3.1。Gradle Wrapper 为 9.7.0，Loom 为 1.17.21；使用 Minecraft 官方未混淆名称。未启用 NeoForge 构建，也未恢复原先停用的 Create Fabric 集成。

### 可选配方查看器

JEI 与 REI 均为可选依赖，不会打包进 Exposure，也不影响未安装它们时加载 Mod。

- JEI 编译版本：31.12.0.66。
- REI 编译版本：26.3.824。安装时还需满足 REI 自身声明的 Architectury API、Cloth Config 依赖。
- REI 提供黑白/彩色照片打印、照片堆叠/拆分说明、相框信息页，以及胶卷冲洗、照片复制等特殊无序合成配方显示。
- 特殊合成配方取自实际服务端配方管理器，复用 REI 自身的显示同步。单人游戏由内置服务端提供；多人游戏要完整显示这些配方，服务端也需要安装 REI。仅客户端安装时，打印/堆叠说明仍可注册，但不能保证显示服务端自定义特殊配方。
- 只有 REI 已注册匹配的特殊合成显示时，才隐藏 Exposure 对应的补充配方提示。原有 `ui.recipe_tooltips_without_jei` 配置键保留，新增 `integration.rei_information` 控制 REI 相框信息页。
- 打印和堆叠页面用于操作说明，不提供自动填充。暗房胶卷页签、相册及重命名界面已声明 REI 避让区域。

### 构建与开发运行

在本目录使用 Java 25 执行：

```powershell
.\gradlew.bat build
.\gradlew.bat :fabric:runClient -Precipe_viewer=none
.\gradlew.bat :fabric:runClient -Precipe_viewer=jei
.\gradlew.bat :fabric:runClient -Precipe_viewer=rei
```

`recipe_viewer` 默认为 `jei`，还支持 `both` 用于共存检查；此开关只控制开发运行依赖。发布文件为 `fabric/build/libs/exposure-fabric-26.3-1.9.18.jar`，请勿使用 `dev-shadow` 或 `sources` 文件。

### 手动验收清单

本次实机验收由维护者手动完成，以下项目尚不代表已验收通过：

- [ ] 无 JEI/REI、仅 JEI、仅 REI，以及同时安装两者时，进入新世界和已有世界；检查 `latest.log` 中是否存在 Mixin、注册表或资源加载错误。
- [ ] 安装胶卷、调整焦距与快门、拍摄、冲洗、打印、复制照片；检查滤镜、手持照片、相框和三脚架渲染。
- [ ] 暗房胶卷页签左右键及 Shift 点击；相册翻页、添加/移除照片、文本输入、重命名和签名；检查左右 Ctrl/Shift、数字键盘加减键、按键重复及输入法。
- [ ] REI 查询胶卷冲洗、照片复制、两种打印、堆叠/拆分与相框说明；检查配方输入输出、标签替代材料、界面遮挡和重复提示。
- [ ] 修改服务端特殊配方后 `/reload`，重新连接/切换世界，确认 REI 显示同步更新；比较服务端有/无 REI 的表现。
- [ ] 独立服务端无 REI 与安装 REI 两种启动；联机检查拍摄、照片传输、配方同步和相册物品操作。
- [ ] 按需安装 Jade 26.3.5、Real Camera 0.7.8-beta-26.3、Easy Anvils 26.3.1，检查现有联动；这些组合尚未进行实机验收。

数据包作者需同步使用 26.3 的 loot `type` / `condition` / `modifier` 格式和 `recipe_unlocked` 的 `recipes` 列表；自定义摄影进度中的 `player`、`entities_in_frame` 条件使用新的 loot predicate 格式。仓库内置数据与示例包版本已相应调整。

### 本次自动化自检（2026-10-22）

Code Review: **PASS WITH RISKS**。范围为本目录的 Fabric 构建、源码、Mixin/AW、可选兼容、资源、测试及文档；无已知 BLOCKER / MAJOR。

- `.\gradlew.bat build --rerun-tasks`：通过全量编译、单元测试和 Access Widener 校验。
- 资源自检修正后再次执行 `.\gradlew.bat build`：通过，共 43 项测试，0 失败、0 错误、0 跳过。新增回归覆盖 SDL 按键行为和合并后的 Fabric 进度资源。
- `.\gradlew.bat :fabric:dependencies --configuration runtimeClasspath -Precipe_viewer=<none|jei|rei|both>`：四种配置全部解析成功，依赖组合符合开关设置。
- 静态资源检查：248 个 JSON/元数据文件可解析，15 个配方解锁条件及配方奖励引用有效；示例包版本与 Minecraft 26.3 的资源包 97.1、数据包 121.0 一致。
- 发布 JAR 检查：入口点、Mixin 类和资源齐全；REI API 引用限于独立集成包；未内嵌 JEI、REI、Jade、Real Camera、Architectury 或 Cloth Config。
- 自检期间修正了 Fabric 三脚架进度的旧字段、四个过期配方奖励 ID、Easy Anvils 目标包名，以及相册两端操作的丢弃动画预测标记。

剩余风险：最终版本的世界加载、完整拍摄/冲洗/打印流程、独立服务端、REI 联机同步及第三方 Mod 共存由维护者按上述清单实机验收。此前无查看器与 REI 开发客户端已启动至标题界面，但不作为最终游戏内功能验收。编译仍有原有 JEI 弃用 API、unchecked 以及 Gradle 未来版本兼容性警告；当前固定工具链构建通过。
