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

