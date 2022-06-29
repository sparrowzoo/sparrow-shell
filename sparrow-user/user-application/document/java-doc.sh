#javadoc -d java-doc-directory -subpackages java-package -sourcepath java-src-path -encoding utf-8 -charset utf-8 -private
#with":"
javadoc \
-classpath \
/Users/laibatour/sparrow/sparrow/sparrow/target/sparrow.jar:\
/Users/laibatour/sparrow/sparrow-integration/sparrow-user/api/target/sparrow-user-api.jar \
-subpackages \
com.sparrow.user:\
com.sparrow.support.protocol \
-exclude \
com.sparrow.user.controller.facade.impl:\
com.sparrow.user.api:\
com.sparrow.user.controller.facade.assemble \
-d protocol \
-sourcepath ../src/main/java:\
/Users/laibatour/sparrow/sparrow/sparrow/src/main/java:\
/Users/laibatour/sparrow/sparrow-integration/sparrow-user/api/src/main/java \
-encoding utf-8 \
-charset utf-8  \
-private
