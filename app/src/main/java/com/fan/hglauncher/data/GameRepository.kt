package com.fan.hglauncher.data

/** 静态演示数据；接真实数据时替换为接口拉取 + Room 缓存 */
object GameRepository {

    val games: List<Game> = listOf(
        Game(
            id = "ak", title = "明日方舟", status = "已安装 · 2.6.01", glyph = "R",
            installed = true, isNew = true, packageName = "com.hypergryph.arknights"
        ),
        Game(
            id = "ex", title = "来自星尘", status = "已安装 · 1.1.4", glyph = "X",
            installed = true, packageName = "com.hypergryph.exastris"
        ),
        Game(
            id = "popu", title = "泡姆泡姆", status = "未安装", glyph = "P",
            installed = false, packageName = "com.hypergryph.popucom"
        ),
        Game(
            id = "endfield", title = "明日方舟\n终末地", status = "预约中", glyph = "E",
            installed = false, packageName = "com.hypergryph.endfield"
        )
    )

    val news: List<NewsItem> = listOf(
        NewsItem("n1", "HOT", "「熔火与冰原」版本更新内容一览", "11-06", hot = true),
        NewsItem("n2", "活动", "限时活动「寒潮将至」开启，参与获取合成玉", "11-05"),
        NewsItem("n3", "维护", "11 月 8 日 04:00 全区停机维护公告", "11-04")
    )
}
