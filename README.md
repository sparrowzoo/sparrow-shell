init 创建新项目或添加子模块(下列代码，不需要执行) 创建仓库后直接加入到submodule
------------------------------------------------
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
	git submodule add git@github.com:sparrowzoo/sparrow-sharded-jedis.git       sparrow-sharded-jedis
	git submodule add git@github.com:sparrowzoo/style.git                       style
	git submodule add git@github.com:sparrowzoo/sparrow-aop.git                 sparrow-aop
	git submodule add git@github.com:sparrowzoo/sparrow-registry.git            sparrow-registry
	git submodule add git@github.com:sparrowzoo/sparrow-test.git                sparrow-test
    git submodule add git@github.com:sparrowzoo/sparrow-distribution-job.git    sparrow-distribution-job
    git submodule add git@github.com:sparrowzoo/sparrow-distribution-config.git    sparrow-distribution-config
            	
客户端批量代码clone
---

初始化 
---

	git clone git@github.com:sparrowzoo/sparrow-shell.git
	cd sparrow-shell
	git submodule update --init
	git submodule foreach git checkout master
	
批量命令
----

	git submodule foreach git ...[fetch|pull|checkout...]


