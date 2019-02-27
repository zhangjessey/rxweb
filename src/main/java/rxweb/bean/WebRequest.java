package rxweb.bean;

/*******************************************************************************
 * Copyright (c) 2017 @gt_tech
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *******************************************************************************/


import io.netty.buffer.ByteBuf;
import io.reactivex.netty.protocol.http.server.HttpServerRequest;
import rxweb.server.WebRequestHandler;

import java.util.List;
import java.util.regex.Matcher;

/**
 * 内部请求对象，包裹HttpServerRequest
 *
 * @author gt_tech
 * @author zhangjessey
 */
public class WebRequest<T> {

    private final HttpServerRequest<ByteBuf> httpServerRequest;

    private Matcher requestPathMatcher;

    private List<Object> urlParams;

    private WebRequestHandler<ByteBuf, ByteBuf> webRequestHandler;

    private T body;

    private final String requestContentType;

    private final String responseAcceptType;


    public WebRequest(final HttpServerRequest<ByteBuf> httpServerRequest) {

        this.httpServerRequest = httpServerRequest;
        this.requestContentType = httpServerRequest.getHeader("Content-Type");

        this.responseAcceptType = httpServerRequest.getHeader("Accept");
    }


    public HttpServerRequest<ByteBuf> getHttpServerRequest() {
        return httpServerRequest;
    }


    public T getBody() {
        return body;
    }


    public void setBody(T body) {
        this.body = body;
    }

    public String getRequestContentType() {
        return requestContentType;
    }

    public String getResponseAcceptType() {
        return responseAcceptType;
    }

    public Matcher getRequestPathMatcher() {
        return requestPathMatcher;
    }

    public void setRequestPathMatcher(Matcher requestPathMatcher) {
        this.requestPathMatcher = requestPathMatcher;
    }

    public List<Object> getUrlParams() {
        return urlParams;
    }

    public void setUrlParams(List<Object> urlParams) {
        this.urlParams = urlParams;
    }

    public WebRequestHandler<ByteBuf, ByteBuf> getWebRequestHandler() {
        return webRequestHandler;
    }

    public void setWebRequestHandler(WebRequestHandler<ByteBuf, ByteBuf> webRequestHandler) {
        this.webRequestHandler = webRequestHandler;
    }
}