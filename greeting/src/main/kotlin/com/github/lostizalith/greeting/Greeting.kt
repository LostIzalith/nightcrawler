package com.github.lostizalith.greeting

import com.fasterxml.jackson.annotation.JsonProperty

data class Greeting(
        @JsonProperty(value = "atomic") val id: Long,
        @JsonProperty(value = "name") val name: String
)
