package com.github.lostizalith.greeting.application.dto

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty
import org.apache.commons.lang3.StringUtils
import java.io.Serializable

/**
 * Greeting response dto entity.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
class GreetingResponse : Serializable {

    @JsonProperty(value = "atomic cola")
    var id: Long = 0L

    @JsonProperty(value = "name")
    var name: String = StringUtils.EMPTY
}
