package com.insyncwithfoo.pyrightls.configuration

import com.insyncwithfoo.pyrightls.isProbablyPyrightLSExecutable
import com.insyncwithfoo.pyrightls.message
import java.nio.file.Path
import kotlin.io.path.exists
import kotlin.io.path.isDirectory


internal enum class HintIcon {
    Success, Info, Warning, Error;
    
    // TODO: Re-add icons
    override fun toString() = ""
}


internal data class Hint(
    val icon: HintIcon,
    val text: String
) {
    
    override fun toString() = "$icon&nbsp;$text"
    
    companion object {
        fun success(text: String) = Hint(HintIcon.Success, text)
        fun info(text: String) = Hint(HintIcon.Info, text)
        fun warning(text: String) = Hint(HintIcon.Warning, text)
        fun error(text: String) = Hint(HintIcon.Error, text)
    }
    
}


internal fun emptyPathHint() = Hint.info(message("configurations.hint.noPathSpecified"))


internal fun invalidPathHint() = Hint.error(message("configurations.hint.invalidPath"))


internal fun executablePathResolvingHint(path: Path) = when {
    !path.exists() ->
        Hint.warning(message("configurations.hint.fileNotFound"))
    path.isDirectory() ->
        Hint.error(message("configurations.hint.unexpectedDirectory"))
    // Uncomment the following if it is asked for.
    // !path.isExecutable() ->
    //     Hint.warning(message("configurations.hint.fileNotExecutable"))
    !path.isProbablyPyrightLSExecutable ->
        Hint.info(message("configurations.hint.unknownExecutable"))
    else ->
        Hint.success(message("configurations.hint.fileFound"))
}
