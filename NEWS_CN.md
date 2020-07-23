# v3.2 release(2020/07/23)
1. 修复 当数据刷新时 RecyclerViewStickyHeadItemDecoration 未刷新bug。
2. 加入模版自定义viewtype类 RecyclerViewItemWrapper 。
3. 加入RecyclerView的点击监听器 OnRecyclerViewItemClickListener 和长按监听器 OnRecyclerViewItemLongClickListener 。

# v3.1 release(2019/12/27)
1. 修复 RecyclerViewStickyHeadItemDecoration bug。
2. RecyclerViewLinearSpaceItemDecoration.Builder 加入属性ignoreTypes。

# v3.0 release(2019/10/23)
1. RecyclerViewItemDecoration 变成 RecyclerViewLinearItemDecoration 和 RecyclerViewGridItemDecoration (不支持带透明通道的颜色值).
2. RecyclerViewSpaceDecoration 变成 RecyclerViewLinearSpaceItemDecoration 和 RecyclerViewGridSpaceItemDecoration  ( 支持 StaggeredGridLayoutManager) .

# v2.1 release(2019/09/20)
修复适配LinearLayoutManger模式的RecyclerView内边距padding。

# v2.0 发布(2019/07/02)
更新使用androidx libs.

# v1.3.2 发布(2018/11/28)
加入RecyclerViewStickyHeadItemDecoration，自动将分组样式变成sticky head模式。

# v1.3.1 发布(2018/08/27)
修复ItemDecoration的Grid模式横向间距的bug。

# v1.3 发布(2018/04/13)
合并 [RecyclerViewDecoration](https://github.com/arjinmc/RecyclerViewDecoration) 库到此库，扩展RecyclerViewGroupItemDecoration，正式公开发布此库。