
添加新项目
---
```
git submodule add git@github.com:sparrowzoo/sparrow-bom.git                 sparrow-bom
git submodule add git@github.com:sparrowzoo/sparrow.git                     sparrow
git submodule add git@github.com:sparrowzoo/sparrow-loader.git              sparrow-loader
git submodule add git@github.com:sparrowzoo/sparrow-container.git           sparrow-container
git submodule add git@github.com:sparrowzoo/sparrow-data-source.git         sparrow-data-source
git submodule add git@github.com:sparrowzoo/sparrow-json.git                sparrow-json
git submodule add git@github.com:sparrowzoo/sparrow-log.git                 sparrow-log
git submodule add git@github.com:sparrowzoo/sparrow-mvc.git                 sparrow-mvc
git submodule add git@github.com:sparrowzoo/sparrow-orm.git                 sparrow-orm
git submodule add git@github.com:sparrowzoo/sparrow-rocketmq-client.git     sparrow-rocketmq-client
git submodule add git@github.com:sparrowzoo/sparrow-kafka-client.git        sparrow-kafka-client
git submodule add git@github.com:sparrowzoo/sparrow-sharded-jedis.git       sparrow-sharded-jedis
git submodule add git@github.com:sparrowzoo/style.git                       style
git submodule add git@github.com:sparrowzoo/sparrow-aop.git                 sparrow-aop
git submodule add git@github.com:sparrowzoo/sparrow-registry.git            sparrow-registry
git submodule add git@github.com:sparrowzoo/sparrow-test.git                sparrow-test
git submodule add git@github.com:sparrowzoo/sparrow-distribution-job.git    sparrow-distribution-job
git submodule add git@github.com:sparrowzoo/sparrow-distribution-config.git    sparrow-distribution-config
git submodule add git@github.com:sparrowzoo/sparrow-protocol.git	    sparrow-protocol

```       
    	
`注:以上代码在新添加项目里，会自动clone 到本地,并会在.gitmodules 文件中自动添加子项目配置，提前是项目的仓库已存在`

初始化 
---

	git clone git@github.com:sparrowzoo/sparrow-shell.git
	cd sparrow-shell
	git checkout develop
	git submodule update --init
	git submodule foreach git checkout develop
	
批量命令
----

	git submodule foreach git ...[fetch|pull|checkout...]


