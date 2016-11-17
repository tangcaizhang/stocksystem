# StockSystem
股票拟购，个人断断续续利用空闲时间维护的项目，充当个人项目模板，今后实际开发学习中经常使用、遇到的点都会持续迭代更新进去。<br/>
后端项目源码见 https://github.com/zackzhou915/stocksystem

1. 基于*Material Design*的应用界面(引入了design库)。<br />
2. 以个人见解的*MVP模式*对项目进行分层架构。<br />
3. 集成*RxAndroid*来组织异步任务链，控制线程调度。<br />
4. 常用的下拉刷新(Google自带)、上拉加载、轮换图等组件。 <br />
5. 使用鸿洋大神的[Android AutoLayout](https://github.com/hongyangAndroid/AndroidAutoLayout)作为布局适配方案，原则上均可自适应。

股票数据来源于  [聚合API](https://www.juhe.cn/docs/api/id/21) <br />
推送服务来源 [极光推送](https://www.jiguang.cn)，目前对普通用户免费，对小型项目可以快速集成推送<br />

####Update
重构了MVP中M层和P层的接口，现在Model层的接口统一为同步的。<br/>
后台线程和主线程的调度都交给Presenter来做，加入RxAndroid组织异步任务链，这样逻辑清晰点、嵌套也少。<br/>
使用泛型基类减少部分冗余代码。<br/>

####Future
持久层的例子(待完善)<br/>
更新下载的例子(待完善)<br/>
单间测试架构(待完善)<br/>

####References
*googlesamples* https://github.com/googlesamples/android-architecture<br/>
*RxAndroid* http://gank.io/post/560e15be2dca930e00da1083

##Screen Shots
<img src="https://github.com/zackzhou915/stocksystem/blob/master/screenshot/stock_list.png" width = "270" height = "480" alt="s1" align=center />
<img src="https://github.com/zackzhou915/stocksystem/blob/master/screenshot/stock_detail.png" width = "270" height = "480" alt="s2" align=center />
<img src="https://github.com/zackzhou915/stocksystem/blob/master/screenshot/favor.png" width = "270" height = "480" alt="s3" align=center />

####License

	Licensed under the Apache License, Version 2.0 (the "License");
	you may not use this file except in compliance with the License.
	You may obtain a copy of the License at

	   http://www.apache.org/licenses/LICENSE-2.0

	Unless required by applicable law or agreed to in writing, software
	distributed under the License is distributed on an "AS IS" BASIS,
	WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
	See the License for the specific language governing permissions and
	limitations under the License.
