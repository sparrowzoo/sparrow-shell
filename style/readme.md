项目构建配置
---

构建前，将 `SPARROW_STYLE_DIR` 环境变量设为本仓库 `style` 目录的绝对路径。请将示例路径替换为本机实际路径：

```bash
export SPARROW_STYLE_DIR="/absolute/path/to/sparrow-shell/style"
```

使用 zsh 时，可将这行配置写入 `~/.zshenv`，供后续启动的 zsh 读取；当前终端也需执行上述 `export`。CI 构建同样需要设置该环境变量。

`sparrow-parent` 通过 `${env.SPARROW_STYLE_DIR}/sparrow_checkstyle.xml` 和 `${env.SPARROW_STYLE_DIR}/check_style_suppressions.xml` 统一配置 Checkstyle。继承该父 POM 的模块不需要重复声明插件或配置路径。

IDE 启动的 Maven 需要在其进程环境中读取到同一变量。已运行的 IDE 不会自动获得终端后来设置的变量；请在 IDE 的 Maven 运行环境中配置，或从已配置环境启动、重启 IDE。仅修改 `~/.zshenv` 不保证从图形界面启动的 IDE 能读取该变量。

checkstyle github
---
https://github.com/checkstyle/checkstyle.git


ningg  code style
---

https://checkstyle.sourceforge.io/google_style.html

code style
---
https://raw.githubusercontent.com/google/styleguide/gh-pages/intellij-java-google-style.xml

style plugin
---
https://maven.apache.org/plugins/maven-checkstyle-plugin/index.html

https://maven.apache.org/plugins/maven-checkstyle-plugin/usage.html


<p> Specifies the location of the XML configuration to use. </p> <p/> <p> Potential values are a filesystem path, a URL,
    or a classpath resource. This parameter expects that the contents of the location conform to the xml format
    (Checkstyle <a href="http://checkstyle.sourceforge.net/config.html#Modules">Checker module</a>) configuration of
    rulesets. </p> <p/> <p> This parameter is resolved as resource, URL, then file. If successfully resolved, the
    contents of the configuration is copied into the
    <code>${project.build.directory}/checkstyle-configuration.xml</code> file before being passed to Checkstyle as a
    configuration. </p> <p/> <p> There are 2 predefined rulesets. </p>
<ul>
    <li><code>sun_checks.xml</code>: Sun Checks.</li>
    <li><code>google_checks.xml</code>: Google Checks.</li>
</ul>

内置的style
---
https://github.com/checkstyle/checkstyle/tree/master/src/main/resources

upgrade check style
---
https://maven.apache.org/plugins/maven-checkstyle-plugin/examples/upgrading-checkstyle.html

