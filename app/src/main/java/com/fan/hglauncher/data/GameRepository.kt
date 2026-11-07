package com.fan.hglauncher.data

/** 静态演示数据；接真实数据时替换为接口拉取 + Room 缓存 */
object GameRepository {

    val games: List<Game> = listOf(
        Game("ak", "明日方舟", "已安装 · 2.6.01", "R", true, isNew = true),
        Game("ex", "来自星尘", "已安装 · 1.1.4", "X", true),
        Game("popu", "泡姆泡姆", "未安装", "P", false),
        Game("endfield", "明日方舟\n终末地", "预约中", "E", false)
    )

    val news: List<NewsItem> = listOf(
        NewsItem("n1", "HOT", "「熔火与冰原」版本更新内容一览", "11-06", hot = true),
        NewsItem("n2", "活动", "限时活动「寒潮将至」开启，参与获取合成玉", "11-05"),
        NewsItem("n3", "维护", "11 月 8 日 04:00 全区停机维护公告", "11-04")
    )
}
