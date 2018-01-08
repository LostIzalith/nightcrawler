package com.github.lostizalith.platformweb.domain

import java.io.Serializable

/**
 * Task execution result.
 */
data class Result(val id: String, val data: Serializable) : Serializable
