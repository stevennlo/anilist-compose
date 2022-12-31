package com.example.anilist.model

import com.example.anilist.data.remote.graphql.DetailMediaQuery
import com.example.anilist.util.StringUtil.orHyphen

data class VoiceActor(
    val id: Int,
    val name: String,
    val imageUrl: String,
    val language: String
) {

    companion object {
        fun parse(voiceActor: DetailMediaQuery.VoiceActor): VoiceActor {
            return VoiceActor(
                id = voiceActor.id,
                name = voiceActor.name?.userPreferred.orHyphen(),
                imageUrl = voiceActor.image?.large.orEmpty(),
                language = voiceActor.language.orHyphen()
            )
        }
    }
}