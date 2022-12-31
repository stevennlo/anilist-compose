package com.example.anilist.model

import com.example.anilist.data.remote.graphql.DetailMediaQuery
import com.example.anilist.util.NumberUtil.orZero
import com.example.anilist.util.StringUtil.orHyphen

data class Character(
    val id: Int,
    val name: String,
    val imageUrl: String,
    val role: CharacterRole,
    val voiceActor: VoiceActor?
) {
    companion object {
        fun parse(character: DetailMediaQuery.Edge): Character {
            return Character(
                id = character.id.orZero(),
                name = character.node?.name?.userPreferred.orHyphen(),
                imageUrl = character.node?.image?.large.orEmpty(),
                role = CharacterRole.parse(character.role),
                voiceActor = character.voiceActors?.firstOrNull()?.let { VoiceActor.parse(it) }
            )
        }
    }
}
