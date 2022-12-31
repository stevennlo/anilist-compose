package com.example.anilist.model

import com.example.anilist.data.remote.graphql.type.CharacterRole as RemoteCharacterRole

enum class CharacterRole(val label: String) {
    MAIN("Main"),
    SUPPORTING("Supporting"),
    BACKGROUND("Background"),
    UNKNOWN("Unknown");

    companion object {
        fun parse(role: RemoteCharacterRole?): CharacterRole {
            return when (role) {
                RemoteCharacterRole.MAIN -> MAIN
                RemoteCharacterRole.SUPPORTING -> SUPPORTING
                RemoteCharacterRole.BACKGROUND -> BACKGROUND
                else -> UNKNOWN
            }
        }
    }
}