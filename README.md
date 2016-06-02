#知乎日报第三方客户端
##效果预览
![](http://thumbnail0.baidupcs.com/thumbnail/209f6060c36b727a786baf5a3e4f5fa2?fid=1178534374-250528-68074144652419&time=1464868800&rt=sh&sign=FDTAER-DCb740ccc5511e5e8fedcff06b081203-W39zfNjM5we6jQTqZlplG8YZAqw%3D&expires=8h&chkv=0&chkbd=0&chkpc=&dp-logid=3568704862234430972&dp-callid=0&size=c710_u400&quality=100)  
##依赖的库
[Gson](https://github.com/google/gson)
##源码结构
|- dailyzhi

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
  
    |- BaseFragment: 为fragments
    
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
    
