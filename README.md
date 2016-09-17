# ZhihuDaily
A demo app of Zhihu Daily based on MVP + RxJava + Retrofit2 + Dagger2 .

仿知乎日报，基于 MVP + RxJava + Retrofit2 + Dagger2 .

本应用是个人的实践项目，目的在于总结归纳近来学习到的新技术，融会贯通。本应用基本涵盖了Android开发最常用的主流框架，界面清新简洁，实现了知乎日报的主要功能。

该项目遵循 google Android 编码规范，适度融合了当前主流框架，针对过度绘制、内存泄漏问题进行了优化，并对 Model 层和 Presenter 层编写了单元测试，项目相对简单，对于学习有一定的参考意义。

本项目仅做学习交流使用，API数据内容所有权归原作公司所有，请勿用于其他用途

##Preview

![](https://github.com/hefuyicoder/ZhihuDaily/blob/master/preview/启动.gif)

![](https://github.com/hefuyicoder/ZhihuDaily/blob/master/preview/内容.gif)

![](https://github.com/hefuyicoder/ZhihuDaily/blob/master/preview/夜间模式.gif)

##Points
- 参考 google 官方 MVP + Dagger2 架构,项目架构清晰，模块间耦合低
- 使用 RxJava 配合 Retrofit2 做网络请求
- 使用 Retrofit2 + okhttp3 实现网络缓存
- 仿知乎官方的夜间模式，实现带渐变效果的流畅切换，无需重启
- 使用 Mockito 和 Robolectric 编写 Model 层和 Presenter 的单元测试

##Version
- 1.0 Beta

##Contact Me
- Github: github.com/hefuyicoder
- Email: hefuyicoder@gmail.com

