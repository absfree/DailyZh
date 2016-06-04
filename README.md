#知乎日报第三方客户端
##效果预览
![](https://github.com/absfree/DailyZh/blob/master/demo/demo.gif)  
##apk下载
[DailyZh](http://pan.baidu.com/s/1eS8YqaQ)
##依赖的库
[Gson](https://github.com/google/gson)
##API
[知乎日报API](https://github.com/izzyleung/ZhihuDailyPurify/wiki/知乎日报-API-分析)
##源码结构

  |- activity
  
    |- SplashActivity: 启动界面
    
    |- MainActivity: 主界面
    
    |- NewsContentActivity: 详情界面
    
  |- adapter
  
    |- MainNewsAdapter: 主界面列表适配器
    
    |- NewsItemAdapter: 具体主题下的新闻列表适配器
  
  |- customView
    
    |- Carousel: 主界面轮播控件
    
  |- fragment
  
    |- BaseFragment: 为fragments定义了基本的状态与行为
    
    |- MainFragment: 主界面
    
    |- ThemeFragment: 特定主题
    
    |- ThemeListFragment: 主题列表，内嵌到主界面布局中
    
  |- model
  
    |- LatestNews: 用于今日热闻的反序列化
    
    |- NewsContent: 用于具体新闻内容的反序列化
    
    |- PrevNews: 用于过去新闻的反序列化
    
    |- Story: 用于主界面列表项的反序列化
    
    |- Theme: 用于单个主题的反序列化
    
    |- ThemeNews: 用于特定主题内容的反序列化
    
  |- util
  
    |- imageLoader: 自定义图片加载缓存库
    
    |- httpUtil: 自定义网络请求库
    
    |- Constants
    
##关于我
Mail：openmindabs@gmail.com

Blog: http://www.cnblogs.com/absfree/

##License

Copyright 2014 absfree

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

   http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
    
