package io.github.soat7.myburguercontrol.exception

class ReasonCodeException(
    val reasonCode: ReasonCode,
    cause: Throwable? = null,
) : RuntimeException(reasonCode.description, cause)
