package com.timetable

import com.timetable.bot.Bot

fun main() {
    val groupId = 217746000
    val accessToken = "vk1.a.LM2U58gxDgJX8I55_PIg6ZfmrSewYdZ1ghYzlh9ulm4mSEqZld5RHHYtTFbOvja2pQQRsDBbzb3DIitTBpxffoDo8oC5YV-nZMTBv1XbHnQJI-XbuhNif7dTrlT_rVCcBJgVPgIkk31HNYhRVS-O4CW_VwP9VbE191uj5xji8Jzv12TUliqUcHxafCcl6KkqtR2RbJgGHzxNLQIZtDc44w"
    Bot(groupId, accessToken).start()
}
