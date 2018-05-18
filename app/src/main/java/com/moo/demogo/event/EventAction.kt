package com.moo.demogo.event

/**
 * @author moodstrikerdd
 * @date 2018/5/11
 * @label
 */
enum class EventAction {
    ACTION_UPDATE, ACTION_DELETE
}

enum class EventAction2(val value: Int) {
    ACTION_UPDATE(3), ACTION_DELETE(4)
}

enum class EventAction3(val key: String, val value: Int) {
    ACTION_UPDATE(key = "ACTION_UPDATE", value = 3), ACTION_DELETE(key = "ACTION_DELETE", value = 4);
}