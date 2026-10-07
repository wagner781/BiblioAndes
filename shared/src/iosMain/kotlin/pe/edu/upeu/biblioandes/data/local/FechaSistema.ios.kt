package pe.edu.upeu.biblioandes.data.local

import platform.Foundation.NSDate
import platform.Foundation.NSDateFormatter
import platform.Foundation.NSLocale
import platform.Foundation.localeWithLocaleIdentifier

actual fun fechaActualIso(): String = NSDateFormatter().run {
    dateFormat = "yyyy-MM-dd"
    locale = NSLocale.localeWithLocaleIdentifier("en_US_POSIX")
    stringFromDate(NSDate())
}
