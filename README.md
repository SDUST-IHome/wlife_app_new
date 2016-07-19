# WLife_v03
WLife_v02 项目重构

## 2016-07-11

- java文件分类管理
    - activity
    - fragment
    - adapter
    - data
    - util

- xUtils 跟findViewById & volley 说再见

- WelcomeActivity逻辑更改
    - 直接跳转至LoginActivity判断
    - 入口统一

- 去除CircularProgressButton
    - 使用ProgressDialog
    - 使用Snackbar

- LoginActivity 去除Card

- RegisterActivity 去除Card

## 2016-07-12

- MainActivity参考ZigSys的设计

- bindGateActivity 参考ZigSys的设计
    - todo 添加按照房间选择

## 2016-07-16

- addDeviceActivity 建成,需要重新设计

- 登录流程优化 使用ProgressDialog

- 确定在ZigSys后台和数据库基础上修改

- Fragment Item 新增上下文菜单,操作逻辑更明显

- Msg 由 Fragment 更改为 Activity 并增加筛选功能

- 设置页面使用 PreferenceScreen 原生设计

## 2016-07-18

- 二维码绑定网关功能已完成 绑定设备待定

- 更改数据库 新增sign 与 place字段

- 设备icon可根据数据库更改颜色 待完成分布图

## 2016-07-19

- 实现布局的动态加载与分布图的设备位置显示

- 设置页面完成大半 新增右滑返回

- 优化登录和解绑流程


## TODO

- 空调 frag && item
- addDeviceActivity
