SystemAPI 包含功能：
    API26的通知栏，
    发短信，
    FatAar打包（先要gradle中编译fataarinner，然后fataar，然后复制到app的libs目录下）,
    retrofit网络接口

Widget
    AppSettingsDialog 申请跳转app详细页的dialog
    AddSpaceTextWatcher  EditText数据监听器
    AutoLoadImageView 通过URLConnection从url加载图片并缓存在CacheDir文件夹
    BundleEditText  用于多个EditText的同步粘贴数字
    CircleProgressButton 环形进度条按钮
    CustomDrawerLayout 自定义滑动边界的抽屉布局
    GridSpacingItemDecoration
    JustifyTextView 中文对齐
    PressImageView 点击时显示明暗变化(滤镜效果)的ImageView
    plurals 文字

Utils
    executor 线程池
    AndroidComponentUtil  查控应用，跳转电话、设置，检查应用是否安装
    ApachePoiUtil word文档阅读
    BaseActivity 含回退逻辑的AppCompatActivity
    Clock 系统时间
    CustomTabsHelper
    DisplayUtils 分辨率转换，屏幕保亮，屏解锁，软键盘，导航栏高度，状态栏颜色高度，设置Editetxt Hint，设置TextView光标
    DocumentDownloadHelper 用DownloadManager下载文件
    FingerprintUtil 指纹识别工具类
    IntentUtils
    JavaUtils 含UUID，int转Byte，浮点除法
    JsonHelp Gson处理
    KeyEventUtils 发送key事件(跨应用需要平台签名)、
    MapUtils 高德和百度网页地图
    NetworkUtils 判断网络连接
    NetworkViewModel  用于返回网络状态的Rx观察者
    PictureUtils 图片读取和处理，Drawable和Bitmap和InputStream互转
    PreferencesHelper
    QRUtils 二维码生成
    RxEventBus 用RX实现的EventBus
    SchedulerProvider Rx线程切换
    ShareUtil 分享工具类
    StringUtils 单词首字符大写

EncryptActivity RSA加密解密，MD5哈希

FragmentPager 展示预制fragment会遇到的错误及 instantiateItem 的正确写法 list.set 。旋转手机后重新建了FragmentPagerActivity，此时系统会自动调用 instantiateItem，所以要重新保存list。

TouchEvent 触屏事件View层级分发

DragWindow 用于置顶window的拖动

ScrollPicker 滑动选择器

Nexus & HttpServer 私服maven仓库，手机HTTP服务

Slidr 右滑退出当前Activity渐变动画

WebView 浏览器