package org.taskmanagementapp.activity

import com.fasterxml.jackson.annotation.JsonValue

abstract class ObjectIdMixin {
    @JsonValue
    abstract fun toHexString(): String
}