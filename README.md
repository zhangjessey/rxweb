# rxweb

*[rxweb](https://github.com/zhangjessey/rxweb)* based on [rxweb](https://github.com/sdeleuze/rxweb) , [smart-framework](https://gitee.com/huangyong/smart-framework) and [nano-rxnetty-mvc-server](https://bitbucket.org/gt_tech/nano-rxnetty-mvc-server/)

It's a RxJava + RxNetty based micro web framework

## [中文README](https://github.com/zhangjessey/rxweb/blob/master/README-CN.md)

## Features
* Annotated Route and Functional route
* Supported HTTP methods - GET, POST, PUT, DELETE
* Maintains non-blocking feature of RxNetty
* Compatible with Java 8
* MVC framework current support is limited to JSON request and responses
* Supports REST URI Path variables (automatically detect path variables to be extracted from HTTP Request URI based on path definition in annotated Route or functional Route)

## Dependencies
* Jackson (for JSON support)
* RxNetty (includes *netty* dependencies)
* RxJava
* SLF4J (with static binding to Logback)
* Reflections
* Google Guava

## Ways to use

* Run as a standalone app
    * User application will be required to satisfy all dependencies and start the server

## How to use
### Annotated Route

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

### Functional Route

```java
nettyServer.get("/functionalRoute/{a}/{b}", 
	(request, response) -> 
		response.writeString(Observable.<String>just("this is functionalRoute".concat(request.getUrlParams().toString())))).
post("/functionalRoutePost", 
	(request, response) -> 
		response.writeString(Observable.<String>just("this is functionalRoutePost")));
```

## Future potential enhancements
*(time-permitting and if there's interest)*

* @RequestMapping support multi method,not contains just one method，just like Get...
* Interceptors support
* Websocket support
* SpringBoot starter
* Support for other Mime/Types

## Contributing
Contributions are highly appreciated, it is encouraged to submit a PULL request or issue.

Contributors must ensure that existing test cases pass (or are modified to adjust to their changes)

## LICENSE
Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at

http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.
