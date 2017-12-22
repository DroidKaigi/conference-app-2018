package io.github.droidkaigi.confsched2018.data.api.response

import java.util.*

data class Post(
        var title: String?,
        var content: String?,
        var date: Date?,
        var published: Boolean?,
        var type: String?
){
    constructor():this(null, null,null,null,null)
}
