package mx.unam.fciencias.ids.eq1.model.analitycs

import java.time.Instant

/**
 * Periodos de tiempo para los análisis
 */
enum class TimePeriod {
    DAILY,
    WEEKLY,
    MONTHLY,
    BIMONTHLY,
    QUARTERLY,
    YEARLY,
    CUSTOM
}