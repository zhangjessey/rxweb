# rxweb

[![Build Status](https://travis-ci.org/zhangjessey/rxweb.svg?branch=master)](https://travis-ci.org/zhangjessey/rxweb)
[![codecov](https://codecov.io/gh/zhangjessey/rxweb/branch/master/graph/badge.svg)](https://codecov.io/gh/zhangjessey/rxweb)
[![License](https://img.shields.io/badge/License-Apache%202.0-blue.svg)](https://opensource.org/licenses/Apache-2.0)

*[rxweb](https://github.com/zhangjessey/rxweb)* 基于 [rxweb](https://github.com/sdeleuze/rxweb) , [smart-framework](https://gitee.com/huangyong/smart-framework) and [nano-rxnetty-mvc-server](https://bitbucket.org/gt_tech/nano-rxnetty-mvc-server/)

这是一个基于 RxJava + RxNetty 的微型web框架


## 特性
* 注解式路由与函数式路由
* 支持的HTTP方法 - GET, POST, PUT, DELETE
* 维持RxNetty的非阻塞特性
* 与Java 8兼容
* 作为MVC框架当前只支持JSON请求与响应
* 支持REST URI的路径参数(基于注解式路由或者函数式路由，自动检测从HTTP请求中提取出来的路径参数)


## 依赖
* Jackson (为了支持JSON)
* RxNetty (包含Netty依赖)
* RxJava
* SLF4J (静态绑定到Logback)
* Reflections
* Google Guava

## 启动方式

* 作为一个独立的应用运行
    * 用户的应用需要满足所有的依赖，并且启动服务器

## 如何使用
### 注解式路由

```java
@Controller
public class TestController {
    @RequestMapping.Post(value = "/postBean/{c}")
    public Observable<Params> postBean(@RequestBody User user, int c, Params params) {

        HashMap<String, Object> stringStringHashMap = new LinkedHashMap<>(5);
        stringStringHashMap.put("result", "success");
        stringStringHashMap.put("id", user.getId());
        stringStringHashMap.put("name", user.getName());
        stringStringHashMap.put("pathValue", c);
        stringStringHashMap.put("param_a_value", params.getMap().get("a"));
        Params p = new Params(stringStringHashMap);

        return Observable.just(p);

    }
}
```

### 函数式路由

```java
nettyServer.get("/functionalRoute/{a}/{b}",
	(request, response) ->
		response.writeString(Observable.<String>just("this is functionalRoute".concat(request.getUrlParams().toString())))).
post("/functionalRoutePost",
	(request, response) ->
		response.writeString(Observable.<String>just("this is functionalRoutePost")));
```

## 未来改进点
*(如果时间允许并且有价值的话)*

* @RequestMapping 注解支持多种方法，不是仅包含一种方法,比如仅支持Get
* 拦截器支持
* Websocket支持
* SpringBoot starter支持
* 支持其他Mime/Types

## 贡献
非常欢迎提供贡献, 鼓励提PR或者issue.

贡献者务必确保已存在的测试用例通过 (或者修改它以适应新的变化)

## LICENSE
Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at

http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.
