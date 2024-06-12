package com.insyncwithfoo.pyrightls

import java.nio.file.InvalidPathException
import java.nio.file.Path
import kotlin.io.path.exists
import kotlin.io.path.nameWithoutExtension


private val KNOWN_EXECUTABLE_FILENAMES = listOf(
    "pyright-langserver",
    "pyright-python-langserver",
    "basedpyright-langserver"
)


internal fun String.toPathOrNull() =
    try {
        Path.of(this)
    } catch (_: InvalidPathException) {
        null
    }


internal fun String.toPathIfItExists(base: Path? = Path.of("")) =
    this.toPathOrNull()
        ?.let { (base?.resolve(it) ?: it).normalize() }
        ?.takeIf { it.exists() }


internal val Path.isEmpty: Boolean
    get() = this.toString() == ""


internal fun Path.resolvedAgainst(base: Path?) =
    base?.resolve(this) ?: this


internal val Path.isProbablyPyrightLSExecutable: Boolean
    get() = nameWithoutExtension in KNOWN_EXECUTABLE_FILENAMES
